package com.markLove.xplan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.markLove.xplan.bean.ChatUser;
import com.markLove.xplan.bean.FriendsBean;
import com.markLove.xplan.bean.GiftItem;
import com.markLove.xplan.bean.MessagePushBean;
import com.markLove.xplan.bean.msg.Message;
import com.markLove.xplan.bean.msg.body.FileMessageBody;
import com.markLove.xplan.bean.msg.body.GiftMessageBody;
import com.markLove.xplan.bean.msg.body.LoveMessageBody;
import com.markLove.xplan.bean.msg.body.MessageBody;
import com.markLove.xplan.bean.msg.body.TxtMessageBody;
import com.markLove.xplan.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoyunmin on 2017/7/25.
 */

public class DBDao {
    //版本从1开始，往后每次加1，方便数据库升级
    private static final int DB_VERSION = 10;//数据库版本
    //消息表及其字段
    private static final String CHAT_MESSAGE_TABLE = "chat_message";
    private static final String FROM_ID_COLUMN = "from_id";
    private static final String TO_ID_COLUMN = "to_id";
    private static final String MSG_ID_COLUMN = "msg_id";
    private static final String TYPE_CLOUMN = "type";
    private static final String MSG_TIME_COLUMN = "msg_time";
    private static final String CHAT_TYPE_COLUMN = "chat_type";
    private static final String BODY_COLUMN = "body";
    private static final String STATUS_COLUMN = "status";
    //聊天用户表及其字段
    private static final String CHAT_USER_TABLE = "chat_user";
    private static final String NICK_NAME_COLUMN = "nick_name";
    private static final String USER_ID_COLUMN = "user_id";
    private static final String HEAD_IMG_URL_COLUMN = "head_img_url";
    private static final String LAST_TIME_COLUMN = "last_time";
    private static final String LAST_MSG_ID_COLUMN = "last_msg_id";
    private static final String LAST_MSG_CONTENT_COLUMN = "last_msg_content";
    private static final String LAST_MSG_CHAT_TYPE_COLUMN = "last_msg_chat_type";
    private static final String UNREAD_MSG_COUNT_COLUMN = "unread_msg_count";
    //消息推送内容表
    private static final String MESSAGE_PUSH_TABLE = "message_push_table";
    private static final String MESSAGE_TITLE = "message_title";
    private static final String MESSAGE_CONTENT = "message_content";
    private static final String MESSAGE_ID = "last_msg_id";
    private static final String MESSAGE_GOWAY = "message_go_way";
    private static final String MESSAGE_LINKURL = "message_link_url";
    private static final String MESSAGE_IMAGE_PATH = "message_image_path";
    private static final String MESSAGE_TIME_COLUMN = "message_time_column";
    //礼物表
    private static final String GIFT_TABLE = "gift";
    private static final String GIFT_ID_COLUMN = "gift_id";
    private static final String GIFT_NAME_COLUMN = "gift_name";
    private static final String GIFT_PRICE_COLUMN = "gift_price";
    private static final String GIFT_PICTURE_COLUMN = "gift_picture";
    private static final String GIFT_CREATE_TIME_COLUMN = "gift_create_time";
    private static final String GIFT_GOLD_COLUMN = "gift_gold";
    private static final String GIFT_COUNT_COLUMN = "gift_count";
    //黑名单表
    private static final String BLACK_LIST_TABLE = "black_list";
    private static final String BLACK_USER_ID_COLUMN = "black_user_id";
    private static final String BLACK_USER_NAME_COLUMN = "black_user_name";
    private static final String BLACK_USER_HEAD_COLUMN = "black_user_head";
    private SQLiteDatabase mDatabase;
    private Context mContext;
    private ChatDBOpenHelper mDbOpenHelper;//数据库打开帮助类

    private static DBDao dbDao;

    private DBDao(Context context) {
        this.mContext = context;
    }

    public static DBDao getDbDao(Context context) {
        if (null == dbDao) {
            dbDao = new DBDao(context.getApplicationContext());
        }
        return dbDao;
    }

    public void logout() {
        mDbOpenHelper = null;
        mDatabase = null;
    }


    //打开数据库
    private synchronized void openDataBase(int me_user_id) {
        if (mDbOpenHelper == null) {
            mDbOpenHelper = new ChatDBOpenHelper(mContext, (me_user_id + ".db"));
        }
        try {
            mDatabase = mDbOpenHelper.getWritableDatabase();//获取可写数据库
        } catch (SQLException e) {
            mDatabase = mDbOpenHelper.getReadableDatabase();//获取只读数据库
        }
    }

