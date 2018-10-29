package com.networkengine.entity;

import java.util.ArrayList;

/**
 * 聊天消息-消息记录
 * (val title: String, val content: String, val receiverId: String, val msgIds: ArrayList<String>)
 */
public class ChatRecordInfo{
    public String title;
    public String content;
    public String receiverId;
    public ArrayList<String> msgIds;

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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public ArrayList<String> getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(ArrayList<String> msgIds) {
        this.msgIds = msgIds;
    }

    @Override
    public String toString() {
        return "ChatRecordInfo{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", msgIds=" + msgIds +
                '}';
    }
}

