package com.xsimple.im.engine.protocol;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.networkengine.PubConstant;
import com.networkengine.entity.CallInfo;
import com.networkengine.entity.ChatRecordInfo;
import com.networkengine.entity.FileInfo;
import com.networkengine.entity.LocalInfo;
import com.networkengine.entity.MsgRequestEntity;
import com.networkengine.entity.ReplyInfo;
import com.networkengine.entity.RequestMessageEntity;
import com.networkengine.entity.RequestMessageParams;
import com.networkengine.util.AtUtil;
import com.networkengine.util.CoracleSdk;
import com.xsimple.im.R;
import com.xsimple.im.db.datatable.IMChatRecordInfo;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMLocationInfo;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMReplyInfo;
import com.xsimple.im.utils.UnicodeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuhao on 2017/5/9.
 */

public class IMMsgRequestEntity extends MsgRequestEntity implements IProcessorParameter<String> {


    public IMMsgRequestEntity() {
        RequestMessageEntity requestMessageEntity = new RequestMessageEntity();
        this.setMsgContent(requestMessageEntity);
    }

    public IMMsgRequestEntity(MsgRequestEntity msgRequestEntity) {
        this.setMsgContent(msgRequestEntity.getMsgContent());
        this.setParam(msgRequestEntity.getParam());
    }


    /**
     * 构建发送消息的请求提
     *
     * @param type             通话类型
     * @param msgType          消息类型
     * @param sendName         发送者名字
     * @param sendId           发送者id
     * @param tagertId         目标id
     * @param groupName        群组名字
     * @param content          发送的类容
     * @param singleTargetName
     * @return
     */
    public IMMsgRequestEntity buildIMMsgRequestEntity(int type, String msgType, String sendName, String sendId, String tagertId, String groupName
            , String content, String singleTargetName) {

        RequestMessageEntity requestMessageEntity = getMsgContent();
        if (msgType.equals(IMMessage.CONTENT_TYPE_MAP)) {
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_location);
        } else if (msgType.equals(IMMessage.CONTENT_TYPE_FUN)) {
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_fun_message);
        } else if (msgType.equals(IMMessage.CONTENT_TYPE_REPLY)) {
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_reply_message);
        } else if (msgType.equals(IMMessage.CONTENT_TYPE_RECORD)) {
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_record_message);
        } else if (IMMessage.CONTENT_TYPE_FILE.equals(msgType)) {
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_file);
        } else if (IMMessage.CONTENT_TYPE_IMG.equals(msgType)) {
            //TODO gif 以后优化
            if (content.endsWith("gif") || CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_image_gif).equals(content)) {
                content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_image_gif);
            } else {
                content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_image);
            }
        } else if (IMMessage.CONTENT_TYPE_SHORT_VOICE.equals(msgType)) {
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_voice);
        } else if (IMMessage.CONTENT_TYPE_VIDEO.equals(msgType)) {
            content = CoracleSdk.getCoracleSdk().getContext().getString(R.string.im_video);
        }
        requestMessageEntity.setContent(UnicodeUtils.string2Unicode(content));

        RequestMessageParams params = new RequestMessageParams();

        if (0 == type) {//单聊
            List<String> receivers = new ArrayList<>();
            receivers.add(tagertId);
            //设置单聊id
            params.setReceiverIds(receivers);
            params.setChatType(com.networkengine.PubConstant.ConversationType.PERSONAL);
            requestMessageEntity.setTarget_name(singleTargetName);
            requestMessageEntity.setChatType(com.networkengine.PubConstant.ConversationType.PERSONAL);
        } else if (1 == type) {//群聊
            //设置群聊id
            params.setGroupId(tagertId);
            params.setGroupName(groupName);
            requestMessageEntity.setGroupName(groupName);
            params.setChatType(PubConstant.ConversationType.CHATROOM);
            requestMessageEntity.setChatType(com.networkengine.PubConstant.ConversationType.CHATROOM);
        } else if (2 == type) {//讨论组
            //设置群聊id
            params.setGroupId(tagertId);
            params.setGroupName(groupName);
            requestMessageEntity.setGroupName(groupName);
            params.setChatType(com.networkengine.PubConstant.ConversationType.CHATROOM);
            requestMessageEntity.setChatType(com.networkengine.PubConstant.ConversationType.CHATROOM);
        }
        //rids id

        params.setType(msgType);
        params.setSender(sendId);
        params.setSenderName(sendName);
        params.setMsgType("0");

        //设置发送人
        requestMessageEntity.setSenderName(sendName);
        requestMessageEntity.setRids(tagertId);
        requestMessageEntity.setType(msgType);


        //设置请请求体内容
        this.setMsgContent(requestMessageEntity);

        this.setParam(params);
        return this;
    }


    /**
     * 构建文件信息
     *
     * @param path
     * @param name
     * @param size
     * @param time
     * @return
     */
    public FileInfo buildFileInfo(String path, String name, String size, String time) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setPath(path);
        String type = name.substring(name.lastIndexOf(".") + 1);
        fileInfo.setType(type);
        fileInfo.setName(name);
        fileInfo.setStatus(IMMessage.STATUS_SUCCESS + "");
        fileInfo.setSize(size);
        if (!TextUtils.isEmpty(time)) {
            getMsgContent().setTime(time);
        }
        getMsgContent().setFileInfo(fileInfo);
        return fileInfo;
    }

    /**
     * 构建文件信息
     *
     * @param sha
     * @param path
     * @param name
     * @param size
     * @param time
     * @return
     */
    public FileInfo buildFileInfo(String sha, String path, String name, String size, String time) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setPath(path);
        fileInfo.setSha(sha);
        String type = name.substring(name.lastIndexOf(".") + 1);
        fileInfo.setType(type);
        fileInfo.setName(name);
        fileInfo.setStatus(IMMessage.STATUS_SUCCESS + "");
        fileInfo.setSize(size);
        if (!TextUtils.isEmpty(time)) {
            getMsgContent().setTime(time);
        }
        getMsgContent().setFileInfo(fileInfo);
        return fileInfo;
    }

    /**
     * @param imFileInfo
     * @return
     */
    public FileInfo buildFileInfo(IMFileInfo imFileInfo) {

        return buildFileInfo(imFileInfo.getSha(), imFileInfo.getPath(), imFileInfo.getName(), String.valueOf(imFileInfo.getSize()), imFileInfo.getTime());
    }

    /**
     * @param fileInfo
     * @return
     */
    public FileInfo buildFileInfo(String time, FileInfo fileInfo) {

        getMsgContent().setFileInfo(fileInfo);
        //解决收藏的发送的时间为空的问题
        if (!TextUtils.isEmpty(time)) {
            getMsgContent().setTime(time);
        }
        return fileInfo;
    }


    /**
     * 构建地址信息
     *
     * @param name
     * @param address
     * @param latitude
     * @param longitude
     * @return
     */
    public LocalInfo buildLocalInfo(String name, String address, String latitude, String longitude) {
        LocalInfo localInfo = new LocalInfo();
        localInfo.setName(name);
        localInfo.setAddress(address);
        localInfo.setLatitude(latitude);
        localInfo.setLongitude(longitude);
        getMsgContent().setLocationInfo(localInfo);
        return localInfo;
    }

    /**
     * 构建地址信息
     *
     * @param imLocationInfo
     * @return
     */
    public LocalInfo buildLocalInfo(IMLocationInfo imLocationInfo) {
        Gson gson = new Gson();
        LocalInfo localInfo = gson.fromJson(gson.toJson(imLocationInfo), LocalInfo.class);
        getMsgContent().setLocationInfo(localInfo);
        return localInfo;
    }

    /**
     * 构建地址信息
     *
     * @param content
     * @return
     */
    public LocalInfo buildLocalInfo(String content) {
        LocalInfo localInfo = new Gson().fromJson(content, LocalInfo.class);
        getMsgContent().setLocationInfo(localInfo);
        return localInfo;
    }

