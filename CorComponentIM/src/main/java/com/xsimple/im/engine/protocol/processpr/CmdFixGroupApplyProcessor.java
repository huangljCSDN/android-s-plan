package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;

import com.networkengine.entity.GroupMember;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdFixGroupApplyProcessor extends CmdGroupProcessor {
    public CmdFixGroupApplyProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        String apply = msgEntity.getMsgContent().getApplyPersonName();
        if(TextUtils.isEmpty(apply)){
            apply = msgEntity.getMsgContent().getUserName();
        }
        return String.format(mCt.getString(R.string.im_apply_join_group), apply);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {
        return true;
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) throws Exception {
        return saveSingleMessage(msgEntity);
    }

    /**
     *
     * @param msgEntity
     * @return false 申请入群的消息不进聊天消息表
     */
    @Override
    protected boolean addUpdateOrDelete(MsgEntity msgEntity) {
        return false;
    }
}
