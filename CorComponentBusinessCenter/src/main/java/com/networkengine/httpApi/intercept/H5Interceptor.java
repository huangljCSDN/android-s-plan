package com.networkengine.httpApi.intercept;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.networkengine.database.table.Member;
import com.networkengine.engine.EngineParameter;
import com.networkengine.entity.BaseResult;
import com.networkengine.httpApi.encrypt.EncryptCenter;

import okhttp3.Request;

/**
 * Created by liuhao on 2018/6/25.
 */

public class H5Interceptor extends CoracleInterceptor {

    public H5Interceptor(EngineParameter engineParameter, Member member) {
        super(engineParameter, member);
    }

    @Override
    protected void addPublicaHeader(Request.Builder requestBuilder) {

        if (!TextUtils.isEmpty(EncryptCenter.getType())) {
            requestBuilder.header("X-xSimple-EM", EncryptCenter.getType());
        }

        if (mEngineParameter != null) {
            String userAgent = mEngineParameter.userAgent;
            requestBuilder.addHeader("User-Agent", TextUtils.isEmpty(userAgent) ? "" : userAgent);
            requestBuilder.addHeader("X-xSimple-appKey", !TextUtils.isEmpty(mEngineParameter.appKey) ? mEngineParameter.appKey : "");
        }
        if (null != mMember) {
            requestBuilder.addHeader("X-xSimple-LoginName", !TextUtils.isEmpty(mMember.getId().toString()) ? mMember.getId().toString() : "");
            requestBuilder.addHeader("X-xSimple-AuthToken", !TextUtils.isEmpty(mMember.getUserToken()) ? mMember.getUserToken() : "");
            requestBuilder.addHeader("X-xSimple-SysCode", !TextUtils.isEmpty(mMember.getUserSystem()) ? mMember.getUserSystem() : "");
            requestBuilder.addHeader("X-xSimple-SysUserID", !TextUtils.isEmpty(mMember.getUserId()) ? mMember.getUserId() : "");
        }

    }

    @Override
    protected boolean isLoginLoseByResponseBodyString(String responseBodyString, String urlStr) {

        if (urlStr.contains(mEngineParameter.mxmServiceBaseUrl)) {
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
        return false;
    }

    @Override
    protected boolean isLegalObj(String json) {
        //mxm 规则
        if (hasKey(json, "res") && hasKey(json, "msg") && hasKey(json, "data")) {
            return true;
        }
        //其他json 规则。。。
        return false;
    }


}
