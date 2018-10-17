package com.markLove.Xplan.bean;

/**
 * Created by Administrator on 2017/10/17.
 */

public class ChatContentBean {
    private String msg;
    private String chatType;
    private String dateTime;
    private String type;
    private String userName;



    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
    public String getChatType() {
        return chatType;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getDateTime() {
        return dateTime;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

}
