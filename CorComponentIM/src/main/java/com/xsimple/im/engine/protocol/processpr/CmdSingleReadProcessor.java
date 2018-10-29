package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.MessageContent;
import com.networkengine.entity.MsgNotice;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengpeng on 17/5/9.
 */

public class CmdSingleReadProcessor extends Processor<MsgEntity, IMCommand> {
    public CmdSingleReadProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    public IMCommand process(MsgEntity msgEntity) throws Exception {
        if(msgEntity == null){
            throw new Exception("process msgEntity is null");
        }
        IMCommand imCommand = new IMCommand();

        MessageContent messageContent = msgEntity.getMsgContent();

        if(messageContent == null){
            throw new Exception("process messageContent is null");
        }
        imCommand.setType(messageContent.getType());

        List<MsgNotice> msgReadNotice = messageContent.getMsgReadNotice();


        if (msgReadNotice == null || msgReadNotice.isEmpty()){
            return imCommand;
        }

        List<IMMessage> imMessages = new ArrayList<>();

        for (MsgNotice notice : msgReadNotice) {
            if (notice == null) {
                continue;
            }
            IMMessage imMessage = updateIMMessageReadState(notice);
            if (imMessage != null) {
                imMessages.add(imMessage);
            }
        }
        imCommand.setImMessage(imMessages);
        return imCommand;
    }

    @Override
    public List<IMCommand> processList(List<MsgEntity> msgEntities) throws Exception {
        List<IMCommand> imCommands = new ArrayList<>();
        if(msgEntities == null || msgEntities.isEmpty()){
            return imCommands;
        }
        for (MsgEntity msgEntity : msgEntities){
            if(msgEntities == null){
                continue;
            }
            imCommands.add(process(msgEntity));
        }
        return imCommands;
    }

    public IMMessage updateIMMessageReadState(MsgNotice notice) {

        if (notice == null)
            return null;
        String virtualMsgId = notice.getVirtualMsgId();
        String totalNumber = notice.getTotalNumber();
        String unReadNumber = notice.getUnReadNumber();
        List<IMMessage> list = mDbManager.queryRawIMMessage("WHERE v_id = ?", virtualMsgId);
        if (list == null || list.isEmpty())
            return null;
        IMMessage imMessage = list.get(0);
        if (imMessage == null)
            return null;
        imMessage.setReadCount(Integer.valueOf(totalNumber) - Integer.valueOf(unReadNumber));
        imMessage.setUnReadCount(Integer.valueOf(unReadNumber));
        if ("0".equals(unReadNumber)) {
            imMessage.setIsRead(true);
        }
        imMessage.update();

        return imMessage;
    }
}
