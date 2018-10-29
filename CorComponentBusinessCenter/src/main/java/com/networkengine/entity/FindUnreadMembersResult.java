package com.networkengine.entity;

/**
 * Created by lvxiaoyu on 17/3/22.
 */

public class FindUnreadMembersResult {

    private String code;

    private String  msg;

    private FindUnreadMembersDetail data;

    public FindUnreadMembersDetail getData() {
        return data;
    }

    public void setData(FindUnreadMembersDetail data) {
        this.data = data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {

        return code;
    }

    public String getMsg() {
        return msg;
    }

}
