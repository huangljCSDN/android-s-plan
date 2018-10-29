package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.networkengine.entity.CallInfo;
import com.networkengine.entity.ChatRecordInfo;
import com.networkengine.entity.FileInfo;
import com.networkengine.entity.LocalInfo;
import com.networkengine.entity.ReplyInfo;
import com.networkengine.networkutil.util.StringUtil;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMCallInfo;
import com.xsimple.im.db.datatable.IMChatRecordInfo;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMLocationInfo;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMReplyInfo;
import com.xsimple.im.engine.IMEngine;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pengpeng on 17/5/5.
 */

public class HisMsgProcessor extends Processor<MsgEntity, IMMessage> {

    String mUid;

    public HisMsgProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
        mUid = IMEngine.getInstance(mCt).getMyId();
    }

    @Override
    public IMMessage process(MsgEntity msgEntity) {
        if (msgEntity == null) {
            return null;
        }
        if (TextUtils.isEmpty(mUid)) {
            return null;
        }
        IMMessage imMessage = transform(msgEntity);
        mDbManager.addOrUpdateHisMsgToChat(mUid, imMessage);

        return imMessage;
    }

    @Override
    public List<IMMessage> processList(List<MsgEntity> msgEntitys) {
        List<IMMessage> imMsgs = new ArrayList<>();

        if (msgEntitys == null || msgEntitys.isEmpty()) {
            return imMsgs;
        }

        for (MsgEntity msgEntity : msgEntitys) {

            if (msgEntity == null) {
                continue;
            }
            if (TextUtils.isEmpty(msgEntity.getParam().getVirtualMsgId())) {
                continue;
            }

            imMsgs.add(transform(msgEntity));
        }
        //像会话中 添加消息
        mDbManager.addOrUpdateHisMsgToChat(mUid, imMsgs);

        return imMsgs;
    }

    public static int getMsgType(String msgStringType) {

        if (IMMessage.STRING_TYPE_CHAT.equals(msgStringType)) {
            return IMMessage.TYPE_CHAT;
        } else if (IMMessage.STRING_TYPE_GROUP.equals(msgStringType)) {
            return IMMessage.TYPE_GROUP;
        } else if (IMMessage.STRING_TYPE_DISCUSS.equals(msgStringType)) {
            return IMMessage.TYPE_DISCUSS;
        }
        return IMMessage.TYPE_CHAT;
    }

    public IMMessage transform(MsgEntity msgEntity) {

        String contentType = msgEntity.getMsgContent().getType();

        FileInfo fileInfo = msgEntity.getMsgContent().getFileInfoWithoutDb();

        CallInfo callInfo = msgEntity.getMsgContent().getCallInfo();

        LocalInfo localInfo = msgEntity.getMsgContent().getLocationInfoWithoutDb();

        IMMessage imMessage = new IMMessage();
        if (null != msgEntity.getMsgContent().getAtInfo()) {
            imMessage.setAtInfo(new Gson().toJson(msgEntity.getMsgContent().getAtInfo()));
        }
        if (IMMessage.CONTENT_TYPE_FILE.equals(contentType) ||
                IMMessage.CONTENT_TYPE_IMG.equals(contentType) ||
                IMMessage.CONTENT_TYPE_VIDEO.equals(contentType) ||
                IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(contentType)) {
            if (fileInfo != null) {
                IMFileInfo imFileInfo = new IMFileInfo();
                imFileInfo.setFId(fileInfo.getfId());
                imFileInfo.setName(fileInfo.getName());
                imFileInfo.setSha(fileInfo.getSha());
                imFileInfo.setPath(fileInfo.getPath());
                imFileInfo.setSize(Long.valueOf(fileInfo.getSize()));
                imFileInfo.setType(fileInfo.getType());
                imFileInfo.setWidth(fileInfo.getWidth());
                imFileInfo.setHeight(fileInfo.getHeight());
                imFileInfo.setIsPlay(true);
                imFileInfo.setSendId(msgEntity.getParam().getSenderId());
                imFileInfo.setSenderName(msgEntity.getMsgContent().getSenderName());
                imFileInfo.setSe_ReTime(Long.valueOf(msgEntity.getParam().getSendTime()));
                imFileInfo.setReceiverName(msgEntity.getMsgContent().getReceiverName());
                //语音和视频的时长
                String time = msgEntity.getMsgContent().getTime();
                if (!TextUtils.isEmpty(time)) {
                    imFileInfo.setTime(time);
                }

                //接受的消息，文件还没有接受
                if (!mUid.equals(msgEntity.getParam().getSenderId())) {
                    imFileInfo.setStatus(IMMessage.STATUS_NO_RECEIVE);
                    imFileInfo.setPos(0l);
                    //发送的文件，已经发送
                } else {
                    imFileInfo.setStatus(IMMessage.STATUS_SUCCESS);
                    imFileInfo.setPos(Long.valueOf(fileInfo.getSize()));
                }
                //设置文件消息
                Long insert = mDbManager.insertIMFileInfo(imFileInfo);

                imMessage.setFId(insert);

            }
        } else if (IMMessage.CONTENT_TYPE_MAP.equals(contentType)) {
            if (localInfo != null) {
                IMLocationInfo imLocationInfo = new IMLocationInfo(localInfo.getName(), localInfo.getAddress(), localInfo.getLatitude(), localInfo.getLongitude());
                long l = mDbManager.insertIMLocationInfo(imLocationInfo);
                imMessage.setLId(l);
            }
        } else if (IMMessage.CONTENT_TYPE_VIDEO_CHAT.equals(contentType) || IMMessage.CONTENT_TYPE_VOICE_CHAT.equals(contentType)
            //  || IMMessage.CONTENT_TYPE_CANCEL.equals(contentType) || IMMessage.CONTENT_TYPE_REJECT.equals(contentType)
                ) {
            if (callInfo != null) {
                IMCallInfo imCallInfo = new IMCallInfo();
                imCallInfo.setHomeid(callInfo.getHomeid());
                imCallInfo.setOptionType(callInfo.getOptionType());
                imCallInfo.setOptionId(callInfo.getOptionId());
                imCallInfo.setCallType(callInfo.getCallType());
                imCallInfo.setGid(callInfo.getGid());
                Long insert = mDbManager.insertIMCallInfo(imCallInfo);
                imMessage.setCallId(insert);

            }
        }else if (IMMessage.CONTENT_TYPE_REPLY.equals(contentType)) {
            ReplyInfo replyInfo = msgEntity.getMsgContent().getReplyInfo();
            if (replyInfo != null) {
                Gson gson = new Gson();
                IMReplyInfo imReplyInfo = gson.fromJson(gson.toJson(replyInfo), IMReplyInfo.class);
                long l = mDbManager.insertIMReplyInfo(imReplyInfo);
                imReplyInfo.setRId(l);
                imMessage.setReplyId(l);
                imMessage.setIMReplyInfo(imReplyInfo);
            }
        } else if (IMMessage.CONTENT_TYPE_RECORD.equals(contentType)) {
            ChatRecordInfo chatRecordInfo = msgEntity.getMsgContent().getChatRecordInfo();
            if (chatRecordInfo != null) {
                Gson gson = new Gson();
                IMChatRecordInfo imChatRecordInfo = gson.fromJson(gson.toJson(chatRecordInfo), IMChatRecordInfo.class);
                long l = mDbManager.insertIMChatRecordInfo(imChatRecordInfo);
                imChatRecordInfo.setRId(l);
                imMessage.setRecordId(l);
                imMessage.setIMChatRecordInfo(imChatRecordInfo);
            }
        }


        //设置消息 id  服务器的垃圾返回字段 冗余
        imMessage.setMsgID(msgEntity.getParam().getMsgID());
        imMessage.setMk(msgEntity.getMsgContent().getChatType());
        imMessage.setVId(msgEntity.getParam().getVirtualMsgId());
        imMessage.setSenderName(msgEntity.getMsgContent().getSenderName());
        imMessage.setGroupName(msgEntity.getMsgContent().getGroupName());
        imMessage.setContent(StringUtil.unicode2String(msgEntity.getMsgContent().getContent()));
        imMessage.setContentType(msgEntity.getMsgContent().getType());
        imMessage.setIsRead(true);
        //
        imMessage.setReadCount(1);
        imMessage.setSenderId(msgEntity.getParam().getSenderId());
        imMessage.setSendOrReceive(mUid.equals(msgEntity.getParam().getSenderId()) ? IMMessage.FLAG_MSG_SENDER : IMMessage.FLAG_MSG_RECEIVER);
        imMessage.setStatus(IMMessage.STATUS_SUCCESS);
        imMessage.setTagertId(msgEntity.getMsgContent().getRids());
        imMessage.setTime(Long.valueOf(msgEntity.getParam().getSendTime()));
        imMessage.setType(getMsgType(msgEntity.getMsgContent().getChatType()));

        // 处理撤回的消息
        if (msgEntity.getParam().isWithdrawFlag()) {
            String myId = mUid;
            if (myId.equals(imMessage.getSenderId())) {
                imMessage.setContent(mCt.getString(R.string.im_you_recall_message));
            } else {
                imMessage.setContent(String.format(mCt.getString(R.string.im_recall_message), "\"" + imMessage.getSenderName() + "\""));
            }
            if (IMMessage.STRING_TYPE_CHAT.equals(imMessage.getType())) {
                imMessage.setContentType(IMMessage.MESSAGE_WITHDRAWAL_SINGLE_CHAT);
            } else {
                imMessage.setContentType(IMMessage.MESSAGE_WITHDRAWAL_GROUP_CHAT);
            }
        }
        return imMessage;
    }

}