package com.networkengine.httpApi.intercept;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonParseException;
import com.networkengine.database.table.Member;
import com.networkengine.engine.EngineParameter;
import com.networkengine.engine.LogicEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by liuhao on 2018/6/25.
 */

public abstract class CoracleInterceptor implements Interceptor {

    protected EngineParameter mEngineParameter;

    protected Member mMember;


    public CoracleInterceptor(EngineParameter engineParameter, Member member) {
        this.mEngineParameter = engineParameter;
        this.mMember = member;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        HttpUrl url = request.url();

        String urlStr = url.toString();

        Request.Builder requestBuilder = request.newBuilder();
        //添加公共头
        addPublicaHeader(requestBuilder);
        requestBuilder.method(request.method(), request.body());
        request = requestBuilder.build();
        //发请求
        Response response = chain.proceed(request);
        if (response == null) {
            return response;
        }
        if (response.code() == 302) {
            loginLose();
        }
        if (response.code() != 200) {
            return response;
        }
        ResponseBody responseBody = response.body();
        //404等情况为空
        MediaType mediaType = responseBody.contentType();
        if (mediaType == null) {
            return response;
        }
        //判断数据返回的格式，以及失效的url
        if (judgeContentType(mediaType.toString(), urlStr)) {
            Response.Builder builder = response.newBuilder();
            builder.body(responseBody);
            return builder.build();
        }
        //
        String responseBodyString = responseBody.string();

        try {
            if (isLoginLoseByResponseBodyString(responseBodyString, urlStr)) {
                loginLose();
            }
        } catch (Exception exception) {

        }

        ResponseBody requestBodyDec = ResponseBody.create(mediaType, responseBodyString);
        Response.Builder builder = response.newBuilder();
        builder.body(requestBodyDec);
        return builder.build();

    }

    /**
     * 判断contentType
     *
     * @param contentType
     * @return
     */
    private boolean judgeContentType(String contentType, String url) {
        if ("application/octet-stream;charset=UTF-8".equals(contentType)) {
            return true;
        }
        if ("application/vnd.android.package-archive;charset=UTF-8".equals(contentType)) {
            return true;
        }
        if ("text/html;charset=UTF-8".equals(contentType)) {
            return true;
        }
        if (url.contains("api/v4/logout")) {
            return true;
        }
        return false;
    }

    /**
     * 登陆失效
     */
    private void loginLose() {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                //失效跳登陆页
//                LogicEngine.getInstance().getSystemController().logout(false);
//            }
//        });
    }

    /**
     * 添加头
     *
     * @param requestBuilder
     */
    protected abstract void addPublicaHeader(Request.Builder requestBuilder);

    /**
     * 验证登陆失效 ,轻应用的需要根据url来判断登陆失效走的逻辑
     *
     * @param responseBodyString
     * @param url
     * @return
     */
    protected abstract boolean isLoginLoseByResponseBodyString(String responseBodyString, String url);

    /**
     * 判断服务器返回的是否为标准json
     *
     * @return
     */
    protected boolean isLegalObj(String json) {
        return true;
    }

    /**
     * 判断key 是否存在
     *
     * @param json
     * @param key
     * @return
     */
    protected boolean hasKey(String json, String key) {
        boolean hasKey = false;
        try {
            JSONObject jsonObject = new JSONObject(json);
            hasKey = !jsonObject.isNull(key);
        } catch (JSONException e) {
            hasKey = false;
        }
        return hasKey;
    }

}
