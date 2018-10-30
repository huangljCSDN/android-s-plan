package com.xsimple.im;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.xsimple.im.utils.NotifyHelper;

/**
 * Description：XSimple IM 全局上下文, 外部集成进行初始化配置及注册相关回调
 */
public class Initializer{

    private static Application sAppContext;
    private static Initializer sInstance;

    private AppLifecycleProxy mAppLifecycle;

//    public static void init(Application app) {
//        sAppContext = app;
//        if (sInstance == null) {
//            synchronized (Initializer.class) {
//                if (sInstance == null) {
//                    sInstance = new Initializer();
//                }
//            }
//        }
//    }

    public static Initializer getInstance() {
        if (sAppContext == null) {
            throw new RuntimeException("Initializer not initialize");
        } else {
            return sInstance;
        }
    }

    /**
     * @return IM 全局上下文
     */
    public static Context getContext() {
        return sAppContext;
    }

    public Activity getCurrActivity() {
        return mAppLifecycle.getTopActivity();
    }

    public AppLifecycleProxy getmAppLifecycle() {
        return mAppLifecycle;
    }

    public void setmAppLifecycle(AppLifecycleProxy mAppLifecycle) {
        this.mAppLifecycle = mAppLifecycle;
    }

    /**
     * @return 前后台切换状态
     */
    public boolean isBackground() {
        return mAppLifecycle.isBackground();
    }

//    public Initializer() {
//        // TODO IM 相关配置初始化...
//        mAppLifecycle = registerLifecycleCallbacks(sAppContext);
//    }

    private AppLifecycleProxy registerLifecycleCallbacks(Application app) {
        AppLifecycleProxy appLifecycle = new AppLifecycleProxy();
        appLifecycle.registerLifecycleCallback(new AppLifecycleProxy
                .SimpleLifecycleCallback() {
            @Override
            public void onForeground(Activity activity) {
                NotifyHelper.cancelAllNotify(activity);
            }

        });
        app.registerActivityLifecycleCallbacks(appLifecycle);
        return appLifecycle;
    }

    public void onCreate(Application application) {
        sAppContext = application;
        if (sInstance == null) {
            synchronized (Initializer.class) {
                if (sInstance == null) {
                    sInstance = new Initializer();
                    sInstance.setmAppLifecycle(registerLifecycleCallbacks(application));
//                    mAppLifecycle = registerLifecycleCallbacks(sAppContext);
                }
            }
        }
//        CorRouter.getCorRouter().regist(new ServiceStub<IMRouterService>() {
//            @Override
//            public String initModule() {
////                return "im";
//                return getId(Initializer.class);
//            }
//        });
    }
}
