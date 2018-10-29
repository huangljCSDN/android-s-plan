package com.networkengine.entity;

/**
 * Created by pengpeng on 16/12/12.
 */

public class RequestLogoutParam {

    private String bizInput;

    /*
    * 平台
    * */
    private String plat;

    /*
    * 设备ID
    * */
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public String getPlat() {
        return plat;
    }

    public String getToken() {
        return token;
    }

    public String getBizInput() {
        return bizInput;
    }

    public void setBizInput(String bizInput) {
        this.bizInput = bizInput;
    }
}
