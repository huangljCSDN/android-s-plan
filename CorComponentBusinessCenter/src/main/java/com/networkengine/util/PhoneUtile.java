package com.networkengine.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by liuhao on 2017/4/12.
 */

public class PhoneUtile {

    /**
     * 计算屏幕宽度
     *
     * @param context 当前上下文
     * @return 屏幕宽度 px
     */
    public static int getWidthPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context 当前上下文
     * @return 屏幕高度 px
     */
    public static int getHeightPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * @param dpVal
     * @return 根据设备的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, int dpVal) {
        return Math.round(dpVal * context.getResources().getDisplayMetrics().density);
    }

    /**
     * @param pxVal
     * @return 根据设备的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, int pxVal) {
        return Math.round(pxVal / context.getResources().getDisplayMetrics().density);
    }

    /**
     * @param spVal
     * @return 根据设备的分辨率从 ps 的单位 转成为 px(像素)
     */
    public static int sp2px(Context context, int spVal) {
        return Math.round(spVal * context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * @param pxVal
     * @return 根据设备的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(Context context, int pxVal) {
        return Math.round(pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取手机当前的语言环境
     *
     * @param ctx
     * @return
     */
    public static String getPhoneLanguage(Context ctx) {
        Locale locale = ctx.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();

        Log.d("hh", "language =  " + language);
        Log.d("hh", "country =  " + country);

        String result = "zh-cn";
        if (language.equals("zh") && country.equalsIgnoreCase("CN")) {
            // 简体中文
            result = "zh-cn";
        } else if (language.equals("en")) {
            // 英文
            result = "en-us";
        } else if (language.equals("zh") && country.equalsIgnoreCase("TW")) {
            // 繁体中文
            result = "zh-tw";
        }
        return result;
    }

    /**
     * @return 设备唯一ID
     */
    public static String getDeviceId(Context context) {
        String deviceId = SharedPrefsHelper.get("devicesID", "");

        if (TextUtils.isEmpty(deviceId)) {
            TelephonyManager telManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
            deviceId = telManager.getDeviceId();

            if (TextUtils.isEmpty(deviceId)) {
                deviceId = Settings.Secure.getString(context.getContentResolver()
                        , Settings.Secure.ANDROID_ID);
                // 山寨机为固定 9774d56d682e549c
                if (!"9774d56d682e549c".equals(deviceId)) {
                    deviceId = UUID.randomUUID().toString().replaceAll("-", "");
                }
            }
        }

        SharedPrefsHelper.put("devicesID", deviceId);

        return deviceId;
    }

    /**
     * @return 手机号
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tm.getLine1Number();
        return TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("hh", "getAppVersionName error " + e.getMessage());
        }
        return versionName;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context) {
        int versionName = 1;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionCode;

        } catch (Exception e) {
            Log.e("hh", "getAppVersionName error " + e.getMessage());
        }
        return versionName;
    }

    /**
     * 获取系统SDK版本
     */
    public static int getSDKVersionNumber() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * @return 系统版本
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * @return 设备品牌
     */
    public static String getBrand() {
        return Build.MODEL;
    }

    /**
     * @return MAC 地址
     */
    public static String getMACAddress(Context context) {
        String macAddress = SharedPrefsHelper.get("macAddress", "");

        if (TextUtils.isEmpty(macAddress)) {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            if (null != wifiMgr) {
                WifiInfo info = wifiMgr.getConnectionInfo();
                if (null != info) {
                    macAddress = info.getMacAddress();
                    SharedPrefsHelper.put("macAddress", macAddress);
                }
            }
        }

        return macAddress;
    }

    /**
     * @param context
     * @param packageName
     * @return 判断应用是否安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

}
