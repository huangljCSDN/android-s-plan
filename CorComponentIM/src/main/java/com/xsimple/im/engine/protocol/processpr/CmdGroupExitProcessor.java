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
 * Created by pengpeng on 17/5/4.
 */

public class CmdGroupExitProcessor extends CmdGroupProcessor {
    public CmdGroupExitProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }


    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        String inviterName = isInviter(msgEntity) ? mCt.getString(R.string.im_you) : msgEntity.getMsgContent().getName();
        return String.format(mCt.getString(R.string.im_exit_have_discussion_group), inviterName);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {
        /*
        * 不包涵自己删群组会话取消订阅
        * */
        if (!containsSelf(msgEntity)) {
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
    protected boolean addUpdateOrDelete(MsgEntity msgEntity) {
        if (containsSelf(msgEntity) && !msgEntity.isHistoryMsg()) {
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

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) {
        return saveSingleMessage(msgEntity);
    }

    @Override
    public boolean isInviter(MsgEntity msgEntity) {
        MessageContent messageContent = msgEntity.getMsgContent();
        return mUid.equals(messageContent.getUser_id());
    }

    @Override
    protected boolean containsSelf(MsgEntity msgEntity) {
        return isInviter(msgEntity);
    }
}
