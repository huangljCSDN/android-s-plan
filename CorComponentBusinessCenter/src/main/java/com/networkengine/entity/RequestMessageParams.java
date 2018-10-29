package com.networkengine.entity;

import java.util.List;

/**
 * author panxiaoan
 * date 2017/8/15.
 */

public class RequestMessageParams {

    /**
     * 消息发送人ID  2018/4/28 改动
     */
    private String sender;
    /**
     * 消息发送人名称
     */
    private String senderName;
    /**
     * 群组ID
     */
    private String groupId;
    /**
     * 群名称
     */
    private String groupName;
    /**
     * 消息类型：0表示IM消息，1表示业务消息
     */
    private String msgType;
    /**
     * IM消息类型，chat表示单人聊天，group表示讨论组，fixGroup表示群组
     */
    private String chatType;
    /**
     * 消息体类型，例如IM_txt,IM_img等
     */
    private String type;
    /**
     * 消息接收人
     */
    private List<String> receiverIds;

    public List<String> getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(List<String> receiverIds) {
        this.receiverIds = receiverIds;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
