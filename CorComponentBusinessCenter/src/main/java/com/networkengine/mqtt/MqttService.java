package com.networkengine.mqtt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.networkengine.database.table.Member;
import com.networkengine.engine.EngineParameter;
import com.networkengine.util.LogUtil;

import java.util.ArrayList;

public class MqttService extends Thread implements Handler.Callback {

    private static final int CMD_CODE_MQTT_START = 0;

    private static volatile boolean sAbortDoingFlag = false;

    private Handler mHandler = null;

    private volatile static MqttService sMqttThread = null;

    private static MqttChannel mMqttChannel = null;

    private Context mContext;

    private ArrayList<Handler.Callback> mMqttObservers = null;

    private MqttService(Context context) {
        mContext = context;
    }

    private SubjectDot<String, Handler.Callback, Message> mSubjectDot = new SubjectDot<String, Handler.Callback, Message>() {
        @Override
        public void execute(Handler.Callback callback, Message msgs) {
            callback.handleMessage(msgs);
        }
    };

    public static MqttService getInstance(Context context) {
        if (sMqttThread == null) {
            sMqttThread = new MqttService(context);
        }
        return sMqttThread;
    }

    private static void clearAbort() {
        sAbortDoingFlag = false;
    }

    private static void setAbort() {
        sAbortDoingFlag = true;
    }

    public boolean start(Context context, EngineParameter parameter, Member member) {
        LogUtil.i("member=="+member.toString());
        mMqttChannel = new MqttChannel(context, parameter
                , member, new Handler(this));

        Message cmd;
        if (mHandler == null) {
            cmd = new Message();
        } else {
            cmd = mHandler.obtainMessage();
        }

        cmd.what = CMD_CODE_MQTT_START;
        cmd.obj = mMqttChannel;

        return sendCmd(cmd);
    }

    public boolean sendCmd(Message cmd) {
        Log.e("pp", "sendCmd");
        if (!waitBackupThreadStared()) {
            Log.e("pp", "waitBackupThreadStared fail");
            return false;
        }

        if (mHandler != null) {
            return mHandler.sendMessage(cmd);
        }
        Log.e("pp", "waitBackupThreadStared mHandler is null");
        return false;

    }

    private boolean waitBackupThreadStared() {
        int timeout = 0;

//        if (sMqttThread == null)
//        {
//        	Log.e("pp", "waitBackupThreadStared 1");
//        	sMqttThread = getNewThread();
//        }

        if (State.NEW == sMqttThread.getState()) {
            Log.e("pp", "waitBackupThreadStared 2");
            sMqttThread.start();
        }

        while (mHandler == null) {

            Log.e("pp", "waitBackupThreadStared 3");
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout++;
            if (timeout > 100) {
                Log.e("pp", "waitBackupThreadStared 4");
                return false;
            }
        }

        return true;
    }

    public void abortLooper() {
        setAbort();
        if (mHandler != null) {
            Looper looper = mHandler.getLooper();
            mHandler = null;
            if (looper != null) {
                looper.quit();
            }
        }

        sMqttThread = null;
    }

    public void run() {
        clearAbort();
        Looper.prepare();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg == null) {
                    return;
                }
                Log.e("pp", "MqttThread work thread msg what : " + msg.what);
                int msgCode = msg.what;
                switch (msgCode) {
                    case CMD_CODE_MQTT_START:
                        startMqtt(msg);
                        break;
                }

            }
        };
        Looper.loop();
    }

    private void startMqtt(Message msg) {
        SharedPreferences sp = mContext.getSharedPreferences(
                MqttChannel.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(MqttChannel.SERVICE_STOP, false).apply();

        if (msg.obj == null) {
            return;
        }
        if (!(msg.obj instanceof MqttChannel)) {
            return;
        }
        MqttChannel mqttChannel = (MqttChannel) msg.obj;

        try {
            mqttChannel.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMqtt() {
        //abortLooper();
        SharedPreferences sp = mContext.getSharedPreferences(
                MqttChannel.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(MqttChannel.SERVICE_STOP, true).apply();
        if (mMqttChannel != null) {
            mMqttChannel.disconnect();
        }
    }

    //断开连接，不置空
    public void disconnectMqtt() {
        if (mMqttChannel != null) {
            mMqttChannel.disconnectMqttServer();
        }
    }

    public void registMqttObserver(String key, Handler.Callback observer) {
        mSubjectDot.attach(key, observer);
    }

    public void unregistMqttObserver(String key) {
        mSubjectDot.dettach(key);
    }


    public void subscribeToTopic(String topicName) {
        mMqttChannel.subscribeToTopic(topicName);
    }

    public void unsubscribeToTopic(String topicName) {
        mMqttChannel.unsubscribeToTopic(topicName);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg == null) {
            return false;
        }
        mSubjectDot.notice(msg);
        return true;
    }

    public MqttChannel getmMqttChannel() {
        return mMqttChannel;
    }
}
