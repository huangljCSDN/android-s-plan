package com.networkengine.entity;

/**
 * Created by pengpeng on 16/12/10.
 */

public class RequestMXMLoginParam {
    String user;

    String password;

    String remember = "true";

    public RequestMXMLoginParam(String user, String password){
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getRemember() {
        return remember;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }

}
