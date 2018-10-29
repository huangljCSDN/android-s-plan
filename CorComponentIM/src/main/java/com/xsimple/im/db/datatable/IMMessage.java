package com.xsimple.im.db.datatable;

import com.networkengine.entity.GetMsgsEntity;
import com.xsimple.im.db.greendao.DaoSession;
import com.xsimple.im.db.greendao.IMCallInfoDao;
import com.xsimple.im.db.greendao.IMChatRecordInfoDao;
import com.xsimple.im.db.greendao.IMFileInfoDao;
import com.xsimple.im.db.greendao.IMGroupRemarkDao;
import com.xsimple.im.db.greendao.IMLocationInfoDao;
import com.xsimple.im.db.greendao.IMMessageDao;
import com.xsimple.im.db.greendao.IMReplyInfoDao;
import com.xsimple.im.db.greendao.IMShareInfoDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by pengpeng on 17/3/24.
 */

@Entity(nameInDb = "im_msg_his_new")
public class IMMessage {
    /**
     * 发送消息
     */
    public static final int ON_SEND_IMMESSAGE = 0;
    /**
     * 接受消息
     */
    public static final int ON_RECEIVE_IMMESSAGE = 1;

    public static final int DEFAULT_READ_COUNT = 0;

    public static final int FLAG_MSG_SENDER = 0;

    public static final int FLAG_MSG_RECEIVER = 1;

    /**
     * 消息状态
     */
    /*发送中或者接受中*/
    public static final int STATUS_SENDING = 0;

    /*发送接受成功*/
    public static final int STATUS_SUCCESS = 1;

    /*发送接受失败*/
    public static final int STATUS_FAIL = -1;

    /*暂停*/
    public static final int STATUS_STOP = -2;
    /**
     * 未接收或者未发送
     */
    public static final int STATUS_NO_RECEIVE = -3;

    /**
     * 消息类型
     */
    public static final int TYPE_CHAT = 0;

    /**
     * 群组
     */
    public static final int TYPE_GROUP = 1;

    /**
     * 讨论组
     */
    public static final int TYPE_DISCUSS = 2;

    /**
     * 系统消息
     */
    public static final int TYPE_SYSTEM = 3;

    /**
     * 消息类型
     */
    public static final String STRING_TYPE_CHAT = "chat";
    /**
     * 群组
     */
    public static final String STRING_TYPE_GROUP = "fixGroup";
    /**
     * 讨论组
     */
    public static final String STRING_TYPE_DISCUSS = "group";
//
//    public static final String STRING_TYPE_SYSTEM = 3;

    /**
     * 消息内容类型
     */
    public static final String CONTENT_MESSAGE_TYPE = "IM_message_type";

    /**
     * 定义系统消息
     */
    public static final String CONTENT_MESSAGER_SYSTEM = "IM_system";

    /**
     * 草稿
     */
    public static final String CONTENT_MESSAGER_DRAFTS = "IM_drafts";

    public static final String CONTENT_TYPE_TXT = "IM_txt";

    public static final String CONTENT_TYPE_IMG = "IM_img";

    public static final String CONTENT_TYPE_FILE = "IM_file";

    public static final String CONTENT_TYPE_MAP = "IM_location";

    public static final String CONTENT_TYPE_SHARE = "IM_share";

    public static final String CONTENT_TYPE_SHORT_VOICE = "IM_audio";

    public static final String CONTENT_TYPE_VIDEO = "IM_video";

    public static final String CONTENT_TYPE_VOICE_CHAT = "IM_Voice_Meeting";

    public static final String CONTENT_TYPE_VIDEO_CHAT = "IM_Video_Meeting";

    public static final String CONTENT_TYPE_CANCEL = "IM_Cancel";

    public static final String CONTENT_TYPE_REJECT = "IM_Reject";

    public static final String CONTENT_TYPE_GROUP_REMARK = "IM_Group_Remark";

    public static final String CONTENT_TYPE_FUN = "IM_function";

