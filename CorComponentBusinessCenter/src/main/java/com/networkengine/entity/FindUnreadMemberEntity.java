package com.networkengine.entity;


/**
 * Created by pengpeng on 16/12/23.
 */
public class FindUnreadMemberEntity {

    private String userId;
    private String userName;
    private String imageAddress;
    private String readTime;

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}
