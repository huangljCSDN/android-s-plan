package com.networkengine.entity;

/**
 * author panxiaoan
 * date 2017/8/16.
 */

public class MessageParam {
    private String msgID;
    private String senderId;
    private String sendTime;
    private String msgVersion;

    private String virtualMsgId;

    private Long mId;

    /** 消息免打扰标志，0：该条消息免打扰  1：该条消息可打扰 **/
    public String isDisturb = "1";

    private String type;

    private String chatType;

    private boolean withdrawFlag;

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(String msgVersion) {
        this.msgVersion = msgVersion;
    }

    public String getVirtualMsgId() {
        return virtualMsgId;
    }

    public void setVirtualMsgId(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public String getIsDisturb() {
        return isDisturb;
    }

    public void setIsDisturb(String isDisturb) {
        this.isDisturb = isDisturb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public boolean isWithdrawFlag() {
        return withdrawFlag;
    }

    public void setWithdrawFlag(boolean withdrawFlag) {
        this.withdrawFlag = withdrawFlag;
    }
}
