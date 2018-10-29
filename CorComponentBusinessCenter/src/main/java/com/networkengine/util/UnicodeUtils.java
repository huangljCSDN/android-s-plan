package com.networkengine.util;

import android.text.TextUtils;

/**
 * Created by liuhao on 2017/5/16.
 */

public class UnicodeUtils {

    /**
     * unicode 转字符串
     *
     * @param unicode 字符串
     * @return
     */
    public static String unicode2String(String unicode) {
        if (TextUtils.isEmpty(unicode))
            return unicode;
        if (!unicode.startsWith("\\u")) {
            return unicode;
        }
        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }


    /**
     * 字符串转换unicode
     *
     * @param string 输入的字符串
     * @return
     */
    public static String string2Unicode(String string) {
        if (TextUtils.isEmpty(string))
            return string;
        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }
}
