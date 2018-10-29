package com.xsimple.im.db.datatable;

import com.xsimple.im.db.greendao.DaoSession;
import com.xsimple.im.db.greendao.IMChatDao;
import com.xsimple.im.db.greendao.IMUserDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by pengpeng on 17/3/25.
 */

@Entity(nameInDb = "im_user_new")
public class IMUser {

    @Id
    private String id;
    private int type;
    private String name;
    private String img;
    private String phone;
    private String tel;
    private String mail;
    private String canChat;
    private String loginName;

//    private String contactId;
//    @ToOne(joinProperty = "id")
//    private ContactEntity contact;

    /**
     * 会话中的消息
     */
    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "uId")
    })
    private List<IMChat> IMChats;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1043596398)
    private transient IMUserDao myDao;

    @Generated(hash = 180295260)
    public IMUser(String id, int type, String name, String img, String phone,
            String tel, String mail, String canChat, String loginName) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.img = img;
        this.phone = phone;
        this.tel = tel;
        this.mail = mail;
        this.canChat = canChat;
        this.loginName = loginName;
    }

    @Generated(hash = 931211978)
    public IMUser() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCanChat() {
        return this.canChat;
    }

    public void setCanChat(String canChat) {
        this.canChat = canChat;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1321954344)
    public List<IMChat> getIMChats() {
        if (IMChats == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMChatDao targetDao = daoSession.getIMChatDao();
            List<IMChat> IMChatsNew = targetDao._queryIMUser_IMChats(id);
            synchronized (this) {
                if (IMChats == null) {
                    IMChats = IMChatsNew;
                }
            }
        }
        return IMChats;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1332583361)
    public synchronized void resetIMChats() {
        IMChats = null;
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
    @Generated(hash = 1462810780)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIMUserDao() : null;
    }

}
