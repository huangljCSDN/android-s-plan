package com.networkengine.mqtt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.networkengine.database.table.Member;
import com.networkengine.engine.EngineParameter;
import com.networkengine.engine.LogicEngine;
import com.networkengine.util.LogUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public class MqttChannel implements MqttCallbackExtended {

    public static final String SHARED_PREFERENCE_NAME = "MqttChannel";

    public static final String SERVICE_STOP = "USER_LOGOUT";

    private static final String TOPIC_PUSH = "/push/";

    public static final int MQTT_ACTION_CONNECTION_LOST = 1;

    public static final int MQTT_ACTION_DELIVERY_COMPLETE = 2;

    public static final int MQTT_ACTION_IM_MESSAGE_ARRIVED = 3;

    public static final int MQTT_ACTION_SYS_MESSAGE_ARRIVED = 4;

    public static final int MQTT_ACTION_CONNECTION_COMPLETE = 5;

    public static final int MQTT_ACTION_CONNECT_FAILED = 6;

    public static final int MQTT_ACTION_PC_SATUS_CHANGED = 7;

    private static final int MQTT_SERVICE_TIMEOUT = 30;

    private static final int MQTT_KEEP_ALIVE_INTERVAL = 30;

    private static final String MQTT_SERVER_SWITCH_KEY = "MqttServerKey";

    private Context mContext;

    private static MqttAndroidClient mClient;

    private Handler mHandler;

    private EngineParameter mParameter;

    private Member member;

    private SharedPreferences sp;

    private BufferContainer mBufferContainer;

    private static int tryCount = 0;

    private final int MAX_TRY_COUNT = 3;

    private class MqttInfo {
        String mMqttTopic;
        MqttMessage mMqttMessage;

        MqttInfo(String mqttTopic, MqttMessage mqttMessage) {
            mMqttTopic = mqttTopic;
            mMqttMessage = mqttMessage;
        }
    }

    public MqttChannel(Context context, EngineParameter parameter, Member member, Handler handler) {
        this.member = member;
        mHandler = handler;
        mContext = context;
        mParameter = parameter;
        initBufferContainer();
        sp = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public boolean create() throws MqttException {
        return connect();
    }

    public boolean connect() throws MqttException {
        /*if (!checkNet()) {
            LogUtil.e("无法进行网络连接");
            return false;
        }*/

        if (!checkMqttParam()) {
            LogUtil.e("用户ID为空");
            return false;
        }

        //已退出登录 推送服务关闭
        if (sp.getBoolean(SERVICE_STOP, true)) {
            LogUtil.d("已退出登录 推送服务关闭");
            return false;
        }

        // 1. 已连接, 换帐号了
        if (mClient != null && !mClient.getClientId().contains(member.getId())) {
            LogUtil.d("已连接, 换帐号了");
            disconnect();
        }

        // 2. 连接为空
        if (mClient == null) {
            String clientId = String.format("%s_%s", member.getId(), System.currentTimeMillis());
            mClient = new MqttAndroidClient(mContext, mParameter.mqttServer, clientId);
            mClient.setCallback(this);
        }

        // 已经连接了
        if (mClient.isConnected()) {
            LogUtil.d("已连接,直接返回");
            return true;
        }

        mClient.connect(getConnectOptions(), null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken token) {
                LogUtil.d("连接成功");

                subCribeTopic();//连接成功后订阅待办
            }

            @Override
            public void onFailure(IMqttToken token, Throwable throwable) {
                LogUtil.e("连接失败");
                if (tryCount < MAX_TRY_COUNT) {
                    try {
                        tryCount++;
                        connect();
                        return;
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(MQTT_ACTION_CONNECT_FAILED);
            }
        });
        return true;
    }

    public void disconnect() {
        LogUtil.d("停止mqtt");
        try {
            if (mClient != null) {
                if (mClient.isConnected()) {
                    mClient.disconnect();
                }
                mClient = null;
            }
        } catch (Exception e) {
            LogUtil.e("Error:", e);
        }
    }

    //只是断开连接，client不置空
    public void disconnectMqttServer() {
        try {
            if (mClient != null) {
                if (mClient.isConnected()) {
                    mClient.disconnect();
                }
            }
        } catch (Exception e) {
            LogUtil.e("Error:", e);
        }
    }

    //订阅待办
    private void subCribeTopic() {

        try {
            // IM消息
            mClient.subscribe(TOPIC_PUSH + mParameter.appKey + "/user/" + member.getId(), 0);
            // 管理平台消息
            mClient.subscribe(TOPIC_PUSH + mParameter.appKey + "/sys/" + member.getId(), 0);
            // 挤下线
            mClient.subscribe(TOPIC_PUSH + mParameter.appKey + "/" + mParameter.imei, 0);
            // PC端上下线
//            mClient.subscribe(TOPIC_PUSH + mParameter.appKey + "/pcStatus/" + mParameter.imei, 0);

            LogUtil.d("订阅成功");
        } catch (Exception e) {
            LogUtil.e("订阅失败: ", e);
        }
    }

    /**
     * 订阅群组
     *
     * @param topicName
     * @author
     */
    public void subscribeToTopic(String topicName) {
        if ((mClient == null) || (!mClient.isConnected())) {
            LogUtil.e("mClient null");
        } else {
            try {
                String subject = TOPIC_PUSH + mParameter.appKey + "/" + topicName;
                mClient.subscribe(subject, 0);
                Toast.makeText(mContext,"订阅聊天室成功 id="+topicName,Toast.LENGTH_SHORT).show();
                LogUtil.d("订阅成功: " + subject);
            } catch (MqttException e) {
                LogUtil.e("订阅失败: " + topicName, e);
                Toast.makeText(mContext,"订阅失败 id="+topicName,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void unsubscribeToTopic(String topicName) {
        if ((mClient == null) || (!mClient.isConnected())) {
            LogUtil.e("Connection error");
        } else {
            try {
                String subject = TOPIC_PUSH + mParameter.appKey + "/" + topicName;
                mClient.unsubscribe(subject);
                LogUtil.d("取消订阅成功: " + subject);
            } catch (MqttException e) {
                LogUtil.e("取消订阅失败: " + topicName, e);
            }
        }
    }

//    private boolean checkNet() {
//        return NetworkUtils.volidateNet(mContext);
//    }

    private boolean checkMqttParam() {
        return !TextUtils.isEmpty(member.getId());
    }

    private MqttConnectOptions getConnectOptions() throws MqttException {
        MqttConnectOptions opt = new MqttConnectOptions();
        opt.setAutomaticReconnect(true);
        opt.setCleanSession(false);
        opt.setConnectionTimeout(MQTT_SERVICE_TIMEOUT);
        opt.setKeepAliveInterval(MQTT_KEEP_ALIVE_INTERVAL);
        return opt;

    }

    @Override
    public void connectionLost(Throwable throwable) {
        LogUtil.e("connectionLost ");

//        disconnect();

        if (sp.getBoolean(SERVICE_STOP, true)) {
//            Message msg = mHandler.obtainMessage(MQTT_ACTION_CONNECTION_LOST, throwable);
//            mHandler.sendMessage(msg);
            return;
        }
        try {
             connect();
        } catch (MqttException e) {
            LogUtil.e("重连失败", e);
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken mqttDeliveryToken) {
        LogUtil.d("deliveryComplete ");
        Message msg = mHandler.obtainMessage(MQTT_ACTION_DELIVERY_COMPLETE, mqttDeliveryToken);
        mHandler.sendMessage(msg);
    }

    @Override
    public void messageArrived(String mqttTopic, MqttMessage mqttMessage)
            throws Exception {
        LogUtil.d("messageArrived mqttTopic ：" + mqttTopic);

        // 挤下线
        if (mqttTopic.equals(TOPIC_PUSH + mParameter.appKey + "/" + mParameter.imei)) {
            sp.edit().putBoolean(SERVICE_STOP, true).apply();
            LogicEngine.getInstance().getSystemController().logout(true);
            disconnect();
        } else if (mqttTopic.equals(TOPIC_PUSH + mParameter.appKey + "/sys/" + member.getId())) { // 管理平台消息
            Message msg = mHandler.obtainMessage(MQTT_ACTION_SYS_MESSAGE_ARRIVED, null);
            mHandler.sendMessage(msg);
        } else { // IM消息比较多，做个优化
            mBufferContainer.pushTask(new MqttInfo(mqttTopic, mqttMessage));
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURL) {
        // 成功连接
        LogUtil.d("connectComplete URL : " + serverURL);
        Message msg = mHandler.obtainMessage(MQTT_ACTION_CONNECTION_COMPLETE, serverURL);
        mHandler.sendMessage(msg);
    }

    private void initBufferContainer() {

        if (mBufferContainer == null) {
            mBufferContainer = new BufferContainer<>(new BufferContainer.BufferAdapter<MqttInfo>() {
                @Override
                public long getCycle() {
                    return 500;
                }

                @Override
                public void onPull(List<MqttInfo> list) {
                    LogUtil.d("new im push msg ");
                    Message msg = mHandler.obtainMessage(MQTT_ACTION_IM_MESSAGE_ARRIVED, list);
                    mHandler.sendMessage(msg);
                }
            });
        }
    }
}
