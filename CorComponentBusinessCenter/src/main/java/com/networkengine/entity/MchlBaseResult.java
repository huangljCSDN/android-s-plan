package com.networkengine.entity;

import android.support.annotation.StringDef;

import com.networkengine.entity.IEntity;

/**
 * Created by liuhao on 2018/5/15.
 */

public class MchlBaseResult<T> implements IEntity {

    @StringDef
    public @interface STATE_CODE {
        /**
         * 失败
         */

        String CODE_FAILED = "1";
        /**
         * 成功
         */
        String CODE_SUCCESS = "0";
    }

    private String code;

    private String msg;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
