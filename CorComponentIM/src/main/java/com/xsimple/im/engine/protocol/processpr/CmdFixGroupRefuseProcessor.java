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

public class CmdFixGroupRefuseProcessor extends CmdGroupProcessor {
    public CmdFixGroupRefuseProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        return mCt.getString(R.string.im_group_rejected);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {
        return true;
    }

    @Override
    protected boolean addUpdateOrDelete(MsgEntity msgEntity) {
        return false;
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) throws Exception {
        return saveSingleMessage(msgEntity);
    }

}
