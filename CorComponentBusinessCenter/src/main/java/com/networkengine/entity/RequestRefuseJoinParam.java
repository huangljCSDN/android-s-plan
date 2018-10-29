package com.networkengine.entity;

public class RequestRefuseJoinParam {

    private String groupId;

    private String groupName;

    private String memberId;

    public RequestRefuseJoinParam(String gId, String gName, String mId) {
        this.groupId = gId;
        this.groupName = gName;
        this.memberId = mId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

}
