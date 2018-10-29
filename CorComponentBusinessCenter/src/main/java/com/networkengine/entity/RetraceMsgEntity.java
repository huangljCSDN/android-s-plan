package com.networkengine.entity;


/**
 * 撤回
 * Created by pwy on 2018/6/21.
 * (val virtualMsgId: String, val receiverId: String?, val groupId: String?)
 */
public class RetraceMsgEntity{
    String virtualMsgId;
    String receiverId;
    String groupId;

    public RetraceMsgEntity(String virtualMsgId, String receiverId, String groupId) {
        this.virtualMsgId = virtualMsgId;
        this.receiverId = receiverId;
        this.groupId = groupId;
    }

    public String getVirtualMsgId() {
        return virtualMsgId;
    }

    public void setVirtualMsgId(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}