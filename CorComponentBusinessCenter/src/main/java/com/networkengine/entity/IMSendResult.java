package com.networkengine.entity;

/**
 * Created by pengpeng on 17/2/21.
 */

public class IMSendResult {

    private String code;
    private String msg;
    private IMSendResultDetail data;

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

    public IMSendResultDetail getData() {
        return data;
    }

    public void setData(IMSendResultDetail data) {
        this.data = data;
    }
}
