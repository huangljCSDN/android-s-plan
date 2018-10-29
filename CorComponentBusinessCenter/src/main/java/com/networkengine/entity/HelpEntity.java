package com.networkengine.entity;

/**
 * Created by pengpeng on 17/2/16.
 */

public class HelpEntity {

    private Boolean status;

    private String errorCode;
    private String errorMessage;
    private HelpDetail detail;

    public Boolean getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HelpDetail getDetail() {
        return detail;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setDetail(HelpDetail detail) {
        this.detail = detail;
    }
}
