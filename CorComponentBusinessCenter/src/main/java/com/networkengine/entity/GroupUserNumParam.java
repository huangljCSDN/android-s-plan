package com.networkengine.entity;

/**
 * Created by lvxiaoyu on 17/3/22.
 */

public class GroupUserNumParam {

    public GroupUserNumParam(String groupId) {
        this.groupId = groupId;
    }

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
