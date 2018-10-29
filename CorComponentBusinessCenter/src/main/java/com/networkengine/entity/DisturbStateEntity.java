package com.networkengine.entity;

/**
 * author panxiaoan
 * date 2017/4/5.
 */

public class DisturbStateEntity {
    private String msg;
    private int code;
    private DisturbStateResult data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DisturbStateResult getData() {
        return data;
    }

    public void setData(DisturbStateResult keepSilentTagResult) {
        this.data = keepSilentTagResult;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
