package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.GroupMember;
import com.networkengine.entity.MessageContent;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;
import com.xsimple.im.event.UpdateMemberEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdGroupAdminProcessor extends CmdGroupProcessor {
    public CmdGroupAdminProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {

        return String.format(mCt.getString(R.string.im_set_admin), getMaster(msgEntity));
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {
        return true;
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) throws Exception {
        return saveSingleMessage(msgEntity);
    }

    protected String getMaster(MsgEntity msgEntity) {

        MessageContent messageContent = msgEntity.getMsgContent();

        String masterId = messageContent.getMasterId();

        return mUid.equals(masterId) ? mCt.getString(R.string.im_you) : messageContent.getMasterName();
    }

    @Override
    protected boolean addUpdateOrDelete(MsgEntity msgEntity) {
        if (!msgEntity.isHistoryMsg()) {
            EventBus.getDefault().post(new UpdateMemberEvent());
        }
        return super.addUpdateOrDelete(msgEntity);
    }
}
