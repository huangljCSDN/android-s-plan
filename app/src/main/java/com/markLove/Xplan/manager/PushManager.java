package com.markLove.Xplan.manager;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Gravity;

import com.google.gson.Gson;
import com.markLove.Xplan.utils.AppManager;
import com.networkengine.engine.LogicEngine;
import com.networkengine.entity.GetMsgsEntity;
import com.networkengine.entity.GetMsgsResult;
import com.networkengine.entity.MessageContent;
import com.networkengine.entity.RequestGetMsgsParam;
import com.networkengine.httpApi.MchlApiService;
import com.networkengine.mqtt.MqttChannel;
import com.networkengine.mqtt.MqttService;
import com.xsimple.im.engine.Initializer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

import static com.networkengine.engine.LogicEngine.getMxmUrl;

public class PushManager {

    private static final long DEF_TRY_COUNT = 3;// 拉去消息重试次数
    private static final int NOTIFY_ID = 0x1000;

    private MqttService mqttService;
    private String appkey;
    private String userId;

    public PushManager(MqttService mqttService, String appkey, String userId) {
        this.mqttService = mqttService;
        this.appkey = appkey;
        this.userId = userId;

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
        mqttService.subscribeToTopic(String.format("sys/%s", userId));

        doPullMessage(0, DEF_TRY_COUNT);
    }

    private void doPullMessage(long msgId, long tryCount) {
        PushMessageTask task = new PushMessageTask();
        task.execute(msgId, tryCount);
    }

    private void showSystemMessage(final Context context, final GetMsgsEntity messageEntity) {
        final MessageContent msgContent = messageEntity.getMsgContent();
        String type = msgContent.getType();
        if (MessageContent.MSG_TYPE_LOCK_DEVICE.equals(type)
                || MessageContent.MSG_TYPE_LOCK_USER.equals(type)
                || MessageContent.MSG_TYPE_UPDATE.equals(type)) {
//            showDialog(context, msgContent, false);
        } else if (MessageContent.MSG_TYPE_SYSTEM.equals(type)) {
        }
    }

    //比较时间，拼接提醒字符串
    private String getNotifyString(MessageContent messageContent) {
        try {
            Date signDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(messageContent.getMessage());
            Calendar signCalendar = Calendar.getInstance();
            signCalendar.setTime(signDate);
            Calendar nowCalendar = Calendar.getInstance();
            if (signCalendar.get(Calendar.YEAR) != nowCalendar.get(Calendar.YEAR)
                    || signCalendar.get(Calendar.MONTH) != nowCalendar.get(Calendar.MONTH)
                    || signCalendar.get(Calendar.DAY_OF_MONTH) != nowCalendar.get(Calendar.DAY_OF_MONTH)
                    || signCalendar.get(Calendar.HOUR_OF_DAY) - nowCalendar.get(Calendar.HOUR_OF_DAY) < 0) {
                return "";
            } else {
                String notifyString = "距离上班时间还有";
                int hour = signCalendar.get(Calendar.HOUR_OF_DAY) - nowCalendar.get(Calendar.HOUR_OF_DAY);
                int minite = signCalendar.get(Calendar.MINUTE) - nowCalendar.get(Calendar.MINUTE);
                if (messageContent.getSignType().equals("SIGNOFF")) {
                    notifyString = hour > 0 || minite > 0 ? "下班时间已到，记得签退哦" : "";
                } else {
                    notifyString = hour > 0 ? notifyString + hour + "小时" : notifyString;
                    notifyString = minite > 0 ? notifyString + minite + "分钟" : notifyString;
                }
                return notifyString;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
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
//                    Activity currActivity = CoracleSdk.getCoracleSdk().getCurrActivity();
                    Activity currActivity =  AppManager.getAppManager().currentActivity();
                    showSystemMessage(currActivity, msg);
                }
            }
        }
    }

}
