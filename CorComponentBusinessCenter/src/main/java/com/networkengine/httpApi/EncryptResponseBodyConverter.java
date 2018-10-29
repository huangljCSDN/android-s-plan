package com.networkengine.httpApi;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.networkengine.httpApi.encrypt.EncryptCenter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by pengpeng on 17/4/24.
 */

public class EncryptResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson mGson;
    private final TypeAdapter<T> mAdapter;
    Class<T> mType;


    EncryptResponseBodyConverter(Gson gson, TypeAdapter<T> mAdapter, Class<T> type) {
        this.mGson = gson;
        this.mAdapter = mAdapter;
        mType = type;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {

        String responseString = responseBody.string();
        responseString = EncryptCenter.decrypt(responseString, EncryptCenter.getType());
        // Log.e("Http", "Decryption ResponseBody : " + responseString);
        Reader reader = new StringReader(responseString);
        JsonReader jsonReader = mGson.newJsonReader(reader);
        try {
            return mAdapter.read(jsonReader);
        } finally {
            responseBody.close();
        }
    }
}
