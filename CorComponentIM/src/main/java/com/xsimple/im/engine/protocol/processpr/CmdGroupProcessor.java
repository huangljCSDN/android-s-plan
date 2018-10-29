package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;

import com.networkengine.entity.GroupMember;
import com.networkengine.entity.MessageContent;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMSysMessage;
import com.xsimple.im.engine.IMEngine;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;
import com.xsimple.im.event.IMChatEvent;
import com.xsimple.im.event.IMChatListEvent;
import com.xsimple.im.event.NewSysMsgEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengpeng on 17/5/5.
 */

public abstract class CmdGroupProcessor extends Processor<MsgEntity, IMCommand> {

    IMCommand mInstruction = new IMCommand();

    public CmdGroupProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    protected abstract String getMsgContent(MsgEntity msgEntity, List<GroupMember> member);

    public IMMessage transform(MsgEntity msgEntity) {
        MessageContent messageContent = msgEntity.getMsgContent();

        IMMessage imMessage = new IMMessage();

        setContentType(imMessage, msgEntity);
        imMessage.setTagertId(messageContent.getG_id());
        imMessage.setSenderId(msgEntity.getParam().getSenderId());
        imMessage.setSenderName(messageContent.getSenderName());
        imMessage.setStatus(IMMessage.STATUS_SUCCESS);
        imMessage.setTime(Long.valueOf(msgEntity.getParam().getSendTime()));
        imMessage.setType(fixGroupOrGroup(msgEntity.getMsgContent().getType()));
        imMessage.setGroupName(messageContent.getG_name());
        imMessage.setSendOrReceive(IMMessage.ON_RECEIVE_IMMESSAGE);
        imMessage.setVId(msgEntity.getParam().getVirtualMsgId());
        imMessage.setReceiverName(messageContent.getReceiverName());
        imMessage.setIsDisturb(msgEntity.getParam().isDisturb);
        return imMessage;
    }

    /**
     * 设置消息类型
     *
     * @param imMessage
     * @param msgEntity
     */
    protected void setContentType(IMMessage imMessage, MsgEntity msgEntity) {
        imMessage.setContentType(IMMessage.CONTENT_MESSAGER_SYSTEM);
    }

//    @Override
//    public IMOrder process(MsgEntity msgEntity) {
//        mImOrder.setType(msgEntity.getMsgContent().getType());
//        return mImOrder;
//    }@Override
//    public IMOrder process(MsgEntity msgEntity) {
//        mImOrder.setType(msgEntity.getMsgContent().getType());
//        return mImOrder;
//    }

    protected abstract boolean processChatAndGroup(MsgEntity msgEntity) throws Exception;

    protected abstract IMCommand processMessage(MsgEntity msgEntity) throws Exception;

    @Override
    public IMCommand process(MsgEntity msgEntity) throws Exception {

        /*
        * 设置命令和操作id
        * */
        setCmd(msgEntity).setmActionId(getGid(msgEntity));

        /*
        * 处理群组会话
        * */
        if (!processChatAndGroup(msgEntity)) {

            throw new Exception("process chat and group fail !");
        }

        /*
        * 处理消息
        * */
        return processMessage(msgEntity);
    }

    @Override
    public List<IMCommand> processList(List<MsgEntity> msgEntities) throws Exception {

        List<IMCommand> instructions = new ArrayList<>();
        for (MsgEntity msgEntity : msgEntities) {

            instructions.add(process(msgEntity));
        }

        return instructions;
    }

    public boolean isInviter(MsgEntity msgEntity) {
        MessageContent messageContent = msgEntity.getMsgContent();
        return mUid.equals(messageContent.getInviter_id());
    }

    public boolean isInvited(MsgEntity msgEntity) {
        MessageContent messageContent = msgEntity.getMsgContent();
        return mUid.equals(messageContent.getInvited_id());
    }

    /**
     * 存入讨论组
     *
     * @param msgEntity
     * @return
     */
    public boolean saveGroup(MsgEntity msgEntity) {
        //历史消息，不再做处理
        if (msgEntity.isHistoryMsg()) {
            return true;
        }
        MessageContent messageContent = msgEntity.getMsgContent();

        IMGroup imGroup = mDbManager.getGroup(messageContent.getG_id());
        if (imGroup != null && !TextUtils.equals(IMMessage.GROUP_UPDATE_REMARK, messageContent.getType())) {
            return mDbManager.saveIMGroup(new IMGroup(messageContent.getG_id(), messageContent.getG_name()
                    , String.valueOf(msgEntity.getParam().getSendTime()), String.valueOf(msgEntity.getParam().getSendTime())
                    , imGroup.getRemark(), IMGroup.TYPE_DISCUSSION));
        }

        return mDbManager.saveIMGroup(new IMGroup(messageContent.getG_id(), messageContent.getG_name()
                , String.valueOf(msgEntity.getParam().getSendTime()), String.valueOf(msgEntity.getParam().getSendTime())
                , msgEntity.getMsgContent().getRemark(), IMGroup.TYPE_DISCUSSION));
    }

