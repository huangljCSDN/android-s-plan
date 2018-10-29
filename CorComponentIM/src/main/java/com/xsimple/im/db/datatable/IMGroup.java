package com.xsimple.im.db.datatable;

import android.support.annotation.IntDef;

import com.xsimple.im.db.greendao.DaoSession;
import com.xsimple.im.db.greendao.IMGroupDao;
import com.xsimple.im.db.greendao.IMGroupUserDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Created by pengpeng on 17/3/26.
 */

@Entity(nameInDb = "im_group_new")
public class IMGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int TYPE_CLUSTER = 1;// 群组
    public static final int TYPE_DISCUSSION = 2;// 讨论组


    public static final int UN_IMPORTANT_FLAG = 0;//非重要
    public static final int IMPORTANT_FLAG = 1;//重要

    @IntDef({TYPE_CLUSTER, TYPE_DISCUSSION})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    public @interface GroupType {
    }

    @IntDef({UN_IMPORTANT_FLAG, IMPORTANT_FLAG})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    public @interface ImportantFlag {

    }

    /**
     * 登陆用户Id
     */
    private String currUserId;

    /**
     * 群组ID
     */
    @Id
    private String id;

    /**
     * 群组名称
     */
    private String name;
    /**
     * 修改时间
     */
    private String update_time;
    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 群组公告
     */
    private String remark;

    /**
     * 组类型(群组&讨论组)
     */
    @GroupType
    private int type;
    /**
     * 群重要程度
     * 0 不重要
     * 1 重要
     */
    @ImportantFlag
    private int importantFlag;
    /**
     * 群公告发布时间
     */
    private String remarkDate;


    /**
     * 群组中的用户
     */
    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "gId")
    })
    private List<IMGroupUser> groupUsers;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 195352510)
    private transient IMGroupDao myDao;

    @Keep
    public IMGroup(String id, String name, String update_time, String create_time,
                   String remark, int type) {
        this.id = id;
        this.name = name;
        this.update_time = update_time;
        this.create_time = create_time;
        this.remark = remark;
        this.type = type;
    }


    @Generated(hash = 1114972139)
    public IMGroup() {
    }


    @Generated(hash = 225977832)
    public IMGroup(String currUserId, String id, String name, String update_time, String create_time,
            String remark, int type, int importantFlag, String remarkDate) {
        this.currUserId = currUserId;
        this.id = id;
        this.name = name;
        this.update_time = update_time;
        this.create_time = create_time;
        this.remark = remark;
        this.type = type;
        this.importantFlag = importantFlag;
        this.remarkDate = remarkDate;
    }


    public String getCurrUserId() {
        return this.currUserId;
    }


    public void setCurrUserId(String currUserId) {
        this.currUserId = currUserId;
    }


    public String getId() {
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getUpdate_time() {
        return this.update_time;
    }


    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }


    public String getCreate_time() {
        return this.create_time;
    }


    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 681581664)
    public List<IMGroupUser> getGroupUsers() {
        if (groupUsers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMGroupUserDao targetDao = daoSession.getIMGroupUserDao();
            List<IMGroupUser> groupUsersNew = targetDao
                    ._queryIMGroup_GroupUsers(id);
            synchronized (this) {
                if (groupUsers == null) {
                    groupUsers = groupUsersNew;
                }
            }
        }
        return groupUsers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 621435850)
    public synchronized void resetGroupUsers() {
        groupUsers = null;
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

    public int getImportantFlag() {
        return this.importantFlag;
    }


    public void setImportantFlag(int importantFlag) {
        this.importantFlag = importantFlag;
    }


    public String getRemarkDate() {
        return this.remarkDate;
    }


    public void setRemarkDate(String remarkDate) {
        this.remarkDate = remarkDate;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1698760888)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIMGroupDao() : null;
    }

}
