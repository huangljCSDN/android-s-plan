package com.networkengine.entity;

/**
 * Created by liuhao on 2018/5/3.
 */

public class MyFileDownLoad {

    private int id;
    private String sha;
    private long senderId;
    private String senderName;
    private long receiverId;
    private String receiverName;
    private String groupId;
    private String groupName;
    private int type;
    private String filename;
    private String contentType;
    private int length;
    private long uploadDatetime;
    private String uploadCount;
    private String content;
    private String favCreateTime;
    private String favUpdateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getUploadDatetime() {
        return uploadDatetime;
    }

    public void setUploadDatetime(long uploadDatetime) {
        this.uploadDatetime = uploadDatetime;
    }

    public String getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(String uploadCount) {
        this.uploadCount = uploadCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFavCreateTime() {
        return favCreateTime;
    }

    public void setFavCreateTime(String favCreateTime) {
        this.favCreateTime = favCreateTime;
    }

    public String getFavUpdateTime() {
        return favUpdateTime;
    }

    public void setFavUpdateTime(String favUpdateTime) {
        this.favUpdateTime = favUpdateTime;
    }
}
