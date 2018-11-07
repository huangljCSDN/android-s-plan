package com.xsimple.im.db;

import android.content.Context;
import android.text.TextUtils;

import com.networkengine.entity.GetMsgsEntity;
import com.networkengine.entity.GroupMember;
import com.networkengine.util.LogUtil;
import com.xsimple.im.db.datatable.IMBHelper;
import com.xsimple.im.db.datatable.IMBoxMessage;
import com.xsimple.im.db.datatable.IMCallInfo;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMChatRecordInfo;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMFileInfoPice;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMGroupUser;
import com.xsimple.im.db.datatable.IMLocationInfo;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMOfficialMessage;
import com.xsimple.im.db.datatable.IMReplyInfo;
import com.xsimple.im.db.datatable.IMSysMessage;
import com.xsimple.im.db.datatable.IMUser;
import com.xsimple.im.db.greendao.DaoMaster;
import com.xsimple.im.db.greendao.DaoSession;
import com.xsimple.im.db.greendao.IMBoxMessageDao;
import com.xsimple.im.db.greendao.IMCallInfoDao;
import com.xsimple.im.db.greendao.IMChatDao;
import com.xsimple.im.db.greendao.IMChatRecordInfoDao;
import com.xsimple.im.db.greendao.IMFileInfoDao;
import com.xsimple.im.db.greendao.IMFileInfoPiceDao;
import com.xsimple.im.db.greendao.IMGroupDao;
import com.xsimple.im.db.greendao.IMGroupRemarkDao;
import com.xsimple.im.db.greendao.IMGroupUserDao;
import com.xsimple.im.db.greendao.IMLocationInfoDao;
import com.xsimple.im.db.greendao.IMMessageDao;
import com.xsimple.im.db.greendao.IMOfficialMessageDao;
import com.xsimple.im.db.greendao.IMReplyInfoDao;
import com.xsimple.im.db.greendao.IMSysMessageDao;
import com.xsimple.im.db.greendao.IMUserDao;
import com.xsimple.im.engine.IMEngine;
import com.xsimple.im.engine.protocol.MsgEntity;
import com.xsimple.im.event.NewSysMsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengpeng on 17/4/10.
 * 数据库工具类
 */

public class DbManager {

    private static final Long FAIL_ID = -1L;

    private Context mContext;

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;

    private IMChatDao mChatDao;

    private IMMessageDao mMessageDao;

    private IMSysMessageDao mSysMessageDao;

    private IMUserDao mUserDao;

    private IMFileInfoDao mFileInfoDao;

    private IMLocationInfoDao mLocationInfoDao;

    private IMGroupDao mIMGroupDao;

    private IMGroupUserDao mIMGroupUserDao;

    private IMFileInfoPiceDao mImFileInfoPiceDao;

    private IMCallInfoDao mCallInfoDao;

    private IMGroupRemarkDao mGroupRemarkDao;

    private IMReplyInfoDao mImReplyInfoDao;

    private IMChatRecordInfoDao mIMChatRecordInfoDao;

    private IMBoxMessageDao mImBoxMessageDao;

    private IMOfficialMessageDao mImOfficialMessageDao;

    private static DbManager mDbManager;

    private DbManager(Context context) {
        mContext = context;

        //   DaoMaster.DevOpenHelper imbHelper = new DaoMaster.DevOpenHelper(context, "xsimple_im_e", null);
        IMBHelper imbHelper = new IMBHelper(context, "xsimple_im_e", null);
        //加密数据库 后面是密码
//        mDaoMaster = new DaoMaster(imbHelper.getEncryptedWritableDb(PubConstant.datebase.DATEBASE_PASSWORD));
         mDaoMaster = new DaoMaster(imbHelper.getWritableDatabase());

        mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);

        mChatDao = mDaoSession.getIMChatDao();

        mMessageDao = mDaoSession.getIMMessageDao();

        mSysMessageDao = mDaoSession.getIMSysMessageDao();

        mUserDao = mDaoSession.getIMUserDao();

        mFileInfoDao = mDaoSession.getIMFileInfoDao();

        mLocationInfoDao = mDaoSession.getIMLocationInfoDao();

        mIMGroupDao = mDaoSession.getIMGroupDao();

        mImFileInfoPiceDao = mDaoSession.getIMFileInfoPiceDao();

        mCallInfoDao = mDaoSession.getIMCallInfoDao();

        mIMGroupUserDao = mDaoSession.getIMGroupUserDao();

        mGroupRemarkDao = mDaoSession.getIMGroupRemarkDao();

        mImReplyInfoDao = mDaoSession.getIMReplyInfoDao();

        mIMChatRecordInfoDao = mDaoSession.getIMChatRecordInfoDao();

        mImBoxMessageDao = mDaoSession.getIMBoxMessageDao();

