package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;

import com.networkengine.entity.IMSendResultDetail;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMCallInfo;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.IMSendResultEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuhao on 2017/5/9.
 */

public class MsgResultProcessor extends Processor<IMSendResultEntity, IMMessage> {


    public MsgResultProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    public IMMessage process(IMSendResultEntity imMsgRequestEntity) throws Exception {

        long localId = imMsgRequestEntity.localId;
        IMMessage message = mDbManager.loadIMMessageByLocalId(localId);
        if (message == null)
            return null;
        IMSendResultDetail imSendResultDetail = imMsgRequestEntity.getData();
        if (imSendResultDetail != null) {
            String vid = imMsgRequestEntity.getData().getVirtualMsgId();
            if (TextUtils.isEmpty(vid)) {
                message.setStatus(IMMessage.STATUS_FAIL);
            } else {
                message.setStatus(IMMessage.STATUS_SUCCESS);
                message.setVId(vid);
                message.setTime(imMsgRequestEntity.getData().getSendTime());
                IMCallInfo imCallInfo = message.getIMCallInfo();
                if (imCallInfo != null) {
                    imCallInfo.setOptionId(vid);
                    mDbManager.insertOrReplaceIMCallInfo(imCallInfo);
                }
                IMFileInfo imFileInfo = message.getIMFileInfo();
                if (imFileInfo != null) {
                    imFileInfo.setSe_ReTime(imMsgRequestEntity.getData().getSendTime());
                    imFileInfo.setStatus(IMMessage.STATUS_SUCCESS);
                    imFileInfo.update();
                }
            }
        } else {
            message.setStatus(IMMessage.STATUS_FAIL);

        }

        message.update();
        Long cId = message.getCId();
        IMChat chat = mDbManager.getChat(cId);
        if (chat != null) {
            chat.setTime(message.getTime());
            chat.update();
        }
        return message;
    }

    @Override
    public List<IMMessage> processList(List<IMSendResultEntity> imMsgRequestEntities) throws Exception {

        if (imMsgRequestEntities == null || imMsgRequestEntities.isEmpty())
            return null;
        List<IMMessage> list = new ArrayList<>();

        for (IMSendResultEntity entity : imMsgRequestEntities) {
            //TODO 需要看单条消息发送失败的情况vid为不为空
            IMMessage process = process(entity);
            list.add(process);
        }
        return list;
    }


}