    public static final String CONTENT_TYPE_REPLY = "IM_Reply";

    public static final String CONTENT_TYPE_RECORD = "IM_ChatRecord";


    /**
     * 消息收藏类型字段
     */
    public static final String COLLECTION_IMAGE = "img";
    public static final String COLLECTION_AUDIO = "audio";
    public static final String COLLECTION_TEXT = "text";
    public static final String COLLECTION_VIDEO = "video";
    public static final String COLLECTION_FILE = "file";
    public static final String COLLECTION_LOCATION = "location";
    public static final String COLLECTION_LINK = "link";

    /**
     * 命令消息类型
     *
     */
    /**
     * 单聊 更改消息阅读状态
     */
    public static final String MESSAGE_READ_SINGLE_CHAT = "MESSAGE_READ_SINGLE_CHAT";
    /**
     * 群聊 更改消息阅读状态
     */
    public static final String MESSAGE_READ_GROUP_CHAT = "MESSAGE_READ_GROUP_CHAT";

    /**
     * 单聊的消息撤回
     */
    public static final String MESSAGE_WITHDRAWAL_GROUP_CHAT = "MESSAGE_WITHDRAWAL_GROUP_CHAT";

    /**
     * 群聊消息的撤回
     */
    public static final String MESSAGE_WITHDRAWAL_SINGLE_CHAT = "MESSAGE_WITHDRAWAL_SINGLE_CHAT";

    /**
     * 群组讨论组相关
     */
    public static final String GROUP_OWN = "GROUP_OWN";

    /**
     * 创建群组
     */
    public static final String GROUP_ADD = "GROUP_ADD";

    /**
     * 同意加入群组
     */
    public static final String GROUP_AGREE = "GROUP_AGREE";

    /**
     * 拒绝加入讨论组
     */
    public static final String GROUP_REFUSE = "GROUP_REFUSE";

    /**
     * 移出讨论组
     */
    public static final String GROUP_REMOVE = "GROUP_REMOVE";

    /**
     * 解散讨论组
     */
    public static final String GROUP_DEL = "GROUP_DEL";

    /**
     * 更新讨论组
     */
    public static final String GROUP_UPDATE = "GROUP_UPDATE";

    /**
     * 更新群组名称
     */
    public static final String GROUP_UPDATE_NAME = "GROUP_UPDATE_NAME";

    /**
     * 跟新名称
     */
    public static final String FIXGROUP_UPDATE_NAME = "FIXGROUP_UPDATE_NAME";


    /**
     * 修改讨论组公告
     */
    public static final String GROUP_UPDATE_REMARK = "GROUP_UPDATE_REMARK";
    /**
     * 修改群公告?
     */
    public static final String FIXGROUP_UPDATE_REMARK = "FIXGROUP_UPDATE_REMARK";

    /**
     * 通过扫描二维码加入群聊
     */
    public static final String SCAN_QRCODE_JOIN_GROUP = "GROUP_QRCODE";

    /**
     * 设置为管理员
     */
    public static final String GROUP_SET_ADMIN = "GROUP_SET_ADMIN";

    public static final String FIXGROUP_APPLY = "FIXGROUP_APPLY";

    public static final String FIXGROUP_REFUSE = "FIXGROUP_REFUSE";

    public static final String FIXGROUP_REMOVE = "FIXGROUP_REMOVE";

    public static final String FIXGROUP_ADD = "FIXGROUP_ADD";

    public static final String FIXGROUP_UPDATE = "FIXGROUP_UPDATE";

    public static final String FIXGROUP_DEL = "FIXGROUP_DEL";

    public static final String FIXGROUP_FORBIDDEN = "FIXGROUP_FORBIDDEN";

    public static final String FIXGROUP_ACTIVE = "FIXGROUP_ACTIVE";

    public static final String FIXGROUP_SET_ADMIN = "FIXGROUP_SET_ADMIN";

    public static final String FIXGROUP_CANCLE_ADMIN = "FIXGROUP_CANCLE_ADMIN";

