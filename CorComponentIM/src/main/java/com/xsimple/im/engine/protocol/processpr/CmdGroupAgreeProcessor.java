package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.GroupMember;
import com.networkengine.entity.MessageContent;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdGroupAgreeProcessor extends CmdGroupProcessor {
    public CmdGroupAgreeProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {

        MessageContent messageContent = msgEntity.getMsgContent();
        String inviterName = isInviter(msgEntity) ? mCt.getString(R.string.im_you) : messageContent.getInviter_name();
        String invitedName = isInvited(msgEntity) ? mCt.getString(R.string.im_you) : messageContent.getInvited_name();
        return String.format(mCt.getString(R.string.im_pass_invite_join_group), invitedName, inviterName);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {
        return saveGroup(msgEntity);
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) {
        return saveSingleMessage(msgEntity);
    }

}
