package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.networkengine.entity.AtInfo;
import com.networkengine.entity.CallInfo;
import com.networkengine.entity.ChatRecordInfo;
import com.networkengine.entity.FileInfo;
import com.networkengine.entity.LocalInfo;
import com.networkengine.entity.ReplyInfo;
import com.networkengine.networkutil.util.StringUtil;
import com.networkengine.util.AtUtil;
import com.networkengine.util.LogUtil;
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
 * 收到的消息
 */

public class MsgProcessor extends Processor<MsgEntity, IMMessage> {

    String mUid;
    String myName;

    public MsgProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
        mUid = IMEngine.getInstance(mCt).getMyId();
        myName = IMEngine.getInstance(mCt).getMyName();
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
        if (imMessage != null){
            LogUtil.i("imMessage="+imMessage.toString());
            mDbManager.addOrUpdateMsgToChat(mUid, imMessage, msgEntity.getMsgContent().getSenderName());
        }

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
        LogUtil.i("imMsgs="+imMsgs.toString());
        mDbManager.addOrUpdateMsgToChat(mUid, imMsgs);

        return imMsgs;
    }

    public static int getMsgType(String msgStringType) {

        if (IMMessage.STRING_TYPE_CHAT.equals(msgStringType)) {
            return IMMessage.TYPE_CHAT;
        } else if (IMMessage.STRING_TYPE_GROUP.equals(msgStringType)) {
            return IMMessage.TYPE_GROUP;
        } else if (IMMessage.STRING_TYPE_DISCUSS.equals(msgStringType)) {
            return IMMessage.TYPE_DISCUSS;
        } else if (IMMessage.STRING_TYPE_CHATROOM.equals(msgStringType)) {
            return IMMessage.TYPE_GROUP;
        }
        return IMMessage.TYPE_CHAT;
    }

    public IMMessage transform(MsgEntity msgEntity) {
        if (mUid.equals(msgEntity.getParam().getSenderId())
                && TextUtils.equals(mUid, msgEntity.getMsgContent().getRids())
                && !TextUtils.equals(msgEntity.getMsgContent().getFromDevice(), "PC")) {
            return null;
        }

        String contentType = msgEntity.getMsgContent().getType();

        FileInfo fileInfo = msgEntity.getMsgContent().getFileInfoWithoutDb();

        CallInfo callInfo = msgEntity.getMsgContent().getCallInfo();

        LocalInfo localInfo = msgEntity.getMsgContent().getLocationInfoWithoutDb();
        IMMessage imMessage = new IMMessage();
        if (null != msgEntity.getMsgContent().getAtInfo()) {
            imMessage.setAtInfo(new Gson().toJson(msgEntity.getMsgContent().getAtInfo()));
            for (AtInfo atInfo : msgEntity.getMsgContent().getAtInfo()) {
                if (AtUtil.AT_ALL_ID.equals(atInfo.getId()) || mUid.equals(atInfo.getId())) {
                    imMessage.setIsAiteMe(true);
                    break;
                }
            }
        }
        if (IMMessage.CONTENT_TYPE_FILE.equals(contentType) ||
                IMMessage.CONTENT_TYPE_IMG.equals(contentType) ||
                IMMessage.CONTENT_TYPE_VIDEO.equals(contentType) ||
                IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(contentType)) {
            if (fileInfo != null) {
                IMFileInfo imFileInfo = new IMFileInfo();
                imFileInfo.setFId(fileInfo.getfId());

//                imFileInfo.setName(FilePathUtils.getFileName(mUid, fileInfo.getType()));
                 imFileInfo.setName(fileInfo.getName());
                imFileInfo.setSha(fileInfo.getSha());
                imFileInfo.setPath(fileInfo.getPath());
                imFileInfo.setSize(Long.valueOf(fileInfo.getSize()));
                imFileInfo.setType(fileInfo.getType());
                imFileInfo.setWidth(fileInfo.getWidth());
                imFileInfo.setHeight(fileInfo.getHeight());
                imFileInfo.setSendId(msgEntity.getParam().getSenderId());
                imFileInfo.setSenderName(msgEntity.getMsgContent().getSenderName());
                imFileInfo.setSe_ReTime(Long.valueOf(msgEntity.getParam().getSendTime()));
                //语音和视频的时长
                String time = msgEntity.getMsgContent().getTime();
                if (!TextUtils.isEmpty(time)) {
                    imFileInfo.setTime(time);
                }

                //接受的消息，文件还没有接受
                if (!mUid.equals(msgEntity.getParam().getSenderId())) {
                    imFileInfo.setStatus(IMMessage.STATUS_NO_RECEIVE);
                    imFileInfo.setPos(0l);
                }
                //消息的发送者是自己，并且sha 值不为空，则任务文件已经发送成功
                if (msgEntity.getParam().getSenderId().equals(mUid) && !TextUtils.isEmpty(fileInfo.getSha())) {
                    imFileInfo.setStatus(IMMessage.STATUS_SUCCESS);
                }
                //设置文件消息
                Long insert = mDbManager.insertIMFileInfo(imFileInfo);
                imMessage.setFId(insert);
                imMessage.setIMFileInfo(imFileInfo);
            }
        } else if (IMMessage.CONTENT_TYPE_MAP.equals(contentType)) {
            if (localInfo != null) {
                IMLocationInfo imLocationInfo = new IMLocationInfo(localInfo.getName(), localInfo.getAddress(), localInfo.getLatitude(), localInfo.getLongitude());
                long l = mDbManager.insertIMLocationInfo(imLocationInfo);
                imMessage.setLId(l);
            }
        } else if (IMMessage.CONTENT_TYPE_VIDEO_CHAT.equals(contentType) || IMMessage.CONTENT_TYPE_VOICE_CHAT.equals(contentType)
                || IMMessage.CONTENT_TYPE_CANCEL.equals(contentType) || IMMessage.CONTENT_TYPE_REJECT.equals(contentType)
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
        } else if (IMMessage.CONTENT_TYPE_REPLY.equals(contentType)) {
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
        imMessage.setIsRead(false);
        imMessage.setUnReadCount(msgEntity.getMsgContent().getUnreadCount());
        imMessage.setReadCount(0);
        imMessage.setReceiverName(msgEntity.getMsgContent().getReceiverName());
        //将拒绝的消息设为已读
        if (IMMessage.CONTENT_TYPE_CANCEL.equals(contentType) || IMMessage.CONTENT_TYPE_REJECT.equals(contentType)) {
            imMessage.setUnReadCount(0);
            imMessage.setIsRead(true);
        }
        imMessage.setSenderId(msgEntity.getParam().getSenderId());
        imMessage.setSendOrReceive(
                (mUid.equals(msgEntity.getParam().getSenderId())
                        ? IMMessage.FLAG_MSG_SENDER : IMMessage.FLAG_MSG_RECEIVER));
        imMessage.setStatus(IMMessage.STATUS_SUCCESS);
        //局长同意报名的目标id
        if (IMMessage.GROUP_OFFICE_AGREE.equals(contentType) || IMMessage.GROUP_OFFICE_APPLY.equals(contentType)){
            imMessage.setTagertId(msgEntity.getMsgContent().getUserId());
        } else {
            imMessage.setTagertId(msgEntity.getMsgContent().getRids());
        }

        imMessage.setTime(Long.valueOf(msgEntity.getParam().getSendTime()));
        imMessage.setType(getMsgType(msgEntity.getMsgContent().getChatType()));
        imMessage.setIsDisturb(msgEntity.getParam().isDisturb);
        imMessage.setReceiverName(msgEntity.getMsgContent().getReceiverName());

        return imMessage;
    }

}