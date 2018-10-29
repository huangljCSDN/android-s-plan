package com.networkengine.httpApi;

import com.networkengine.httpApi.encrypt.EncryptCenter;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit2.Converter;

/**
 * Created by pengpeng on 17/4/26.
 */

public class EncryptStringConverter implements Converter<Object, String> {


    private Annotation[] mAnnotations;

    public EncryptStringConverter(Annotation[] annotations) {
        this.mAnnotations = annotations;
    }

    @Override
    public String convert(Object value) throws IOException {

        for (int i = 0; i < mAnnotations.length; i++) {
            Annotation annotation = mAnnotations[i];
            //注解头不加密
            if (retrofit2.http.HeaderMap.class.equals(annotation.annotationType())) {
                return value.toString();
            }
            //QueryMap 不加密
            if (retrofit2.http.QueryMap.class.equals(annotation.annotationType())) {
                return value.toString();
            }
        }

        String encrtpy = EncryptCenter.encrtpy(value.toString(), EncryptCenter.getType());
        // Log.e("Http", "EncryptStringConverter ##### " + encrtpy);
        return encrtpy;
    }
}
