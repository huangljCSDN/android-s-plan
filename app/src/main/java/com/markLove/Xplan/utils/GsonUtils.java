package com.markLove.Xplan.utils;

import com.google.gson.Gson;

/**
 * Created by hs on 2018/8/20.
 */
public class GsonUtils {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 将对象转化为json字符串
     *
     * @param o
     * @return
     */
    public static String obj2Json(Object o) {
        //数据特别大的情况下，应使用JsonReader
        return gson.toJson(o);
    }

    /**
     * 将json字符串转化成实体类
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }
}
