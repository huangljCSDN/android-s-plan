package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.GroupMember;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * Created by liuhao on 2017/5/23.
 */

public class CmdFixGroupUpdateName extends CmdGroupProcessor {

    public CmdFixGroupUpdateName(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {

        String inviterName = isInviter(msgEntity) ? mCt.getString(R.string.im_you) : msgEntity.getMsgContent().getInviter_name();
        String g_name = msgEntity.getMsgContent().getG_name();

        return String.format(mCt.getString(R.string.im_mend_group_name), inviterName, g_name);

    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {

        return updateGroupName(msgEntity);
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) throws Exception {


        return saveSingleMessage(msgEntity);
    }

    /**
     * 修改讨论组名称
     *
     * @param msgEntity
     * @return
     */
    private boolean updateGroupName(MsgEntity msgEntity) {
        //历史消息
        if (msgEntity.isHistoryMsg()) {
            return true;
        }
        String name = msgEntity.getMsgContent().getG_name();

        IMGroup imGroup = getGroup(msgEntity);
        if (imGroup == null) {
            return false;
        }
        imGroup.setName(name);
        return updateGroup(imGroup);
    }

}
