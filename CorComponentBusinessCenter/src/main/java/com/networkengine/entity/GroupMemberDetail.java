package com.networkengine.entity;


public class GroupMemberDetail {

    /**
     * 头像
     */
    private String imageAddress;
    /**
     * 拼音首字母
     */
    private String initial;
    /**
     * 1表示为管理员，2为普通成员
     */
    private int job;
    /**
     * 入群时间
     */
    private long joinTime;
    /**
     * 姓名全拼
     */
    private String pinying;
    /**
     * 成员id
     */
    private String userId;
    /**
     * 成员姓名
     */
    private String userName;

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public String getPinying() {
        return pinying;
    }

    public void setPinying(String pinying) {
        this.pinying = pinying;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
