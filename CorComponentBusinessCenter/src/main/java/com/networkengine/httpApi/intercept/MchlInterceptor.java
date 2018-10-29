package com.networkengine.httpApi.intercept;

import android.text.TextUtils;

import com.networkengine.database.table.Member;
import com.networkengine.engine.EngineParameter;
import com.networkengine.httpApi.encrypt.EncryptCenter;

import okhttp3.Request;

/**
 * Created by liuhao on 2018/6/25.
 */

public class MchlInterceptor extends CoracleInterceptor {

    public MchlInterceptor(EngineParameter engineParameter, Member member) {
        super(engineParameter, member);
    }

    @Override
    protected void addPublicaHeader(Request.Builder requestBuilder) {

        if (null != mEngineParameter) {
            requestBuilder.header("X-xSimple-appKey", !TextUtils.isEmpty(mEngineParameter.appKey) ? mEngineParameter.appKey : "");
            requestBuilder.header("User-Agent", !TextUtils.isEmpty(mEngineParameter.userAgent) ? mEngineParameter.userAgent : "");
        }

        if (null != mMember) {
            requestBuilder.header("X-xSimple-LoginName", !TextUtils.isEmpty(mMember.getId().toString()) ? mMember.getId().toString() : "");
            requestBuilder.header("X-xSimple-AuthToken", !TextUtils.isEmpty(mMember.getUserToken()) ? mMember.getUserToken() : "");
            requestBuilder.header("X-xSimple-SysCode", !TextUtils.isEmpty(mMember.getUserSystem()) ? mMember.getUserSystem() : "");
            requestBuilder.header("X-xSimple-SysUserID", !TextUtils.isEmpty(mMember.getUserId()) ? mMember.getUserId() : "");
        }

        if (!TextUtils.isEmpty(EncryptCenter.getType())) {
            requestBuilder.header("X-xSimple-EM", EncryptCenter.getType());
        }
        requestBuilder.header("Accept-Encoding", "");

        requestBuilder.header("Content-Type", "text/plain; charset=UTF-8");

    }

    /**
     * mchl 先不做失效判断
     * @param responseBodyString
     * @param urlStr
     * @return
     */
    @Override
    protected boolean isLoginLoseByResponseBodyString(String responseBodyString, String urlStr)  {

        return false;
    }


}
