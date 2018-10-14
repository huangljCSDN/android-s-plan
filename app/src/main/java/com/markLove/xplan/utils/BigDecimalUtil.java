package com.markLove.xplan.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author ChenRen / 2015-02-15
 *         由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 *         确的浮点数运算，包括加减乘除和四舍五入。
 */
public class BigDecimalUtil {
    //默认除法运算精度
    private static final int SCALE_TWO = 2;

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, SCALE_TWO);
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    除数
     * @param v2    被除数(如果等于0,将返回0)
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (v2 == 0) {
            Ln.w("Division by zero, will return zero.");
            return 0;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数2位四舍五入处理。
     *
     * @param v 需要四舍五入的数字
     * @return 四舍五入后的结果
     */
    public static double round(double v) {
        return round(v, SCALE_TWO);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 元: 默认值
     */
    private final static String YUAN_DEFAULT = "0.00";

    /**
     * 分 转成 元. 保留两位小数, 小数位不足两位补0<br/>
     * 例: 123 -> 1.23
     *
     * @param cent 分
     * @return 元
     */
    public static String centToYuan(String cent) {
        if (TextUtils.isEmpty(cent) || "0".equals(cent) || "0.0".equals(cent)) {
            return YUAN_DEFAULT;
        }

        double yuan_double = div(Double.parseDouble(cent), 100);
        DecimalFormat df = new DecimalFormat("0.00");  // 格式化成两个小数, 不足补0
        return df.format(yuan_double);
    }

    /**
     * 元 转成 分. 全整数<br/>
     * 例: 1.23 -> 123<br/>
     * 1.235 -> 124
     *
     * @param yuan 元
     * @return 分
     */
    public static String yuanToCent(String yuan) {
        double cent_double = BigDecimalUtil.mul(Double.parseDouble(yuan), 100);
        DecimalFormat x = new DecimalFormat("##");  // 格式化成0小数
        return x.format(cent_double);
    }

    /**
     * double保留两位小数转成string
     *
     * @param d
     * @return 四舍五入后的结果
     */
    public static String doubleToMoney(double d) {
        DecimalFormat df = new DecimalFormat("0.00");  // 格式化成两个小数, 不足补0
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(d);
    }

    public static String floatToTime(double d) {
        DecimalFormat df = new DecimalFormat("00.00");  // 格式化成两个小数, 不足补0
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(d);
    }

    /**
     * 分 转成 元. 小数点末尾0舍弃掉
     * 例: 123 -> 1.23
     *
     * @param cent 分
     * @return 元
     */
    public static String centToYuan1(String cent) {
        if (TextUtils.isEmpty(cent) || "0".equals(cent) || "0.0".equals(cent)) {
            return "0";
        }
        double yuan_double = div(Double.parseDouble(cent), 100);
        DecimalFormat df = new DecimalFormat("0.00");  // 格式化成两个小数, 不足补0
        String s= df.format(yuan_double);
        if(s.endsWith(".00")){
          s=s.substring(0,s.length()-3);
        }else if(s.endsWith("0")){
          s=s.substring(0,s.length()-1);
        }
        return s;
    }



}
