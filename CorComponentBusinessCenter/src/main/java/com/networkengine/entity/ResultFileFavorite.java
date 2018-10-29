package com.networkengine.entity;

/**
 * Created by liuhao on 2018/6/5.
 */

public class ResultFileFavorite {

    private String code;
    private String msg;
    private ResultFavoriteData data;

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

    public ResultFavoriteData getData() {
        return data;
    }

    public void setData(ResultFavoriteData data) {
        this.data = data;
    }
}
