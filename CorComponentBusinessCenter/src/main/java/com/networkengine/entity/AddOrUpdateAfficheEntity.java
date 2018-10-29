package com.networkengine.entity;

/**
 * Created by liuhao on 2018/4/16.
 */

public class AddOrUpdateAfficheEntity {



    private String title;
    private String content;
    private long groupId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
