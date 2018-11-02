package com.markLove.Xplan.bean;

/**
 * 作者：created by huanglingjun on 2018/10/24
 * 描述：
 */
public class GoImgLibraryBean {

    private String uploadUrl;
    private String sCallback;
    private String selectType;
    private int backType;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public int getBackType() {
        return backType;
    }

    public void setBackType(int backType) {
        this.backType = backType;
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
