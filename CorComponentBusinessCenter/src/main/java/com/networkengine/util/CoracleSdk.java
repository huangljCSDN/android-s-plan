package com.networkengine.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public final class CoracleSdk {

    static ApplicationLifecycleListener sLifecycleListener;

    static Context sContext;

    static CoracleSdk sCoracleSdk;

    public static boolean IS_DEGUG = false;

    static volatile boolean isInit = false;

    public static void init(@NonNull Context context) {
        if (!isInit) {
            Preconditions.checkNotNull(context);
            sContext = context.getApplicationContext();

            // 全局生命周期监听
            sLifecycleListener = new ApplicationLifecycleListener();
            Application app = (Application) sContext.getApplicationContext();
            app.registerActivityLifecycleCallbacks(sLifecycleListener);

            // 工具库初始化
            SharedPrefsHelper.initPrefsConfig(sContext, null);

            sCoracleSdk = new CoracleSdk();

            isInit = true;
        }
    }

    public static CoracleSdk getCoracleSdk() {
        if (!isInit)
            throw new RuntimeException("please perform init method");

        return sCoracleSdk;
    }

    /**
     * @return 全局上下文
     */
    public Context getContext() {
        return sContext;
    }

    /**
     * @return 前后台切换状态
     */
    public boolean isBackground() {
        return sLifecycleListener.isBackground();
    }

    /**
     * @return 当前栈顶 Activity
     */
    public Activity getCurrActivity() {
        return sLifecycleListener.getTopActivity();
    }


    private List<String> mTabList = new ArrayList<>();

    public void registerTabFragment(int index, String tabFragment) {
        if (index < 0 || index != 0 && index > mTabList.size()) {
            String error = String.format("%s%d%s%d", "list size is ", mTabList.size(), "insert index is ", index);
            throw new IndexOutOfBoundsException(error);
        }

        if (tabFragment == null) {
            throw new NullPointerException(" tabFragment is null");
        }
        mTabList.add(index, tabFragment);
    }

    public List<String> getTabList() {
        return mTabList;
    }

    private CoracleSdk() {
    }

}
