package com.networkengine.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static Gson gson = new Gson();

    private JsonUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * @param json
     * @param clazz
     * @return 解析 Json 中的数据封装到指定对象
     */
    public static <T> T fromJson(String json, @NonNull Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }


    /**
     * @param json
     * @return 解析 Json 中的数据封装到List中
     */
    public static <T> List<T> fromJsonList(String json) {
        return gson.fromJson(json, new TypeToken<List<T>>() {}.getType());
    }

    /**
     * @param json
     * @return 解析 Json 中的数据封装到Map中
     */
    public static <K, V> Map<K, V> fromJsonMap(String json) {
        return gson.fromJson(json, new TypeToken<Map<K, V>>() {}.getType());
    }

    /**
     * @param obj
     * @return 解析指定对象为 json 格式
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * @param json
     * @param key
     * @return 获取 Json 中指定 key 值对应的 json 数据
     */
    public static String getString(String json, String key) {
        JsonElement je = new JsonParser().parse(json);
        return je == null ? null : je.getAsJsonObject().get(key).getAsString();
    }

    public static int getInt(String json, String key) {
        try {
            JsonElement je = new JsonParser().parse(json);
            if (je != null && je.getAsJsonObject() != null && je.getAsJsonObject().get(key) != null) {
                return je.getAsJsonObject().get(key).getAsInt();
            } else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       return 0;
    }
}

