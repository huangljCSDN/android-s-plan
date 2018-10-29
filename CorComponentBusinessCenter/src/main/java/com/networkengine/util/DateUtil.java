package com.networkengine.util;


/**
 * 日期格式化工具类
 *
 * @title DateUtil.java
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类.
 */

public class DateUtil {
    /**
     * 格式化年月日小时分秒
     */
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式化分秒
     */
    public static final String FORMAT_MS = "mm:ss";
    /**
     * 格式时分秒
     */
    public static final String FORMAT_HMS = "HH:mm:ss";
    /**
     * 格式到天
     */
    public static final String FORMAT_YMD = "yyyy-MM-dd";

    /**
     * 格式化
     *
     * @param str 需要格式的时间字符串
     * @return
     */
    public static Date str2Date(String str) {
        return str2Date(str, null);
    }

    /**
     * 时间格式化
     *
     * @param str    需要格式的时间字符串
     * @param format 格式类型
     * @return
     */
    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }

        Date date = new Date(Long.valueOf(str));

        return date;

    }

    /**
     * @param c
     * @return
     */
    public static String date2Str(Calendar c) {
        return date2Str(c, null);
    }

    /**
     * @param c
     * @param format
     * @return
     */
    public static String date2Str(Calendar c, String format) {
        if (c == null) {
            return "";
        }
        return date2Str(c.getTime(), format);
    }

    /**
     * @param d
     * @return
     */
    public static String date2Str(Date d) {
        return date2Str(d, null);
    }

    /**
     * @param d
     * @param format
     * @return
     */
    public static String date2Str(Date d, String format) {
        if (d == null) {
            return "";
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }


    /**
     * @param time
     * @return
     */
    public static String time2Str(long time) {
        return time2Str(time, null);
    }

    /**
     * @param time
     * @param format
     * @return
     */
    public static String time2Str(long time, String format) {

        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(time);
        return s;
    }


    /**
     * 获得当前日期的字符串格式
     *
     * @param format
     * @return
     */
    public static String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);
    }

    /**
     * 获取当前的时间
     * @return
     */
    public static String curTime2Str() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 获取时间格式
     *
     * @param trialTime
     * @return
     */
    public static String getFormatTime(Date trialTime) {

        final Calendar trial = Calendar.getInstance();
        trial.setTime(trialTime);
        final Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        SimpleDateFormat sdf;
        if (trial.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(trialTime).toString();
        } else if (trial.get(Calendar.MONTH) != now.get(Calendar.MONTH) || now.get(Calendar.DAY_OF_MONTH) - trial.get(Calendar.DAY_OF_MONTH) > 2) {
            sdf = new SimpleDateFormat("MM-dd HH:mm");
            return sdf.format(trialTime).toString();
        } else if (now.get(Calendar.DAY_OF_MONTH) - trial.get(Calendar.DAY_OF_MONTH) == 2) {
            sdf = new SimpleDateFormat("HH:mm");
            return "前天 " + sdf.format(trialTime).toString();
        } else if (now.get(Calendar.DAY_OF_MONTH) - trial.get(Calendar.DAY_OF_MONTH) == 1) {
            sdf = new SimpleDateFormat("HH:mm");
            return "昨天 " + sdf.format(trialTime).toString();
        } else {
            sdf = new SimpleDateFormat("HH:mm");
            return "今天 " + sdf.format(trialTime).toString();

        }
    }


}