    /**
     * 存入群组
     *
     * @param msgEntity
     * @return
     */
    public boolean saveFixGroup(MsgEntity msgEntity) {
        if (msgEntity.isHistoryMsg()) {
            return true;
        }

        MessageContent messageContent = msgEntity.getMsgContent();

        return mDbManager.saveIMGroup(new IMGroup(messageContent.getG_id(), messageContent.getG_name()
                , String.valueOf(msgEntity.getParam().getSendTime()), String.valueOf(msgEntity.getParam().getSendTime())
                , msgEntity.getMsgContent().getRemark(), IMGroup.TYPE_CLUSTER));
    }

    /**
     * 获得操作命令
     *
     * @param msgEntity
     * @return
     */
    public IMCommand setCmd(MsgEntity msgEntity) {

        MessageContent messageContent = msgEntity.getMsgContent();

        mInstruction.setType(messageContent.getType());
        return mInstruction;
    }

    public IMCommand setActionId(MsgEntity msgEntity) {
        mInstruction.setmActionId(getGid(msgEntity));
        return mInstruction;
    }

    /**
     * @param msgEntity
     * @return 存入命令=消息
     */
    public IMCommand saveSingleMessage(MsgEntity msgEntity) {

        List<IMMessage> msgs = new ArrayList<>();

        IMMessage imMessage = transform(msgEntity);
        imMessage.setContent(getMsgContent(msgEntity, msgEntity.getMsgContent().getMems()));
        //历史消息处理
        if (msgEntity.isHistoryMsg()) {
            imMessage.setIsRead(true);
            imMessage.setUnReadCount(0);
        }
        //扩展消息类型的处理
        saveExpandMessage(imMessage, msgEntity);
        msgs.add(imMessage);
        /*
        * 存入消息表
        * 可根据子类的addUpdateOrDelete方法决定是否同时插入聊天消息表和系统消息表
        * 子类可根据自身消息类型重写addUpdateOrDelete方法
        * */
        if (addUpdateOrDelete(msgEntity)) {
            mDbManager.addOrUpdateMsgToChat(mUid, msgs);
            mInstruction.setImMessage(msgs);
            return mInstruction;
        }
        processSystemMessage(msgEntity);

        return null;
    }

    protected boolean saveExpandMessage(IMMessage imMessage, MsgEntity msgEntity) {
        return true;
    }

    protected List<GroupMember> getGroupMember(MsgEntity msgEntity) {
        MessageContent messageContent = msgEntity.getMsgContent();

        List<GroupMember> mems = messageContent.getMems();
        return mems;
    }

    /**
     * 判断是否需要存入修改或者删除会话
     * 子类需要重写
     * 改方法
     *
     * @param msgEntity
     * @return
     */
    protected boolean addUpdateOrDelete(MsgEntity msgEntity) {

        return true;
    }


