package com.networkengine.entity;


public class IMSendResultMultipleDetail extends IMSendResultDetail {


    private boolean status;

    private String errorMsg;

    public IMSendResultMultipleDetail(Long sendTime, String virtualMsgId) {
        super(sendTime, virtualMsgId);
    }

    public IMSendResultMultipleDetail(Long sendTime, String virtualMsgId, boolean status, String errorMsg) {
        this(sendTime, virtualMsgId);
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
