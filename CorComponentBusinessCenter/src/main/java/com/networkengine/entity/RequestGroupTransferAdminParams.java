package com.networkengine.entity;


/**
 * 转让群管理员
 */
public class RequestGroupTransferAdminParams {
    /**
     * 群id
     */
    private String id;
    /**
     * 新的群组管理员姓名
     */
    private String adminName;
    /**
     * 新的群管理员id
     */
    private String adminId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}
