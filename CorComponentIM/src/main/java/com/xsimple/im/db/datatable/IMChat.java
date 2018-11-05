package com.xsimple.im.db.datatable;

import android.support.annotation.IntDef;

import com.xsimple.im.db.greendao.DaoSession;
import com.xsimple.im.db.greendao.IMChatDao;
import com.xsimple.im.db.greendao.IMMessageDao;
import com.xsimple.im.db.greendao.IMSysMessageDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import com.xsimple.im.db.greendao.IMOfficialMessageDao;
import com.xsimple.im.db.greendao.IMBoxMessageDao;


/**
 * Created by pengpeng on 17/3/25.
 */

@Entity(nameInDb = "im_chat_new")
public class IMChat {

    public static final int SESSION_PERSON = 0;// 个人
    public static final int SESSION_GROUP_CLUSTER = 1;// 群组
    public static final int SESSION_GROUP_DISCUSSION = 2;// 讨论组
    public static final int SESSION_SYSTEM_MSG = 3;// 系统消息
    public static final int SESSION_LIGHT_MSG = 4;// 轻应用消息


    public static final int SESSION_BOX_MSG = 5;// 消息盒子
    public static final int SESSION_OFFICIAL_MSG = 6;// 官方消息

    @IntDef({SESSION_PERSON, SESSION_GROUP_CLUSTER, SESSION_GROUP_DISCUSSION, SESSION_SYSTEM_MSG, SESSION_LIGHT_MSG})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    public @interface SessionType {
    }

    @Id(autoincrement = true)
    private Long id;

    /**
     * 会话所属用户id
     */
    private String uId;

    /**
     * 发送或接收id1 配合判断是否为同一个chat
     */
    private String senderOrTarget1;

    private String receiverName;

    /**
     * 发送或接收id2  对于群组，senderOrTarget2 为群组 id
     */
    private String senderOrTarget2;

    /**
     * 会话类型
     */
    @SessionType
    private int type;

    /**
     * 会话mingc
     */
    private String name;

    /**
     * 未读消息数
     */
    private int UnReadCount;

    /**
     * 最后修改时间
     */
    private Long time;


    /**
     * 置顶
     */
    private boolean isStick;


    /**
     * 是否免打扰
     */
    private boolean isNotDisturb;
    /**
     * 草稿
     */
    private String drafts;
    /**
     * 下拉刷新的最后时间
     */
    private long refreshTime;
    /**
     * 轻应用类型
     */
    private String funKey;

