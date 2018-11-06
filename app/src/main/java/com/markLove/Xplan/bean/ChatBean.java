package com.markLove.Xplan.bean;

/**
 * 作者：created by huanglingjun on 2018/10/24
 * 描述：
 */
public class ChatBean {

    /**
     * chatType : 1
     * chatId : 1
     */

    private int chatType;
    private int chatId;
    private int dataId;
    private String dataName;
    private String headImageUrl;

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getHeadImgUrl() {
        return headImageUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImageUrl = headImgUrl;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
}
