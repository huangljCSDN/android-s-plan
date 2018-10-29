package com.networkengine.entity;

/**
 * 聊天消息-回复消息
 * val virtualMsgId: String, val msgSenderId: String, val msgSender: String, val msgContent: String, var content: String
 */

public class ReplyInfo{
   public String virtualMsgId;
   public String msgSenderId;
   public String msgSender;
   public String msgContent;
    public String content;

    public ReplyInfo(String virtualMsgId, String msgSenderId, String msgSender, String msgContent, String content) {
        this.virtualMsgId = virtualMsgId;
        this.msgSenderId = msgSenderId;
        this.msgSender = msgSender;
        this.msgContent = msgContent;
        this.content = content;
    }

    public String getVirtualMsgId() {
        return virtualMsgId;
    }

    public void setVirtualMsgId(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }

    public String getMsgSenderId() {
        return msgSenderId;
    }

    public void setMsgSenderId(String msgSenderId) {
        this.msgSenderId = msgSenderId;
    }

    public String getMsgSender() {
        return msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ReplyInfo{" +
                "virtualMsgId='" + virtualMsgId + '\'' +
                ", msgSenderId='" + msgSenderId + '\'' +
                ", msgSender='" + msgSender + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

