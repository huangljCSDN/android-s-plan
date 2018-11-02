package com.markLove.Xplan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.markLove.Xplan.base.App;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PreferencesUtils {

    public static final String KEY_USER = "key_user";
    public static final String KEY_MEMBER = "key_merber";
    public static final String KEY_USER_TOKEN = "key_user_token";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static void clear(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void remove(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).commit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    /**
     * 获取boolean类型数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    /**
     * 获取boolean类型数据 默认值是true
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, true);
    }

    //----------------------------

    public static void putString(Context context, String key,
                                 String value) {
        getSharedPreferences(context).edit().putString(key, value).commit();
    }


    public static String getString(Context context, String key, String defValue) {
        return getSharedPreferences(context).getString(key, defValue);
    }


    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static void putLong(Context context, String key,
                               long value) {
        getSharedPreferences(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSharedPreferences(context).getLong(key, defValue);
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, 0);
    }


    public static void putInt(Context context, String key,
                              int value) {
        getSharedPreferences(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSharedPreferences(context).getInt(key, defValue);
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }

    public static void putFloat(Context context, String key,
                                float value) {
        getSharedPreferences(context).edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getSharedPreferences(context).getFloat(key, defValue);
    }

    public static float getFloat(Context context, String key) {
        return getFloat(context,key,0.0f);
    }
    /**
     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param object 待加密的转换为String的对象
     * @return String   加密后的String
     */
    private static String Object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 使用SharedPreference保存对象
     *
     * @param fileKey    储存文件的key
     * @param key        储存对象的key
     * @param saveObject 储存的对象
     */
    public static void save(String fileKey, String key, Object saveObject) {
        SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences(fileKey, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String string = Object2String(saveObject);
        editor.putString(key, string);
        editor.commit();
    }

    /**
     * 获取SharedPreference保存的对象
     *
     * @param fileKey 储存文件的key
     * @param key     储存对象的key
     * @return object 返回根据key得到的对象
     */
    public static Object get(String fileKey, String key) {
        SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences(fileKey, Activity.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, null);
        if (string != null) {
            Object object = String2Object(string);
            return object;
        } else {
            return null;
        }
    }

}
