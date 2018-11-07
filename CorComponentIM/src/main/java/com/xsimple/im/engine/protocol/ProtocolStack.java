package com.xsimple.im.engine.protocol;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.networkengine.entity.MsgRequestEntity;
import com.networkengine.util.LogUtil;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.Factory.CmdProcessorFactory;
import com.xsimple.im.engine.protocol.Factory.HisMsgProcessorFactory;
import com.xsimple.im.engine.protocol.Factory.MsgProcessorFactory;
import com.xsimple.im.engine.protocol.Factory.MsgSendProcessorFactory;
import com.xsimple.im.engine.protocol.Factory.MsgSendResultProcessorFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pengpeng on 17/5/4.
 */

public class ProtocolStack {

    private CmdProcessorFactory mCmdProcessorFactory;
    //
    private MsgProcessorFactory mMsgProcessorFactory;
    //
    private MsgSendProcessorFactory mMsgSendProcessorFactory;

    private MsgSendResultProcessorFactory mMsgSendResultProcessorFactory;

    private HisMsgProcessorFactory mHisMsgProcessorFactory;

    public ProtocolStack(Context ct, DbManager db) {

        mCmdProcessorFactory = new CmdProcessorFactory(ct, db);

        mMsgProcessorFactory = new MsgProcessorFactory(ct, db);

        mMsgSendProcessorFactory = new MsgSendProcessorFactory(ct, db);

        mMsgSendResultProcessorFactory = new MsgSendResultProcessorFactory(ct, db);

        mHisMsgProcessorFactory = new HisMsgProcessorFactory(ct, db);
    }

