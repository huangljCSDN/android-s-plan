package com.xsimple.im.db.datatable;

import com.xsimple.im.db.greendao.DaoSession;
import com.xsimple.im.db.greendao.IMGroupDao;
import com.xsimple.im.db.greendao.IMSysMessageDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Description：盒子小助手
 */
@Entity(nameInDb = "im_box_message")
public class IMBoxMessage {

    @Id(autoincrement = true)
    private Long _id;
    /**
     * 会话Id
     */
    @Property(nameInDb = "chat_id")
    private Long cId;

    private String userId;

    /**
     * 接收时间
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
     * 消息类型 box
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    @Generated(hash = 126479589)
    public IMBoxMessage(Long _id, Long cId, String userId, Long sendTimer, boolean isClear, boolean isRead, String type, String title, String content) {
        this._id = _id;
        this.cId = cId;
        this.userId = userId;
        this.sendTimer = sendTimer;
        this.isClear = isClear;
        this.isRead = isRead;
        this.type = type;
        this.title = title;
        this.content = content;

    }

    @Generated(hash = 1366921220)
    public IMBoxMessage() {
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
