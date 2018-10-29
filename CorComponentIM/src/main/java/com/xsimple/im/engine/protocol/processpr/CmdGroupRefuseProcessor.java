package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.GroupMember;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdGroupRefuseProcessor extends CmdGroupProcessor {
    public CmdGroupRefuseProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        String invitedName = msgEntity.getMsgContent().getInvited_name();
        return String.format(mCt.getString(R.string.im_rejected_discussion_group), invitedName);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {
        return true;
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) {
        return saveSingleMessage(msgEntity);
    }
}
