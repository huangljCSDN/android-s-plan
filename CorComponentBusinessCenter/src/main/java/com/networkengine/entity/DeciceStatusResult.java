package com.networkengine.entity;

/**
 * Created by pengpeng on 17/2/15.
 */

public class DeciceStatusResult {

    private Boolean status;
    private String errorCode;
    private String errorMessage;
    private String time;
    private String dName;
    private String type;
    private String deviceType;

    public Boolean getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getTime() {
        return time;
    }

    public String getdName() {
        return dName;
    }

    public String getType() {
        return type;
    }

    public String getDeviceType() {
        return deviceType;
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

    public void setTime(String time) {
        this.time = time;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
