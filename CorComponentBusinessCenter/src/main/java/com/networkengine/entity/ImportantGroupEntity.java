package com.networkengine.entity;

/**
 * Created by liuhao on 2018/4/16.
 */

public class ImportantGroupEntity {

    private long groupId;
    /**
     * 0 不重要
     * 1 重要
     */
    private int importantFlag;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getImportantFlag() {
        return importantFlag;
    }

    public void setImportantFlag(int importantFlag) {
        this.importantFlag = importantFlag;
    }
}

