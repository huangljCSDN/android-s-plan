package com.networkengine.entity;

/**
 * Created by pengpeng on 16/12/12.
 */

public class RequestAgreeOrRefuseParam {

    private String receiver;
    private String name;
    /*
    *  agreae ? "AGREE" : "REFUSE"
    * */
    private String mark;

    public String getMark() {
        return mark;
    }

    public String getName() {
        return name;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

}
