package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.networkengine.PubConstant;
import com.networkengine.entity.CallInfo;
import com.networkengine.entity.FileInfo;
import com.networkengine.entity.LocalInfo;
import com.networkengine.entity.RequestMessageEntity;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMCallInfo;
import com.xsimple.im.db.datatable.IMChatRecordInfo;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMLocationInfo;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMReplyInfo;
import com.xsimple.im.engine.protocol.IMMsgRequestEntity;
import com.xsimple.im.utils.UnicodeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuhao on 2017/5/9.
 */

public class MsgParameterProcessor extends Processor<IMMsgRequestEntity, IMMessage> {


    public MsgParameterProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    public IMMessage process(IMMsgRequestEntity imMsgRequestEntity) throws Exception {

        boolean opinion = opinion(imMsgRequestEntity);
        if (!opinion) {
            return null;
        }
        IMMessage imMessage = buildIMMessage(imMsgRequestEntity);


        return imMessage;
    }

    @Override
    public List<IMMessage> processList(List<IMMsgRequestEntity> imMsgRequestEntities) throws Exception {

        if (imMsgRequestEntities == null || imMsgRequestEntities.isEmpty())
            return null;
        List<IMMessage> list = new ArrayList<>();
        for (IMMsgRequestEntity entity : imMsgRequestEntities) {
            IMMessage message = process(entity);
            if (message == null)
                continue;
            list.add(message);
        }

        return list;
    }

    /**
     * 判断参数
     *
     * @param imMsgRequestEntity
     * @return
     */
    private boolean opinion(IMMsgRequestEntity imMsgRequestEntity) {
        if (imMsgRequestEntity == null)
            return false;
        RequestMessageEntity msgContent = imMsgRequestEntity.getMsgContent();
        if (msgContent == null)
            return false;
//        String type = msgContent.getType();
        String type = imMsgRequestEntity.getParam().getType();
        if (TextUtils.isEmpty(type))
            return false;
        if (IMMessage.CONTENT_TYPE_VIDEO_CHAT.equals(type) || IMMessage.CONTENT_TYPE_VOICE_CHAT.equals(type)) {
            if (msgContent.getCallInfo() == null)
                return false;
        } else if (IMMessage.CONTENT_TYPE_FILE.equals(type) ||
                IMMessage.CONTENT_TYPE_IMG.equals(type) ||
                IMMessage.CONTENT_TYPE_VIDEO.equals(type) ||
                IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(type)) {
            if (msgContent.getFileInfo() == null)
                return false;
        } else if (IMMessage.CONTENT_TYPE_MAP.equals(type)) {
            if (msgContent.getLocationInfo() == null)
                return false;
        }
        return true;
    }

    /**
     * 构建消息
     *
     * @param imMsgRequestEntity
     * @return
     */
    private IMMessage buildIMMessage(IMMsgRequestEntity imMsgRequestEntity) {

        RequestMessageEntity msgContent = imMsgRequestEntity.getMsgContent();

        IMMessage message = new IMMessage();
        //发送者id
        message.setSenderId(imMsgRequestEntity.getParam().getSender());
        //发送者名字
        message.setSenderName(msgContent.getSenderName());
        //目标id
        message.setTagertId(msgContent.getRids());
        //群聊和讨论组设置名字
        if (PubConstant.ConversationType.FIXEDGROUP.equals(imMsgRequestEntity.getParam().getChatType()) || PubConstant.ConversationType.DISCUSSIONGROUP.equals(imMsgRequestEntity.getParam().getChatType())) {
            message.setGroupName(msgContent.getGroupName());
        }

        //消息类型
        message.setContentType(imMsgRequestEntity.getParam().getType());
//        message.setContentType(msgContent.getType());
        //collectContent
        message.setContent(UnicodeUtils.unicode2String(msgContent.getContent()));
        //聊天类型
        if (PubConstant.ConversationType.PERSONAL.equals(imMsgRequestEntity.getParam().getChatType())) {
            message.setType(0);
        } else if (PubConstant.ConversationType.FIXEDGROUP.equals(imMsgRequestEntity.getParam().getChatType())) {
            message.setType(1);
        } else if (PubConstant.ConversationType.DISCUSSIONGROUP.equals(imMsgRequestEntity.getParam().getChatType())) {
            message.setType(2);
        } else if ("1".equals(imMsgRequestEntity.getParam().getMsgType())) {
            message.setType(3);
        }
        //消息发送中
        message.setStatus(IMMessage.STATUS_SENDING);
        //设置是否已读
        message.setIsRead(false);
        //设置未读人数
        message.setUnReadCount(msgContent.getUnreadCount());
        //设置发送
        message.setSendOrReceive(IMMessage.STATUS_SENDING);
        //时间 TODO 同步服务器时间
        message.setTime(System.currentTimeMillis());
        //构建附件消息
        buildAccessoryMsg(message, imMsgRequestEntity);
        return message;
    }

