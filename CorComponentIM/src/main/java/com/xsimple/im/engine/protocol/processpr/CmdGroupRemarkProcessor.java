package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.networkengine.entity.GroupMember;
import com.networkengine.entity.MessageContent;
import com.networkengine.entity.MessageParam;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMGroup;
import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdGroupRemarkProcessor extends CmdGroupProcessor {

    public CmdGroupRemarkProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {

        String inviterName = isInviter(msgEntity) ? mCt.getString(R.string.im_you) : msgEntity.getMsgContent().getCreateName();

        return String.format(mCt.getString(R.string.im_change_affiche), inviterName);
    }

    @Override
    protected void setContentType(IMMessage imMessage, MsgEntity msgEntity) {
        imMessage.setContentType(IMMessage.CONTENT_TYPE_GROUP_REMARK);
    }

    @Override
    protected boolean saveExpandMessage(IMMessage imMessage, MsgEntity msgEntity) {
        MessageContent msgContent = msgEntity.getMsgContent();
        IMGroupRemark imGroupRemark = new IMGroupRemark();
        //  imGroupRemark.setContent(StringUtil.unicode2String(msgContent.getContent()));
        imGroupRemark.setContent(msgContent.getContent());
        // imGroupRemark.setTitle(StringUtil.unicode2String(msgContent.getTitle()));
        imGroupRemark.setTitle(msgContent.getTitle());
        imGroupRemark.setCreateDatetime(msgContent.getCreateDatetime());
        imGroupRemark.setSendTime(Long.valueOf(msgEntity.getParam().getSendTime()));
        imGroupRemark.setCreateName(msgContent.getCreateName());
        imGroupRemark.setGroupId(msgContent.getGroupId());
        imGroupRemark.setGroupName(msgContent.getGroupName());
        imGroupRemark.setUId(mUid);
        imGroupRemark.setUserId(msgEntity.getParam().getSenderId());
        if (msgEntity.isHistoryMsg()) {
            imGroupRemark.setRead(true);
        }
        long rId = mDbManager.insertOrReplaceIMGroupRemark(imGroupRemark);
        imMessage.setRId(rId);
        return true;
    }

    @Override
    public boolean isInviter(MsgEntity msgEntity) {
        MessageParam param = msgEntity.getParam();
        return mUid.equals(param.getSenderId());
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {

        if (!updateGroupRemark(msgEntity)) {
            return false;
        }

        return true;
    }

    @Override
    protected IMCommand processMessage(MsgEntity msgEntity) throws Exception {
        return saveSingleMessage(msgEntity);
    }

    /**
     * 修改讨论组公告
     *
     * @param msgEntity
     * @return
     */
    private boolean updateGroupRemark(MsgEntity msgEntity) {
        if (msgEntity.isHistoryMsg()) {
            return true;
        }
        String remark = msgEntity.getMsgContent().getTitle();

        IMGroup imGroup = getGroup(msgEntity);
        if (imGroup == null) {
            return false;
        }
        imGroup.setRemark(remark);
        return updateGroup(imGroup);
    }
}