    /**
     * 操作集合里包涵是否自己
     *
     * @param msgEntity
     * @return
     */
    protected boolean containsSelf(MsgEntity msgEntity) {
        if (msgEntity == null) {
            return false;
        }
        MessageContent messageContent = msgEntity.getMsgContent();
        if (messageContent == null) {
            return false;
        }
        if (mUid.equals(messageContent.getInvited_id())) {
            return true;
        }
        List<GroupMember> mems = getGroupMember(msgEntity);
        if (mems == null || mems.isEmpty()) {
            return false;
        }
        for (GroupMember groupMember : mems) {
            if (groupMember == null) {
                continue;
            }
            if (mUid.equals(groupMember.getLn())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 操作集合里包涵是否自己
     *
     * @param msgEntity
     * @return
     */
    protected boolean containsSelfByLn(MsgEntity msgEntity) {
        if (msgEntity == null) {
            return false;
        }
        MessageContent messageContent = msgEntity.getMsgContent();
        if (messageContent == null) {
            return false;
        }
        List<GroupMember> mems = getGroupMember(msgEntity);
        if (mems == null || mems.isEmpty()) {
            return false;
        }
        for (GroupMember groupMember : mems) {
            if (groupMember == null) {
                continue;
            }
            if (mUid.equals(groupMember.getLn())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 删除群组和会话
     *
     * @param msgEntity
     * @return
     */
    protected boolean removeChatAndGroup(MsgEntity msgEntity) {
        if (msgEntity.isHistoryMsg()) {
            return true;
        }
        //删除群组和会话
        try {
            mDbManager.deleteIMChatByGroup(mUid, getGid(msgEntity));
            mDbManager.deleteIMGroupDao(getGid(msgEntity));
            EventBus.getDefault().post(new IMChatListEvent());
            IMChatEvent imChatEvent = new IMChatEvent(IMChatEvent.GROUP_DISSOLVE, msgEntity.getMsgContent().getGroupId());
            EventBus.getDefault().post(imChatEvent);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新会话
     *
     * @param imChat
     * @return
     */
    protected boolean updateChat(IMChat imChat) {
        if (imChat == null) {
            return false;
        }
        imChat.update();
        return true;
    }

    /**
     * 更新讨论组
     *
     * @param imGroup
     * @return
     */
    protected boolean updateGroup(IMGroup imGroup) {
        if (imGroup == null) {
            return false;
        }
        imGroup.update();
        return true;
    }

    /**
     * 获得会话
     *
     * @param msgEntity
     * @return
     */
    protected IMChat getChat(MsgEntity msgEntity) {
        if (msgEntity == null) {
            return null;
        }
        String gId = getGid(msgEntity);
        return mDbManager.getChat(mUid, gId, fixGroupOrGroup(msgEntity.getMsgContent().getType()));
    }

    /**
     * 获得群组
     *
     * @param msgEntity
     * @return
     */
    protected IMGroup getGroup(MsgEntity msgEntity) {
        if (msgEntity == null) {
            return null;
        }
        String gId = getGid(msgEntity);
        if (TextUtils.isEmpty(gId)) {
            return null;
        }

        return mDbManager.getGroup(gId);
    }

    /**
     * 取消订阅
     *
     * @param msgEntity
     * @return
     */
    protected boolean unsubscribeToTopic(MsgEntity msgEntity) {
        if (msgEntity.isHistoryMsg()) {
            return true;
        }
        String gid = getGid(msgEntity);
        if (TextUtils.isEmpty(gid)) {
            return false;
        }
        IMEngine.getInstance(mCt).unsubscribeToTopic(getGid(msgEntity));
        return true;
    }

    /**
     * 添加订阅
     *
     * @param msgEntity
     * @return
     */
    protected boolean subscribeToTopic(MsgEntity msgEntity) {
        String gid = getGid(msgEntity);
        if (TextUtils.isEmpty(gid)) {
            return false;
        }
        if (!msgEntity.isHistoryMsg()) {
            IMEngine.getInstance(mCt).subscribeToTopic(getGid(msgEntity));
        }
        return true;
    }

    /**
     * 获得群组id
     *
     * @param msgEntity
     * @return
     */
    protected String getGid(MsgEntity msgEntity) {
        if (msgEntity == null) {
            return null;
        }
        MessageContent messageContent = msgEntity.getMsgContent();
        if (messageContent == null) {
            return null;
        }
        return !TextUtils.isEmpty(messageContent.getG_id()) ? messageContent.getG_id() : messageContent.getGroupId();
    }

    /**
     * 获得群组名称
     *
     * @param msgEntity
     * @return
     */
    protected String getGname(MsgEntity msgEntity) {
        if (msgEntity == null) {
            return null;
        }
        MessageContent messageContent = msgEntity.getMsgContent();
        if (messageContent == null) {
            return null;
        }
        return !TextUtils.isEmpty(messageContent.getG_name()) ? messageContent.getG_name() : messageContent.getGroupName();
    }

    protected boolean processSystemMessage(MsgEntity msgEntity) {

        IMSysMessage imSysMessage = new IMSysMessage();
        imSysMessage.setCurrUserId(IMEngine.getInstance(mCt).getMyId());
        imSysMessage.setUserId(msgEntity.getMsgContent().getUserId());
        imSysMessage.setUserName(msgEntity.getMsgContent().getUserName());
        imSysMessage.setGroupId(msgEntity.getMsgContent().getGroupId());
        imSysMessage.setReceivedTimer(Long.parseLong(msgEntity.getParam().getSendTime()));
        imSysMessage.setType(msgEntity.getMsgContent().getType());
        imSysMessage.setTitle(msgEntity.getMsgContent().getGroupName());
        //历史消息，置为已读
        if (msgEntity.isHistoryMsg()) {
            imSysMessage.setIsRead(true);
            imSysMessage.setIsClear(true);
        }
        List<GroupMember> members = msgEntity.getMsgContent().getReArray();
        imSysMessage.setContent(getMsgContent(msgEntity, members));

        // mDbManager.insertSysMessage(imSysMessage);
        if (mDbManager.addOrUpdateSystemMsgToChat(imSysMessage)) {
            mInstruction.setImSysMessage(imSysMessage);
            if (!msgEntity.isHistoryMsg()) {
                EventBus.getDefault().post(new NewSysMsgEvent());
            }
        }
        return true;
    }


}
