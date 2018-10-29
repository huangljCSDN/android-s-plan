package com.networkengine.entity;

/**
 * 扫描二维码加入群聊
 * Created by chenbin on 2018/6/22.
 */

public class ScanQRCodeJoinGroupEntity {
    /**
     * 讨论组或群组id
     */
    private String id;
    /**
     * 分享二维码的用户id
     */
    private String inviterId;
    /**
     * 分享二维码的用户名
     */
    private String inviterName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
    }
}
