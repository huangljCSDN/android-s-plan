package com.markLove.Xplan.bean;

import java.util.List;

/**
 * Created by huanglingjun on 2018/5/10.
 */

public class PostQueryInfo {

    private String message;
    private String nu;
    private String ischeck;
    private String com;
    private String status;
    private String condition;
    private String state;
    private List<DataBean> data;
    public static class DataBean {
        private String time;
        private String context;
        private String ftime;

        @Override
        public String toString() {
            return "DataBean{" +
                    "time='" + time + '\'' +
                    ", context='" + context + '\'' +
                    ", ftime='" + ftime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PostQueryInfo{" +
                "message='" + message + '\'' +
                ", nu='" + nu + '\'' +
                ", ischeck='" + ischeck + '\'' +
                ", com='" + com + '\'' +
                ", status='" + status + '\'' +
                ", condition='" + condition + '\'' +
                ", state='" + state + '\'' +
                ", data=" + data +
                '}';
    }
}
