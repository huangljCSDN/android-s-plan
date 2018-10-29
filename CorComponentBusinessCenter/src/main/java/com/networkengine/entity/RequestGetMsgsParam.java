package com.networkengine.entity;

/**
 * Created by pengpeng on 16/12/23.
 */

public class RequestGetMsgsParam {

    public RequestGetMsgsParam(int count, long clientMaxMsgId) {
        this.count = count;
        this.clientMaxMsgId = clientMaxMsgId;
    }

    private int count;
    private long clientMaxMsgId;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getClientMaxMsgId() {
        return clientMaxMsgId;
    }

    public void setClientMaxMsgId(long clientMaxMsgId) {
        this.clientMaxMsgId = clientMaxMsgId;
    }
}
