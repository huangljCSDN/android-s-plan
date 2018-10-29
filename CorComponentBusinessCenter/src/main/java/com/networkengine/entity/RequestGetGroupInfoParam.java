package com.networkengine.entity;

import android.support.annotation.StringDef;

public class RequestGetGroupInfoParam {

    @StringDef
    public @interface GroupType {
        /**
         * 固定群组
         */
        String FIXED_GROUP = "1";
        /**
         * 讨论组
         */
        String DISCUSSION_GROUP = "2";
    }

    private String groupName;

    @GroupType
    private String type;

    public String getGroupName() {
        return groupName;
    }

    public RequestGetGroupInfoParam() {

    }

    public RequestGetGroupInfoParam(@GroupType String type) {
        this.type = type;
    }

    public RequestGetGroupInfoParam(String groupName, @GroupType String type) {
        this.groupName = groupName;
        this.type = type;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