        mImOfficialMessageDao = mDaoSession.getIMOfficialMessageDao();
    }

    public static DbManager getInstance(Context ct) {

        if (mDbManager == null) {

            mDbManager = new DbManager(ct.getApplicationContext());
        }
        return mDbManager;
    }

    /**
     * 添加所有用户
     */
    public boolean insertAllUser(List<IMUser> IMUsers) {

        if (IMUsers == null || IMUsers.isEmpty()) {
            return false;
        }

        List<IMUser> oldIMUsers = mUserDao.loadAll();

        if (oldIMUsers != null && !oldIMUsers.isEmpty()) {
            mUserDao.deleteAll();
        }

        // mUserDao.insertInTx(IMUsers);
        mUserDao.insertOrReplaceInTx(IMUsers);

        return true;
    }

    /**
     * 添加或修改用户
     */
    public boolean insertOrReplaceUser(IMUser imUser) {
        return imUser != null && mUserDao.insertOrReplace(imUser) != FAIL_ID;
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(IMUser imUser) {
        if (imUser == null) {
            return false;
        }
        mUserDao.delete(imUser);
        return getUser(imUser.getId()) == null;
    }

    /**
     * 添加所有用户
     */
    public List<IMUser> getAllUser() {
        return mUserDao.loadAll();
    }

    /**
     * 从本地缓存中获取user对象
     *
     * @param uId 用户ID
     * @return 返回用户信息
     */
    public IMUser getUser(String uId) {
        Query<IMUser> query = mUserDao.queryBuilder().where(IMUserDao.Properties.Id.eq(uId))
                .orderDesc(IMUserDao.Properties.Id).build();
        return query.unique();
    }

    /**
     * 获得用户所有会话
     *
     * @param uId 用户表主键
     */
    public List<IMChat> getAllChatByUid(String uId) {

        if (mUserDao == null) {
            return null;
        }

        IMUser IMUser = mUserDao.load(uId);

        if (IMUser == null) {
            return mChatDao._queryIMUser_IMChats(uId);
        }

        return IMUser.getIMChats();
    }

    /**
     * 保存会话
     *
     * @param imChat 对象
     */
    public void saveChat(IMChat imChat) {
        mChatDao.insertOrReplaceInTx(imChat);
    }

    /**
     * 获得会话中所有的消息
     *
     * @param cId 会话id
     */
    public List<IMMessage> getMessages(Long cId) {
        // TODO: 2018/6/7 PWY: 如果消息太多的话，会有性能问题
        if (mChatDao == null) {
            return null;
        }

        IMChat IMChat = mChatDao.load(cId);

        if (IMChat == null) {
            return null;
        }
        return IMChat.getIMMessages();
    }

    /**
     * 获得会话中所有的消息
     *
     * @param LocalId 会话id
     */
    public IMMessage getIMMessages(Long LocalId) {

        return mMessageDao.load(LocalId);
    }


    /**
     * 查询所有系统消息(未清理状态)
     */
    public List<IMSysMessage> queryAllSystemMessages() {
        String myId = IMEngine.getInstance(mContext).getMyId();
        return mSysMessageDao.queryBuilder()
                .where(IMSysMessageDao.Properties.IsClear.eq(false)
                        , IMSysMessageDao.Properties.CurrUserId.eq(myId))
                .orderDesc(IMSysMessageDao.Properties.ReceivedTimer)
                .list();
    }

    public long queryUnreadSysMessageCount() {
        String myId = IMEngine.getInstance(mContext).getMyId();
        return mSysMessageDao.queryBuilder()
                .where(IMSysMessageDao.Properties.IsClear.eq(false)
                        , IMSysMessageDao.Properties.IsRead.eq(false)
                        , IMSysMessageDao.Properties.CurrUserId.eq(myId))
                .orderDesc(IMSysMessageDao.Properties.ReceivedTimer)
                .count();
    }

    public long queryUnreadMessageCount() {
        String myId = IMEngine.getInstance(mContext).getMyId();
        List<IMChat> chats = IMEngine.getInstance(mContext).getChats(myId);
        long newConunt = 0;
        if (chats != null && !chats.isEmpty()) {

            for (IMChat chat : chats) {
                if (chat == null) {
                    continue;
                }
                newConunt += chat.getUnReadCount();
            }
        }
        return newConunt;
    }


    public void insertSysMessage(IMSysMessage sysMessage) {
        if (sysMessage != null) {
            mSysMessageDao.insertOrReplace(sysMessage);
        }
    }

    public void insertBoxMessage(IMBoxMessage boxMessage) {
        if (boxMessage != null) {
            mImBoxMessageDao.insertOrReplace(boxMessage);
        }
    }

    public void insertOfficailMessage(IMOfficialMessage officialMessage) {
        if (officialMessage != null) {
            mImOfficialMessageDao.insertOrReplace(officialMessage);
        }
    }

    /**
     * 添加所有消息
     */
    public void insertAllMessage(List<IMMessage> IMMessages) {

        if (IMMessages == null || IMMessages.isEmpty()) {
            return;
        }

        List<IMMessage> oldIMMessages = mMessageDao.loadAll();

        if (oldIMMessages != null && !oldIMMessages.isEmpty()) {
            mMessageDao.deleteAll();
        }

        mMessageDao.insertInTx(IMMessages);
    }

    /**
     * 获取会话的所有消息
     */
    public List<IMMessage> loadIMMessage(long chat_id) {

        QueryBuilder<IMMessage> builder;

        builder = mMessageDao.queryBuilder();

        builder.where(IMMessageDao.Properties.CId.eq(chat_id), IMMessageDao.Properties.ContentType.notEq(IMMessage.CONTENT_TYPE_REJECT), IMMessageDao.Properties.ContentType.notEq(IMMessage.CONTENT_TYPE_CANCEL))
                .orderAsc(IMMessageDao.Properties.Time);


        return builder.build().list();
    }

    /**
     * 所有的盒子消息
     * @param chat_id
     * @return
     */
    public List<IMBoxMessage> loadBoxIMMessage(long chat_id) {

        QueryBuilder<IMBoxMessage> builder;

        builder = mImBoxMessageDao.queryBuilder();

        builder.where(IMBoxMessageDao.Properties.CId.eq(chat_id))
                .orderAsc(IMMessageDao.Properties.Time);
        return builder.build().list();
    }

    /**
     * 获取所有的官方消息
     * @param chat_id
     * @return
     */
    public List<IMOfficialMessage> loadOfficialIMMessage(long chat_id) {

        QueryBuilder<IMOfficialMessage> builder;

        builder = mImOfficialMessageDao.queryBuilder();

        builder.where(IMOfficialMessageDao.Properties.CId.eq(chat_id))
                .orderAsc(IMMessageDao.Properties.Time);
        return builder.build().list();
    }

    /**
     * 获取会话的所有消息，只用于聊天记录详情页查询，不区分登录用户
     */
    public List<IMMessage> loadMsgHisByVid(ArrayList<String> ids) {
        QueryBuilder<IMMessage> builder;
        builder = mMessageDao.queryBuilder();
        builder.where(IMMessageDao.Properties.CId.eq(-1L), IMMessageDao.Properties.VId.in(ids));
        return builder.build().list();
    }

    public void saveChatHisMsg(IMMessage imMessage) {
        mMessageDao.insertOrReplaceInTx(imMessage);
    }

    public IMMessage loadUnreadMsgIndex(long chat_id) {

        QueryBuilder<IMMessage> builder;

        builder = mMessageDao.queryBuilder();

        builder.where(IMMessageDao.Properties.CId.eq(chat_id), IMMessageDao.Properties.ContentType.notEq(IMMessage.CONTENT_TYPE_REJECT)
                , IMMessageDao.Properties.ContentType.notEq(IMMessage.CONTENT_TYPE_CANCEL)
                , IMMessageDao.Properties.IsRead.eq(false)
                , IMMessageDao.Properties.SendOrReceive.eq(IMMessage.ON_RECEIVE_IMMESSAGE))
                .orderAsc(IMMessageDao.Properties.Time).limit(1);

        return builder.unique();
    }


    /**
     * 获取aite 我的消息
     */
    public List<IMMessage> loadAtIMMessage(long chat_id) {

        QueryBuilder<IMMessage> builder;


        builder = mMessageDao.queryBuilder();

        builder.where(IMMessageDao.Properties.CId.eq(chat_id), IMMessageDao.Properties.ContentType.notEq(IMMessage.CONTENT_TYPE_REJECT)
                , IMMessageDao.Properties.ContentType.notEq(IMMessage.CONTENT_TYPE_CANCEL)
                , IMMessageDao.Properties.IsRead.eq(false)
                , IMMessageDao.Properties.SendOrReceive.eq(IMMessage.ON_RECEIVE_IMMESSAGE)
                , IMMessageDao.Properties.IsAiteMe.eq(true))
                .orderAsc(IMMessageDao.Properties.Time);
        //
        return builder.build().list();
    }


    /**
     * 添加消息
     */
    public void addMessages(List<IMMessage> IMMessages) {
        if (IMMessages == null || IMMessages.isEmpty()) {
            return;
        }

        mMessageDao.insertOrReplaceInTx(IMMessages);

    }

    /**
     * 根据虚拟ID查消息，用于转发
     *
     * @param vid 虚拟ID
     * @return 消息
     */
    public IMMessage loadIMMessageByVid(String vid) {
        QueryBuilder<IMMessage> builder;
        builder = mMessageDao.queryBuilder();
        builder.where(IMMessageDao.Properties.VId.eq(vid)).limit(1);
        List<IMMessage> list = builder.list();
        return null == list || list.size() == 0 ? null : list.get(0);
    }

    /**
     * 根据localId查消息
     *
     * @param localId 本地id
     * @return 消息
     */
    public IMMessage loadIMMessageByLocalId(long localId) {
        return mMessageDao.load(localId);
    }


    /**
     * 添加消息
     */
    public void addMessages(IMMessage iMMessages) {
        if (iMMessages == null) {
            return;
        }
        mMessageDao.insert(iMMessages);

    }

    //删除所有聊天记录
    public void deleteAllData() {
        DaoMaster.dropAllTables(mDaoMaster.getDatabase(), true);
        DaoMaster.createAllTables(mDaoMaster.getDatabase(), true);
    }

    public IMChat getChat(String myId, String targetId, int sessionType) {

        if (TextUtils.isEmpty(myId) || TextUtils.isEmpty(targetId)) {
            return null;
        }

        List<IMChat> chats = getAllChatByUid(myId);

        if (chats == null || chats.isEmpty()) {
            return null;
        }

        if (TextUtils.equals(myId, targetId)) {
            for (IMChat chat : chats) {
                if (chat == null) {
                    continue;
                }
                //自己跟自己聊天，文件助手
                if (TextUtils.equals(chat.getSenderOrTarget1(), chat.getSenderOrTarget2()) && chat.getType() == sessionType) {
                    return chat;
                }
            }
        } else {
            for (IMChat chat : chats) {

                if (chat == null) {
                    continue;
                }

            /*在我的所有会话中，是否存在和target的会话*/
                if ((chat.getSenderOrTarget1().equals(targetId) || chat.getSenderOrTarget2().equals(targetId)) && chat.getType() == sessionType) {
                    return chat;
                }
            }
        }

        return null;
    }

    /**
     * 创建一个会话，同时会在消息列表显示，所以调用该方法时思考一下你操作会不会导致更新消息列表
     *
     * @param myId     当前用户id
     * @param targetId 会话对方用户id
     * @param type     会话类型
     */
    public IMChat createChat(String myId, String targetId, String targetName, @IMChat.SessionType int type) {
        IMChat chat = new IMChat(myId, myId, type, targetName, 0, System.currentTimeMillis(), targetId, "", "");
        chat.setId(mChatDao.insert(chat));
        return chat;
    }

    /**
     * 判断会话是否存在
     */
    private IMChat getChatId(List<IMChat> oldIMChats, IMMessage iMMessage) {

        if (oldIMChats == null || oldIMChats.isEmpty() || iMMessage == null) {
            return null;
        }

        /*单聊*/
        if (isSingleChatMessage(iMMessage)) {

            for (IMChat chat : oldIMChats) {
                LogUtil.i("IMChat ==="+chat.toString());
                if (chat == null) {
                    continue;
                }

                /*
                ＊ 接口设计问题导致这种蹩脚逻辑
                ＊ 会话中记录的发送者标签等于新消息中的发送者Id并且接收者标签等于新消息里的接收者Id 或者
                ＊ 会话中记录的发送者标签等于新消息中的接收者Id并且接收者标签等于新消息里的发送者Id 视为已经存在该会话了
                 * */
                if ((chat.getSenderOrTarget1().equals(iMMessage.getSenderId())
                        && chat.getSenderOrTarget2().equals(iMMessage.getTagertId())
                        || chat.getSenderOrTarget1().equals(iMMessage.getTagertId())
                        && chat.getSenderOrTarget2().equals(iMMessage.getSenderId()))
                        && chat.getType() == iMMessage.getType()) {
                    return chat;
                }

            }

            return null;

        } else {

            for (IMChat chat : oldIMChats) {

                if (chat == null) {
                    continue;
                }

                /*群组消息是订阅后才能收到，如果能收到,一定和当前用户相关属于当前用户的会话，只用判断Target是否相等就能确定是不是同一个会话*/
                if (chat.getSenderOrTarget2().equals(iMMessage.getTagertId())) {
                    return chat;
                }
            }

            return null;
        }

    }

    /**
     * 更新会话
     */
    public boolean addOrUpdateMsgToChat(String uId, List<IMMessage> iMMessages) {

        if (uId == null || iMMessages == null || iMMessages.isEmpty()) {
            return false;
        }

        for (IMMessage msg : iMMessages) {

            if (msg == null) {
                continue;
            }

            addOrUpdateMsgToChat(uId, msg, "");

        }

        return true;
    }


    /**
     * 更新会话---添加历史消息
     */
    public boolean addOrUpdateHisMsgToChat(String uId, List<IMMessage> iMMessages) {

        if (uId == null || iMMessages == null || iMMessages.isEmpty()) {
            return false;
        }

        for (IMMessage msg : iMMessages) {

            if (msg == null) {
                continue;
            }

            addOrUpdateHisMsgToChat(uId, msg);

        }

        return true;
    }

    /**
     * 删除消息
     */
    public boolean deleteMsgs(List<IMMessage> msgs) {

        if (msgs != null && !msgs.isEmpty()) {
            mMessageDao.deleteInTx(msgs);
        }

        return true;
    }

    /**
     * 向会话中添加消息
     */
    public boolean addOrUpdateMsgToChat(String uId, IMMessage iMMessage, String singleTargetName) {
        if (iMMessage == null || uId == null) {
            return false;
        }

        IMUser imUser = mUserDao.load(uId);

        List<IMChat> oldIMChats;

        if (imUser == null) {
            oldIMChats = mChatDao.queryBuilder().where(IMChatDao.Properties.UId.eq(uId)).build().list();
        } else {
            oldIMChats = getAllChatByUid(uId);
        }

        IMChat chat = null;

        /*本地有会话查寻确认是否有对应的会话存在*/
        if (oldIMChats != null && !oldIMChats.isEmpty()) {
            chat = getChatId(oldIMChats, iMMessage);
        }

        boolean isSetUnReadCount = setUnReadCount(uId, iMMessage);

         /*是否为新会话要加入本地数据库*/
        if (chat == null) {

            String chatName;
            if (isSingleChatMessage(iMMessage)) {
                if (uId.equals(IMEngine.getInstance(mContext).getMyId())) {
                    chatName = singleTargetName;
                } else {
                    chatName = iMMessage.getSenderName();
                }
            } else {
                chatName = IMEngine.getInstance(mContext).getIMGroup(iMMessage.getTagertId()).getName();
            }

            IMChat newIMChat = new IMChat(uId, iMMessage.getSenderId(), iMMessage.getType(), chatName, 0, iMMessage.getTime(), iMMessage.getTagertId(), iMMessage.getReceiverName(), "");
            //设置消息的未阅读数量  消息为接收的消息
//            if (iMMessage.getSendOrReceive() == IMMessage.ON_RECEIVE_IMMESSAGE) {
//                newIMChat.setUnReadCount(1);
//            }
            if (isSetUnReadCount) {
                newIMChat.setUnReadCount(1);
            }
            newIMChat.setTime(iMMessage.getTime());
            Long chatId = mChatDao.insert(newIMChat);
            iMMessage.setCId(chatId);
        } else {
            iMMessage.setCId(chat.getId());
            if (isSetUnReadCount) {
                chat.setUnReadCount(chat.getUnReadCount() + 1);
            }
            //设置消息的未阅读数量  消息为接收的消息
//            if (iMMessage.getSendOrReceive() == IMMessage.ON_RECEIVE_IMMESSAGE) {
//                if (!(iMMessage.getContentType().equals(IMMessage.CONTENT_TYPE_REJECT) ||
//                        iMMessage.getContentType().equals(IMMessage.CONTENT_TYPE_CANCEL))) {
//                    if (!iMMessage.getIsRead()) {
//                        chat.setUnReadCount(chat.getUnReadCount() + 1);
//                    }
//                }
//            }
            chat.setTime(iMMessage.getTime());
            //修改会话的名字
            if (!isSingleChatMessage(iMMessage) && !TextUtils.isEmpty(iMMessage.getGroupName())) {
                chat.setName(iMMessage.getGroupName());
            }

            chat.update();


        }


        /*将消息插入数据库*/
        mMessageDao.insertOrReplaceInTx(iMMessage);

        return true;

    }

    /**
     * 是否设置未读
     *
     * @param uid
     * @param imMessage
     * @return
     */
    private boolean setUnReadCount(String uid, IMMessage imMessage) {
        if (imMessage.getSendOrReceive() == IMMessage.ON_SEND_IMMESSAGE) {
            return false;
        }
        if (imMessage.getIsRead()) {
            return false;
        }
        //消息为转化过的系统消息
        if (IMMessage.CONTENT_MESSAGER_SYSTEM.equals(imMessage.getContentType())) {
            //系统消息的发送人为自己
            if (imMessage.getSenderId().equals(uid)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 向会话中添加系统消息
     */
    public boolean addOrUpdateSystemMsgToChat(IMSysMessage imSysMessage) {

        if (imSysMessage == null) {
            return false;
        }


        //获取系统消息的会话
        List<IMChat> iMChats = mChatDao.queryBuilder().where(IMChatDao.Properties.Type.eq(IMChat.SESSION_SYSTEM_MSG)).build().list();


        IMChat chat = null;

        if (iMChats != null && !iMChats.isEmpty()) {
            chat = iMChats.get(0);
        }

         /*是否为新会话要加入本地数据库*/
        if (chat == null) {
            String chatName = "系统消息";
            IMChat newIMChat = new IMChat(imSysMessage.getCurrUserId(), "", IMChat.SESSION_SYSTEM_MSG, chatName, 1, imSysMessage.getReceivedTimer(), "", "", "");
            Long chatId = mChatDao.insert(newIMChat);
            imSysMessage.setCId(chatId);
        } else {
            imSysMessage.setCId(chat.getId());
            if (!imSysMessage.getIsRead()) {
                chat.setUnReadCount(chat.getUnReadCount() + 1);
            }
            chat.setTime(imSysMessage.getReceivedTimer());
            chat.update();
        }

        /*将消息插入数据库*/
        insertSysMessage(imSysMessage);
        return true;

    }

    /**
     * 向会话中添加盒子消息
     */
    public boolean addOrUpdateBoxMsgToChat(IMBoxMessage imBoxMessage) {

        if (imBoxMessage == null) {
            return false;
        }


        //获取系统消息的会话
        List<IMChat> iMChats = mChatDao.queryBuilder().where(IMChatDao.Properties.Type.eq(IMChat.SESSION_BOX_MSG)).build().list();


        IMChat chat = null;

        if (iMChats != null && !iMChats.isEmpty()) {
            chat = iMChats.get(0);
        }

        /*是否为新会话要加入本地数据库*/
        if (chat == null) {
            String chatName = "盒子小助手";
            IMChat newIMChat = new IMChat(imBoxMessage.getUserId(), "", IMChat.SESSION_BOX_MSG, chatName, 1, imBoxMessage.getSendTimer(), "", "", "");
            Long chatId = mChatDao.insert(newIMChat);
            imBoxMessage.setCId(chatId);
        } else {
            imBoxMessage.setCId(chat.getId());
            if (!imBoxMessage.getIsRead()) {
                chat.setUnReadCount(chat.getUnReadCount() + 1);
            }
            chat.setTime(imBoxMessage.getSendTimer());
            chat.update();
        }

        /*将消息插入数据库*/
        insertBoxMessage(imBoxMessage);
        return true;
    }

    /**
     * 向会话中添加官方消息
     */
    public boolean addOrUpdateOfficialMsgToChat(IMOfficialMessage imOfficialMessage) {

        if (imOfficialMessage == null) {
            return false;
        }


        //获取系统消息的会话
        List<IMChat> iMChats = mChatDao.queryBuilder().where(IMChatDao.Properties.Type.eq(IMChat.SESSION_OFFICIAL_MSG)).build().list();


        IMChat chat = null;

        if (iMChats != null && !iMChats.isEmpty()) {
            chat = iMChats.get(0);
        }

        /*是否为新会话要加入本地数据库*/
        if (chat == null) {
            String chatName = "官方消息";
            IMChat newIMChat = new IMChat(imOfficialMessage.getUserId(), "", IMChat.SESSION_OFFICIAL_MSG, chatName, 1, imOfficialMessage.getSendTimer(), "", "", "");
            Long chatId = mChatDao.insert(newIMChat);
            imOfficialMessage.setCId(chatId);
        } else {
            imOfficialMessage.setCId(chat.getId());
            if (!imOfficialMessage.getIsRead()) {
                chat.setUnReadCount(chat.getUnReadCount() + 1);
            }
            chat.setTime(imOfficialMessage.getSendTimer());
            chat.update();
        }

        /*将消息插入数据库*/
        insertOfficailMessage(imOfficialMessage);
        return true;

    }

    /**
     * 向会话中添加消息----历史消息
     */
    public boolean addOrUpdateHisMsgToChat(String uId, IMMessage iMMessage) {

        if (iMMessage == null || uId == null) {
            return false;
        }

        IMUser imUser = mUserDao.load(uId);

        List<IMChat> oldIMChats;

        if (imUser == null) {
            oldIMChats = mChatDao.queryBuilder().where(IMChatDao.Properties.UId.eq(uId)).build().list();
        } else {
            oldIMChats = getAllChatByUid(uId);
        }

        IMChat chat = null;

        /*本地有会话查寻确认是否有对应的会话存在*/
        if (oldIMChats != null && !oldIMChats.isEmpty()) {
            chat = getChatId(oldIMChats, iMMessage);
        }
        LogUtil.i("getChat=="+chat.toString());

         /*是否为新会话要加入本地数据库*/
        if (chat == null) {

            String chatName = isSingleChatMessage(iMMessage) ? iMMessage.getSenderName() : iMMessage.getGroupName();

            IMChat newIMChat = new IMChat(uId, iMMessage.getSenderId(), iMMessage.getType(), chatName, 0, iMMessage.getTime(), iMMessage.getTagertId(), iMMessage.getReceiverName(), "");

            newIMChat.setTime(iMMessage.getTime());
            Long chatId = mChatDao.insert(newIMChat);
            iMMessage.setCId(chatId);
        } else {
            iMMessage.setCId(chat.getId());

            chat.setTime(iMMessage.getTime());
            //修改会话的名字
            if (!isSingleChatMessage(iMMessage) && !TextUtils.isEmpty(iMMessage.getGroupName())) {
                chat.setName(iMMessage.getGroupName());
            }

            chat.update();

        }

        mMessageDao.insertOrReplaceInTx(iMMessage);


        return true;

    }


    /**
     * 向会话中添加草稿
     */
    public boolean addOrUpdateMsgToChatDrafts(IMChat chat, IMMessage iMMessage) {
        chat.setDrafts(iMMessage.getContent());
        chat.setTime(iMMessage.getTime());
        //修改会话的名字
        if (!isSingleChatMessage(iMMessage) && !TextUtils.isEmpty(iMMessage.getGroupName())) {
            chat.setName(iMMessage.getGroupName());
        }
        chat.update();
        return true;

    }


    /**
     * 添加或修改群组
     */
    public Long addOrUpdateGroup(IMGroup group) {
        if (group == null) {
            return FAIL_ID;
        }

        IMGroup oldIMUser = getGroup(group.getId());

        if (oldIMUser != null) {
            group.setId(oldIMUser.getId());
        }

        String myId = IMEngine.getInstance(mContext).getMyId();
        group.setCurrUserId(myId);
        return mIMGroupDao.insertOrReplace(group);

    }


    /**
     * 获取指定群组 or 讨论组
     *
     * @param gId 群组 or 讨论组ID
     */
    public IMGroup getGroup(String gId) {
        String myId = IMEngine.getInstance(mContext).getMyId();
        Query<IMGroup> query = mIMGroupDao.queryBuilder()
                .where(IMGroupDao.Properties.Id.eq(gId)
                        , IMGroupDao.Properties.CurrUserId.eq(myId))
                .orderDesc(IMGroupDao.Properties.Id).build();
        return query.unique();
    }

    /**
     * 查询会话中的群聊天id
     *
     * @param myId
     * @param groupType
     * @return
     */
    public List<String> queryOrderDescIMGroupsId(String myId, int groupType) {
        List<IMChat> list = mChatDao.queryBuilder()
                .where(IMChatDao.Properties.Type.eq(groupType),
                        IMChatDao.Properties.UId.eq(myId))
                .orderDesc(IMChatDao.Properties.Time)
                .build().list();
        List<String> orderDesc = new ArrayList<>();
        if (list != null) {
            for (IMChat imChat : list) {
                orderDesc.add(imChat.getSenderOrTarget2());
            }
        }
        return orderDesc;
    }


    /**
     * 获取所有群组 or 讨论组
     *
     * @param groupType 组类型
     */
    public List<IMGroup> queryIMGroups(String myId, int groupType) {


        Query<IMGroup> query = mIMGroupDao.queryBuilder()
                .where(IMGroupDao.Properties.Type.eq(groupType)
                        , IMGroupDao.Properties.CurrUserId.eq(myId))
                .orderDesc(IMGroupDao.Properties.Name)
                .build();


        return query.list();
    }

    /**
     * 插入讨论组&群组数据
     */
    public void insertIMGroups(List<IMGroup> groupList) {
        String myId = IMEngine.getInstance(mContext).getMyId();
        for (IMGroup group : groupList) {
            group.setCurrUserId(myId);
        }
        mIMGroupDao.insertOrReplaceInTx(groupList);
    }

    public void insertIMGroup(IMGroup group) {
        String myId = IMEngine.getInstance(mContext).getMyId();
        group.setCurrUserId(myId);
        mIMGroupDao.insertOrReplace(group);
    }

    public void insertIMGroupUser(IMGroupUser groupUser) {
        mIMGroupUserDao.insertOrReplace(groupUser);
    }

    public IMGroupUser loadIMGroupUser(String gid, String uid) {
        QueryBuilder<IMGroupUser> builder;
        builder = mIMGroupUserDao.queryBuilder();

        List<IMGroupUser> list = builder.where(IMGroupUserDao.Properties.GId.eq(gid),
                IMGroupUserDao.Properties.UserId.eq(uid)).list();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }


    public boolean saveIMGroup(IMGroup group) {
        String myId = IMEngine.getInstance(mContext).getMyId();
        group.setCurrUserId(myId);
        return mIMGroupDao.insertOrReplace(group) != FAIL_ID;
    }

    public void deleteIMGroupDao(String gId) {
        mIMGroupDao.deleteByKey(gId);
    }

    public void deleteAllIMGroups() {
        mIMGroupDao.deleteAll();
    }

    private boolean isSingleChatMessage(IMMessage msg) {
        return msg.getType() == IMMessage.TYPE_CHAT;
    }

    /**
     * 删除会话
     */
    public void deleteIMChat(long id) {
        mChatDao.deleteByKey(id);
    }

    public void deleteAll(long id) {
        mChatDao.deleteByKey(id);
    }

    /**
     * 删除盒子小助手消息
     * @param id
     */
    public void deleteBoxMessage(long id) {
        mImBoxMessageDao.deleteByKey(id);
    }

    public void deleteOfficialMessage(long id) {
        mImOfficialMessageDao.deleteByKey(id);
    }

    /**
     * 根据发送者和接受者删除会话
     */
    public void deleteIMChat(String sendId, String targetId) {
        QueryBuilder<IMChat> chatQueryBuilder = mChatDao.queryBuilder();

        WhereCondition whereCondition1 = chatQueryBuilder.and(IMChatDao.Properties.SenderOrTarget1.eq(sendId), IMChatDao.Properties.SenderOrTarget2.eq(targetId));
        WhereCondition whereCondition2 = chatQueryBuilder.and(IMChatDao.Properties.SenderOrTarget2.eq(sendId), IMChatDao.Properties.SenderOrTarget1.eq(targetId));
        List<IMChat> IMChats = mChatDao.queryBuilder().whereOr(whereCondition1, whereCondition2).list();
        mChatDao.deleteInTx(IMChats);
    }


    /**
     * 根据发送者和接受者删除会话
     */
    public void deleteIMChatByGroup(String muid, String groupid) {
        QueryBuilder<IMChat> chatQueryBuilder = mChatDao.queryBuilder();

        WhereCondition whereCondition = chatQueryBuilder.and(IMChatDao.Properties.UId.eq(muid), IMChatDao.Properties.SenderOrTarget2.eq(groupid));
        List<IMChat> IMChats = mChatDao.queryBuilder().where(whereCondition).list();
        mChatDao.deleteInTx(IMChats);
    }


    public IMChat getChat(long id) {
        return mChatDao.load(id);
    }

    public IMFileInfo getFileInfo(long id) {
        return mFileInfoDao.load(id);
    }

    public long insertIMFileInfo(IMFileInfo imFileInfo) {
        return mFileInfoDao.insert(imFileInfo);
    }

    public long insertIMLocationInfo(IMLocationInfo imLocationInfo) {
        return mLocationInfoDao.insert(imLocationInfo);
    }

    public void deleteMessage(IMMessage imMessage) {
        mMessageDao.delete(imMessage);
    }

    public void deleteFileinfo(Long key) {
        mFileInfoDao.deleteByKey(key);
    }

    public void deleteFileinfoPice(List<IMFileInfoPice> list) {
        if (list == null || list.isEmpty())
            return;
        for (int i = 0; i < list.size(); i++) {
            mImFileInfoPiceDao.delete(list.get(i));
        }

    }

    public IMFileInfo loadFileInfo(Long key) {
        return mFileInfoDao.load(key);
    }

    public List<IMFileInfo> loadAllFileInfo() {
        return mFileInfoDao.loadAll();
    }

    public List<IMMessage> queryRawIMMessage(String where, String... selectionArg) {
        return mMessageDao.queryRaw(where, selectionArg);
    }

    public long insertIMCallInfo(IMCallInfo imCallInfo) {
        return mCallInfoDao.insert(imCallInfo);
    }

    public long insertOrReplaceIMCallInfo(IMCallInfo imCallInfo) {
        return mCallInfoDao.insertOrReplace(imCallInfo);
    }


    public IMLocationInfo loadIMLocationInfo(long id) {
        return mLocationInfoDao.load(id);
    }

    public void deleteIMLocationInfo(IMLocationInfo info) {
        mLocationInfoDao.delete(info);
    }

    public long insertOrReplaceIMLocationInfo(IMLocationInfo imLocationInfo) {

        return mLocationInfoDao.insertOrReplace(imLocationInfo);
    }

    public IMCallInfo loadIMCallInfo(Long key) {

        return mCallInfoDao.load(key);
    }


    public IMFileInfo getIMFileInfo(long id) {
        return mFileInfoDao.load(id);
    }

    /**
     * 获取会话中的图片的消息
     */
    public List<IMMessage> loadImgMessage(long chat_id) {

        QueryBuilder<IMMessage> builder;
        builder = mMessageDao.queryBuilder();

        builder.where(IMMessageDao.Properties.CId.eq(chat_id), IMMessageDao.Properties.ContentType.eq(IMMessage.CONTENT_TYPE_IMG))
                .orderDesc(IMMessageDao.Properties.Time);

        return builder.build().list();
    }

    /**
     * 获取最后修改的一条消息
     */
    public List<IMMessage> loadLastMessage(long chat_id) {

        QueryBuilder<IMMessage> builder;
        builder = mMessageDao.queryBuilder();

        builder.where(IMMessageDao.Properties.CId.eq(chat_id))
                .orderDesc(IMMessageDao.Properties.Time)
                .limit(1);
        return builder.build().list();

    }

    /**
     * 加载最后一条盒子消息
     * @param chat_id
     * @return
     */
    public List<IMBoxMessage> loadLastBoxMessage(long chat_id) {

        QueryBuilder<IMBoxMessage> builder;
        builder = mImBoxMessageDao.queryBuilder();

        builder.where(IMBoxMessageDao.Properties.CId.eq(chat_id))
                .orderDesc(IMBoxMessageDao.Properties.SendTimer)
                .limit(1);
        return builder.build().list();

    }

    /**
     * 加载最后一条官方消息
     * @param chat_id
     * @return
     */
    public List<IMOfficialMessage> loadLastOfficialMessage(long chat_id) {

        QueryBuilder<IMOfficialMessage> builder;
        builder = mImOfficialMessageDao.queryBuilder();

        builder.where(IMOfficialMessageDao.Properties.CId.eq(chat_id))
                .orderDesc(IMOfficialMessageDao.Properties.SendTimer)
                .limit(1);
        return builder.build().list();
    }

    /**
     * 获取最后修改的一条消息
     */
    public List<IMSysMessage> loadLastSysMessage(long chat_id) {

        QueryBuilder<IMSysMessage> builder;
        builder = mSysMessageDao.queryBuilder();

        builder.where(IMSysMessageDao.Properties.CId.eq(chat_id), IMSysMessageDao.Properties.IsClear.eq(false))
                .orderDesc(IMSysMessageDao.Properties.ReceivedTimer)
                .limit(1);
        return builder.build().list();

    }

    public long insertIMReplyInfo(IMReplyInfo replyInfo) {
        return mImReplyInfoDao.insert(replyInfo);
    }

    public long insertIMChatRecordInfo(IMChatRecordInfo recordInfo) {
        return mIMChatRecordInfoDao.insert(recordInfo);
    }

    public long insertOrReplaceIMGroupRemark(IMGroupRemark imGroupRemark) {
        return mGroupRemarkDao.insertOrReplace(imGroupRemark);
    }

    public List<IMGroupRemark> getUnReadGroupRemark(String myId, String groupId) {

        QueryBuilder<IMGroupRemark> builder = mGroupRemarkDao.queryBuilder();

        List<IMGroupRemark> list = builder.where(IMGroupRemarkDao.Properties.UId.eq(myId),
                IMGroupRemarkDao.Properties.GroupId.eq(groupId),
                IMGroupRemarkDao.Properties.Read.eq(false)
        )
                .orderDesc(IMGroupRemarkDao.Properties.SendTime)
                .list();
        return list;

    }

    public void updateGroupRemark(IMGroupRemark imGroupRemark) {

        mGroupRemarkDao.update(imGroupRemark);

    }

    /**
     * 从未读的消息中查出是否有@q
     *
     * @param chatId 对象ID
     * @param count  未读消息数
     * @return 是否有@我
     */
    public boolean hasAtMe(long chatId, int count) {
        if (count > 100) {
            count = 100;
        }
        List<IMMessage> msgs = mMessageDao.queryBuilder().where(IMMessageDao.Properties.CId.eq(chatId)).orderDesc(IMMessageDao.Properties.Time).limit(count).list();
        for (IMMessage msg : msgs) {
            if (msg.getIsAiteMe()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理并保存官方消息
     * @param msgEntity
     * @return
     */
    public boolean processOfficialMessage(GetMsgsEntity msgEntity) {
        IMOfficialMessage imOfficialMessage = new IMOfficialMessage();
        imOfficialMessage.setUserId(msgEntity.getMsgContent().getUserId());
        imOfficialMessage.setSendTimer(Long.parseLong(msgEntity.getParam().getSendTime()));
        imOfficialMessage.setType(msgEntity.getMsgContent().getType());
        imOfficialMessage.setTitle(msgEntity.getMsgContent().getGroupName());
        imOfficialMessage.setContent(msgEntity.getMsgContent().getContent());
        imOfficialMessage.setImgUrl(msgEntity.getMsgContent().getImgUrl());
        imOfficialMessage.setNetUrl(msgEntity.getMsgContent().getNetUrl());
        if (mDbManager.addOrUpdateOfficialMsgToChat(imOfficialMessage)){
            EventBus.getDefault().post(new NewSysMsgEvent());
        }
        return true;
    }

    /**
     * 处理并保存盒子助手消息
     * @param msgEntity
     * @return
     */
    public boolean processBoxMessage(GetMsgsEntity msgEntity) {
        IMBoxMessage imBoxMessage = new IMBoxMessage();
        imBoxMessage.setUserId(msgEntity.getMsgContent().getUserId());
        imBoxMessage.setSendTimer(Long.parseLong(msgEntity.getParam().getSendTime()));
        imBoxMessage.setType(msgEntity.getMsgContent().getType());
        imBoxMessage.setTitle(msgEntity.getMsgContent().getGroupName());
        imBoxMessage.setContent(msgEntity.getMsgContent().getContent());

        if (mDbManager.addOrUpdateBoxMsgToChat(imBoxMessage)){
            EventBus.getDefault().post(new NewSysMsgEvent());
        }
        return true;
    }

}
