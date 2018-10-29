package com.networkengine.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.networkengine.entity.AtInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * At表达式
 * Created by pwy on 2018/6/6.
 */
public class AtUtil {
    public static final String AT_ALL_ID = "0";
    /**
     * 默认字体大小
     */
    private static final int DEFAULT_FONT_SIZE_SP = 16;
    /**
     * 正则
     */
    private static final Pattern AT_PATTERN = Pattern.compile("@<\\S+:\\d+>\\s{1}"); // @<名字:ID>空格

    /**
     * 按规则生成字符串
     *
     * @param name 名字
     * @param id   ID
     * @return 生成的字符串
     */
    public static String encode(String name, String id) {
        return "@<" + name + ":" + id + "> ";
    }

    /**
     * 去掉规则，只显示@某人
     *
     * @param text 待处理的字符串
     * @return 处理后的字符串
     */
    public static String decode(String text) {
        Matcher m = AT_PATTERN.matcher(text);
        while (m.find()) {
            String str = m.group();
            str = str.substring(0, str.indexOf(":")).replace("<", "") + " ";
            text = text.replace(m.group(), str);
        }
        return text;
    }

    /**
     * 从内容中提取被@的人的信息
     *
     * @param text 待处理的字符串
     * @return 信息
     */
    public static ArrayList<AtInfo> getAtInfos(String text) {
        ArrayList<AtInfo> infos = new ArrayList<>();
        Matcher m = AT_PATTERN.matcher(text);
        while (m.find()) {
            String str = m.group();
            String id = str.substring(str.indexOf(":") + 1, str.indexOf(">"));
            String name = str.substring(str.indexOf("<") + 1, str.indexOf(":"));
            infos.add(new AtInfo(id, name));
        }
        return infos;
    }


    /**
     * 转换
     *
     * @param context   上下文
     * @param spannable 待转换
     * @param fontSize  字体大小
     * @return 已转换
     */
    public static Spannable spannableFilter(Context context, Spannable spannable, float fontSize) {
//        if (fontSize <= 0) {
//            fontSize = Util.sp2px(context, DEFAULT_FONT_SIZE_SP);
//        }
//        Matcher m = AT_PATTERN.matcher(spannable);
//        while (m.find()) {
//            String str = m.group();
//            str = str.substring(0, str.indexOf(":")).replace("<", "") + " ";
//
//            Paint paint = new Paint();
////            paint.setColor(ColorChangeUtil.getThemeColor());
//            paint.setAntiAlias(true);
//            paint.setTextAlign(Paint.Align.CENTER);
//            paint.setTextSize(fontSize);
//
//            Rect bounds = new Rect();
//            paint.getTextBounds(str, 0, str.length(), bounds);
////            int tw = bounds.width() + Util.dip2px(context, 10); // 文本宽度，补空格
////            int th = bounds.height() + Util.dip2px(context, 1); // 文本高度
////
////            Bitmap bitmap = Bitmap.createBitmap(tw, th, Bitmap.Config.ARGB_8888);
////            Canvas canvas = new Canvas(bitmap);
//            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
//            int baseline = (th - fontMetrics.bottom - fontMetrics.top) / 2 - Util.dip2px(context, 2);
//            canvas.drawText(str, tw / 2, baseline, paint);
//            ImageSpan imageSpan = new ImageSpan(context, bitmap);
//            spannable.setSpan(imageSpan, m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        return spannable;
    }
}
