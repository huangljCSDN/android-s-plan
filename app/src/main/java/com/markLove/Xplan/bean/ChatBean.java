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

    private String sCallback;
    private int page;
    private int rows;
    private long id; //官方会话/盒子会话id

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getsCallback() {
        return sCallback;
    }

    public void setsCallback(String sCallback) {
        this.sCallback = sCallback;
    }

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
