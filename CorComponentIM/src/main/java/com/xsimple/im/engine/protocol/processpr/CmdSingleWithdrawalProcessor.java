package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;

import com.networkengine.entity.MessageContent;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.greendao.IMMessageDao;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengpeng on 17/5/9.
 */

public class CmdSingleWithdrawalProcessor extends Processor<MsgEntity, IMCommand> {
    public CmdSingleWithdrawalProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    public IMCommand process(MsgEntity msgEntity) throws Exception {

        return handlerMsgWithdrwal(msgEntity);
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

    public IMCommand handlerMsgWithdrwal(MsgEntity msgEntity) {
        MessageContent messageContent = msgEntity.getMsgContent();

        String msgId = messageContent.getVirtualMsgId();
        String sendID = messageContent.getSenderId();
        String msgType = messageContent.getType();
        if (TextUtils.isEmpty(msgId))
            return null;
        IMCommand instruction = new IMCommand();
        instruction.setType(IMMessage.MESSAGE_WITHDRAWAL_SINGLE_CHAT);
        List<IMMessage> list = new ArrayList<>();
        IMMessage message = updateIMMessageWithdrwal(msgId, sendID, msgType);
        if (message != null) {
            list.add(message);
        }
        instruction.setImMessage(list);
        return instruction;
    }

    public IMMessage updateIMMessageWithdrwal(String msgId, String sendID, String msgType) {
        if (TextUtils.isEmpty(msgId))
            return null;
        List<IMMessage> list = null;

        list = mDbManager.queryRawIMMessage("WHERE " + IMMessageDao.Properties.VId.columnName + " = ?", msgId);

        if (list == null || list.isEmpty())
            return null;
        IMMessage message = list.get(0);
        if (message == null)
            return null;
        Long cId = message.getCId();
        IMChat chat = mDbManager.getChat(cId);
        if (chat != null) {
            //修改会话的最后修改时间
            chat.setTime(System.currentTimeMillis());
            chat.update();
        }
        int type = message.getType();
        String content = "";
        String myId = mUid;
        if (myId.equals(sendID)) {
            content = mCt.getString(R.string.im_you_recall_message);
        } else {
            content = String.format(mCt.getString(R.string.im_recall_message), "\"" + message.getSenderName() + "\"");
        }

        message.setContent(content);
        message.setContentType(msgType);
        message.update();

        return message;
    }
}
