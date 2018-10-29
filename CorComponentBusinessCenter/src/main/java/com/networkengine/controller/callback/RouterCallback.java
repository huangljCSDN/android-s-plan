package com.networkengine.controller.callback;

/**
 * Created by pengpeng on 17/8/5.
 */

public interface RouterCallback {

    class Result{
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;

        private int code;

        private String msg;

        private String data;

        public Result(int code, String msg, String data) {
            this.code = code;
            this.msg = msg;
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

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    void callback(Result result);
}
