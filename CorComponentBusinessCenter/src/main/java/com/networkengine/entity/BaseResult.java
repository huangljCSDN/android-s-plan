package com.networkengine.entity;


/**
 * Created by liuhao on 2018/5/15.
 */

public class BaseResult<T> implements IEntity {

    /**
     * 登陆失效
     */
    public static final int CODE_LOGIN_LOSE = -1;

    /**
     * 失败
     */
    public static final int CODE_FAILED = 0;

    /**
     * 成功
     */
    public static final int CODE_SUCCESS = 1;

    /**
     * 0失败
     * 1成功
     */
    private int res;

    private String msg;

    private T data;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
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
