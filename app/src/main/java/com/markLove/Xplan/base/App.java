package com.markLove.Xplan.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.ui.activity.LoginActivity;
import com.markLove.Xplan.ui.dialog.TokenTipDialog;
import com.markLove.Xplan.utils.AppManager;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.PreferencesUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zhaotangwu on 2017/2/24.
 */

public class App extends Application {

    public static App mInstance;
    public AMapLocation aMapLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initPhotoError();
        com.xsimple.im.Initializer initializer = new com.xsimple.im.Initializer();
        initializer.onCreate(this);
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

    public void onTokenExpires() {
        final Activity lastActivity = AppManager.getAppManager().currentActivity();
        TokenTipDialog tokenTipDialog = new TokenTipDialog(lastActivity);
        tokenTipDialog.setOnDialogCallBack(new TokenTipDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {
                outLogin(lastActivity);
            }
        });
        tokenTipDialog.setCanceledOnTouchOutside(false);
        tokenTipDialog.show();
    }

    public void outLogin(Activity lastActivity){
        PreferencesUtils.clear(getApplicationContext());

        Intent intent = new Intent(lastActivity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        lastActivity.finish();
    }

    public String getToken(){
        String token = "";
        UserBean userBean = GsonUtils.json2Bean(PreferencesUtils.getString(this,PreferencesUtils.KEY_USER),UserBean.class);
        if (userBean != null){
            token = userBean.getToken();
        }
        return token;
    }

    public String getUserId(){
        String userId = "";
        UserBean userBean = GsonUtils.json2Bean(PreferencesUtils.getString(this,PreferencesUtils.KEY_USER),UserBean.class);
        if (userBean != null){
            userId = String.valueOf(userBean.getUserInfo().getUserId());
        }
        return userId;
    }

    public boolean isLogin(){
       if (!TextUtils.isEmpty(PreferencesUtils.getString(this,PreferencesUtils.KEY_USER))){
           return true;
       }
       return false;
    }

    public UserBean getUserBean(){
        UserBean userBean = GsonUtils.json2Bean(PreferencesUtils.getString(this,PreferencesUtils.KEY_USER),UserBean.class);
        return userBean;
    }

    public AMapLocation getaMapLocation() {
        return aMapLocation;
    }

    public void setaMapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }
}

