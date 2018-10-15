package com.markLove.xplan.bean;

import com.markLove.xplan.bean.msg.Message;

/**
 * Created by luoyunmin on 2017/7/24.
 */

public class ChatUser {
    private String headImgUrl;
    private String nickName;
    private String chatTime;
    private String lastMsgId;
    private String lastMsgContent;
    private Message.ChatType lastMsgChatType;
    private int unreadCount;
    private int userID;

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(String lastMsgId) {
        this.lastMsgId = lastMsgId;
    }

    public String getLastMsgContent() {
        return lastMsgContent;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void setLastMsgContent(String lastMsgContent) {
        this.lastMsgContent = lastMsgContent;
    }

    public Message.ChatType getLastMsgChatType() {
        return lastMsgChatType;
    }

    public void setLastMsgChatType(Message.ChatType lastMsgChatType) {
        this.lastMsgChatType = lastMsgChatType;
    }
}