//    /**
//     * 构建轻应用消息
//     *
//     * @param imFunInfo
//     * @return
//     */
//    public FunInfo buildFunInfo(IMFunInfo imFunInfo) {
//        Gson gson = new Gson();
//        FunInfo funInfo = gson.fromJson(gson.toJson(imFunInfo), FunInfo.class);
//        getMsgContent().setFunInfo(funInfo);
//        return funInfo;
//    }
//
//    /**
//     * 构建轻应用消息
//     *
//     * @param content
//     * @return
//     */
//    public FunInfo buildFunInfo(String content) {
//        FunInfo funInfo = new Gson().fromJson(content, FunInfo.class);
//        getMsgContent().setFunInfo(funInfo);
//        return funInfo;
//    }

    /**
     * 构建 回复消息
     *
     * @param imReplyInfo
     * @return
     */
    public ReplyInfo buildReplyInfo(IMReplyInfo imReplyInfo) {

        ReplyInfo replyInfo = new ReplyInfo(imReplyInfo.getVirtualMsgId(), imReplyInfo.getMsgSenderId()
                , imReplyInfo.getMsgSender(), imReplyInfo.getMsgContent(), imReplyInfo.getContent());
        replyInfo.setContent(AtUtil.decode(replyInfo.getContent()));
        getMsgContent().setReplyInfo(replyInfo);
        return replyInfo;
    }

    /**
     * 构建 回复消息
     *
     * @param content
     * @return
     */
    public ReplyInfo buildReplyInfo(String content) {
        ReplyInfo replyInfo = new Gson().fromJson(content, ReplyInfo.class);
        replyInfo.setContent(AtUtil.decode(replyInfo.getContent()));
        getMsgContent().setReplyInfo(replyInfo);
        return replyInfo;
    }

    /**
     * 构建 聊天记录消息
     *
     * @param imChatRecordInfo
     * @return
     */
    public ChatRecordInfo buildChatRecordInfo(IMChatRecordInfo imChatRecordInfo) {
        Gson gson = new Gson();
        ChatRecordInfo recordInfo = gson.fromJson(gson.toJson(imChatRecordInfo), ChatRecordInfo.class);
        getMsgContent().setChatRecordInfo(recordInfo);
        return recordInfo;
    }

    /**
     * 构建 聊天记录消息
     *
     * @param content
     * @return
     */
    public ChatRecordInfo buildChatRecordInfo(String content) {

        ChatRecordInfo recordInfo = new Gson().fromJson(content, ChatRecordInfo.class);
        getMsgContent().setChatRecordInfo(recordInfo);

        return recordInfo;
    }


    /**
     * 构建通话消息
     *
     * @param homeid
     * @param optionType
     * @param gid
     * @return
     */
    public CallInfo buildCallInfo(String homeid, String optionType, String gid) {
        CallInfo callInfo = new CallInfo();
        callInfo.setHomeid(homeid);
        callInfo.setOptionType(optionType);
        callInfo.setGid(gid);
        getMsgContent().setCallInfo(callInfo);
        return callInfo;
    }

    @Override
    public String getKey() {

        return this.getParam().getType();
    }
}
