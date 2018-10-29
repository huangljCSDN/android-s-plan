package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.GroupMember;
import com.networkengine.entity.MessageContent;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;
import com.xsimple.im.event.IMChatEvent;
import com.xsimple.im.event.IMChatListEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdGroupRemoveProcessor extends CmdGroupProcessor {
    public CmdGroupRemoveProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        if (member == null || member.isEmpty())
            return "";

        MessageContent content = msgEntity.getMsgContent();

        StringBuilder sb = new StringBuilder();
        for (GroupMember groupMember : member) {
            boolean isMyself = mUid.equals(groupMember.getLn());
            if (isMyself) {
                sb.insert(0, mCt.getString(R.string.im_you));
            } else {
                sb.append(groupMember.getUn() + "，");
            }
        }
        String result = sb.toString().trim();
        String memberNames = result.substring(0, result.length() - 1);
        String remover = mUid.equals(content.getUser_id()) ? mCt.getString(R.string.im_you) : content.getName();

        return String.format(mCt.getString(R.string.im_remove_discussion_group), remover, memberNames);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {
        /*
        * 不包涵自己删群组会话取消订阅
        * */
        if (!containsSelfByLn(msgEntity)) {

            return true;
        }

        /*
        * 删除失败返回false
        * */
        if (!removeChatAndGroup(msgEntity)) {
            return false;
        }

        /*
        * 取消订阅
        * */
        unsubscribeToTopic(msgEntity);


        return true;
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) {

        return saveSingleMessage(msgEntity);
    }

    @Override
    protected boolean addUpdateOrDelete(MsgEntity msgEntity) {
        if (containsSelfByLn(msgEntity) && !msgEntity.isHistoryMsg()) {
            IMChat chat = mDbManager.getChat(mUid, msgEntity.getMsgContent().getG_id(), fixGroupOrGroup(msgEntity.getMsgContent().getType()));
            if (chat != null) {
                chat.delete();
            }
            IMChatEvent imChatEvent = new IMChatEvent(IMChatEvent.REMOVE_GEOUP_MYSELF, msgEntity.getMsgContent().getGroupId());
            EventBus.getDefault().post(imChatEvent);
            EventBus.getDefault().post(new IMChatListEvent());
            return false;
        }

        return super.addUpdateOrDelete(msgEntity);
    }

    /**
     * 重写 获得被操作集合方法
     *
     * @param msgEntity
     * @return
     */
    @Override
    protected List<GroupMember> getGroupMember(MsgEntity msgEntity) {
        return msgEntity.getMsgContent().getReArray();
    }

}
