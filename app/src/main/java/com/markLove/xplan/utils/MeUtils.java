package com.markLove.xplan.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import com.markLove.xplan.BuildConfig;
import com.markLove.xplan.base.App;
import com.markLove.xplan.config.Constants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/4.
 */

public abstract class MeUtils {

    private static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sMMDDFormat = new SimpleDateFormat("MM月dd日");

    private static int ONE_MINUTE = 60 * 1000;
    private static int ONE_HOUR = 60 * ONE_MINUTE;
    private static int ONE_DAY = 24 * ONE_HOUR;
    private static int ONE_WEEK = 7 * ONE_DAY;



    /**
     * 根据屏幕获取列宽
     *
     * @param context
     * @param columns
     * @param extraRange
     * @return
     */
    public static int getColumnWidth(Context context, int columns, int extraRange) {
        int columnWidth = (getScreenWidth(context) - extraRange) / columns;
        return columnWidth;
    }


    public static String getOnlineTime(long loginTime, long currentTime) {
        long l = currentTime - loginTime;
        StringBuffer buffer = new StringBuffer();
        if (l < 0 || loginTime <= 0) {
            return buffer.append("当前在线").toString();
        } else if (l >= 0 && l < 30 * ONE_MINUTE) {
            return buffer.append("刚刚活跃").toString();
        } else {
            if (l >= 30 * ONE_MINUTE && l < 1 * ONE_HOUR) {
                buffer.append("30分钟");
            } else if (l >= 1 * ONE_HOUR && l < 2 * ONE_HOUR) {
                buffer.append("1小时");
            } else if (l >= 2 * ONE_HOUR && l < 3 * ONE_HOUR) {
                buffer.append("2小时");
            } else if (l >= 3 * ONE_HOUR && l < 4 * ONE_HOUR) {
                buffer.append("3小时");
            } else if (l >= 4 * ONE_HOUR && l < 5 * ONE_HOUR) {
                buffer.append("4小时");
            } else if (l >= 5 * ONE_HOUR && l < 6 * ONE_HOUR) {
                buffer.append("5小时");
            } else if (l >= 6 * ONE_HOUR && l < 7 * ONE_HOUR) {
                buffer.append("6小时");
            } else if (l >= 7 * ONE_HOUR && l < 8 * ONE_HOUR) {
                buffer.append("7小时");
            } else if (l >= 8 * ONE_HOUR && l < 9 * ONE_HOUR) {
                buffer.append("8小时");
            } else if (l >= 9 * ONE_HOUR && l < 10 * ONE_HOUR) {
                buffer.append("9小时");
            } else if (l >= 10 * ONE_HOUR && l < 11 * ONE_HOUR) {
                buffer.append("10小时");
            } else if (l >= 11 * ONE_HOUR && l < 12 * ONE_HOUR) {
                buffer.append("11小时");
            } else if (l >= 12 * ONE_HOUR && l < 13 * ONE_HOUR) {
                buffer.append("12小时");
            } else if (l >= 13 * ONE_HOUR && l < 14 * ONE_HOUR) {
                buffer.append("13小时");
            } else if (l >= 14 * ONE_HOUR && l < 15 * ONE_HOUR) {
                buffer.append("14小时");
            } else if (l >= 15 * ONE_HOUR && l < 16 * ONE_HOUR) {
                buffer.append("15小时");
            } else if (l >= 16 * ONE_HOUR && l < 17 * ONE_HOUR) {
                buffer.append("16小时");
            } else if (l >= 17 * ONE_HOUR && l < 18 * ONE_HOUR) {
                buffer.append("17小时");
            } else if (l >= 18 * ONE_HOUR && l < 19 * ONE_HOUR) {
                buffer.append("18小时");
            } else if (l >= 19 * ONE_HOUR && l < 20 * ONE_HOUR) {
                buffer.append("19小时");
            } else if (l >= 20 * ONE_HOUR && l < 21 * ONE_HOUR) {
                buffer.append("20小时");
            } else if (l >= 21 * ONE_HOUR && l < 22 * ONE_HOUR) {
                buffer.append("21小时");
            } else if (l >= 22 * ONE_HOUR && l < 23 * ONE_HOUR) {
                buffer.append("22小时");
            } else if (l >= 23 * ONE_HOUR && l < ONE_DAY) {
                buffer.append("23小时");
            } else if (l >= 1 * ONE_DAY && l < 2 * ONE_DAY) {
                buffer.append("1天");
            } else if (l >= 2 * ONE_DAY && l < 3 * ONE_DAY) {
                buffer.append("2天");
            } else if (l >= 3 * ONE_DAY && l < 4 * ONE_DAY) {
                buffer.append("3天");
            } else if (l >= 4 * ONE_DAY && l < 5 * ONE_DAY) {
                buffer.append("4天");
            } else if (l >= 5 * ONE_DAY && l < 6 * ONE_DAY) {
                buffer.append("5天");
            } else if (l >= 6 * ONE_DAY && l < ONE_WEEK) {
                buffer.append("6天");
            } else {
                buffer.append("1周");
            }
            return buffer.append("前在线").toString();
        }

    }


