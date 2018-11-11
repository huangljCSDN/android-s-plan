package com.markLove.Xplan.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationManagerCompat;

import com.markLove.Xplan.base.App;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.networkengine.engine.LogicEngine;
import com.networkengine.entity.GetMsgsEntity;
import com.networkengine.entity.GetMsgsResult;
import com.networkengine.entity.MessageContent;
import com.networkengine.entity.RequestGetMsgsParam;
import com.networkengine.httpApi.MchlApiService;
import com.networkengine.mqtt.MqttChannel;
import com.networkengine.mqtt.MqttService;
import com.xsimple.im.db.DbManager;
import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class PushManager {

    private static final long DEF_TRY_COUNT = 3;// 拉去消息重试次数
    private static final int NOTIFY_ID = 0x1000;

    private MqttService mqttService;
    private String appkey;
    private String userId;
    private DbManager dbManager;
    private Context mContext;

    public PushManager(MqttService mqttService, String appkey, String userId,Context context) {
        this.mqttService = mqttService;
        this.appkey = appkey;
        this.userId = userId;
        mContext = context;
        dbManager = DbManager.getInstance(context);
        stepUp();
    }

    public void cancelNotity(Context context) {
        NotificationManagerCompat.from(context).cancel(NOTIFY_ID);
    }

    private void stepUp() {
        mqttService.registMqttObserver(appkey, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MqttChannel.MQTT_ACTION_SYS_MESSAGE_ARRIVED:
                        doPullMessage(0, DEF_TRY_COUNT);
                        break;
                }
                return false;
            }
        });
//        mqttService.subscribeToTopic(String.format("sys/%s", userId));

        doPullMessage(0, DEF_TRY_COUNT);
    }

    private void doPullMessage(long msgId, long tryCount) {
        PushMessageTask task = new PushMessageTask();
        task.execute(msgId, tryCount);
    }

    private void showSystemMessage(final GetMsgsEntity messageEntity) {
        final MessageContent msgContent = messageEntity.getMsgContent();
        String type = msgContent.getType();
        //盒子小助手消息
        if (MessageContent.MSG_TYPE_BOX.equals(type)) {
            messageEntity.getMsgContent().setUserId(App.getInstance().getUserId());
            dbManager.processBoxMessage(messageEntity);
        } else if (MessageContent.MSG_TYPE_OFFICIAL.equals(type)) { //官方消息
            messageEntity.getMsgContent().setUserId(App.getInstance().getUserId());
            dbManager.processOfficialMessage(messageEntity);
        }
    }

    private void showPushNotify(Context context, String title, String message) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationManagerCompat.from(context).notify(NOTIFY_ID, new NotificationCompat
//                .Builder(context)
//                .setContentTitle(title) // 设置通知栏标题
//                .setContentText(message) // 设置通知栏显示内容
//                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//                .setPriority(Notification.PRIORITY_DEFAULT) // 设置该通知优先级
//                .setContentIntent(pendingIntent) // 设置通知栏点击意图
//                .setAutoCancel(true) // 设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(false) // ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                .setVibrate(new long[]{0, 200, 150, 200}) // 延迟0ms，然后振动200ms，在延迟150ms，接着在振动200ms。类推
//                // .setLargeIcon(bitmap)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .build());
    }

    private class PushMessageTask extends AsyncTask<Long, Void, List<GetMsgsEntity>> {
        @Override
        protected List<GetMsgsEntity> doInBackground(Long... params) {
            Long tryCount = params[1];
            try {
                MchlApiService mchlApiService = LogicEngine.getInstance().getMchlClient();
                RequestGetMsgsParam requestGetMsgsParam = new RequestGetMsgsParam(10, params[0]);
                Response<GetMsgsResult> execute = mchlApiService.getNewMsgs(requestGetMsgsParam).execute();
                if (execute == null) {
                    return null;
                }
                LogUtils.i("PushManager="+GsonUtils.obj2Json(execute.body()));
                if (execute.isSuccessful()) {

                    GetMsgsResult body = execute.body();

                    List<GetMsgsEntity> data = null;
                    if (body != null && body.getData() != null) {
                        data = body.getData().getData();
                    }
                    if (data != null && !data.isEmpty()) {
                        long clientMaxMsgId = 0;
                        try {
                            clientMaxMsgId = Long.parseLong(body.getData().getClientMaxMsgId());
                        } catch (Exception e) {

                        }
                        doPullMessage(clientMaxMsgId, DEF_TRY_COUNT);
                        return data;
                    }

                }
            } catch (IOException e) {
                doPullMessage(params[0], --tryCount);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<GetMsgsEntity> messageEntity) {
            super.onPostExecute(messageEntity);
            if (messageEntity != null) {
                for (GetMsgsEntity msg : messageEntity) {
                    showSystemMessage(msg);
                }
            }
        }
    }

}
