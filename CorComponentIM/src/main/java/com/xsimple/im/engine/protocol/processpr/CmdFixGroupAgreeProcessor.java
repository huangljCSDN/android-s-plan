package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.GroupMember;
import com.networkengine.entity.MessageContent;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;


/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdFixGroupAgreeProcessor extends CmdGroupProcessor {

    public CmdFixGroupAgreeProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {

        MessageContent messageContent = msgEntity.getMsgContent();

        String applyPersonId = messageContent.getApplyPersonId();
        String applyPersonName = messageContent.getApplyPersonName();

        String adminId = messageContent.getAdminId();
        String adminName = messageContent.getAdminName();


        String inviterName = mUid.equals(adminId) ? mCt.getString(R.string.im_you) : adminName;


        String invitedName = mUid.equals(applyPersonId) ? mCt.getString(R.string.im_you) : applyPersonName;


        return String.format(mCt.getString(R.string.im_through_join_group), inviterName, invitedName);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {

        if(!saveFixGroup(msgEntity)){
            return false;
        }

        subscribeToTopic(msgEntity);
        return true;
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) throws Exception {
        return saveSingleMessage(msgEntity);
    }


    @Override
    public IMMessage transform(MsgEntity msgEntity) {
        MessageContent messageContent = msgEntity.getMsgContent();

        IMMessage imMessage = new IMMessage();
        imMessage.setContentType(IMMessage.CONTENT_MESSAGER_SYSTEM);
        imMessage.setTagertId(messageContent.getGroupId());
        imMessage.setSenderId(msgEntity.getParam().getSenderId());
        imMessage.setStatus(IMMessage.STATUS_SUCCESS);
        imMessage.setTime(Long.valueOf(msgEntity.getParam().getSendTime()));
        imMessage.setType(IMMessage.TYPE_GROUP);
        imMessage.setGroupName(messageContent.getGroupName());
        imMessage.setReceiverName(messageContent.getReceiverName());


        return imMessage;
    }

    /**
     * 存入讨论组
     *
     * @param msgEntity
     * @return
     */
    @Override
    public boolean saveFixGroup(MsgEntity msgEntity) {

        MessageContent messageContent = msgEntity.getMsgContent();

        return mDbManager.saveIMGroup(new IMGroup(messageContent.getGroupId(), messageContent.getGroupName()
                , String.valueOf(msgEntity.getParam().getSendTime()), String.valueOf(msgEntity.getParam().getSendTime())
                , IMMessage.STRING_TYPE_GROUP, IMGroup.TYPE_CLUSTER));
    }
}
