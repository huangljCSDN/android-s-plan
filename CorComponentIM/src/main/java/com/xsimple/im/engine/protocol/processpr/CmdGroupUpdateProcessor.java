package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.GroupMember;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdGroupUpdateProcessor extends CmdGroupProcessor {
    public CmdGroupUpdateProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        String inviterName = isInviter(msgEntity) ? mCt.getString(R.string.im_you) : msgEntity.getMsgContent().getInviter_name();
        String groupName = msgEntity.getMsgContent().getG_name();
        return String.format(mCt.getString(R.string.im_changed_group_name), inviterName, groupName);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {

        if (!updateGroupName(msgEntity)) {
            return false;
        }

        if (!updateChatName(msgEntity)) {
            return false;
        }


        return true;
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) throws Exception {
        return saveSingleMessage(msgEntity);
    }

    /**
     * 修改会话名字
     *
     * @param msgEntity
     * @return
     */
    private boolean updateChatName(MsgEntity msgEntity) {
        if (msgEntity.isHistoryMsg()) {
            return true;
        }
        String groupName = getGname(msgEntity);

        IMChat chat = getChat(msgEntity);
        if (chat == null) {
            return false;
        }
        chat.setName(groupName);
        return updateChat(chat);
    }

    /**
     * 修改讨论组名字
     *
     * @param msgEntity
     * @return
     */
    private boolean updateGroupName(MsgEntity msgEntity) {
        if (msgEntity.isHistoryMsg()) {
            return true;
        }
        String groupName = getGname(msgEntity);

        IMGroup imGroup = getGroup(msgEntity);
        if (imGroup == null) {
            return false;
        }
        imGroup.setName(groupName);
        return updateGroup(imGroup);
    }
}
