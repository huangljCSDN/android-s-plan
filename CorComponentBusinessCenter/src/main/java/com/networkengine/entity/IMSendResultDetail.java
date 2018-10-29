package com.networkengine.entity;

/**
 * author panxiaoan
 * date 2017/8/15.
 */

public class IMSendResultDetail {
    private Long sendTime;
    private String virtualMsgId;

    public IMSendResultDetail(Long sendTime, String virtualMsgId) {
        this.sendTime = sendTime;
        this.virtualMsgId = virtualMsgId;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public String getVirtualMsgId() {
        return virtualMsgId;
    }

    public void setVirtualMsgId(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }
}
