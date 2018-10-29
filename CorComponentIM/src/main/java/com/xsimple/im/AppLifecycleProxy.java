package com.xsimple.im;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.LinkedList;

import com.networkengine.util.LogUtil;

/**
 * Description: 全局生命周期监听
 */
public class AppLifecycleProxy implements Application.ActivityLifecycleCallbacks {

    /**
     * 后台切换广播
     */
    public static final String ACTION_BACKGROUND_CHANGED = "app.intent.action.BACKGROUND_CHANGED";

    /**
     * 前台切换广播
     */
    public static final String ACTION_FOREGROUND_CHANGED = "app.intent.action.FOREGROUND_CHANGED";

    static final int LIFECYLCE_ONCREATED = 0;
    static final int LIFECYLCE_ONSTARTED = 1;
    static final int LIFECYLCE_ONRESUMED = 2;
    static final int LIFECYLCE_ONPAUSED = 3;
    static final int LIFECYLCE_ONSTOPPED = 4;
    static final int LIFECYLCE_ONDESTROYED = 5;
    static final int LIFECYLCE_ONFOREGROUND = 6;
    static final int LIFECYLCE_ONBACKGROUND = 7;

    /**
     * 可见 Activity 数量，用于识别前后台切换
     */
    private int activeCount = 0;

    /**
     * 应用是否处于后台
     */
    private boolean isBackground = true;

    private Activity preActivity;

    private Activity currActivity;

    private LinkedList<Activity> activityStack = new LinkedList<>();

    private ArrayList<LifecycleCallback> lifecycleCallbacks = new ArrayList<>();

    /**
     * @return 应用前后台状态
     */
    public boolean isBackground() {
        return isBackground;
    }

    /**
     * @return 当前任务栈栈顶下的 Activity 索引
     */
    @Nullable
    public Activity getPreActivity() {
        return preActivity;
    }

    /**
     * @return 当前任务栈栈顶 Activity 索引
     */
    @NonNull
    public Activity getTopActivity() {
        return currActivity;
    }

    /**
     * @return 获取 Activity 任务栈
     */
    public LinkedList<Activity> getActivityStack() {
        return new LinkedList<>(activityStack);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.i(activity.getClass().getSimpleName());
        activityStack.add(activity);

        dispatchCallback(AppLifecycleProxy.LIFECYLCE_ONCREATED, activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogUtil.i(activity.getClass().getSimpleName());

        activeCount++;

        dispatchCallback(AppLifecycleProxy.LIFECYLCE_ONSTARTED, activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtil.i(activity.getClass().getSimpleName());

        if (isBackground) {
            isBackground = false;
            foreground(activity);
        }

        currActivity = activityStack.peekLast();

        LogUtil.d("PreActivity:" + preActivity);
        LogUtil.d("CurrActivity:" + currActivity);

        dispatchCallback(AppLifecycleProxy.LIFECYLCE_ONRESUMED, activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtil.i(activity.getClass().getSimpleName());

        preActivity = activity;
        if (activity.isFinishing()) {// 移除正在关闭 Activity, 处理延迟销毁问题
            activityStack.pollLast();
            if (activityStack.size() > 1) {
                preActivity = activityStack.get(activityStack.size() - 2);
            } else {
                preActivity = null;
            }
        }

        dispatchCallback(AppLifecycleProxy.LIFECYLCE_ONPAUSED, activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogUtil.i(activity.getClass().getSimpleName());

        activeCount--;
        if (activeCount == 0) {
            isBackground = true;
            background(activity);

            if (activityStack.size() > 1) {// 切换到后台, 重新调整 preActivity 索引
                preActivity = activityStack.get(activityStack.size() - 2);
            } else {
                preActivity = null;
            }
        }

        dispatchCallback(AppLifecycleProxy.LIFECYLCE_ONSTOPPED, activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtil.i(activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.i(activity.getClass().getSimpleName());

        activityStack.remove(activity);
        if (activity == preActivity) preActivity = null;
        if (activity == currActivity) currActivity = null;

        LogUtil.d("PreActivity:" + preActivity);
        LogUtil.d("CurrActivity:" + currActivity);

        dispatchCallback(AppLifecycleProxy.LIFECYLCE_ONDESTROYED, activity);
    }

    /**
     * 切换后台调用
     *
     * @Hide
     */
    private void background(Activity activity) {
        LogUtil.i("The background to im_switch");
        LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent(ACTION_BACKGROUND_CHANGED));

        dispatchCallback(AppLifecycleProxy.LIFECYLCE_ONBACKGROUND, activity);
    }

    /**
     * 切换前台调用
     */
    private void foreground(Activity activity) {
        LogUtil.i("The foreground to im_switch");
        LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent(ACTION_FOREGROUND_CHANGED));

        dispatchCallback(AppLifecycleProxy.LIFECYLCE_ONFOREGROUND, activity);
    }

    private void dispatchCallback(int type, Activity activity) {
        for (LifecycleCallback callback : lifecycleCallbacks) {
            switch (type) {
                case AppLifecycleProxy.LIFECYLCE_ONCREATED:
                    callback.onActivityCreated(activity);
                    break;
                case AppLifecycleProxy.LIFECYLCE_ONSTARTED:
                    callback.onActivityStarted(activity);
                    break;
                case AppLifecycleProxy.LIFECYLCE_ONRESUMED:
                    callback.onActivityResumed(activity);
                    break;
                case AppLifecycleProxy.LIFECYLCE_ONPAUSED:
                    callback.onActivityPaused(activity);
                    break;
                case AppLifecycleProxy.LIFECYLCE_ONSTOPPED:
                    callback.onActivityStopped(activity);
                    break;
                case AppLifecycleProxy.LIFECYLCE_ONDESTROYED:
                    callback.onActivityDestroyed(activity);
                    break;
                case AppLifecycleProxy.LIFECYLCE_ONFOREGROUND:
                    callback.onForeground(activity);
                    break;
                case AppLifecycleProxy.LIFECYLCE_ONBACKGROUND:
                    callback.onBackground(activity);
                    break;
            }
        }
    }

    public void registerLifecycleCallback(LifecycleCallback callback) {
        if (callback != null) {
            lifecycleCallbacks.add(callback);
        }
    }

    public void unregisterLifecycleCallback(LifecycleCallback callback) {
        if (callback != null) {
            lifecycleCallbacks.remove(callback);
        }
    }

    public interface LifecycleCallback {

        void onActivityCreated(Activity activity);

        void onActivityStarted(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityStopped(Activity activity);

        void onActivityDestroyed(Activity activity);

        void onBackground(Activity activity);

        void onForeground(Activity activity);
    }

    public static class SimpleLifecycleCallback implements LifecycleCallback {
        @Override
        public void onActivityCreated(Activity activity) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        @Override
        public void onBackground(Activity activity) {

        }

        @Override
        public void onForeground(Activity activity) {

        }
    }

}
