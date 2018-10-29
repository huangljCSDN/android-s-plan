package com.networkengine.entity;

/**
 * Created by lvxy
 */

public class CollectStatus {
    /**
     * 消息发送人，用于显示头像
     */
    private String userId;
    /**
     * 发送人名字
     */
    private String userName;
    /**
     * 来自哪个群或同事圈
     */
    private String source;
    /**
     * 类型，文本、链接、图片、视频、语音、位置、文件
     */
    private String favoritesType;
    /**
     * 内容
     */
    private CollectContent content;

    public CollectStatus(CollectContent content, String userName, String favoritesType) {
        this.content = content;
        this.userName = userName;
        this.favoritesType = favoritesType;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFavoritesType() {
        return favoritesType;
    }

    public void setFavoritesType(String favoritesType) {
        this.favoritesType = favoritesType;
    }

    public CollectContent getContent() {
        return content;
    }

    public void setContent(CollectContent content) {
        this.content = content;
    }
}
