package com.networkengine.entity;

/**
 * Created by liuhao on 2018/4/16.
 */

public class AfficheListEntity {
    private int pageNo;
    private int pageSize;
    private long groupId;

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

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
