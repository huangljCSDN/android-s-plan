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

public class CmdGroupDelProcessor extends CmdGroupProcessor {

    public CmdGroupDelProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        String operateName = msgEntity.getMsgContent().getOperateName();
        if (mUid.equals(msgEntity.getMsgContent().getOperateId())) {
            operateName = mCt.getString(R.string.im_you);
        }
        return String.format(mCt.getString(R.string.im_discussion_group_dissolved), operateName);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {

        /*
        * 删除群组会话
        * */
        if (!removeChatAndGroup(msgEntity)) {
            return false;
        }

        /*
        * 取消相关订阅
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
        return false;
    }
}
