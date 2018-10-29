package com.networkengine.entity;

import com.networkengine.entity.IEntity;

public class Result<T> implements IEntity {

    // TODO 适配统一所有数据入口
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * status : true
     * errorCode :
     * errorMessage :
     */
    private boolean status;
    private String errorCode;// 某些接口没有status, 通过判断 errorCode 进行判断
    private String errorMessage;
    private String msg;// 映射到 errorMessage
    //  "code": "0",
//  "message": "操作成功"
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage != null ? errorMessage : msg;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
