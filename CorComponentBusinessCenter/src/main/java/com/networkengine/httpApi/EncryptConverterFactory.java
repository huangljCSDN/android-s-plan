package com.networkengine.httpApi;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by pengpeng on 17/4/24.
 */

public class EncryptConverterFactory extends Converter.Factory {

    private final Gson mGson;

    public static EncryptConverterFactory create() {
        return create(new Gson());
    }

    public static EncryptConverterFactory create(Gson gson) {
        return new EncryptConverterFactory(gson);
    }


    private EncryptConverterFactory(Gson mGson) {
        if (mGson == null) throw new NullPointerException("mGson == null");
        this.mGson = mGson;
    }


    /**
     * 获得http响应中ResponseBody的数据解密转换器
     *
     * @param type
     * @param annotations
     * @param retrofit
     * @return
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));

        return new EncryptResponseBodyConverter(mGson, adapter, TypeToken.get(type).getRawType());
    }

    /**
     * 获得http请求中RequestBody的数据加密转换器
     *
     * @param type
     * @param parameterAnnotations
     * @param methodAnnotations
     * @param retrofit
     * @return
     */
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new EncryptRequestBodyConverter<>(mGson, adapter);
    }

    /**
     * 获得http请求中 表单参数的加密转换器
     *
     * @param type
     * @param annotations
     * @param retrofit
     * @return
     */
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        return new EncryptStringConverter(annotations);
    }
}