    /**
     * 会话中的消息
     */
    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "cId")
    })
    private List<IMMessage> IMMessages;
    /**
     * 会话中的系统消息
     */
    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "cId")
    })
    private List<IMSysMessage> IMSysMessage;

    /**
     * 盒子小助手消息
     */
    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "cId")
    })
    private List<IMBoxMessage> IMBoxMessage;

    /**
     * 官方消息消息
     */
    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "cId")
    })
    private List<IMOfficialMessage> IMOfficialMessage;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1759008295)
    private transient IMChatDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Keep
    public IMChat(String uId, String senderOrTarget1, int type, String name,
                  int UnReadCount, Long time, String senderOrTarget2, String receiverName,String funKey) {
        this.uId = uId;
        this.senderOrTarget1 = senderOrTarget1;
        this.type = type;
        this.name = name;
        this.UnReadCount = UnReadCount;
        this.time = time;
        this.senderOrTarget2 = senderOrTarget2;
        this.receiverName = receiverName;
        this.funKey = funKey;
    }


    @Generated(hash = 39292687)
    public IMChat() {
    }


    @Generated(hash = 830058073)
    public IMChat(Long id, String uId, String senderOrTarget1, String receiverName, String senderOrTarget2, int type,
            String name, int UnReadCount, Long time, boolean isStick, boolean isNotDisturb, String drafts, long refreshTime,
            String funKey) {
        this.id = id;
        this.uId = uId;
        this.senderOrTarget1 = senderOrTarget1;
        this.receiverName = receiverName;
        this.senderOrTarget2 = senderOrTarget2;
        this.type = type;
        this.name = name;
        this.UnReadCount = UnReadCount;
        this.time = time;
        this.isStick = isStick;
        this.isNotDisturb = isNotDisturb;
        this.drafts = drafts;
        this.refreshTime = refreshTime;
        this.funKey = funKey;
    }





    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getUId() {
        return this.uId;
    }


    public void setUId(String uId) {
        this.uId = uId;
    }


    public String getSenderOrTarget1() {
        return this.senderOrTarget1;
    }


    public void setSenderOrTarget1(String senderOrTarget1) {
        this.senderOrTarget1 = senderOrTarget1;
    }


    public String getSenderOrTarget2() {
        return this.senderOrTarget2;
    }


    public void setSenderOrTarget2(String senderOrTarget2) {
        this.senderOrTarget2 = senderOrTarget2;
    }


    public int getType() {
        return this.type;
    }


    public void setType(int type) {
        this.type = type;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getUnReadCount() {
        return this.UnReadCount;
    }


    public void setUnReadCount(int UnReadCount) {
        this.UnReadCount = UnReadCount;
    }


    public Long getTime() {
        return this.time;
    }


    public void setTime(Long time) {
        this.time = time;
    }


    public boolean getIsStick() {
        return this.isStick;
    }


    public void setIsStick(boolean isStick) {
        this.isStick = isStick;
    }

    public boolean getIsNotDisturb() {
        return this.isNotDisturb;
    }


    public void setIsNotDisturb(boolean isNotDisturb) {
        this.isNotDisturb = isNotDisturb;
    }


    public String getDrafts() {
        return this.drafts;
    }


    public void setDrafts(String drafts) {
        this.drafts = drafts;
    }


    public long getRefreshTime() {
        return this.refreshTime;
    }


    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 214684576)
    public List<IMMessage> getIMMessages() {
        if (IMMessages == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMMessageDao targetDao = daoSession.getIMMessageDao();
            List<IMMessage> IMMessagesNew = targetDao._queryIMChat_IMMessages(id);
            synchronized (this) {
                if (IMMessages == null) {
                    IMMessages = IMMessagesNew;
                }
            }
        }
        return IMMessages;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1047170603)
    public synchronized void resetIMMessages() {
        IMMessages = null;
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
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 528062423)
    public List<IMSysMessage> getIMSysMessage() {
        if (IMSysMessage == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMSysMessageDao targetDao = daoSession.getIMSysMessageDao();
            List<IMSysMessage> IMSysMessageNew = targetDao._queryIMChat_IMSysMessage(id);
            synchronized (this) {
                if (IMSysMessage == null) {
                    IMSysMessage = IMSysMessageNew;
                }
            }
        }
        return IMSysMessage;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 849586974)
    public synchronized void resetIMSysMessage() {
        IMSysMessage = null;
    }


    public String getFunKey() {
        return funKey;
    }

    public void setFunKey(String funKey) {
        this.funKey = funKey;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1137074926)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIMChatDao() : null;
    }

    @Override
    public String toString() {
        return "IMChat{" +
                "id=" + id +
                ", uId='" + uId + '\'' +
                ", senderOrTarget1='" + senderOrTarget1 + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", senderOrTarget2='" + senderOrTarget2 + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", UnReadCount=" + UnReadCount +
                ", time=" + time +
                ", isStick=" + isStick +
                ", isNotDisturb=" + isNotDisturb +
                ", drafts='" + drafts + '\'' +
                ", refreshTime=" + refreshTime +
                ", funKey='" + funKey + '\'' +
                ", IMMessages=" + IMMessages +
                ", IMSysMessage=" + IMSysMessage +
                ", myDao=" + myDao +
                ", daoSession=" + daoSession +
                '}';
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 941858358)
    public List<IMBoxMessage> getIMBoxMessage() {
        if (IMBoxMessage == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMBoxMessageDao targetDao = daoSession.getIMBoxMessageDao();
            List<IMBoxMessage> IMBoxMessageNew = targetDao._queryIMChat_IMBoxMessage(id);
            synchronized (this) {
                if (IMBoxMessage == null) {
                    IMBoxMessage = IMBoxMessageNew;
                }
            }
        }
        return IMBoxMessage;
    }


    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1431968648)
    public synchronized void resetIMBoxMessage() {
        IMBoxMessage = null;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1146339301)
    public List<IMOfficialMessage> getIMOfficialMessage() {
        if (IMOfficialMessage == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMOfficialMessageDao targetDao = daoSession.getIMOfficialMessageDao();
            List<IMOfficialMessage> IMOfficialMessageNew = targetDao._queryIMChat_IMOfficialMessage(id);
            synchronized (this) {
                if (IMOfficialMessage == null) {
                    IMOfficialMessage = IMOfficialMessageNew;
                }
            }
        }
        return IMOfficialMessage;
    }


    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1174372829)
    public synchronized void resetIMOfficialMessage() {
        IMOfficialMessage = null;
    }
}
