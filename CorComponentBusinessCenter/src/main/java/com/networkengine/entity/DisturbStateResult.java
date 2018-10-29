package com.networkengine.entity;

/**
 * author panxiaoan
 * date 2017/4/5.
 */

public class DisturbStateResult {
    private int nonDisturbType;
    private String dndGroupId;
    private String dndUserId;
    private Boolean nonDisturbStatus;

    public int getNonDisturbType() {
        return nonDisturbType;
    }

    public void setNonDisturbType(int nonDisturbType) {
        this.nonDisturbType = nonDisturbType;
    }

    public String getDndGroupId() {
        return dndGroupId;
    }

    public void setDndGroupId(String dndGroupId) {
        this.dndGroupId = dndGroupId;
    }

    public String getDndUserId() {
        return dndUserId;
    }

    public void setDndUserId(String dndUserId) {
        this.dndUserId = dndUserId;
    }

    public Boolean getNonDisturbStatus() {
        return nonDisturbStatus;
    }

    public void setNonDisturbStatus(Boolean nonDisturbStatus) {
        this.nonDisturbStatus = nonDisturbStatus;
    }
}
