package com.networkengine.entity;

/**
 * Created by pengpeng on 16/12/10.
 */

public class RequestCheckDeviceStatusParam {
    private String key;

    private String token;

    public String getToken() {
        return token;
    }

    public String getKey() {
        return key;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
