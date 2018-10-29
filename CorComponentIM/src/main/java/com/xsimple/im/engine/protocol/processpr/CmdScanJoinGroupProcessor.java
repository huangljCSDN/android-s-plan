package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.GroupMember;
import com.networkengine.entity.MessageContent;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * 扫描哦二维码加入讨论组提示
 * Created by chenbin on 2018/6/23.
 */

public class CmdScanJoinGroupProcessor extends CmdGroupProcessor {
    public CmdScanJoinGroupProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        MessageContent messageContent = msgEntity.getMsgContent();
        return messageContent.getTip();
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) {
        if (!saveGroup(msgEntity)) {
            return false;
        }

        subscribeToTopic(msgEntity);
        return true;
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) {

        return saveSingleMessage(msgEntity);
    }

}
