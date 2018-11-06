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
 * Created by huanglingjun on 18/11/6.
 * 同意加入组局命令
 */

public class CmdChatRoomAgreeProcessor extends CmdGroupProcessor {
    public CmdChatRoomAgreeProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {

        MessageContent messageContent = msgEntity.getMsgContent();
        return messageContent.getContent();
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
