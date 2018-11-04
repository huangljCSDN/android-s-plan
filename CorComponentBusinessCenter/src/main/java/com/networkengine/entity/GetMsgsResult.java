package com.networkengine.entity;

import java.util.List;

/**
 * Created by pengpeng on 16/12/23.
 * 获取消息接口返回结果
 * (val code: String, val msg: String, val data: GetMsgsDetail?)
 * (val clientMaxMsgId: String, val data: List<GetMsgsEntity>)
 */

public class GetMsgsResult{
    public String code;
    public String msg;
    public GetMsgsDetail data;



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

    public GetMsgsDetail getData() {
        return data;
    }

    public void setData(GetMsgsDetail data) {
        this.data = data;
    }

    public class GetMsgsDetail{
        public String clientMaxMsgId;
        public List<GetMsgsEntity> data;

        public String getClientMaxMsgId() {
            return clientMaxMsgId;
        }

        public void setClientMaxMsgId(String clientMaxMsgId) {
            this.clientMaxMsgId = clientMaxMsgId;
        }

        public List<GetMsgsEntity> getData() {
            return data;
        }

        public void setData(List<GetMsgsEntity> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "GetMsgsDetail{" +
                    "clientMaxMsgId='" + clientMaxMsgId + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetMsgsResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
