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
