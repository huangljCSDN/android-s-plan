package com.markLove.Xplan.bean;

/**
 * 作者：created by huanglingjun on 2018/10/24
 * 描述：
 */
public class GoPhotoBean {

    private String uploadUrl;
    private String sCallback;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getsCallback() {
        return sCallback;
    }

    public void setsCallback(String sCallback) {
        this.sCallback = sCallback;
    }
}