    public static boolean stringsIsEmpty(String... params) {
        boolean isEmpty = false;
        for (int i = 0; i < params.length; i++) {
            if (TextUtils.isEmpty(params[i])) {
                isEmpty = true;
                break;
            }
        }
        return isEmpty;
    }


    /**
     * 对字符串md5加密(小写+字母)
     *
     * @param str 传入要加密的字符串
     * @return MD5加密后的字符串
     */
    public static String getMD5(String str) {
        try {
            //生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取图片的旋转角度
     *
     * @param path
     * @return
     */
    public static int getPhotoDegree(String path) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);

        } catch (IOException e) {

            e.printStackTrace();
            exif = null;
        }
        int degree = 0;
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        }
        return degree;
    }


    public static String getString(int resId) {
        return App.getInstance().getString(resId);
    }

    /**
     * @return 返回boolean, 判断网络是否可用, 是否为移动网络
     */

    public static boolean hasGPRSConnection(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if (wifiNetworkInfo != null && wifiNetworkInfo.isAvailable() && wifiNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
            return false;
        } else if (mobileNetworkInfo != null && mobileNetworkInfo.isAvailable() && mobileNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取指定文件大小(单位：字节)
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }

        }
        return size;
    }

    public static String formatPublishTime(String time) {
        Date date = parse_yyyyMMdd_hhmmss_date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuffer buffer = new StringBuffer();
        String timeStr = buffer.append(month + "").append("月").append(day + "").append("日").toString();
        // String format = sMMDDFormat.format(t);
        return timeStr;
//        try {
//            long t = parse_yyyyMMdd_hhmmss_long(time);
//            long currentTime = System.currentTimeMillis();
//            if (currentTime - t < 60 * 1000) {
//                return "刚刚";
//            } else if (currentTime - t >= 60 * 1000 && currentTime - t < 3 * 60 * 1000) {
//                return "1分钟前";
//            } else if (currentTime - t >= 3 * 60 * 1000 && currentTime - t < 5 * 60 * 1000) {
//                return "3分钟前";
//            } else if (currentTime - t >= 5 * 60 * 1000 && currentTime - t < 15 * 60 * 1000) {
//                return "5分钟前";
//            } else if (currentTime - t >= 15 * 60 * 1000 && currentTime - t < 30 * 60 * 1000) {
//                return "15分钟前";
//            } else if (currentTime - t >= 30 * 60 * 1000 && currentTime - t < ONE_HOUR) {
//                return "30分钟前";
//            } else if (currentTime - t >= ONE_HOUR && currentTime - t < 3 * ONE_HOUR) {
//                return "1小时前";
//            } else if (currentTime - t >= 3 * ONE_HOUR && currentTime - t < 5 * ONE_HOUR) {
//                return "3小时前";
//            } else if (DataUtils.IsToday(time)) {
//                return "今天";
//            } else if (DataUtils.IsYesterday(time)) {
//                return "昨天";
//            } else {
//                return time;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return time;
//        }

    }

    public static long getVideoDuration(Context context, Uri uri) {
        long duration = 0;
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(context, uri);  //recordingFilePath（）为音频文件的路径
            player.prepare();
            duration = player.getDuration();//获取音频的时间
            Log.d("ACETEST", "### duration: " + duration);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            player.release();//记得释放资源
        }
        return duration / 1000;
    }

    public static String formatVideoDuration(int duration) {
        if (duration < 10) {
            return "0:0" + duration + "″";
        } else if (duration < 60) {
            return "0:" + duration + "″";
        } else {
            StringBuffer buffer = new StringBuffer();
            int m = duration / 60;
            buffer.append(m + ":");
            int s = duration - m * 60;
            if (s < 10) {
                buffer.append("0").append(s + "″");
            } else {
                buffer.append(s + "″");
            }
            return buffer.toString();
        }
    }

    public static String getResString(int resId) {
        return App.getInstance().getResources().getString(resId);
    }

    public static void loge(String tag, Object obj) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "loge: " + obj);
        }
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public static String format_yyyyMMdd(String date) {
        return sFormat.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public static String format_yyyyMMdd(Date date) {
        String format = sFormat.format(date);
        return format;
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public static String format_yyyyMMdd_hhmmss(Date date) {
        String format = sDateFormat.format(date);
        return format;
    }


    /**
     * 解析时间
     *
     * @param date
     * @return
     */
    public static long parse_yyyyMMdd_hhmmss_long(String date) {
        try {
            return sDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 解析时间
     *
     * @param date
     * @return
     */
    public static Date parse_yyyyMMdd_hhmmss_date(String date) {
        try {
            return sDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取屏幕的宽高比
     *
     * @param context
     * @return
     */
    public static float getScreenScale(Context context) {
        float scale = getScreenWidth(context) * 1.0f / getScreenHeight(context);
        return scale;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return ScreenUtils.getScreenWidth(context);
    }

    public static int getScreenHeight(Context context) {
        return ScreenUtils.getScreenHeight(context);
    }

    /**
     * db转换成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转成px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * px转换成dp
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转换成sp
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * @param context
     * @param toast
     */
    public static void showToast(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringResId) {
        Toast.makeText(context, App.getInstance().getString(stringResId), Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @param textView
     * @param resId
     * @param direction 1:left , 2:top , 3:right, 4:bottom
     */
    public static void setTextViewDrawable(Context context, TextView textView, int resId, int direction) {
        Drawable drawable = context.getResources().getDrawable(resId);// 找到资源图片。
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置图片宽高
        switch (direction) {
            case 1:
                textView.setCompoundDrawables(drawable, null, null, null);
                break;
            case 2:
                textView.setCompoundDrawables(null, drawable, null, null);
                break;
            case 3:
                textView.setCompoundDrawables(null, null, drawable, null);
                break;
            case 4:
                textView.setCompoundDrawables(null, null, null, drawable);
                break;
        }

    }

    public static Map<String, String> getDefaultParams(Context context) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", PreferencesUtils.getString(context, Constants.TOKEN_KEY));

        return params;
    }

    /**
     * 获取视频文件截图
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public static String getVideoThumb(Context context, String path) throws IOException {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        Bitmap bitmap = media.getFrameAtTime();
        return saveFile(context, bitmap, System.currentTimeMillis() + ".jpg");


    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static String saveFile(Context context, Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/ThumbFiles/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile.getAbsolutePath();
    }

    /**
     * 格式化数据
     *
     * @param number
     * @return
     */
    public static String formatNumber(int number) {
        int i = number / 1000;//小数点前
        int i1 = (number - i * 1000) / 100;//小数点后

        StringBuffer buffer = new StringBuffer();
        if (i > 0) {
            buffer.append(i + "").append(".").append(i1 + "").append("K");
        } else {
            buffer.append(number + "");
        }
        return buffer.toString();
    }

    /**
     * 播放视频
     *
     * @param context
     * @param url
     */
    public static void playVideo(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String type = "video/*";
        Uri uri = Uri.parse(url);
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }
}
