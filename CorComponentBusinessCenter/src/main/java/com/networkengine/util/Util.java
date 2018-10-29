package com.networkengine.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.networkengine.engine.LogicEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author ouyangbo
 * @date 2014-12-25
 * @email ouyangbo@kingnode.com
 * @remark
 * @modify by
 */
@SuppressWarnings("ALL")
public class Util {

    /**
     * dp转换px
     */
    public static int dip2px(Context context, float dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources()
                .getDisplayMetrics());
    }

    /**
     * sp转换px
     */
    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources()
                .getDisplayMetrics());
    }

    /**
     * 判断是否为空
     *
     * @param value
     * @return
     */
    public static boolean isNull(String value) {
        if (null != value && !"null".equals(value) && value.length() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 计算百分比
     *
     * @param all , pro
     * @return
     */
    public static String getSHCollagen(int all, int pro) {
        String str = "";
        if (all < 0 || pro < 0 || all < pro) {
            return str;
        }
        try {
            double proTemp = (double) pro * 100;
            double allTemp = all;
            BigDecimal bigPro = new BigDecimal(proTemp + "");
            BigDecimal bigAll = new BigDecimal(allTemp + "");
            BigDecimal proDou = bigPro.divide(bigAll, 2, BigDecimal.ROUND_HALF_UP);
            str = proDou.toString();
            if (str.indexOf(".") > 0) {
                str = str.substring(0, str.indexOf("."));
            }
        } catch (Exception e) {
            LogUtil.e("error", e);
        }
        return str + "%";
    }

    /**
     * 当前手机是否有可用网络 (所有网络类型)
     *
     * @param context
     * @return
     */
    public static boolean isNetConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {

                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        if (info.isAvailable()) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取应用当前版本号
     *
     * @param context
     * @return
     */
    //public static String getAppVersion() {
//        PackageManager pm = AppContext.getInstance().getPackageManager();
//        try {
//            PackageInfo packinfo = pm.getPackageInfo(AppContext.getInstance().getPackageName(), 0);
//            return packinfo.versionName;
//        } catch (NameNotFoundException e) {
//            LogUtil.exception(e);
//            return "1.0";
//        }
    //   }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context mCt) {
        try {
            PackageManager packageManager = mCt.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mCt.getPackageName(), 0);
            return packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 收集设备参数信息
     *
     * @param context
     */
    public static String collectDeviceInfo(Context context) {
        Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
        try {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            LogUtil.e("error", e);
        }

        Field[] fields = Build.class.getDeclaredFields();// 反射机制
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
            } catch (IllegalArgumentException e) {
                LogUtil.e("error", e);
            } catch (IllegalAccessException e) {
                LogUtil.e("error", e);
            }
        }

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }
        sb.append("\r\n");
        sb.append("*********user=" + LogicEngine.getInstance().getUser().getId());
        sb.append("*********\r\n");
        return sb.toString();
    }

    /**
     * 消除notification通知
     */
    public static void cancelNoty() {
//        NotificationManager myNotiManager = (NotificationManager) AppContext.getInstance().getSystemService(
//                Context.NOTIFICATION_SERVICE);
//        myNotiManager.cancel(0);
    }

