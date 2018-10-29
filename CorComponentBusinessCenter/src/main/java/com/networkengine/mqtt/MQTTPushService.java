package com.networkengine.mqtt;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 推送消息接收后台常驻服务
 */
public class MQTTPushService extends Service {

    private final static int GRAY_SERVICE_ID = 1001;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else {
            startService(new Intent(this, GrayInnerService.class));
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static class GrayInnerService extends Service {
        @Nullable
        @Override
        public IBinder onBind(Intent intent) { return null; }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();

            return super.onStartCommand(intent, flags, startId);
        }
    }

}