    //关闭数据库
    private synchronized void closeDataBase() {
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    //插入一条聊天用户表
    public synchronized long insertChatUser(int me_user_id, ChatUser chatUser) {
        openDataBase(me_user_id);
        ContentValues values = new ContentValues();
        values.put(NICK_NAME_COLUMN, chatUser.getNickName());
        values.put(HEAD_IMG_URL_COLUMN, chatUser.getHeadImgUrl());
        values.put(USER_ID_COLUMN, chatUser.getUserID());
        values.put(LAST_MSG_ID_COLUMN, chatUser.getLastMsgId());
        values.put(LAST_MSG_CONTENT_COLUMN, chatUser.getLastMsgContent());
        values.put(LAST_TIME_COLUMN, chatUser.getChatTime());
        values.put(LAST_MSG_CHAT_TYPE_COLUMN, chatUser.getLastMsgChatType().ordinal());
        values.put(UNREAD_MSG_COUNT_COLUMN, chatUser.getUnreadCount());
        long result = mDatabase.insertWithOnConflict(CHAT_USER_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        closeDataBase();
        return result;
    }


    //插入一条消息数据
    public synchronized long insertMessage(int me_user_id, Message message) {
        openDataBase(me_user_id);
        //判断是否创建表
        ContentValues values = new ContentValues();
        values.put(FROM_ID_COLUMN, message.getFromID());
        values.put(TO_ID_COLUMN, message.getToID());
        values.put(MSG_ID_COLUMN, message.getMsgID());
        values.put(TYPE_CLOUMN, message.getType().flag);
        values.put(CHAT_TYPE_COLUMN, message.getChatType().flag);
        values.put(MSG_TIME_COLUMN, message.getMsgTime());
        values.put(BODY_COLUMN, message.getBody().toString());
        values.put(STATUS_COLUMN, message.getStatus().ordinal());
        long result = mDatabase.insertWithOnConflict(CHAT_MESSAGE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        closeDataBase();
        return result;
    }

    //插入一条官方推送
    public synchronized long insertMessagePush(int me_user_id, MessagePushBean messagePushBean) {
        openDataBase(me_user_id);
        ContentValues values = new ContentValues();
        values.put(MESSAGE_TITLE, messagePushBean.getTitle());
        values.put(MESSAGE_CONTENT, messagePushBean.getMsg());
        values.put(MESSAGE_GOWAY, messagePushBean.getGoWay());
        values.put(MESSAGE_ID, messagePushBean.getId());
        values.put(MESSAGE_TIME_COLUMN, messagePushBean.getMessageTime());
        values.put(MESSAGE_LINKURL, messagePushBean.getLinkUrl());
        values.put(MESSAGE_IMAGE_PATH, messagePushBean.getImagePath());
        long result = mDatabase.insertWithOnConflict(MESSAGE_PUSH_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        closeDataBase();
        return result;
    }

    public synchronized long delectMessage(int me_user_id, String msgID) {
        openDataBase(me_user_id);
        long result = mDatabase.delete(CHAT_MESSAGE_TABLE, MSG_ID_COLUMN + "=?", new String[]{msgID});
        closeDataBase();
        return result;
    }

    public synchronized long delectMessageAll(int me_user_id, int from_id,int to_id) {
        openDataBase(me_user_id);
//        long result = mDatabase.delete(CHAT_MESSAGE_TABLE, TO_ID_COLUMN+"=?", new String[]{(to_id + "")});
        long result = mDatabase.delete(CHAT_MESSAGE_TABLE, TO_ID_COLUMN+"=? or "+FROM_ID_COLUMN+"=?", new String[]{(from_id + ""),(from_id + "")});
        closeDataBase();
        return result;
    }

    public synchronized void delectChatUser(int me_user_id, int user_id) {
        openDataBase(me_user_id);
        int result = mDatabase.delete(CHAT_USER_TABLE, USER_ID_COLUMN + "=?", new String[]{(user_id + "")});
        closeDataBase();
    }

    public synchronized int delectMessagePush(int me_user_id, String msgId) {
        openDataBase(me_user_id);
        int result = mDatabase.delete(MESSAGE_PUSH_TABLE, MESSAGE_ID+"=?", new String[]{msgId});
        closeDataBase();
        return result;
    }

    public synchronized void delectMessagePushAll(int me_user_id, String msgId) {
        openDataBase(me_user_id);
        int result = mDatabase.delete(MESSAGE_PUSH_TABLE, null, null);
        closeDataBase();
    }

    //更新一条数据
    public synchronized void updateDataMessage(int me_user_id, String msgID, Message.ChatStatus status) {
        openDataBase(me_user_id);
        ContentValues values = new ContentValues();
        values.put(STATUS_COLUMN, status.ordinal());
        mDatabase.updateWithOnConflict(CHAT_MESSAGE_TABLE, values, MSG_ID_COLUMN + "=?", new String[]{msgID}, SQLiteDatabase.CONFLICT_REPLACE);
        closeDataBase();
    }

    public synchronized void updateMessageTime(int me_user_id, String msgID, long time) {
        openDataBase(me_user_id);
        ContentValues values = new ContentValues();
        values.put(MSG_TIME_COLUMN, time);
        mDatabase.updateWithOnConflict(CHAT_MESSAGE_TABLE, values, MSG_ID_COLUMN + "=?", new String[]{msgID}, SQLiteDatabase.CONFLICT_REPLACE);
        closeDataBase();
    }

    //查询一条数据
    public synchronized List<Message> queryChatMessage(int me_user_id, int to_user_id) {
        openDataBase(me_user_id);
        Cursor results = mDatabase.query(CHAT_MESSAGE_TABLE,
                new String[]{FROM_ID_COLUMN, TO_ID_COLUMN, MSG_ID_COLUMN, TYPE_CLOUMN, CHAT_TYPE_COLUMN, BODY_COLUMN, MSG_TIME_COLUMN, STATUS_COLUMN},
                FROM_ID_COLUMN + "=? or " + TO_ID_COLUMN + "=?", new String[]{to_user_id + "", to_user_id + ""}, null, null, null);
        return parseMessage(results);
    }

    public synchronized Message queryLastMessage(int me_user_id, int to_user_id, String msgid) {
        openDataBase(me_user_id);
        Cursor results = mDatabase.query(CHAT_MESSAGE_TABLE,
                new String[]{FROM_ID_COLUMN, TO_ID_COLUMN, MSG_ID_COLUMN, TYPE_CLOUMN, CHAT_TYPE_COLUMN, BODY_COLUMN, MSG_TIME_COLUMN, STATUS_COLUMN},
                FROM_ID_COLUMN + "=? or " + TO_ID_COLUMN + "=?", new String[]{to_user_id + "", to_user_id + ""}, null, null, null);
        return parseLastMessage(results);
    }

    public synchronized Message queryMessage(int me_user_id, String msgID) {
        openDataBase(me_user_id);
        Cursor results = mDatabase.query(CHAT_MESSAGE_TABLE,
                new String[]{FROM_ID_COLUMN, TO_ID_COLUMN, MSG_ID_COLUMN, TYPE_CLOUMN, CHAT_TYPE_COLUMN, BODY_COLUMN, MSG_TIME_COLUMN, STATUS_COLUMN},
                MSG_ID_COLUMN + "=?", new String[]{msgID}, null, null, null);
        return parseLastMessage(results);
    }

    private Message parseLastMessage(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        Message message = new Message();
        if (cursor.moveToFirst()) {
            do {
                message.setFromID(cursor.getInt(cursor.getColumnIndex(FROM_ID_COLUMN)));
                message.setToID(cursor.getInt(cursor.getColumnIndex(TO_ID_COLUMN)));
                message.setMsgID(cursor.getString(cursor.getColumnIndex(MSG_ID_COLUMN)));
                message.setType(Message.Type.values()[cursor.getInt(cursor.getColumnIndex(TYPE_CLOUMN))]);
                int chatType = cursor.getInt(cursor.getColumnIndex(CHAT_TYPE_COLUMN));
                message.setChatType(Message.ChatType.values()[chatType]);
                message.setMsgTime(cursor.getLong(cursor.getColumnIndex(MSG_TIME_COLUMN)));
                int status = cursor.getInt(cursor.getColumnIndex(STATUS_COLUMN));
                message.setStatus(Message.ChatStatus.values()[status]);
                String body = cursor.getString(cursor.getColumnIndex(BODY_COLUMN));
                MessageBody messageBody = null;
                if (Message.ChatType.values()[chatType] == Message.ChatType.TXT) {
                    messageBody = GsonUtils.json2Bean(body, TxtMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.IMAGE) {
                    messageBody = GsonUtils.json2Bean(body, FileMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.LOVE) {
                    messageBody = GsonUtils.json2Bean(body, LoveMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.SUPERLIKE) {
                    messageBody = GsonUtils.json2Bean(body, TxtMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.VOICE) {
                    messageBody = GsonUtils.json2Bean(body, FileMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.GIFT) {
                    messageBody = GsonUtils.json2Bean(body, GiftMessageBody.class);
                }
                message.setBody(messageBody);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        closeDataBase();
        return message;
    }

    private List<Message> parseMessage(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        List<Message> mMessageList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setFromID(cursor.getInt(cursor.getColumnIndex(FROM_ID_COLUMN)));
                message.setToID(cursor.getInt(cursor.getColumnIndex(TO_ID_COLUMN)));
                message.setMsgID(cursor.getString(cursor.getColumnIndex(MSG_ID_COLUMN)));
                message.setType(Message.Type.values()[cursor.getInt(cursor.getColumnIndex(TYPE_CLOUMN))]);
                int chatType = cursor.getInt(cursor.getColumnIndex(CHAT_TYPE_COLUMN));
                message.setChatType(Message.ChatType.values()[chatType]);
                message.setMsgTime(cursor.getLong(cursor.getColumnIndex(MSG_TIME_COLUMN)));
                int status = cursor.getInt(cursor.getColumnIndex(STATUS_COLUMN));
                message.setStatus(Message.ChatStatus.values()[status]);
                String body = cursor.getString(cursor.getColumnIndex(BODY_COLUMN));
                MessageBody messageBody = null;
                if (Message.ChatType.values()[chatType] == Message.ChatType.TXT) {
                    messageBody = GsonUtils.json2Bean(body, TxtMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.IMAGE) {
                    messageBody = GsonUtils.json2Bean(body, FileMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.LOVE) {
                    messageBody = GsonUtils.json2Bean(body, LoveMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.SUPERLIKE) {
                    messageBody = GsonUtils.json2Bean(body, TxtMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.VOICE) {
                    messageBody = GsonUtils.json2Bean(body, FileMessageBody.class);
                } else if (Message.ChatType.values()[chatType] == Message.ChatType.GIFT) {
                    messageBody = GsonUtils.json2Bean(body, GiftMessageBody.class);
                }
                message.setBody(messageBody);
                mMessageList.add(message);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        closeDataBase();
        return mMessageList;
    }

    public synchronized List<ChatUser> queryChatUser(int me_user_id) {
        openDataBase(me_user_id);
        Cursor results = mDatabase.query(CHAT_USER_TABLE, new String[]{USER_ID_COLUMN, NICK_NAME_COLUMN,
                        HEAD_IMG_URL_COLUMN, LAST_MSG_ID_COLUMN,
                        LAST_TIME_COLUMN, LAST_MSG_CONTENT_COLUMN,
                        LAST_MSG_CHAT_TYPE_COLUMN, UNREAD_MSG_COUNT_COLUMN},
                null, null, null, null, null);
        return parseChatUser(results);
    }

    public synchronized List<MessagePushBean> queryMessagePush(int me_user_id) {
        openDataBase(me_user_id);
        Cursor results = mDatabase.query(MESSAGE_PUSH_TABLE, new String[]{MESSAGE_CONTENT, MESSAGE_GOWAY,
                        MESSAGE_TIME_COLUMN, MESSAGE_TITLE,
                        MESSAGE_LINKURL, MESSAGE_IMAGE_PATH, MESSAGE_ID,
                        },
                null, null, null, null, null);
        return parseMessagePush(results);
    }

    private List<ChatUser> parseChatUser(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        List<ChatUser> mChatUserList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ChatUser chatUser = new ChatUser();
                chatUser.setUserID(cursor.getInt(cursor.getColumnIndex(USER_ID_COLUMN)));
                chatUser.setNickName(cursor.getString(cursor.getColumnIndex(NICK_NAME_COLUMN)));
                chatUser.setHeadImgUrl(cursor.getString(cursor.getColumnIndex(HEAD_IMG_URL_COLUMN)));
                chatUser.setChatTime(cursor.getString(cursor.getColumnIndex(LAST_TIME_COLUMN)));
                chatUser.setLastMsgContent(cursor.getString(cursor.getColumnIndex(LAST_MSG_CONTENT_COLUMN)));
                chatUser.setLastMsgId(cursor.getString(cursor.getColumnIndex(LAST_MSG_ID_COLUMN)));
                chatUser.setLastMsgChatType(Message.ChatType.values()[cursor.getInt(cursor.getColumnIndex(LAST_MSG_CHAT_TYPE_COLUMN))]);
                chatUser.setUnreadCount(cursor.getInt(cursor.getColumnIndex(UNREAD_MSG_COUNT_COLUMN)));
                mChatUserList.add(chatUser);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        closeDataBase();
        return mChatUserList;
    }

    //TODO:做过修改
    private List<MessagePushBean> parseMessagePush(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        List<MessagePushBean> messagePushBeanList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                MessagePushBean messagePushBean = new MessagePushBean();
                messagePushBean.setMsg(cursor.getString(cursor.getColumnIndex(MESSAGE_CONTENT)));
                messagePushBean.setGoWay(cursor.getInt(cursor.getColumnIndex(MESSAGE_GOWAY)));
                messagePushBean.setMessageTime(cursor.getString(cursor.getColumnIndex(MESSAGE_TIME_COLUMN)));
                messagePushBean.setTitle(cursor.getString(cursor.getColumnIndex(MESSAGE_TITLE)));
                messagePushBean.setLinkUrl(cursor.getString(cursor.getColumnIndex(MESSAGE_LINKURL)));
                messagePushBean.setImagePath(cursor.getString(cursor.getColumnIndex(MESSAGE_IMAGE_PATH)));
                messagePushBean.setId(cursor.getString(cursor.getColumnIndex(MESSAGE_ID)));
                messagePushBeanList.add(messagePushBean);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        closeDataBase();
        return messagePushBeanList;
    }

    public synchronized int queryUnreadMsgCount(int me_user_id) {
        openDataBase(me_user_id);
        if (mDatabase != null) {
            Cursor results = mDatabase.query(CHAT_USER_TABLE, new String[]{UNREAD_MSG_COUNT_COLUMN},
                    null, null, null, null, null);
            return parseUnreadMsgCount(results);
        }
        return 0;
    }

    private int parseUnreadMsgCount(Cursor cursor) {
        if (cursor == null) {
            return 0;
        }
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                count += cursor.getInt(cursor.getColumnIndex(UNREAD_MSG_COUNT_COLUMN));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        closeDataBase();
        return count;
    }

    public synchronized void insertGiftList(int me_user_id, List<GiftItem.DataBean> data) {
        openDataBase(me_user_id);
        try {
            for (GiftItem.DataBean dataBean : data) {
                Cursor cursor = mDatabase.query(GIFT_TABLE, new String[]{GIFT_COUNT_COLUMN}, GIFT_ID_COLUMN + "=?", new String[]{(dataBean.getId() + "")}, null, null, null, null);
                int count = 0;
                if (cursor.moveToFirst()) {
                    do {
                        count = cursor.getInt(cursor.getColumnIndex(GIFT_COUNT_COLUMN));
                    } while (cursor.moveToNext());
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(GIFT_ID_COLUMN, dataBean.getId());
                contentValues.put(GIFT_NAME_COLUMN, dataBean.getGiftName());
                contentValues.put(GIFT_PICTURE_COLUMN, dataBean.getGiftPicture());
                contentValues.put(GIFT_PRICE_COLUMN, dataBean.getGiftPrice());
                contentValues.put(GIFT_CREATE_TIME_COLUMN, dataBean.getCreateTime());
                contentValues.put(GIFT_GOLD_COLUMN, dataBean.getGiftGold());
                contentValues.put(GIFT_COUNT_COLUMN, count);
                mDatabase.insertWithOnConflict(GIFT_TABLE, GIFT_COUNT_COLUMN, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDataBase();
        }

    }

    public synchronized GiftItem.DataBean queryGift(int me_user_id, int giftID) {
        openDataBase(me_user_id);
        Cursor cursor = mDatabase.query(GIFT_TABLE,
                new String[]{GIFT_ID_COLUMN, GIFT_NAME_COLUMN, GIFT_PICTURE_COLUMN, GIFT_PRICE_COLUMN, GIFT_CREATE_TIME_COLUMN, GIFT_GOLD_COLUMN},
                GIFT_ID_COLUMN + "=?", new String[]{(giftID + "")}, null, null, null, null);
        return parseGift(cursor);
    }

    private GiftItem.DataBean parseGift(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        GiftItem.DataBean gift = new GiftItem.DataBean();
        if (cursor.moveToFirst()) {
            do {
                gift.setId(cursor.getInt(cursor.getColumnIndex(GIFT_ID_COLUMN)));
                gift.setGiftName(cursor.getString(cursor.getColumnIndex(GIFT_NAME_COLUMN)));
                gift.setGiftPicture(cursor.getString(cursor.getColumnIndex(GIFT_PICTURE_COLUMN)));
                gift.setGiftPrice(cursor.getDouble(cursor.getColumnIndex(GIFT_PRICE_COLUMN)));
                gift.setCreateTime(cursor.getString(cursor.getColumnIndex(GIFT_CREATE_TIME_COLUMN)));
                gift.setGiftGold(cursor.getDouble(cursor.getColumnIndex(GIFT_GOLD_COLUMN)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        closeDataBase();
        return gift;
    }

    private List<GiftItem.DataBean> parseAllGift(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        List<GiftItem.DataBean> gifts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                GiftItem.DataBean gift = new GiftItem.DataBean();
                gift.setId(cursor.getInt(cursor.getColumnIndex(GIFT_ID_COLUMN)));
                gift.setGiftName(cursor.getString(cursor.getColumnIndex(GIFT_NAME_COLUMN)));
                gift.setGiftPicture(cursor.getString(cursor.getColumnIndex(GIFT_PICTURE_COLUMN)));
                gift.setGiftPrice(cursor.getDouble(cursor.getColumnIndex(GIFT_PRICE_COLUMN)));
                gift.setCreateTime(cursor.getString(cursor.getColumnIndex(GIFT_CREATE_TIME_COLUMN)));
                gift.setGiftGold(cursor.getDouble(cursor.getColumnIndex(GIFT_GOLD_COLUMN)));
                gifts.add(gift);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        closeDataBase();
        return gifts;
    }

    public synchronized List<GiftItem.DataBean> queryAllGift(int me_user_id) {
        openDataBase(me_user_id);
        Cursor cursor = mDatabase.query(GIFT_TABLE,
                new String[]{GIFT_ID_COLUMN, GIFT_NAME_COLUMN, GIFT_PICTURE_COLUMN, GIFT_PRICE_COLUMN, GIFT_CREATE_TIME_COLUMN, GIFT_GOLD_COLUMN}, null, null, null, null, null, null);
        return parseAllGift(cursor);
    }

    public synchronized ChatUser queryOneChatUser(int me_user_id, int userid) {
        openDataBase(me_user_id);
        Cursor results = mDatabase.query(CHAT_USER_TABLE, new String[]{USER_ID_COLUMN, NICK_NAME_COLUMN,
                        HEAD_IMG_URL_COLUMN, LAST_MSG_ID_COLUMN,
                        LAST_TIME_COLUMN, LAST_MSG_CONTENT_COLUMN,
                        LAST_MSG_CHAT_TYPE_COLUMN, UNREAD_MSG_COUNT_COLUMN},
                null, null, null, null, null);
        return parseOneChatUser(results);
    }

    private ChatUser parseOneChatUser(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ChatUser chatUser = new ChatUser();
        if (cursor.moveToFirst()) {
            do {
                chatUser.setUserID(cursor.getInt(cursor.getColumnIndex(USER_ID_COLUMN)));
                chatUser.setNickName(cursor.getString(cursor.getColumnIndex(NICK_NAME_COLUMN)));
                chatUser.setHeadImgUrl(cursor.getString(cursor.getColumnIndex(HEAD_IMG_URL_COLUMN)));
                chatUser.setChatTime(cursor.getString(cursor.getColumnIndex(LAST_TIME_COLUMN)));
                chatUser.setLastMsgContent(cursor.getString(cursor.getColumnIndex(LAST_MSG_CONTENT_COLUMN)));
                chatUser.setLastMsgId(cursor.getString(cursor.getColumnIndex(LAST_MSG_ID_COLUMN)));
                chatUser.setLastMsgChatType(Message.ChatType.values()[cursor.getInt(cursor.getColumnIndex(LAST_MSG_CHAT_TYPE_COLUMN))]);
                chatUser.setUnreadCount(cursor.getInt(cursor.getColumnIndex(UNREAD_MSG_COUNT_COLUMN)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        closeDataBase();
        return chatUser;
    }

    public synchronized boolean queryBlackListToId(int me_user_id, int queryUserId) {
        openDataBase(me_user_id);
        try {
            Cursor cursor = mDatabase.query(BLACK_LIST_TABLE, new String[]{BLACK_USER_ID_COLUMN},
                    BLACK_USER_ID_COLUMN + "=?", new String[]{(queryUserId + "")}, null, null, null);
            if (null == cursor) {
                return false;
            }
            if (cursor.moveToFirst()) {
                do {
                    String userId = cursor.getString(cursor.getColumnIndex(BLACK_USER_ID_COLUMN));
                    if (userId.equals(queryUserId + "")) {
                        return true;
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            closeDataBase();
        }
        return false;
    }

    public synchronized List<FriendsBean.LikeInfoBean> getBlackList(int me_user_id) {
        openDataBase(me_user_id);
        List<FriendsBean.LikeInfoBean> list = new ArrayList<>();
        try {
            Cursor cursor = mDatabase.query(BLACK_LIST_TABLE, new String[]{BLACK_USER_ID_COLUMN, BLACK_USER_NAME_COLUMN, BLACK_USER_HEAD_COLUMN},
                    null, null, null, null, null);
            if (null == cursor) {
                return null;
            }
            if (cursor.moveToFirst()) {
                do {
                    FriendsBean.LikeInfoBean likeInfoBean = new FriendsBean.LikeInfoBean();
                    String userId = cursor.getString(cursor.getColumnIndex(BLACK_USER_ID_COLUMN));
                    String userName = cursor.getString(cursor.getColumnIndex(BLACK_USER_NAME_COLUMN));
                    String userHead = cursor.getString(cursor.getColumnIndex(BLACK_USER_HEAD_COLUMN));
                    likeInfoBean.setUserID(userId);
                    likeInfoBean.setNickName(userName);
                    likeInfoBean.setHeadImageUrl(userHead);
                    list.add(likeInfoBean);
                } while (cursor.moveToNext());
            }
        } finally {
            closeDataBase();
        }
        return list;
    }

    public synchronized void addBlackList(int me_user_id, List<FriendsBean.LikeInfoBean> blackUsers) {
        openDataBase(me_user_id);
        mDatabase.delete(BLACK_LIST_TABLE, null, null);
        closeDataBase();
        for (int i = 0; i < blackUsers.size(); i++) {
            addBlackListUser(me_user_id, blackUsers.get(i));
        }
    }

    public synchronized void addBlackListUser(int me_user_id, FriendsBean.LikeInfoBean blackUser) {
        openDataBase(me_user_id);
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BLACK_USER_ID_COLUMN, blackUser.getUserID());
            contentValues.put(BLACK_USER_NAME_COLUMN, blackUser.getNickName());
            contentValues.put(BLACK_USER_HEAD_COLUMN, blackUser.getHeadImageUrl());
            mDatabase.insertWithOnConflict(BLACK_LIST_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        } finally {
            closeDataBase();
        }
    }

    public synchronized void deleteBlackListToID(int me_user_id, String blackUserID) {
        openDataBase(me_user_id);
        try {
            mDatabase.delete(BLACK_LIST_TABLE, BLACK_USER_ID_COLUMN + "=?", new String[]{blackUserID});
        } finally {
            closeDataBase();
        }
    }

    public synchronized void insertGiftCountAdd(int me_user_id, int giftID) {
        openDataBase(me_user_id);
        try {
            Cursor cursor = mDatabase.query(GIFT_TABLE, new String[]{GIFT_COUNT_COLUMN}, GIFT_ID_COLUMN + "=?", new String[]{(giftID + "")}, null, null, null, null);
            int count = 0;
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex(GIFT_COUNT_COLUMN));
                } while (cursor.moveToNext());
            }
            ContentValues values = new ContentValues();
            values.put(GIFT_COUNT_COLUMN, ++count);
            mDatabase.update(GIFT_TABLE, values, GIFT_ID_COLUMN + "=?", new String[]{(giftID + "")});
        } finally {
            closeDataBase();
        }

    }

    public synchronized void insertGiftCountLower(int me_user_id, int giftID) {
        openDataBase(me_user_id);
        try {
            Cursor cursor = mDatabase.query(GIFT_TABLE, new String[]{GIFT_COUNT_COLUMN}, GIFT_ID_COLUMN + "=?", new String[]{(giftID + "")}, null, null, null, null);
            int count = 0;
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex(GIFT_COUNT_COLUMN));
                } while (cursor.moveToNext());
            }
            //查询到现在的消息发送条数
            ContentValues values = new ContentValues();
            values.put(GIFT_COUNT_COLUMN, --count);
            mDatabase.update(GIFT_TABLE, values, GIFT_ID_COLUMN + "=?", new String[]{(giftID + "")});
        } finally {
            closeDataBase();
        }
    }

    public synchronized int queryGiftCount(int me_user_id, int giftID) {
        openDataBase(me_user_id);
        try {
            Cursor cursor = mDatabase.query(GIFT_TABLE, new String[]{GIFT_COUNT_COLUMN}, GIFT_ID_COLUMN + "=?", new String[]{(giftID + "")}, null, null, null, null);
            int count = 0;
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndex(GIFT_COUNT_COLUMN));
                } while (cursor.moveToNext());
            }
            return count;
        } finally {
            closeDataBase();
        }
    }

    /**
     * 数据表打开帮助类
     */
    private static class ChatDBOpenHelper extends SQLiteOpenHelper {

        private ChatDBOpenHelper(Context context, String name) {
            super(context, name, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //创建一张聊天消息表
            String message = "CREATE TABLE IF NOT EXISTS " + CHAT_MESSAGE_TABLE
                    + " (id  INTEGER  PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
                    + FROM_ID_COLUMN + "    INT       NOT NULL,"
                    + TO_ID_COLUMN + "      INT       NOT NULL,"
                    + MSG_ID_COLUMN + "     CHAR(50)  NOT NULL UNIQUE,"
                    + TYPE_CLOUMN + "       INT       NOT NULL,"
                    + CHAT_TYPE_COLUMN + "  INT       NOT NULL,"
                    + BODY_COLUMN + "       TEXT      NOT NULL"
                    + ");";
            db.execSQL(message);

            //创建一张聊天用户表
            String user = "CREATE TABLE IF NOT EXISTS " + CHAT_USER_TABLE
                    + " (id  INTEGER  PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
                    + NICK_NAME_COLUMN + "     CHAR(50)   NOT NULL,"
                    + USER_ID_COLUMN + "       INT        NOT NULL UNIQUE,"
                    + HEAD_IMG_URL_COLUMN + "  CHAR(50)   NOT NULL,"
                    + LAST_MSG_ID_COLUMN + "   CHAR(50)   NOT NULL,"
                    + LAST_MSG_CONTENT_COLUMN + " TEXT       NOT NULL,"
                    + LAST_TIME_COLUMN + "     TimeStamp  NOT NULL DEFAULT CURRENT_TIMESTAMP"
                    + ");";
            db.execSQL(user);

            final int FIRST_DB_VERSION = 1;
            onUpgrade(db, FIRST_DB_VERSION, DB_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for (int i = oldVersion; i < newVersion; i++) {
                switch (i) {
                    case 1:
                        String createGiftTable = "CREATE TABLE IF NOT EXISTS " + GIFT_TABLE
                                + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                + "gift_id INTEGER NOT NULL UNIQUE,"
                                + "gift_name CHAR(50) NOT NULL,"
                                + "gift_picture CHAR(50) NOT NULL,"
                                + "gift_price CHAR(50) NOT NULL,"
                                + "gift_create_time CHAR(50) NOT NULL"
                                + ");";
                        db.execSQL(createGiftTable);
                        break;
                    case 2:
                        String alertChatUser = "ALTER TABLE " + CHAT_USER_TABLE + " ADD COLUMN " + LAST_MSG_CHAT_TYPE_COLUMN + " INT";
                        db.execSQL(alertChatUser);
                        break;
                    case 3:
                        String alertStatusColumn = "ALTER TABLE " + CHAT_MESSAGE_TABLE + " ADD COLUMN " + STATUS_COLUMN + " INT";
                        db.execSQL(alertStatusColumn);
                        break;
                    case 4:
                        String alertMsgTimeColumn = "ALTER TABLE " + CHAT_MESSAGE_TABLE + " ADD COLUMN " + MSG_TIME_COLUMN + " BIGINT";
                        db.execSQL(alertMsgTimeColumn);
                        break;
                    case 5:
                        String alertReadStatusColumn = "ALTER TABLE " + CHAT_USER_TABLE + " ADD COLUMN " + UNREAD_MSG_COUNT_COLUMN + " INT";
                        db.execSQL(alertReadStatusColumn);
                        break;
                    case 6:
                        String createBlackListTable = "CREATE TABLE IF NOT EXISTS " + BLACK_LIST_TABLE
                                + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                + BLACK_USER_ID_COLUMN + " CHAR(50) NOT NULL UNIQUE,"
                                + BLACK_USER_NAME_COLUMN + " CHAR(50) NOT NULL,"
                                + BLACK_USER_HEAD_COLUMN + " CHAR(50) NOT NULL"
                                + ");";
                        db.execSQL(createBlackListTable);
                        break;
                    case 7:
                        String alertGiftGoldColumn = "ALTER TABLE " + GIFT_TABLE + " ADD COLUMN " + GIFT_GOLD_COLUMN + " INT";
                        db.execSQL(alertGiftGoldColumn);
                        break;
                    case 8:
                        String alertGiftCountColumn = "ALTER TABLE " + GIFT_TABLE + " ADD COLUMN " + GIFT_COUNT_COLUMN + " INT";
                        db.execSQL(alertGiftCountColumn);
                        break;
                    case 9:
                        String messagePushTable = "CREATE TABLE IF NOT EXISTS " + MESSAGE_PUSH_TABLE
                                + " (id  INTEGER  PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
                                + MESSAGE_ID + "         CHAR(50)  NOT NULL UNIQUE,"
                                + MESSAGE_GOWAY + "      INT       NOT NULL,"
                                + MESSAGE_TITLE + "      TEXT      NOT NULL,"
                                + MESSAGE_CONTENT + "    TEXT      NOT NULL,"
                                + MESSAGE_LINKURL + "    TEXT      NOT NULL,"
                                + MESSAGE_IMAGE_PATH + " TEXT      NOT NULL,"
                                + MESSAGE_TIME_COLUMN + "     TimeStamp  NOT NULL DEFAULT CURRENT_TIMESTAMP"
                                + ");";
                        db.execSQL(messagePushTable);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
