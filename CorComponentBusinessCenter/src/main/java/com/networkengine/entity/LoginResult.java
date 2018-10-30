package com.networkengine.entity;

/**
 * val code: String, val msg: String, val data: LoginEntity
 */
public class LoginResult{
    public String code;
    public String msg;
    public LoginEntity data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LoginEntity getData() {
        return data;
    }

    public void setData(LoginEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
