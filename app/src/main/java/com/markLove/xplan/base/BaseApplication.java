package com.markLove.xplan.base;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by zhaotangwu on 2017/2/24.
 */

public class BaseApplication extends Application {

    public static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initPhotoError();
    }


    public static BaseApplication getInstance() {

        return mInstance;
    }

    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

//    void onTokenExpires() {
//        // 清空缓存信息
//        User.removeLastJSONStr();
//        BaseApplication.getInstance().setUserCache(null);
//        SharePreferenceConfigHelper.getInstance().putToken("");
//
//        Ln.i("#onTokenExpires token过期   重新登录");
//        Activity lastActivity = ActivityManager.getInstance().currentActivity();
//        Intent intent = new Intent(lastActivity, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);
//        lastActivity.finish();
//        Toast.makeText(BaseApplication.getInstance(), getString(R.string.token_expires), Toast.LENGTH_SHORT).show();
//    }

}

