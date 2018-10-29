package com.networkengine.entity;

/**
 * Created by pengpeng on 17/3/22.
 */

public class ChangeReadStatusResult {
    private String errorCode;
    private boolean status;
    private String msg;

    public String getErrorCode() {
        return errorCode;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
