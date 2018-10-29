package com.networkengine.entity;

import java.util.List;


public class IMSendMultipleResult {

    private String code;
    private String msg;
    private List<IMSendResultMultipleDetail> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<IMSendResultMultipleDetail> getData() {
        return data;
    }

    public void setData(List<IMSendResultMultipleDetail> data) {
        this.data = data;
    }
}
