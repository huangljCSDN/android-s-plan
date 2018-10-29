package com.xsimple.im.db.datatable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 回复消息，回复和被回复的内容只能是文本
 */
@Entity(nameInDb = "im_msg_reply_info_new")
public class IMReplyInfo {

    @Id(autoincrement = true)
    private Long rId;
    /**
     * 被回复的消息ID
     */
    private String virtualMsgId;
    /**
     * 被回复的消息原发送人
     */
    private String msgSenderId;
    /**
     * 被回复的消息原发送人
     */
    private String msgSender;
    /**
     * 被回复的消息内容
     */
    private String msgContent;
    /**
     * 回复的内容
     */
    private String content;
    @Generated(hash = 670384375)
    public IMReplyInfo(Long rId, String virtualMsgId, String msgSenderId,
            String msgSender, String msgContent, String content) {
        this.rId = rId;
        this.virtualMsgId = virtualMsgId;
        this.msgSenderId = msgSenderId;
        this.msgSender = msgSender;
        this.msgContent = msgContent;
        this.content = content;
    }
    @Generated(hash = 1932922695)
    public IMReplyInfo() {
    }
    public Long getRId() {
        return this.rId;
    }
    public void setRId(Long rId) {
        this.rId = rId;
    }
    public String getVirtualMsgId() {
        return this.virtualMsgId;
    }
    public void setVirtualMsgId(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }
    public String getMsgSenderId() {
        return this.msgSenderId;
    }
    public void setMsgSenderId(String msgSenderId) {
        this.msgSenderId = msgSenderId;
    }
    public String getMsgSender() {
        return this.msgSender;
    }
    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }
    public String getMsgContent() {
        return this.msgContent;
    }
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
