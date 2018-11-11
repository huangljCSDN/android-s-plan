package com.networkengine.entity;

import java.io.Serializable;

/**
 * author panxiaoan
 * date 2018/1/15.
 * desc 简单的联系人实体，用于去掉通讯录之后，界面之间的数据传输
 */

public class MemEntity implements Serializable {
    //id (个人id，店铺聊天室id，组局聊天室id)
    private String userId;
    //组局/店铺id
    private String dataId;
    //名字
    private String userName;
    //类型，个人0,组局聊天室1 店铺聊天室2
    private int type;
    //头像地址
    private String imgUrl = "";
    //登陆名
    private String loginName = "";
    //邮箱
    private String email = "";
    //电话
    private String ploneNume = "";
    //职位
    private String organization = "";

    public MemEntity(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }


    public MemEntity(String userId, String userName, int type) {
        this.userId = userId;
        this.userName = userName;
        this.type = type;
    }

    public MemEntity(String userId,String dataId, String userName, int type) {
        this.userId = userId;
        this.userName = userName;
        this.type = type;
        this.dataId = dataId;
    }

    public MemEntity(String userId, String userName, int type, String imgUrl) {
        this.userId = userId;
        this.userName = userName;
        this.type = type;
        this.imgUrl = imgUrl;
    }

    public MemEntity(String userId, String userName, int type, String imgUrl, String loginName, String email, String ploneNume, String organization) {
        this.userId = userId;
        this.userName = userName;
        this.type = type;
        this.imgUrl = imgUrl;
        this.loginName = loginName;
        this.email = email;
        this.ploneNume = ploneNume;
        this.organization = organization;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPloneNume() {
        return ploneNume;
    }

    public void setPloneNume(String ploneNume) {
        this.ploneNume = ploneNume;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
