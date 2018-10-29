package com.xsimple.im.engine.protocol.Factory;

import android.content.Context;

import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupActiveProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupAddProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupAdminProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupAgreeProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupApplyProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupCancleAdminProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupDelProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupForbiddenProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupRefuseProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupRemoveProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupUpdateName;
import com.xsimple.im.engine.protocol.processpr.CmdFixGroupUpdateProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupAddProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupAdminProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupAgreeProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupDelProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupExitProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupReadProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupRefuseProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupRemarkProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupRemoveProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupUpdateName;
import com.xsimple.im.engine.protocol.processpr.CmdGroupUpdateProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdGroupWithdrawalProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdScanJoinGroupProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdSingleReadProcessor;
import com.xsimple.im.engine.protocol.processpr.CmdSingleWithdrawalProcessor;
import com.xsimple.im.engine.protocol.processpr.Processor;

import java.util.Map;

/**
 * Created by pengpeng on 17/5/4.
 */

public class CmdProcessorFactory extends ProcessorFactory<String, MsgEntity, IMCommand> {

    public CmdProcessorFactory(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected void initProcessorMap(Map<String, Processor<MsgEntity, IMCommand>> processorMap) {
        processorMap.put(IMMessage.GROUP_ADD, new CmdGroupAddProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.GROUP_AGREE, new CmdGroupAgreeProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.GROUP_REFUSE, new CmdGroupRefuseProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.GROUP_REMOVE, new CmdGroupRemoveProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.GROUP_DEL, new CmdGroupDelProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.GROUP_OWN, new CmdGroupExitProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.GROUP_UPDATE, new CmdGroupUpdateProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.GROUP_UPDATE_REMARK, new CmdGroupRemarkProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_UPDATE_REMARK, new CmdGroupRemarkProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.GROUP_SET_ADMIN, new CmdGroupAdminProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_UPDATE, new CmdFixGroupUpdateProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_DEL, new CmdFixGroupDelProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_FORBIDDEN, new CmdFixGroupForbiddenProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_ACTIVE, new CmdFixGroupActiveProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_SET_ADMIN, new CmdFixGroupAdminProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_CANCLE_ADMIN, new CmdFixGroupCancleAdminProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_ADD, new CmdFixGroupAddProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_REMOVE, new CmdFixGroupRemoveProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_APPLY, new CmdFixGroupApplyProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_AGREE, new CmdFixGroupAgreeProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.FIXGROUP_REFUSE, new CmdFixGroupRefuseProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.MESSAGE_READ_SINGLE_CHAT, new CmdSingleReadProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.MESSAGE_READ_GROUP_CHAT, new CmdGroupReadProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.MESSAGE_WITHDRAWAL_SINGLE_CHAT, new CmdSingleWithdrawalProcessor(mCt, mDbManager));
        processorMap.put(IMMessage.MESSAGE_WITHDRAWAL_GROUP_CHAT, new CmdGroupWithdrawalProcessor(mCt, mDbManager));

        processorMap.put(IMMessage.FIXGROUP_UPDATE_NAME, new CmdFixGroupUpdateName(mCt, mDbManager));
        processorMap.put(IMMessage.GROUP_UPDATE_NAME, new CmdGroupUpdateName(mCt, mDbManager));
        processorMap.put(IMMessage.SCAN_QRCODE_JOIN_GROUP, new CmdScanJoinGroupProcessor(mCt, mDbManager));


    }

    @Override
    public Processor<MsgEntity, IMCommand> create(String key) {
        return super.create(key);
    }
}
