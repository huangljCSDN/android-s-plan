package com.networkengine.entity;

/**
 * Created by liuhao on 2018/5/30.
 */

public class RequestAuditJoinGroupParams extends RequestGroupAddOrRemovePersonParams {
    /**
     * 申请入群审批结果
     * true表示同意，false表示拒绝
     */
    private boolean auditResult;

    public boolean isAuditResult() {
        return auditResult;
    }

    public void setAuditResult(boolean auditResult) {
        this.auditResult = auditResult;
    }
}
