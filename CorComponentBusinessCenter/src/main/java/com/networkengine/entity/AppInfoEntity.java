package com.networkengine.entity;

/**
 * Created by pengpeng on 17/2/16.
 */

public class AppInfoEntity {

    private Boolean status;

    private String errorCode;

    private String errorMessage;

    private String detail;

    public Boolean getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getDetail() {
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

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