    /**
     * 构建附件消息
     *
     * @param imMessage
     * @param imMsgRequestEntity
     */
    private void buildAccessoryMsg(IMMessage imMessage, IMMsgRequestEntity imMsgRequestEntity) {
        RequestMessageEntity msgContent = imMsgRequestEntity.getMsgContent();
        String type = imMsgRequestEntity.getParam().getType();
//        String type = msgContent.getType();
        if (null != msgContent.getAtInfo()) {
            imMessage.setAtInfo(new Gson().toJson(msgContent.getAtInfo()));
        }
        if (IMMessage.CONTENT_TYPE_VIDEO_CHAT.equals(type) || IMMessage.CONTENT_TYPE_VOICE_CHAT.equals(type)) {
            CallInfo callInfo = msgContent.getCallInfo();
            IMCallInfo imCallInfo = new IMCallInfo();
            imCallInfo.setCallMsg(UnicodeUtils.unicode2String(msgContent.getContent()));
            imCallInfo.setOptionType(type);
            imCallInfo.setGid(callInfo.getGid());
            imCallInfo.setHomeid(callInfo.getHomeid());
            imCallInfo.setCallTime(System.currentTimeMillis());
            long l = mDbManager.insertIMCallInfo(imCallInfo);
            imMessage.setCallId(l);


        } else if (IMMessage.CONTENT_TYPE_FILE.equals(type) ||
                IMMessage.CONTENT_TYPE_IMG.equals(type) ||
                IMMessage.CONTENT_TYPE_VIDEO.equals(type) ||
                IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(type)) {
            FileInfo fileInfo = msgContent.getFileInfo();
            IMFileInfo imFileInfo = new IMFileInfo();
            imFileInfo.setName(fileInfo.getName());
            imFileInfo.setPath(fileInfo.getPath());
            imFileInfo.setPos(0l);
            imFileInfo.setSize(Long.valueOf(fileInfo.getSize()));
            imFileInfo.setTime(msgContent.getTime());
            imFileInfo.setStatus(IMMessage.STATUS_NO_RECEIVE);
            imFileInfo.setType(type);
            imFileInfo.setSha(fileInfo.getSha());
            imFileInfo.setWidth(fileInfo.getWidth());
            imFileInfo.setHeight(fileInfo.getHeight());
            imFileInfo.setSe_ReTime(System.currentTimeMillis());
            imFileInfo.setSendId(imMsgRequestEntity.getParam().getSender());
            imFileInfo.setSenderName(imMsgRequestEntity.getMsgContent().getSenderName());
            long l = mDbManager.insertIMFileInfo(imFileInfo);
            imMessage.setFId(l);


        } else if (IMMessage.CONTENT_TYPE_MAP.equals(type)) {
            LocalInfo localInfo = msgContent.getLocationInfo();
            IMLocationInfo locationInfo = new IMLocationInfo();
            locationInfo.setName(localInfo.getName());
            locationInfo.setAddress(localInfo.getAddress());
            locationInfo.setLongitude(localInfo.getLongitude());
            locationInfo.setLatitude(localInfo.getLatitude());
            long l = mDbManager.insertIMLocationInfo(locationInfo);
            imMessage.setLId(l);
        }else if (IMMessage.CONTENT_TYPE_REPLY.equals(type)) {
            Gson gson = new Gson();
            IMReplyInfo imReplyInfo = gson.fromJson(gson.toJson(msgContent.getReplyInfo()), IMReplyInfo.class);
            long l = mDbManager.insertIMReplyInfo(imReplyInfo);
            imReplyInfo.setRId(l);
            imMessage.setReplyId(l);
            imMessage.setIMReplyInfo(imReplyInfo);
        } else if (IMMessage.CONTENT_TYPE_RECORD.equals(type)) {
            Gson gson = new Gson();
            IMChatRecordInfo imChatRecordInfo = gson.fromJson(gson.toJson(msgContent.getChatRecordInfo()), IMChatRecordInfo.class);
            long l = mDbManager.insertIMChatRecordInfo(imChatRecordInfo);
            imChatRecordInfo.setRId(l);
            imMessage.setRecordId(l);
            imMessage.setIMChatRecordInfo(imChatRecordInfo);
        }
        //加入到会话
        mDbManager.addOrUpdateMsgToChat(mUid, imMessage, imMsgRequestEntity.getMsgContent().getTarget_name());
    }


}



