package com.markLove.Xplan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/6.
 */

public class DataUtils {
    /**
     * 判断给定字符串时间是否为今日(效率不是很高，不过也是一种方法)
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(long day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = new Date(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    public static int getDateDistanceNow(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(str);
        long s1 = date.getTime();//将时间转为毫秒
        long s2 = System.currentTimeMillis();//得到当前的毫秒
        int day = (int) ((s2 - s1) / 1000 / 60 / 60 / 24);
        return day+1;

       /* Date time = toDate(str);
        Calendar cal = Calendar.getInstance();
        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        Log.d("oyf",lt+"---"+ct+"---"+days);
        return days;*/
    }
    public void friendlyTimeFormat(String sdate) {

    }




    public static String getContentText(String s) {
        String str = "";
        s = s.replace("<br/>", "");
        if (!s.contains("</p>")) {
            return "";
        }
        String[] split = s.split("</p>");
        if (split.length > 0) {
            str = split[0].replace("<p>", "");
            if (str.contains("<img")) {
                int i = str.indexOf("<img");//img标签的位置
                str = str.substring(0, i);
            }
        }
        return str;
    }

    public static ArrayList<String> getContentImg(String s) {
        ArrayList arrayList= new ArrayList();
        if (!s.contains("src=")) {
            arrayList.add("");
            return arrayList;
        }
        String[] split = s.split("src=\"");
        if (split.length==2) {
            int i = split[1].indexOf("\"");
            arrayList.add(split[1].substring(0, i));
        }else if (split.length>2){
            if (split.length>10){
                for (int i=1;i<10;i++){
                    arrayList.add(split[i].substring(0,split[i].indexOf("\"")));
                }
            }else {
            for (int i=1;i<split.length;i++){
                arrayList.add(split[i].substring(0,split[i].indexOf("\"")));
            }}
        }
        return arrayList;
    }

//    public static String str2HTML(Map<String, String> map, List<SEditorData> sEditorDatas) {
//        StringBuilder str = new StringBuilder();
//        for (SEditorData mSEditorData : sEditorDatas) {
//            if (!TextUtils.isEmpty(mSEditorData.getInputStr())) {
//                String inputStr = mSEditorData.getInputStr();
//                inputStr = inputStr.replace("<br>", "");
//                if (!inputStr.contains("<p>")) {
//                    str.append("<p>");
//                    str.append(inputStr);
//                    str.append("</p>");
//                    str.append("<br/>");
//                } else {
//                    str.append(inputStr);
//                }
//            }
//            if (mSEditorData.getImagePath() != null) {
//                String s = map.get(mSEditorData.getImagePath());
//                str.append("<img src=\"");
//                str.append(s);
//                str.append("\"/>");
//                str.append("<br/>");
//            }
//        }
//        return str.toString();
//    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getDatetime() {
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return sdfIn.format(date);
    }

    /**
     * 返回指定时间字符串�??
     * <p>
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return String 指定格式的日期字符串.
     */
    public static String getDateTime(long microsecond) {
        return getFormatDateTime(new Date(microsecond), "yyyy/MM/dd HH:mm:ss");
    }

    /**
     * 根据给定的格式与时间(Date类型�?)，返回时间字符串。最为�?�用�?<br>
     *
     * @param date   指定的日�?
     * @param format 日期格式字符�?
     * @return String 指定格式的日期字符串.
     */
    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    /**
     * 根据指定的日期，返回其时间戳
     *
     * @param dateString 指定的日期，时间格式为yyyy/MM/dd HH:mm:ss
     * @return 日期的时间戳
     */
    public static long getMillisFromDateString(String dateString) {
        long diff = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date startDate = ft.parse(dateString);
            diff = startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    public static String str2time(String creatorTime){
        try {
            creatorTime = creatorTime.replace("/", "-");

            int dateDistanceNow = DataUtils.getDateDistanceNow(creatorTime);
            if (DataUtils.IsToday(creatorTime)){//是否是今天
                creatorTime="今天 ";
            }else{
                if (DataUtils.IsYesterday(creatorTime)){//是否是昨天
                    creatorTime="昨天 ";
                }else{
                    if (dateDistanceNow>1&&dateDistanceNow<8){
                        creatorTime=dateDistanceNow+"天之前 ";
                    }else if(dateDistanceNow==1){
                        creatorTime="昨天 ";
                    }else {
                        creatorTime="7天之前";
                    }
                }
            }
            return creatorTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //根据秒数转化为时分秒   00:00:00
    public static String second2time(long second) {
        if (second < 10) {
            return "00:00:0" + second;
        }
        if (second < 60) {
            return "00:00:" + second;
        }
        if (second < 3600) {
            int minute = (int) (second / 60);
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "00:"+"0" + minute + ":0" + second;
                }
                return "00:"+"0" + minute + ":" + second;
            }
            if (second < 10) {
                return "00:"+minute + ":0" + second;
            }
            return "00:"+minute + ":" + second;
        }
        int hour = (int) (second / 3600);
        int minute = (int) ((second - hour * 3600) / 60);
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour +":"+ minute + ":0" + second;
            }
            return "0" + hour +":"+ minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour +":"+ minute + ":0" + second;
        }
        return hour +":"+ minute + ":" + second;
    }
}
