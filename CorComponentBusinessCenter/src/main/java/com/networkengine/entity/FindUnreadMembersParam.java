package com.networkengine.entity;

/**
 * Created by lvxiaoyu on 17/3/22.
 */

public class FindUnreadMembersParam {

    public FindUnreadMembersParam(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }

    private String virtualMsgId;

    public String getGroupId() {
        return virtualMsgId;
    }

    public void setGroupId(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }
}
