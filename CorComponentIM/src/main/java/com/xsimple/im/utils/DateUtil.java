//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xsimple.im.utils;

import com.xsimple.im.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_MS = "mm:ss";
    public static final String FORMAT_HMS = "HH:mm:ss";
    public static final String FORMAT_YMD = "yyyy-MM-dd";

    public DateUtil() {
    }

    public static Date str2Date(String str) {
        return str2Date(str, (String) null);
    }

    public static Date str2Date(String str, String format) {
        if (str != null && str.length() != 0) {
            Date date = new Date(Long.valueOf(str).longValue());
            return date;
        } else {
            return null;
        }
    }

    public static String date2Str(Calendar c) {
        return date2Str((Calendar) c, (String) null);
    }

    public static String date2Str(Calendar c, String format) {
        return c == null ? "" : date2Str(c.getTime(), format);
    }

    public static String date2Str(Date d) {
        return date2Str((Date) d, (String) null);
    }

    public static String date2Str(Date d, String format) {
        if (d == null) {
            return "";
        } else {
            if (format == null || format.length() == 0) {
                format = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String s = sdf.format(d);
            return s;
        }
    }

    public static String time2Str(long time) {
        return time2Str(time, (String) null);
    }

    public static String time2Str(long time, String format) {
        if (format == null || format.length() == 0) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(Long.valueOf(time));
        return s;
    }

    public static String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);
    }

    public static String curTime2Str() {
        return (new SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date());
    }

    //  Calendar now = Calendar.getInstance();
    //now.setTime(new Date());

    //        Log.e("hh", "trial.get(YEAR) " + trial.get(Calendar.YEAR));