    public static final String FIXGROUP_AGREE = "FIXGROUP_AGREE";

    public static boolean isCommand(GetMsgsEntity entity) {
        return entity.getParam().getType().startsWith("FIXGROUP")
                || entity.getParam().getType().startsWith("GROUP")
                || entity.getParam().getType().startsWith("MESSAGE");
    }

    /**
     *
     */
    // @Property(nameInDb = "local_id")
    @Id(autoincrement = true)
    // private String localId;
    private Long localId;

    @Property(nameInDb = "msg_id")
    private String msgID;

    /**
     * 服务器给的id
     */
    //  @Id
    @Property(nameInDb = "receiverName")
    private String receiverName;

    @Property(nameInDb = "v_id")
    private String vId;


    /**
     * 会话Id
     */
    @Property(nameInDb = "chat_id")
    private Long cId;

    /**
     * 目标id
     */
    @Property(nameInDb = "tagert_id")
    private String tagertId;

    /**
     * 消息发送类型 0 发送的 1 接收的
     */
    @Property(nameInDb = "sendOrReceive")
    private int sendOrReceive;

    /**
     * 组群名字
     */
    @Property(nameInDb = "group_name")
    private String groupName;

    /**
     * 发送者Id
     */
    @Property(nameInDb = "sender_id")
    private String senderId;

    /**
     * 发送者名字
     */
    @Property(nameInDb = "sender_name")
    private String senderName;

    /**
     * 创建时间
     */
    @Property(nameInDb = "time")
    private long time;

    /**
     * 消息类型
     * 0:单聊 1:群组 2:讨论组 3：系统推送
     */
    @Property(nameInDb = "type")
    private int type;

    /**
     * 消息类型,主要用于在消息在列表中的显示控制
     */
    @Property(nameInDb = "content_type")
    private String contentType;


    @Property(nameInDb = "content")
    private String content;

    /**
     * 发送状态 0:发送中/接受中，1:发送/接受成功，－1:发送/接受失败，－2:暂停
     */
    @Property(nameInDb = "status")
    private int status;

    /**
     * 是否已读
     */
    @Property(nameInDb = "read")
    private boolean isRead;

    /**
     * 已读人数
     */
    @Property(nameInDb = "read_count")
    private int readCount;

    @Property(nameInDb = "un_read_count")
    private int unReadCount;

    /**
     * 类型 0单聊 1讨论组 2群聊
     */
    @Property(nameInDb = "mk")
    private String mk;

    /**
     * 是否免打扰
     */
    private String isDisturb;
    /**
     * 是否@ 我
     */
    private boolean isAiteMe;

    /**
     * 文件
     */
    private Long fId;
    @ToOne(joinProperty = "fId")
    private IMFileInfo IMFileInfo;


    private Long callId;
    @ToOne(joinProperty = "callId")
    private IMCallInfo IMCallInfo;

    /**
     * 地图
     */
    private Long lId;
    @ToOne(joinProperty = "lId")
    private IMLocationInfo IMLocationInfo;

    /**
     * 分享
     */
    private Long sId;
    @ToOne(joinProperty = "sId")
    private IMShareInfo IMShareInfo;

    /**
     * 公告
     */
    private Long rId;
    @ToOne(joinProperty = "rId")
    private IMGroupRemark IMGroupRemark;

    /**
     * 回复消息
     */
    private Long replyId;
    @ToOne(joinProperty = "replyId")
    private IMReplyInfo IMReplyInfo;

    /**
     * 消息记录
     */
    private Long recordId;
    @ToOne(joinProperty = "recordId")
    private IMChatRecordInfo IMChatRecordInfo;

