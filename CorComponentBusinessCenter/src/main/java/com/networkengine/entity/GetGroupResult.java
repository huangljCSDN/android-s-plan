package com.networkengine.entity;

/**
 * Created by pengpeng on 16/12/14.
 */

public class GetGroupResult {

    private String code;
    private String msg;
    private RequestGroupData data;

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

    public RequestGroupData getData() {
        return data;
    }

    public void setData(RequestGroupData data) {
        this.data = data;
    }

}
