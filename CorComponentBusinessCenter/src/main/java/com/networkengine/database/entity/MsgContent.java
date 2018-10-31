package com.networkengine.database.entity;

/**
 * 作者：created by huanglingjun on 2018/10/31
 * 描述：
 */
public class MsgContent {
    public String groupName;
    public String content;
    public String mk;
    public String rids;
    public String senderName;
    public String type;
    public String time;
    public FileInfo fileInfo;

    public MsgContent() {

    }

    public MsgContent(String groupName, String mk, String rids, String senderName, String type, String time, FileInfo fileInfo) {
        this.groupName = groupName;
        this.mk = mk;
        this.rids = rids;
        this.senderName = senderName;
        this.type = type;
        this.time = time;
        this.fileInfo = fileInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMk() {
        return mk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }

    public String getRids() {
        return rids;
    }

    public void setRids(String rids) {
        this.rids = rids;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public String toString() {
        return "MsgContent{" +
                "groupName='" + groupName + '\'' +
                ", mk='" + mk + '\'' +
                ", rids='" + rids + '\'' +
                ", senderName='" + senderName + '\'' +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", fileInfo=" + fileInfo +
                '}';
    }
}
