package com.networkengine.entity;

import java.util.List;

/**
 * Created by pengpeng on 17/3/22.
 */

public class ChangeReadStatus {
    private List<String> msgIds;
    private String senderId;
    private String groupId;

    public ChangeReadStatus(List<String> list_localid, String groupid) {
        this.groupId = groupid;
        this.msgIds = list_localid;
    }

    public ChangeReadStatus(List<String> list_localid) {
        this.msgIds = list_localid;
    }

    public List<String> getList_localid() {
        return msgIds;
    }

    public String getGroupi1d() {
        return groupId;
    }

    public void setList_localid(List<String> list_localid) {
        this.msgIds = list_localid;
    }

    public void setGroupi1d(String groupi1d) {
        this.groupId = groupi1d;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
