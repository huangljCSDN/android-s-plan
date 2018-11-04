package com.xsimple.im.db.datatable;

import com.xsimple.im.db.greendao.DaoSession;
import com.xsimple.im.db.greendao.IMFileInfoDao;
import com.xsimple.im.db.greendao.IMFileInfoPiceDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;


/**
 * Created by pengpeng on 17/3/24.
 */

@Entity(nameInDb = "im_msg_file_new")
public class IMFileInfo {

    @Id(autoincrement = true)
    private Long fId;

    private String name;
    /**
     * 文件地址 对于接受的img ,为 压缩级别为 S 的图片
     */
    private String path;
    /**
     * img 压缩级别为 m 的图片
     */
    private String mPath;
    /**
     * img 没有压缩的图片地址
     */
    private String lPath;

    //发送状态 0:发送中/接受中，1:发送/接受成功，－1:发送/接受失败，－2:暂停 -3：未接受
    private int status;

    private Long size;
    /**
     * 进度
     */
    private Long pos;

    private String type;

    //  @Unique
    private String sha;

    private String breakPoint;

    //语音和视频的时长
    private String time;
    /**
     * 视频缩略图
     */
    private String thumbnail;
    /**
     * 语音是否播放
     */
    private boolean isPlay;
    /**
     * 本地文件的MD5 值
     */
    private String clientSid;
    /**
     * 文件发送者id
     */
    private String sendId;
    //文件发送者名称
    private String senderName;
    /**
     * 发送或者接受的时间
     */
    private long se_ReTime;

    /**
     * 附件地址；
     * 多用在非聊天类图片收藏，图片完整地址；
     */
    private String url;

    private String receiverName;
    @Property(nameInDb ="IMG_WIDTH")
    private int width;
    @Property(nameInDb ="IMG_HEIGHT")
    private int height;

    /**
     * 下载失败的次数
     */
    private int failedCount;

    @ToMany(joinProperties = {
            @JoinProperty(name = "fId", referencedName = "fid")
    })
    private List<IMFileInfoPice> IMFileThreadPice;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1013003954)
    private transient IMFileInfoDao myDao;


    @Generated(hash = 1595828222)
    public IMFileInfo() {
    }

    @Generated(hash = 828921947)
    public IMFileInfo(Long fId, String name, String path, String mPath, String lPath, int status, Long size,
            Long pos, String type, String sha, String breakPoint, String time, String thumbnail, boolean isPlay,
            String clientSid, String sendId, String senderName, long se_ReTime, String url, String receiverName,
            int width, int height, int failedCount) {
        this.fId = fId;
        this.name = name;
        this.path = path;
        this.mPath = mPath;
        this.lPath = lPath;
        this.status = status;
        this.size = size;
        this.pos = pos;
        this.type = type;
        this.sha = sha;
        this.breakPoint = breakPoint;
        this.time = time;
        this.thumbnail = thumbnail;
        this.isPlay = isPlay;
        this.clientSid = clientSid;
        this.sendId = sendId;
        this.senderName = senderName;
        this.se_ReTime = se_ReTime;
        this.url = url;
        this.receiverName = receiverName;
        this.width = width;
        this.height = height;
        this.failedCount = failedCount;
    }

    @Keep
    public List<IMFileInfoPice> getIMFileThread() {
        return IMFileThreadPice;
    }


    public Long getFId() {
        return this.fId;
    }


    public void setFId(Long fId) {
        this.fId = fId;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPath() {
        return this.path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getMPath() {
        return this.mPath;
    }


    public void setMPath(String mPath) {
        this.mPath = mPath;
    }


    public String getLPath() {
        return this.lPath;
    }


    public void setLPath(String lPath) {
        this.lPath = lPath;
    }


    public int getStatus() {
        return this.status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public Long getSize() {
        return this.size;
    }


    public void setSize(Long size) {
        this.size = size;
    }


    public Long getPos() {
        return this.pos;
    }


    public void setPos(Long pos) {
        this.pos = pos;
    }


    public String getType() {
        return this.type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getSha() {
        return this.sha;
    }


    public void setSha(String sha) {
        this.sha = sha;
    }


    public String getBreakPoint() {
        return this.breakPoint;
    }


    public void setBreakPoint(String breakPoint) {
        this.breakPoint = breakPoint;
    }


    public String getTime() {
        return this.time;
    }


    public void setTime(String time) {
        this.time = time;
    }


    public String getThumbnail() {
        return this.thumbnail;
    }


    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public boolean getIsPlay() {
        return this.isPlay;
    }


    public void setIsPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }


    public String getClientSid() {
        return this.clientSid;
    }


    public void setClientSid(String clientSid) {
        this.clientSid = clientSid;
    }


    public String getSendId() {
        return this.sendId;
    }


    public void setSendId(String sendId) {
        this.sendId = sendId;
    }


    public long getSe_ReTime() {
        return this.se_ReTime;
    }


    public void setSe_ReTime(long se_ReTime) {
        this.se_ReTime = se_ReTime;
    }


    public String getUrl() {
        return this.url;
    }


    public void setUrl(String url) {
        this.url = url;
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
    @Generated(hash = 1454955620)
    public List<IMFileInfoPice> getIMFileThreadPice() {
        if (IMFileThreadPice == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMFileInfoPiceDao targetDao = daoSession.getIMFileInfoPiceDao();
            List<IMFileInfoPice> IMFileThreadPiceNew = targetDao
                    ._queryIMFileInfo_IMFileThreadPice(fId);
            synchronized (this) {
                if (IMFileThreadPice == null) {
                    IMFileThreadPice = IMFileThreadPiceNew;
                }
            }
        }
        return IMFileThreadPice;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1430157359)
    public synchronized void resetIMFileThreadPice() {
        IMFileThreadPice = null;
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


    public String getSenderName() {
        return this.senderName;
    }


    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getFailedCount() {
        return this.failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1773264691)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIMFileInfoDao() : null;
    }


    @Override
    public String toString() {
        return "IMFileInfo{" +
                "fId=" + fId +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", mPath='" + mPath + '\'' +
                ", lPath='" + lPath + '\'' +
                ", status=" + status +
                ", size=" + size +
                ", pos=" + pos +
                ", type='" + type + '\'' +
                ", sha='" + sha + '\'' +
                ", breakPoint='" + breakPoint + '\'' +
                ", time='" + time + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", isPlay=" + isPlay +
                ", clientSid='" + clientSid + '\'' +
                ", sendId='" + sendId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", se_ReTime=" + se_ReTime +
                ", url='" + url + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", failedCount=" + failedCount +
                '}';
    }
}
