package com.xsimple.im.db;

import android.util.Property;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;

import com.networkengine.util.Base64Utils;


/**
 * 将ArraryLsit存数据库
 * Created by pwy on 2018/5/21.
 */
public class StringConverter implements PropertyConverter<ArrayList<String>, String> {

    @Override
    public String convertToDatabaseValue(ArrayList<String> entityProperty) {
        StringBuilder sb = new StringBuilder();
        for (String s : entityProperty) {
            s = new String(Base64Utils.encode(s.getBytes())); // 处理一下，防止String原来就有逗号
            sb.append(s).append(",");
        }
        return sb.toString();
    }

    @Override
    public ArrayList<String> convertToEntityProperty(String databaseValue) {
        ArrayList<String> arr = new ArrayList<>();
        for (String s : databaseValue.split(",")) {
            arr.add(new String(Base64Utils.decode(s.getBytes()))); // 存的时候加了密，取的时候解一下
        }
        return arr;
    }
}