//        Log.e("hh", "trial.get(MONTH) " + trial.get(Calendar.MONTH));
//        Log.e("hh", "trial.get(DATE) " + trial.get(Calendar.DATE));
//        Log.e("hh", "trial.get(HOUR_OF_DAY) " + trial.get(Calendar.HOUR_OF_DAY));
//        Log.e("hh", "trial.get(MINUTE) " + trial.get(Calendar.MINUTE));
//        Log.e("hh", "trial.get(SECOND) " + trial.get(Calendar.SECOND));
//        Log.e("hh", "trial.get(DAY_OF_WEEK) " + trial.get(Calendar.DAY_OF_WEEK));
//
//        Log.e("hh", "now.get(YEAR) " + now.get(Calendar.YEAR));
//        Log.e("hh", "now.get(MONTH) " + now.get(Calendar.MONTH));
//        Log.e("hh", "now.get(DATE) " + now.get(Calendar.DATE));
//        Log.e("hh", "now.get(HOUR_OF_DAY) " + now.get(Calendar.HOUR_OF_DAY));
//        Log.e("hh", "now.get(MINUTE) " + now.get(Calendar.MINUTE));
//        Log.e("hh", "now.get(SECOND) " + now.get(Calendar.SECOND));
//        Log.e("hh", "now.get(DAY_OF_WEEK) " + now.get(Calendar.DAY_OF_WEEK));


    /**
     * 统一时间显示
     *
     * @param trialTime
     * @return
     */
    public static String getFormatDate(Date trialTime) {

        SimpleDateFormat sdf;
//        int yeaterday = 2;
//        try {
//            yeaterday = isYeaterday(trialTime);
//        } catch (ParseException e) {
//            yeaterday = 2;
//        }
//        //今天的消息，不带日期
//        if (yeaterday == -1) {
//            int time = (int) ((System.currentTimeMillis() - trialTime.getTime()) / 1000);
//            if (time >= 0 && time <= 60) {
//                return CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_just_now);
//            } else if (time > 60 && time <= 300) {
//                return Math.max(time / 60, 1) + CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_minutes_ago);
//            } else {
//                sdf = new SimpleDateFormat("aHH:mm");
//                return sdf.format(trialTime).toString();
//            }
//
//        }
//        //昨天的消息
//        if (yeaterday == 0) {
//            sdf = new SimpleDateFormat("aHH:mm");
//            return CoracleSdk.getCoracleSdk().getContext().getString(R.string.base_yesterday) + sdf.format(trialTime).toString();
//        }
//        //一周内的消息
//        if (yeaterday == 1) {
//            sdf = new SimpleDateFormat("aHH:mm");
//            Calendar trial = Calendar.getInstance();
//            trial.setTime(trialTime);
//            return CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_week) + getWeek(trial.get(Calendar.DAY_OF_WEEK)) + " " + sdf.format(trialTime).toString();
//        }
//        sdf = new SimpleDateFormat("yyyy-MM-dd aHH:mm");mm
        return "";

    }


    /**
     * 群文件的时间显示
     *
     * @param trialTime
     * @return
     */
    public static String getFormatDateToGroupFile(Date trialTime) {

        SimpleDateFormat sdf;
//        int yeaterday = 2;
//        try {
//            yeaterday = isYeaterday(trialTime);
//        } catch (ParseException e) {
//            yeaterday = 2;
//        }
//        //今天的HH:mm
//        if (yeaterday == -1) {
//            sdf = new SimpleDateFormat("HH:mm");
//            return sdf.format(trialTime).toString();
//        }
//        //昨天
//        if (yeaterday == 0) {
//            return CoracleSdk.getCoracleSdk().getContext().getString(R.string.business_yesterday);
//        }
//        //同一年的
//        Calendar trial = Calendar.getInstance();
//        trial.setTime(trialTime);
//        int year = trial.get(Calendar.YEAR);
//        Calendar trialNow = Calendar.getInstance();
//        trialNow.setTime(new Date());
//        int yearNow = trialNow.get(Calendar.YEAR);
//        if (year == yearNow) {
//            sdf = new SimpleDateFormat("MM-dd");
//            return sdf.format(trialTime).toString();
//        }
//        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "";
    }


    private static String getWeek(int week) {
        if (week == 1) {
            return "日";
        } else if (week == 2) {
            return "一";
        } else if (week == 3) {
            return "二";
        } else if (week == 4) {
            return "三";
        } else if (week == 5) {
            return "四";
        } else if (week == 6) {
            return "五";
        } else if (week == 7) {
            return "六";
        }
        return "";
    }


    /**
     * @param oldTime 较小的时间
     * @return -1 ：同一天.    0：昨天 .   1 :一周内. 一周以前2
     * @author LuoB.
     */
    private static int isYeaterday(Date oldTime) throws ParseException {
        Date newTime = new Date();
        //将下面的 理解成  yyyy-MM-dd 00：00：00 更好理解点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(newTime);
        //今天0时
        Date today = format.parse(todayStr);
        //昨天 86400000=24*60*60*1000 一天
        if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime() - oldTime.getTime()) <= 86400000) {
            return 0;
        } else if ((today.getTime() - oldTime.getTime()) > 86400000 && (today.getTime() - oldTime.getTime()) <= 86400000 * 8) {
            return 1;
        } else if ((today.getTime() - oldTime.getTime()) <= 0) { //至少是今天
            return -1;
        } else { //至少是一周前
            return 2;
        }

    }


    public static String getFormatTime(Date trialTime) {
//        Calendar trial = Calendar.getInstance();
//        trial.setTime(trialTime);
//        Calendar now = Calendar.getInstance();
//        now.setTime(new Date());
//        SimpleDateFormat sdf;
////        Log.e("hh", "trial.get(1) " + trial.get(1));
////        Log.e("hh", "trial.get(2) " + trial.get(2));
////        Log.e("hh", "trial.get(3) " + trial.get(3));
////        Log.e("hh", "trial.get(4) " + trial.get(4));
////        Log.e("hh", "trial.get(5) " + trial.get(5));
////
////
////        Log.e("hh", "now.get(1) " + now.get(1));
////        Log.e("hh", "now.get(2) " + now.get(2));
////        Log.e("hh", "now.get(3) " + now.get(3));
////        Log.e("hh", "now.get(4) " + now.get(4));
////        Log.e("hh", "now.get(5) " + now.get(5));
//
//        if (trial.get(1) != now.get(1)) {
//            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            return sdf.format(trialTime).toString();
//        } else if (trial.get(2) == now.get(2) && now.get(5) - trial.get(5) <= 2) {
//            if (now.get(5) - trial.get(5) == 2) {
//                sdf = new SimpleDateFormat("HH:mm");
//                return sdf.format(trialTime).toString();
//            } else if (now.get(5) - trial.get(5) == 1) {
//                sdf = new SimpleDateFormat("HH:mm");
//                return sdf.format(trialTime).toString();
//            } else {
//                sdf = new SimpleDateFormat("HH:mm");
//                return sdf.format(trialTime).toString();
//            }
//        } else {
//            sdf = new SimpleDateFormat("MM-dd HH:mm");
//            return sdf.format(trialTime).toString();
//        }
        return "";
    }

    /**
     * 收藏显示的时间格式
     *
     * @param trialTime
     * @return
     */
    public static String getCollectionFormatTime(Date trialTime) {
//        Calendar trial = Calendar.getInstance();
//        trial.setTime(trialTime);
//        Calendar now = Calendar.getInstance();
//        now.setTime(new Date());
//        SimpleDateFormat sdf;
//        if (trial.get(1) != now.get(1)) {
//            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            return sdf.format(trialTime).toString();
//        } else if (trial.get(2) == now.get(2) && now.get(5) - trial.get(5) <= 2) {
//            if (now.get(5) - trial.get(5) == 1) {
//                return CoracleSdk.getCoracleSdk().getContext().getString(com.networkengine.R.string.base_yesterday);
//            } else {
//                return CoracleSdk.getCoracleSdk().getContext().getString(com.networkengine.R.string.base_today);
//            }
//        } else {
//            sdf = new SimpleDateFormat("MM-dd HH:mm");
//            return sdf.format(trialTime).toString();
//        }
        return "";
    }
}
