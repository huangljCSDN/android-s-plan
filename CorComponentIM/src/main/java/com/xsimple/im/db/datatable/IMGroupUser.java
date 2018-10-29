package com.xsimple.im.db.datatable;

import android.support.annotation.IntDef;

import com.xsimple.im.db.greendao.DaoSession;
import com.xsimple.im.db.greendao.IMGroupUserDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.greenrobot.greendao.DaoException;
import com.xsimple.im.db.greendao.IMGroupDao;

/**
 * Created by pengpeng on 17/3/26.
 */
@Entity(nameInDb = "im_group_user_new")
public class IMGroupUser {

    public static final int JOB_OWNER = 1;
    public static final int JOB_MEMBER = 2;

    @IntDef({JOB_OWNER, JOB_MEMBER})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    public @interface JobType {
    }

    @Id(autoincrement = true)
    private Long id;

    private String gId;
    @ToOne(joinProperty = "gId")
    private IMGroup group;

//    @ToOne(joinProperty = "uId")
//    private IMUser user;

    /**
     * (1:群主, 2:成员)
     */
    @JobType
    private int job;

    /**
     * 加入时间
     */
    private long joinTime;
    /**
     * 头像
     */
    private String imageAddress;
    /**
     * 拼音首字母
     */
    private String initial;
    /**
     * 姓名全拼
     */
    private String pinying;
    /**
     * 成员id
     */
    private String userId;
    /**
     * 成员姓名
     */
    private String userName;

    @Keep
    public boolean isGroupOwner() {
        return job == JOB_OWNER;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGId() {
        return this.gId;
    }
    public void setGId(String gId) {
        this.gId = gId;
    }
    public int getJob() {
        return this.job;
    }
    public void setJob(int job) {
        this.job = job;
    }
    public long getJoinTime() {
        return this.joinTime;
    }
    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }
    public String getImageAddress() {
        return this.imageAddress;
    }
    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
    public String getInitial() {
        return this.initial;
    }
    public void setInitial(String initial) {
        this.initial = initial;
    }
    public String getPinying() {
        return this.pinying;
    }
    public void setPinying(String pinying) {
        this.pinying = pinying;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1849367955)
    public IMGroup getGroup() {
        String __key = this.gId;
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
    @Generated(hash = 1148800517)
    public void setGroup(IMGroup group) {
        synchronized (this) {
            this.group = group;
            gId = group == null ? null : group.getId();
            group__resolvedKey = gId;
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1156634887)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIMGroupUserDao() : null;
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 318675937)
    private transient IMGroupUserDao myDao;

    @Generated(hash = 2058198880)
    public IMGroupUser(Long id, String gId, int job, long joinTime,
            String imageAddress, String initial, String pinying, String userId,
            String userName) {
        this.id = id;
        this.gId = gId;
        this.job = job;
        this.joinTime = joinTime;
        this.imageAddress = imageAddress;
        this.initial = initial;
        this.pinying = pinying;
        this.userId = userId;
        this.userName = userName;
    }
    @Generated(hash = 1883615637)
    public IMGroupUser() {
    }

    @Generated(hash = 1288353610)
    private transient String group__resolvedKey;



}
