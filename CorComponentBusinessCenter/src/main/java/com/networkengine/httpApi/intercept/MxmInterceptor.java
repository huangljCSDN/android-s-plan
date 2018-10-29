package com.networkengine.httpApi.intercept;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.networkengine.database.table.Member;
import com.networkengine.engine.EngineParameter;
import com.networkengine.entity.BaseResult;
import com.networkengine.httpApi.encrypt.EncryptCenter;

import okhttp3.Request;

/**
 * Created by liuhao on 2018/6/8.
 * MxM 拦截器
 */

public class MxmInterceptor extends CoracleInterceptor {


    public MxmInterceptor(EngineParameter engineParameter, Member member) {
        super(engineParameter, member);
    }

    @Override
    protected void addPublicaHeader(Request.Builder requestBuilder) {
        if (!TextUtils.isEmpty(EncryptCenter.getType())) {
            requestBuilder.header("X-xSimple-EM", EncryptCenter.getType());
        }
        requestBuilder.addHeader("User-Agent", mEngineParameter.userAgent);
    }

    @Override
    protected boolean isLoginLoseByResponseBodyString(String responseBodyString, String urlStr) throws JsonParseException {

        Gson gson = new Gson();
        BaseResult baseResult = null;
        if (!TextUtils.isEmpty(EncryptCenter.getType())) {
            String decryptResponseBodyString = EncryptCenter.decrypt(responseBodyString, EncryptCenter.getType());
            //合乎平台规则的json,才判断登陆失效的问题
            if (isLegalObj(decryptResponseBodyString)) {
                baseResult = gson.fromJson(decryptResponseBodyString, BaseResult.class);
            }
        } else {
            if (isLegalObj(responseBodyString)) {
                baseResult = gson.fromJson(responseBodyString, BaseResult.class);
            }
        }
        if (baseResult == null) {
            return false;
        }
        return BaseResult.CODE_LOGIN_LOSE == baseResult.getRes();
    }

    @Override
    protected boolean isLegalObj(String json) {
        //mxm 规则
        if (hasKey(json, "res") && hasKey(json, "msg") && hasKey(json, "data")) {
            return true;
        }
        return false;
    }

    // 别删了，这是失效后调用登陆的逻辑，以后可能会用到
//    private boolean loginMxmAgain(Chain chain) throws IOException {
//
//        Request.Builder loginRequestBuilder = new Request.Builder();
//        if (!TextUtils.isEmpty(EncryptCenter.getType())) {
//            loginRequestBuilder.header("X-xSimple-EM", EncryptCenter.getType());
//        }
//        loginRequestBuilder.addHeader("User-Agent", mEngineParameter.userAgent);
//        loginRequestBuilder.url(mEngineParameter.mxmServiceBaseUrl + "/api/v4/login");
//        MxmLoginEntity entity = new MxmLoginEntity();
//        entity.setUserName(mEngineParameter.userName);
//        entity.setPassWord(mEngineParameter.pwd);
//        entity.setDeviceToken(PhoneUtil.getDeviceId(CoracleSdk.getCoracleSdk().getContext()));
//        entity.setAppKey(mEngineParameter.appKey);
//        String loginJosn = gson.toJson(entity);
//        //  Log.e("hh", "loginJosn >>>>  " + loginJosn);
//        //加密请求，加密
//        if (!TextUtils.isEmpty(EncryptCenter.getType())) {
//            loginJosn = EncryptCenter.encrtpy(loginJosn, EncryptCenter.getType());
//        }
//        RequestBody loginRequestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), loginJosn);
//        loginRequestBuilder.method("POST", loginRequestBody);
//        Request loginRequest = loginRequestBuilder.build();
//        Response loginResponse = chain.proceed(loginRequest);
//        ResponseBody loginResponseBody = loginResponse.body();
//        String loginResponseBodyStr = loginResponseBody.string();
//        //  Log.e("hh", "loginResponseBodyStr >>>>  " + loginResponseBodyStr);
//
//        //加密请求，解密
//        if (!TextUtils.isEmpty(EncryptCenter.getType())) {
//            loginResponseBodyStr = EncryptCenter.decrypt(loginResponseBodyStr, EncryptCenter.getType());
//        }
////        BaseResult<Member> loginResult = gson.fromJson(loginResponseBodyStr, new TypeToken<BaseResult<Member>>() {
////        }.getType());
//        BaseResult loginResult = gson.fromJson(loginResponseBodyStr, BaseResult.class);
//
//        if (loginResult.getRes() == BaseResult.CODE_FAILED) {
//            // Log.e("hh", "重新登陆成功失败");
//            return false;
//        }
//        //  Log.e("hh", "重新登陆成功成功");
//        return true;
//    }

}
