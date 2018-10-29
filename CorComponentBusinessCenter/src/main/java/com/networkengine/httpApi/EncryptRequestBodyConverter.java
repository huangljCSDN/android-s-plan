package com.networkengine.httpApi;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.networkengine.httpApi.encrypt.EncryptCenter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.ByteString;
import retrofit2.Converter;

/**
 * Created by pengpeng on 17/4/25.
 */

public class EncryptRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson mGson;
    private final TypeAdapter<T> mAdapter;


    EncryptRequestBodyConverter(Gson mGson, TypeAdapter<T> mAdapter) {
        this.mGson = mGson;
        this.mAdapter = mAdapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = mGson.newJsonWriter(writer);
        mAdapter.write(jsonWriter, value);
        jsonWriter.close();
        ByteString requestByteString = buffer.readByteString();
        String requestString = requestByteString.utf8();
        //Log.e("Http", "RequestBody : " + requestString);
        requestString = EncryptCenter.encrtpy(requestString, EncryptCenter.getType());
       // Log.e("Http", "Encrypt RequestBody : " + requestString);
        return RequestBody.create(MEDIA_TYPE, requestString);
    }
}
