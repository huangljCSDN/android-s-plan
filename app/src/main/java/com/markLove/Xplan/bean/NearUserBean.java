package com.markLove.Xplan.bean;

public class NearUserBean {

    /**
     * distance : 10341.660783600611
     * nickName : 测试3
     * headImageUrl :
     * sex : 1
     * lastOnline : 一小时前
     * userId : 369024
     */
    private double distance;
    private String nickName;
    private String headImageUrl;
    private int sex;
    private String lastOnline;
    private long userId;

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getDistance() {
        return distance;
    }

    public String getNickName() {
        return nickName;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public long getSex() {
        return sex;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "NearUserBean{" +
                "distance=" + distance +
                ", nickName='" + nickName + '\'' +
                ", headImageUrl='" + headImageUrl + '\'' +
                ", sex=" + sex +
                ", lastOnline='" + lastOnline + '\'' +
                ", userId=" + userId +
                '}';
    }
}