//    /**
//     * 获取 移动终端设备id号
//     *
//     * @param context :上下文文本对象
//     * @return id 移动终端设备id号
//     */
//    public static String getDeviceId(Context context) {
//        String id = PreferenceUtils.getInstance().getString("devicesID", "");
//        if (id.length() == 0) {
//            try {
//                id = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//            } catch (Exception e) {
//            }
//            if (id == null)
//                id = "";
//        }
//        if (id.length() == 0) {
//            try {
//                id = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
//            } catch (Exception e) {
//            }
//            if (id == null)
//                id = "";
//        }
//        if (id.length() == 0) {
//            try {
//                id = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
//            } catch (Exception e) {
//            }
//            if (id == null)
//                id = "";
//        }
//        if (id.length() == 0) {
//            try {
//                Class<?> c = Class.forName("android.os.SystemProperties");
//                Method get = c.getMethod("get", String.class, String.class);
//                id = (String) (get.invoke(c, "ro.serialno", "unknown"));
//            } catch (Exception e) {
//            }
//            LogUtil.i("id is " + id);
//        }
//        if (id.length() == 0 || "0".equals(id)) {
//            // 随机生成
//            id = UUID.randomUUID().toString().replaceAll("-", "");
//            PreferenceUtils.getInstance().putString("devicesID", id);
//        }
//        return id;
//    }

    /**
     * 多电话拨打
     *
     * @param context
     * @param phones
     */
    public static void selectCallPhone(final Context context, final String[] phones) {
//        TextView titleView = new TextView(context);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context).setCustomTitle(titleView).setItems(phones,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String phone = phones[which];
//                        goCallPhone(context, phone);
//                        dialog.dismiss();
//                    }
//                });
//        builder.setTitle(context.getString(R.string.select_phone_app)).setNegativeButton(android.R.string.cancel, null)
//                .show();
    }

    /**
     * 单号码拨打
     *
     * @param context
     * @param phone
     */
    public static void goCallPhone(Context context, String phone) {
        if (!isNull(phone)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

//    /**
//     * 提示更新
//     *
//     * @param uri   下载地址
//     * @param title 显示标题
//     * @param type  类型， 0：普通更新，1：强制更新
//     */
//    public static void showUpdateDialog(final Context context, final String uri, String title, final String message,
//                                        int type, final boolean isFinish) {
//        // show dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(context.getResources().getString(R.string.txt_found_new_version));
//        builder.setMessage(title);
//        builder.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 开始下载
//                try {
//                    String url = AppContext.getInstance().getAppHost() + uri;
//                    // 进入下载界面开始下载
//                    // String fileName = url.indexOf(".") > 0 ? url.substring(url.lastIndexOf("."), url.length()) : "";
//                    String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
//                    KndDownLoadManager.getInstance().startNotiUpdateTask(context, url, fileName);
//                } catch (Exception e) {
//                    LogUtil.e("error",e);
//                }
//                PreferenceUtils.getInstance().putBoolen("isupdateshow", false);
//                if (isFinish) {
//                    ((Activity) context).finish();
//                }
//            }
//        });
//        if (type == 0) {
//            builder.setNegativeButton(context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if (isFinish) {
//                        ((Activity) context).finish();
//                    }
//                    // 0514_有关检测版本更新提示
//                    PreferenceUtils.getInstance().putBoolen("isOneChecUp", true);
//                    PreferenceUtils.getInstance().putBoolen("isupdateshow", false);
//                }
//            });
//        }
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
//
//        Boolean isshow = PreferenceUtils.getInstance().getBoolean("isupdateshow", false);
//        if (!isshow) {
//            alertDialog.show();
//            PreferenceUtils.getInstance().putBoolen("isupdateshow", true);
//        }
//
//
//    }

    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String formatFileLen(long size) {
        if (size >= 1024 * 1024 * 1024) {
            return String.format("%.1fG", (size * 1.0) / (1024 * 1024 * 1024));
        }
        if (size > 1024 * 1024) {
            return String.format("%.1fM", (size * 1.0) / (1024 * 1024));
        }
        if (size > 1024) {
            return String.format("%.1fK", (size * 1.0) / 1024);
        }
        return size + "B";
    }

    /**
     * 删除字符串中的换行以及空白符 主要是处理图片转base64后里面带有回车
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断系统是否安装某应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        boolean hasInstalled = false;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> list = pm.getInstalledPackages(PackageManager.PERMISSION_GRANTED);
        for (PackageInfo p : list) {
            if (packageName != null && packageName.equals(p.packageName)) {
                hasInstalled = true;
                break;
            }
        }
        return hasInstalled;
    }

    /**
     * 获取手机号码
     */
    public static String getPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String pin = tm.getLine1Number();
        if (isNull(pin)) {
            // pin = PreferenceUtils.getInstance().getString(PubConstant.PHONE_NUMBER, "");
        }
        return pin;
    }

    /**
     * 解压压缩文件
     *
     * @param zipPath
     * @param toPath
     * @return
     */
    public static boolean unZipResourcePackage(File zipFile, String descDir) {
        boolean success = true;
        try {
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            ZipFile zip = new ZipFile(zipFile);
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = (descDir + "/" + zipEntryName).replaceAll("\\*", "/");
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
            System.out.println("******************解压完毕********************");
        } catch (Exception e) {
            LogUtil.e("error", e);
            success = false;
        }
        return success;
    }


    /**
     * 弹出提示框退出应用
     */
    public static void showLogOutDialog(final Context context, final boolean exitApp) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(context.getString(R.string.exit_prompt_title));
//        builder.setMessage(context.getString(R.string.sign_in));
//        builder.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                LogicEngine.getInstance().getSystemController().logout(Util.getDeviceId(context), com.networkengine.PubConstant.PLATEFORM, new BusinessController.BusinessHandler<String>() {
//                    @Override
//                    public void onFail(Integer code) {
//                        ToastUtil.showToast(context, R.string.login_out_fail);
//                    }
//
//                    @Override
//                    public void onSuccess(String s, Integer code) {
//                        if (!exitApp) { // 注销操作
////							context.sendBroadcast(new Intent(PubConstant.CLEAR_LOGIN_PWD));
//                        }
//                        AppManager.getAppManager().AppExit(context, exitApp);
//
//                        User user = LogicEngine.getInstance().getContactController().getUser(MsgSyncCenter.getInstance(context).getDndUserId());
//                        UmsUtils.onEvent(context, "退出登陆", user != null ? user.getUserName() : "UNKNOW", 1);
//                    }
//                });
////                LogOut params = new LogOut();
////                new RequestTask(context, new RequestListener() {
////                    @Override
////                    public void responseResult(JSONObject jsonObject) {
////                        if (!exitApp) { // 注销操作
//////							context.sendBroadcast(new Intent(PubConstant.CLEAR_LOGIN_PWD));
////                        }
////                        AppManager.getAppManager().AppExit(context, exitApp);
////                        User user = UserManager.getInstance(context).getUserById(MsgSyncCenter.getInstance(context).getDndUserId());
////                        UmsUtils.onEvent(context, "退出登陆", user != null ? user.getName() : "UNKNOW", 1);
////                    }
////
////                    @Override
////                    public void responseException(String errorMessage) {
////                        ToastUtil.showToast(context, R.string.login_out_fail);
////                    }
////                }, true, "正在退出", "logout").execute(params);
//            }
//        });
//        builder.setNegativeButton(context.getString(android.R.string.cancel), null);
//        builder.create().show();
    }

    /**
     * 弹出AlertDialog
     *
     * @param context          -- 上下文
     * @param title            -- 标题
     * @param content          --内容
     * @param confirmListenner -- 确认回调
     * @param cancelListenner  -- 取消回调
     * @param type             -- 显示类型，0=默认触摸空闲区域会消失dialog；1=触摸空闲区域不会消失dialog
     */
    public static void showDialog(Context context, String title, String content, String confirmLab,
                                  DialogInterface.OnClickListener confirmListenner, String cancelLab,
                                  DialogInterface.OnClickListener cancelListenner, int type) {

//        PromptDialog.Builder builder = new PromptDialog.Builder(context);
//        if (!TextUtils.isEmpty(title)) {
//            builder.setTitle(title);
//        }
//        if (!TextUtils.isEmpty(content)) {
//            builder.setMessage(content);
//        }
//        if (!TextUtils.isEmpty(confirmLab)) {
//            builder.setPositiveButton(confirmLab, confirmListenner != null ? confirmListenner : null);
//        }
//        if (!TextUtils.isEmpty(cancelLab)) {
//            builder.setNegativeButton(cancelLab, cancelListenner != null ? cancelListenner : null);
//        }
//        if (type == 1) {
//            builder.setCanceledOnTouchOutside(false);
//            builder.setCancelable(false);
//        }
//        PromptDialog alertDialog = builder.create();
//        alertDialog.show();

    }

    /**
     * 判断URL是否为MXM或者MCHL的
     *
     * @param url
     * @return
     * @updateTime 2016-5-25 下午2:22:37
     * @updateAuthor Troy
     * @updateInfo
     */
    public static boolean isMxmOrMchlUrl(String url) {
        if (url == null) {
            return false;
        }
//        if (url.contains(AppContext.getInstance().getAppHost()) || url.contains(AppContext.getInstance().getMchlHost())) {
//            return true;
//        }
        return false;
    }

    private static DisplayMetrics dm;
    static {
        WindowManager wm = (WindowManager) CoracleSdk.getCoracleSdk().getContext().getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return dm;
    }
}
