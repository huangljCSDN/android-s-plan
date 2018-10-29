package com.networkengine.entity;

import java.util.ArrayList;

public class RequestMessageEntity extends MessageEntity {

    private String content;
    private ArrayList<AtInfo> atInfo;
    private int unreadCount;
    private FileInfo fileInfo;
    private LocalInfo locationInfo;
//    private FunInfo funInfo;
    private ReplyInfo replyInfo;
    private ChatRecordInfo chatRecordInfo;
    /**
     * 语音和视频的属性
     */
    private CallInfo callInfo;
    private String rids;


    private String senderName;
    private String type;
    private String chatType;
    private String groupName;
    //语音、视频文件的时长
    private String time;
    //发送方设备类型:PC或者PHONE
    private String fromDevice = "PHONE";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocalInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
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

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public CallInfo getCallInfo() {
        return callInfo;
    }

    public void setCallInfo(CallInfo callInfo) {
        this.callInfo = callInfo;
    }

//    public FunInfo getFunInfo() {
//        return funInfo;
//    }
//
//    public void setFunInfo(FunInfo funInfo) {
//        this.funInfo = funInfo;
//    }

    public ReplyInfo getReplyInfo() {
        return replyInfo;
    }

    public void setReplyInfo(ReplyInfo replyInfo) {
        this.replyInfo = replyInfo;
    }

    public ChatRecordInfo getChatRecordInfo() {
        return chatRecordInfo;
    }

    public void setChatRecordInfo(ChatRecordInfo chatRecordInfo) {
        this.chatRecordInfo = chatRecordInfo;
    }

    public ArrayList<AtInfo> getAtInfo() {
        return atInfo;
    }

    public void setAtInfo(ArrayList<AtInfo> atInfo) {
        this.atInfo = atInfo;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
