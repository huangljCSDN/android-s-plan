package com.markLove.Xplan.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.PreferencesUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zhaotangwu on 2017/2/24.
 */

public class App extends Application {

    public static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initPhotoError();
    }


    public static App getInstance() {

        return mInstance;
    }

    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    /**
     * * 获取当前进程的名字，一般就是当前app的包名
     * *
     * * @param context 当前上下文
     * * @return 返回进程的名字
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid(); // Returns the identifier of this process
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    return info.processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

//    void onTokenExpires() {
//        // 清空缓存信息
//        User.removeLastJSONStr();
//        App.getInstance().setUserCache(null);
//        SharePreferenceConfigHelper.getInstance().putToken("");
//
//        Ln.i("#onTokenExpires token过期   重新登录");
//        Activity lastActivity = ActivityManager.getInstance().currentActivity();
//        Intent intent = new Intent(lastActivity, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);
//        lastActivity.finish();
//        Toast.makeText(App.getInstance(), getString(R.string.token_expires), Toast.LENGTH_SHORT).show();
//    }

    public String getToken(){
        String token = "";
        UserBean userBean = GsonUtils.json2Bean(PreferencesUtils.getString(this,PreferencesUtils.KEY_USER),UserBean.class);
        if (userBean != null){
            token = userBean.getToken();
        }
        return token;
    }
}

