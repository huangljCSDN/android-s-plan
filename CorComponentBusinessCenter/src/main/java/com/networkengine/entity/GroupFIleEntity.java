package com.networkengine.entity;

/**
 * Created by liuhao on 2018/4/20.
 */

public class GroupFIleEntity {

    public static final String TYPE_FILE="file";

    public static final String TYPE_IMG="image";


    private int groupId;
    private String type;
    private String keyWord;
    private int pageNo;
    private int pageSize;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
