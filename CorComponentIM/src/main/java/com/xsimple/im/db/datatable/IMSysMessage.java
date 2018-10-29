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
 * Description：系统消息列表
 */
@Entity(nameInDb = "im_system_message")
public class IMSysMessage {

    @Id(autoincrement = true)
    private Long _id;
    /**
     * 会话Id
     */
    @Property(nameInDb = "chat_id")
    private Long cId;
    /**
     * 登陆用户Id
     */
    private String currUserId;

    private String userId;
    private String userName;

    private String groupId;
    @ToOne(joinProperty = "groupId")
    private IMGroup group;

    /**
     * 接收时间
     */
    private Long receivedTimer;

    /**
     * 是否答复
     */
    private boolean isReply;

    /**
     * 是否同意
     */
    private boolean isAgree;

    /**
     * 是否清空操作
     */
    private boolean isClear;

    /**
     * 是否已读状态
     */
    private boolean isRead;

    /**
     * 消息类型
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

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1774432021)
    private transient IMSysMessageDao myDao;



    @Generated(hash = 721201946)
    public IMSysMessage(Long _id, Long cId, String currUserId, String userId, String userName,
            String groupId, Long receivedTimer, boolean isReply, boolean isAgree,
            boolean isClear, boolean isRead, String type, String title, String content) {
        this._id = _id;
        this.cId = cId;
        this.currUserId = currUserId;
        this.userId = userId;
        this.userName = userName;
        this.groupId = groupId;
        this.receivedTimer = receivedTimer;
        this.isReply = isReply;
        this.isAgree = isAgree;
        this.isClear = isClear;
        this.isRead = isRead;
        this.type = type;
        this.title = title;
        this.content = content;
    }

    @Generated(hash = 995433128)
    public IMSysMessage() {
    }

    @Generated(hash = 1288353610)
    private transient String group__resolvedKey;



    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getCurrUserId() {
        return this.currUserId;
    }

    public void setCurrUserId(String currUserId) {
        this.currUserId = currUserId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getReceivedTimer() {
        return this.receivedTimer;
    }

    public void setReceivedTimer(Long receivedTimer) {
        this.receivedTimer = receivedTimer;
    }

    public boolean getIsReply() {
        return this.isReply;
    }

    public void setIsReply(boolean isReply) {
        this.isReply = isReply;
    }

    public boolean getIsAgree() {
        return this.isAgree;
    }

    public void setIsAgree(boolean isAgree) {
        this.isAgree = isAgree;
    }

    public boolean getIsClear() {
        return this.isClear;
    }

    public void setIsClear(boolean isClear) {
        this.isClear = isClear;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 793644082)
    public IMGroup getGroup() {
        String __key = this.groupId;
        if (group__resolvedKey == null || group__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMGroupDao targetDao = daoSession.getIMGroupDao();
            IMGroup groupNew = targetDao.load(__key);
            synchronized (this) {
                group = groupNew;
                group__resolvedKey = __key;
            }
        }
        return group;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 423009160)
    public void setGroup(IMGroup group) {
        synchronized (this) {
            this.group = group;
            groupId = group == null ? null : group.getId();
            group__resolvedKey = groupId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public Long getCId() {
        return this.cId;
    }

    public void setCId(Long cId) {
        this.cId = cId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 345423039)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIMSysMessageDao() : null;
    }


}
