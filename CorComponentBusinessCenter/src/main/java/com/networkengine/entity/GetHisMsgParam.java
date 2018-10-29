package com.networkengine.entity;

/**
 * Created by pengpeng on 17/3/22.
 */

public class GetHisMsgParam {

    private String senderId;
    private String groupId;
    private String receiverId;
    private String virtualMsgId;
    private String appKey;
    private String pageSize;
    private String pageNo;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getVirtualMsgId() {
        return virtualMsgId;
    }

    public void setVirtualMsgId(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }
}
