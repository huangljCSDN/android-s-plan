package com.networkengine.entity;

import java.util.List;

/**
 * Created by liuhao on 2017/5/19.
 */

public class HisResult {


    private int code;
    private String msg;
    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class Data {
        private int pageCount;
        private List<GetMsgsEntity> msgs;

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public List<GetMsgsEntity> getMsgs() {
            return msgs;
        }

        public void setMsgs(List<GetMsgsEntity> msgs) {
            this.msgs = msgs;
        }
    }

}
