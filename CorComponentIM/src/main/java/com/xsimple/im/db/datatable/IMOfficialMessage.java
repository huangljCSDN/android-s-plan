package com.xsimple.im.db.datatable;

import com.xsimple.im.db.greendao.DaoSession;
import com.xsimple.im.db.greendao.IMSysMessageDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Description：官方消息
 */
@Entity(nameInDb = "im_official_message")
public class IMOfficialMessage {

    @Id(autoincrement = true)
    private Long _id;
    /**
     * 会话Id
     */
    @Property(nameInDb = "chat_id")
    private Long cId;

    private String userId;

    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 连接
     */
    private String netUrl;

    /**
     * 发送时间
     */
    private Long sendTimer;

    /**
     * 是否清空操作
     */
    private boolean isClear;

    /**
     * 是否已读状态
     */
    private boolean isRead;

    /**
     * 消息类型 system
     */
    private String type;

    /**
     * 消息标题(群组有可能被删除, 无法通过 group 获取, 备用)
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    @Generated(hash = 1989759411)
    public IMOfficialMessage(Long _id, Long cId, String userId, String imgUrl, String netUrl, Long sendTimer, boolean isClear, boolean isRead, String type, String title, String content) {
        this._id = _id;
        this.cId = cId;
        this.userId = userId;
        this.imgUrl = imgUrl;
        this.netUrl = netUrl;
        this.sendTimer = sendTimer;
        this.isClear = isClear;
        this.isRead = isRead;
        this.type = type;
        this.title = title;
        this.content = content;
    }

    @Generated(hash = 506091673)
    public IMOfficialMessage() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }

    public Long getSendTimer() {
        return sendTimer;
    }

    public void setSendTimer(Long sendTimer) {
        this.sendTimer = sendTimer;
    }

    public boolean isClear() {
        return isClear;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCId() {
        return this.cId;
    }

    public void setCId(Long cId) {
        this.cId = cId;
    }

    public boolean getIsClear() {
        return this.isClear;
    }

    public void setIsClear(boolean isClear) {
        this.isClear = isClear;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