    private String AtInfo;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 450159840)
    private transient IMMessageDao myDao;

    @Generated(hash = 1298365402)
    public IMMessage(Long localId, String msgID, String receiverName, String vId, Long cId,
            String tagertId, int sendOrReceive, String groupName, String senderId, String senderName,
            long time, int type, String contentType, String content, int status, boolean isRead,
            int readCount, int unReadCount, String mk, String isDisturb, boolean isAiteMe, Long fId,
            Long callId, Long lId, Long sId, Long rId, Long replyId, Long recordId, String AtInfo) {
        this.localId = localId;
        this.msgID = msgID;
        this.receiverName = receiverName;
        this.vId = vId;
        this.cId = cId;
        this.tagertId = tagertId;
        this.sendOrReceive = sendOrReceive;
        this.groupName = groupName;
        this.senderId = senderId;
        this.senderName = senderName;
        this.time = time;
        this.type = type;
        this.contentType = contentType;
        this.content = content;
        this.status = status;
        this.isRead = isRead;
        this.readCount = readCount;
        this.unReadCount = unReadCount;
        this.mk = mk;
        this.isDisturb = isDisturb;
        this.isAiteMe = isAiteMe;
        this.fId = fId;
        this.callId = callId;
        this.lId = lId;
        this.sId = sId;
        this.rId = rId;
        this.replyId = replyId;
        this.recordId = recordId;
        this.AtInfo = AtInfo;
    }


    @Generated(hash = 1610895367)
    public IMMessage() {
    }

    @Generated(hash = 1235618567)
    private transient Long IMFileInfo__resolvedKey;
    @Generated(hash = 2032225523)
    private transient Long IMCallInfo__resolvedKey;
    @Generated(hash = 438041281)
    private transient Long IMLocationInfo__resolvedKey;
    @Generated(hash = 1975155453)
    private transient Long IMShareInfo__resolvedKey;
    @Generated(hash = 196453123)
    private transient Long IMGroupRemark__resolvedKey;
    @Generated(hash = 452308016)
    private transient Long IMReplyInfo__resolvedKey;
    @Generated(hash = 1933025039)
    private transient Long IMChatRecordInfo__resolvedKey;

    @Keep
    public void setFileInfo(IMFileInfo IMFileInfo) {
        this.IMFileInfo = IMFileInfo;
    }

    @Keep
    public IMFileInfo getFileInfo() {
        return IMFileInfo;
    }

    @Keep
    public void setLocationInfo(IMLocationInfo localinfo) {
        this.IMLocationInfo = localinfo;
    }

    @Keep
    public IMLocationInfo getLocationInfo() {
        return IMLocationInfo;
    }

    @Keep
    public IMCallInfo getCallInfo() {
        return IMCallInfo;
    }

    @Keep
    public void setCallInfo(IMCallInfo IMCallInfo) {
        this.IMCallInfo = IMCallInfo;
    }

    @Keep
    public boolean isDisturb() {
        return "1".equals(isDisturb);// false 表示免打扰, 反之
    }

    @Keep
    public boolean isFileMsg() {
        if (CONTENT_TYPE_FILE.equals(contentType)) {
            return true;
        }
        if (CONTENT_TYPE_SHORT_VOICE.equals(contentType)) {
            return true;
        }
        if (CONTENT_TYPE_VIDEO.equals(contentType)) {
            return true;
        }
        if (CONTENT_TYPE_IMG.equals(contentType)) {
            return true;
        }
        return false;
    }


    public Long getLocalId() {
        return this.localId;
    }


    public void setLocalId(Long localId) {
        this.localId = localId;
    }


    public String getMsgID() {
        return this.msgID;
    }


    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }


    public String getReceiverName() {
        return this.receiverName;
    }


    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    public String getVId() {
        return this.vId;
    }


    public void setVId(String vId) {
        this.vId = vId;
    }


    public Long getCId() {
        return this.cId;
    }


    public void setCId(Long cId) {
        this.cId = cId;
    }


    public String getTagertId() {
        return this.tagertId;
    }


    public void setTagertId(String tagertId) {
        this.tagertId = tagertId;
    }


    public int getSendOrReceive() {
        return this.sendOrReceive;
    }


    public void setSendOrReceive(int sendOrReceive) {
        this.sendOrReceive = sendOrReceive;
    }


    public String getGroupName() {
        return this.groupName;
    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public String getSenderId() {
        return this.senderId;
    }


    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }


    public String getSenderName() {
        return this.senderName;
    }


    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public long getTime() {
        return this.time;
    }


    public void setTime(long time) {
        this.time = time;
    }


    public int getType() {
        return this.type;
    }


    public void setType(int type) {
        this.type = type;
    }


    public String getContentType() {
        return this.contentType;
    }


    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public String getContent() {
        return this.content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public int getStatus() {
        return this.status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public boolean getIsRead() {
        return this.isRead;
    }


    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }


    public int getReadCount() {
        return this.readCount;
    }


    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }


    public int getUnReadCount() {
        return this.unReadCount;
    }


    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }


    public String getMk() {
        return this.mk;
    }


    public void setMk(String mk) {
        this.mk = mk;
    }


    public String getIsDisturb() {
        return this.isDisturb;
    }


    public void setIsDisturb(String isDisturb) {
        this.isDisturb = isDisturb;
    }


    public boolean getIsAiteMe() {
        return this.isAiteMe;
    }


    public void setIsAiteMe(boolean isAiteMe) {
        this.isAiteMe = isAiteMe;
    }


    public Long getFId() {
        return this.fId;
    }


    public void setFId(Long fId) {
        this.fId = fId;
    }


    public Long getCallId() {
        return this.callId;
    }


    public void setCallId(Long callId) {
        this.callId = callId;
    }


    public Long getLId() {
        return this.lId;
    }


    public void setLId(Long lId) {
        this.lId = lId;
    }


    public Long getSId() {
        return this.sId;
    }


    public void setSId(Long sId) {
        this.sId = sId;
    }


    public Long getRId() {
        return this.rId;
    }


    public void setRId(Long rId) {
        this.rId = rId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1833053811)
    public IMFileInfo getIMFileInfo() {
        Long __key = this.fId;
        if (IMFileInfo__resolvedKey == null || !IMFileInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMFileInfoDao targetDao = daoSession.getIMFileInfoDao();
            IMFileInfo IMFileInfoNew = targetDao.load(__key);
            synchronized (this) {
                IMFileInfo = IMFileInfoNew;
                IMFileInfo__resolvedKey = __key;
            }
        }
        return IMFileInfo;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1201477960)
    public void setIMFileInfo(IMFileInfo IMFileInfo) {
        synchronized (this) {
            this.IMFileInfo = IMFileInfo;
            fId = IMFileInfo == null ? null : IMFileInfo.getFId();
            IMFileInfo__resolvedKey = fId;
        }
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1800698918)
    public IMCallInfo getIMCallInfo() {
        Long __key = this.callId;
        if (IMCallInfo__resolvedKey == null || !IMCallInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMCallInfoDao targetDao = daoSession.getIMCallInfoDao();
            IMCallInfo IMCallInfoNew = targetDao.load(__key);
            synchronized (this) {
                IMCallInfo = IMCallInfoNew;
                IMCallInfo__resolvedKey = __key;
            }
        }
        return IMCallInfo;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1989300522)
    public void setIMCallInfo(IMCallInfo IMCallInfo) {
        synchronized (this) {
            this.IMCallInfo = IMCallInfo;
            callId = IMCallInfo == null ? null : IMCallInfo.getId();
            IMCallInfo__resolvedKey = callId;
        }
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 688096774)
    public IMLocationInfo getIMLocationInfo() {
        Long __key = this.lId;
        if (IMLocationInfo__resolvedKey == null || !IMLocationInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMLocationInfoDao targetDao = daoSession.getIMLocationInfoDao();
            IMLocationInfo IMLocationInfoNew = targetDao.load(__key);
            synchronized (this) {
                IMLocationInfo = IMLocationInfoNew;
                IMLocationInfo__resolvedKey = __key;
            }
        }
        return IMLocationInfo;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2005769930)
    public void setIMLocationInfo(IMLocationInfo IMLocationInfo) {
        synchronized (this) {
            this.IMLocationInfo = IMLocationInfo;
            lId = IMLocationInfo == null ? null : IMLocationInfo.getLId();
            IMLocationInfo__resolvedKey = lId;
        }
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1023897891)
    public IMShareInfo getIMShareInfo() {
        Long __key = this.sId;
        if (IMShareInfo__resolvedKey == null || !IMShareInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMShareInfoDao targetDao = daoSession.getIMShareInfoDao();
            IMShareInfo IMShareInfoNew = targetDao.load(__key);
            synchronized (this) {
                IMShareInfo = IMShareInfoNew;
                IMShareInfo__resolvedKey = __key;
            }
        }
        return IMShareInfo;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1403537516)
    public void setIMShareInfo(IMShareInfo IMShareInfo) {
        synchronized (this) {
            this.IMShareInfo = IMShareInfo;
            sId = IMShareInfo == null ? null : IMShareInfo.getSId();
            IMShareInfo__resolvedKey = sId;
        }
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1975555056)
    public IMGroupRemark getIMGroupRemark() {
        Long __key = this.rId;
        if (IMGroupRemark__resolvedKey == null || !IMGroupRemark__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMGroupRemarkDao targetDao = daoSession.getIMGroupRemarkDao();
            IMGroupRemark IMGroupRemarkNew = targetDao.load(__key);
            synchronized (this) {
                IMGroupRemark = IMGroupRemarkNew;
                IMGroupRemark__resolvedKey = __key;
            }
        }
        return IMGroupRemark;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 914700176)
    public void setIMGroupRemark(IMGroupRemark IMGroupRemark) {
        synchronized (this) {
            this.IMGroupRemark = IMGroupRemark;
            rId = IMGroupRemark == null ? null : IMGroupRemark.getRId();
            IMGroupRemark__resolvedKey = rId;
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


    public Long getReplyId() {
        return this.replyId;
    }


    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 163847087)
    public IMReplyInfo getIMReplyInfo() {
        Long __key = this.replyId;
        if (IMReplyInfo__resolvedKey == null || !IMReplyInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMReplyInfoDao targetDao = daoSession.getIMReplyInfoDao();
            IMReplyInfo IMReplyInfoNew = targetDao.load(__key);
            synchronized (this) {
                IMReplyInfo = IMReplyInfoNew;
                IMReplyInfo__resolvedKey = __key;
            }
        }
        return IMReplyInfo;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1583240933)
    public void setIMReplyInfo(IMReplyInfo IMReplyInfo) {
        synchronized (this) {
            this.IMReplyInfo = IMReplyInfo;
            replyId = IMReplyInfo == null ? null : IMReplyInfo.getRId();
            IMReplyInfo__resolvedKey = replyId;
        }
    }


    public Long getRecordId() {
        return this.recordId;
    }


    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 554553995)
    public IMChatRecordInfo getIMChatRecordInfo() {
        Long __key = this.recordId;
        if (IMChatRecordInfo__resolvedKey == null || !IMChatRecordInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IMChatRecordInfoDao targetDao = daoSession.getIMChatRecordInfoDao();
            IMChatRecordInfo IMChatRecordInfoNew = targetDao.load(__key);
            synchronized (this) {
                IMChatRecordInfo = IMChatRecordInfoNew;
                IMChatRecordInfo__resolvedKey = __key;
            }
        }
        return IMChatRecordInfo;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 988639720)
    public void setIMChatRecordInfo(IMChatRecordInfo IMChatRecordInfo) {
        synchronized (this) {
            this.IMChatRecordInfo = IMChatRecordInfo;
            recordId = IMChatRecordInfo == null ? null : IMChatRecordInfo.getRId();
            IMChatRecordInfo__resolvedKey = recordId;
        }
    }


    public String getAtInfo() {
        return this.AtInfo;
    }


    public void setAtInfo(String AtInfo) {
        this.AtInfo = AtInfo;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 851604617)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIMMessageDao() : null;
    }


}