    /**
     * 处理收到的消息
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    public IMMessage proceessMessage(MsgEntity parameter) throws Exception {
        LogUtil.i("MsgEntity====="+parameter.toString());
        if (parameter == null) {
            return null;
        }

        if (TextUtils.isEmpty(parameter.getKey())) {
            return null;
        }

        return mMsgProcessorFactory.create(parameter.getKey()).process(parameter);
    }

    /**
     * 处理收到的消息
     *
     * @param msgEntities
     * @return
     */
    public List<IMMessage> proceessMessages(List<MsgEntity> msgEntities) {

        List<IMMessage> imMessages = new ArrayList<IMMessage>();

        if (msgEntities == null) {
            return imMessages;
        }

        for (MsgEntity msgEntity : msgEntities) {
            try {
                IMMessage message = proceessMessage(msgEntity);
                if (message == null)
                    continue;
                imMessages.add(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imMessages;
    }

    /**
     * 处理历史消息
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    public IMMessage proceessHisMessage(MsgEntity parameter) throws Exception {

        if (parameter == null) {
            return null;
        }

        if (TextUtils.isEmpty(parameter.getKey())) {
            return null;
        }

        return mHisMsgProcessorFactory.create(parameter.getKey()).process(parameter);
    }

    /**
     * 处理历史命令
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    public List<IMMessage> proceessHisCmdMessage(MsgEntity parameter) throws Exception {

        if (parameter == null) {
            return null;
        }

        if (TextUtils.isEmpty(parameter.getKey())) {
            return null;
        }
        IMCommand imCommand = proceessInstruction(parameter);
        if (imCommand == null) {
            return null;
        }
        return imCommand.getImMessage();
    }


    /**
     * 处理历史消息
     *
     * @param msgEntities
     * @return
     */
    public List<IMMessage> proceessHisMessages(List<MsgEntity> msgEntities) {

        List<IMMessage> imMessages = new ArrayList<IMMessage>();

        if (msgEntities == null) {
            return imMessages;
        }
        for (MsgEntity msgEntity : msgEntities) {
            if (msgEntities == null)
                continue;
            try {
                //系统消息
                if (IMMessage.isCommand(msgEntity)) {
                    List<IMMessage> cmdMessages = proceessHisCmdMessage(msgEntity);
                    if (cmdMessages == null || cmdMessages.isEmpty()) {
                        continue;
                    }
                    for (IMMessage immessage : cmdMessages) {
                        if (immessage == null) {
                            continue;
                        }
                        imMessages.add(immessage);
                    }
                    continue;
                }
                //普通消息
                IMMessage imMessage = proceessHisMessage(msgEntity);
                if (imMessage == null) {
                    continue;
                }
                imMessages.add(imMessage);
            } catch (Exception e) {
                Log.e("hh", e.getMessage());
            }
        }
        return imMessages;
    }

    /**
     * 单条命令消息的处理
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    public IMCommand proceessInstruction(MsgEntity parameter) throws Exception {

        if (parameter == null) {
            return null;
        }

        if (TextUtils.isEmpty(parameter.getKey())) {
            return null;
        }
        LogUtil.i("IMCommand==="+mCmdProcessorFactory.create(parameter.getKey()).process(parameter).toString());
        return mCmdProcessorFactory.create(parameter.getKey()).process(parameter);
    }

    /**
     * 多条命令消息的处理
     *
     * @param msgEntities
     * @return
     */
    public List<IMCommand> proceessInstructions(List<MsgEntity> msgEntities) {

        List<IMCommand> instructions = new ArrayList<>();

        if (msgEntities == null) {
            return instructions;
        }

        for (MsgEntity msgEntity : msgEntities) {
            try {
                instructions.add(proceessInstruction(msgEntity));
            } catch (Exception e) {
                Log.e("pp", "处理命令  " + e.getMessage());
            }
        }
        return instructions;
    }


    public boolean isCmd(MsgEntity parameter) {

        boolean isCmd = mCmdProcessorFactory.canCreate(parameter.getKey());

        boolean isMsg = mMsgProcessorFactory.canCreate(parameter.getKey());

        return isCmd && !isMsg;

    }

    /**
     * 通过消息发送体构建单条消息
     *
     * @param parameter
     * @return
     */
    public IMMessage proceessMessage(IMMsgRequestEntity parameter) {

        if (parameter == null) {
            return null;
        }

        if (TextUtils.isEmpty(parameter.getKey())) {
            return null;
        }

        try {
            return mMsgSendProcessorFactory.create(parameter.getKey()).process(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过消息发送体构建多条消息
     *
     * @param msgRequestMultipleEntity
     * @return
     */
    public List<IMMessage> proceessMessage(IMMsgRequestMultipleEntity msgRequestMultipleEntity) {
        List<IMMessage> list = new ArrayList<>();
        if (msgRequestMultipleEntity == null) {
            return list;
        }
        List<MsgRequestEntity> msgList = msgRequestMultipleEntity.getMsgList();
        if (msgList == null) {
            return list;
        }
        Iterator<MsgRequestEntity> iterator = msgList.iterator();
        while (iterator.hasNext()) {
            MsgRequestEntity requestEntity = iterator.next();
            if (requestEntity == null) {
                iterator.remove();
                continue;
            }
            IMMessage imMessage = proceessMessage(new IMMsgRequestEntity(requestEntity));
            if (imMessage == null) {
                iterator.remove();
                continue;
            }
            msgRequestMultipleEntity.putMsgRequestEntityId(imMessage.getLocalId(), requestEntity);
            list.add(imMessage);
        }

        return list;
    }

    /**
     * 单条消息发送后的消息结果处理
     *
     * @param parameter
     * @return
     */
    public IMMessage proceessMessage(IMSendResultEntity parameter) {

        if (parameter == null) {
            return null;
        }

        if (TextUtils.isEmpty(parameter.getKey())) {
            return null;
        }

        try {
            return mMsgSendResultProcessorFactory.create(parameter.getKey()).process(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 多条消息发送后的消息结果处理
     *
     * @param parameter
     * @return
     */
    public List<IMMessage> proceessMessage(IMSendResultMultipleEntity parameter) {
        List<IMMessage> list = new ArrayList<>();
        if (parameter == null) {
            return list;
        }
        List<IMSendResultEntity> imSendResultEntities = parameter.getImSendResultEntities();

        try {
            return mMsgSendResultProcessorFactory.create(parameter.getKey()).processList(imSendResultEntities);
        } catch (Exception e) {
            return null;
        }
    }


//        Iterator<IMSendResultEntity> iterator = imSendResultEntities.iterator();
//        while (iterator.hasNext()) {
//            IMSendResultEntity imSendResultEntity = iterator.next();
//            if (imSendResultEntity == null) {
//                continue;
//            }
//            final IMMessage imMessage = proceessMessage(imSendResultEntity);
//            if (imMessage == null) {
//                continue;
//            }
//            list.add(imMessage);
//        }
//        return list;
}
