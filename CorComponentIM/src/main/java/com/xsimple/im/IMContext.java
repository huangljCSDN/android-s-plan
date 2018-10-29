//package com.xsimple.im;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.Context;
//
//import com.xsimple.im.utils.NotifyHelper;
//
///**
// * Description：XSimple IM 全局上下文, 外部集成进行初始化配置及注册相关回调
// */
//public class IMContext {
//
//    private static Application sAppContext;
//    private static IMContext sInstance;
//    private static String sAppKey;
//
//    private AppLifecycleProxy mAppLifecycle;
//
//    public static void init(Application app, String appkey) {
//        sAppContext = app;
//        sAppKey = appkey;
//        if (sInstance == null) {
//            synchronized (IMContext.class) {
//                if (sInstance == null) {
//                    sInstance = new IMContext();
//                }
//            }
//        }
//    }
//
//    public static IMContext getInstance() {
//        if (sAppContext == null) {
//            throw new RuntimeException("IMContext not initialize");
//        } else {
//            return sInstance;
//        }
//    }
//
//    /**
//     * @return IM 全局上下文
//     */
//    public Context getContext() {
//        return sAppContext;
//    }
//
//    public String getAppkey() {
//        return sAppKey;
//    }
//
//    public Activity getCurrActivity() {
//        return mAppLifecycle.getTopActivity();
//    }
//
//    /**
//     * @return 前后台切换状态
//     */
//    public boolean isBackground() {
//        return mAppLifecycle.isBackground();
//    }
//
//    private IMContext() {
//        // TODO IM 相关配置初始化...
//        mAppLifecycle = registerLifecycleCallbacks(sAppContext);
//    }
//
//    private AppLifecycleProxy registerLifecycleCallbacks(Application app) {
//        AppLifecycleProxy appLifecycle = new AppLifecycleProxy();
//        appLifecycle.registerLifecycleCallback(new AppLifecycleProxy
//                .SimpleLifecycleCallback() {
//            @Override
//            public void onForeground(Activity activity) {
//                NotifyHelper.cancelAllNotify(activity);
//            }
//
//        });
//        app.registerActivityLifecycleCallbacks(appLifecycle);
//        return appLifecycle;
//    }
//
//}
