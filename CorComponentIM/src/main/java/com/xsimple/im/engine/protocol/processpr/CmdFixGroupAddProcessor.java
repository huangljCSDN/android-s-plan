package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;

import com.networkengine.entity.GroupMember;
import com.networkengine.entity.MessageContent;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdFixGroupAddProcessor extends CmdGroupProcessor {

    public CmdFixGroupAddProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        if (member == null || member.isEmpty())
            return "";

        MessageContent messageContent = msgEntity.getMsgContent();
        StringBuilder sb = new StringBuilder();
        for (GroupMember groupMember : member) {
            boolean isMyself = mUid.equals(groupMember.getLn());
            if (isMyself) {
                sb.insert(0, mCt.getString(R.string.im_you));
            } else {
                sb.append(groupMember.getUn() + "，");
            }
        }

        String inviterName = isInviter(msgEntity) ? mCt.getString(R.string.im_you) : messageContent.getAdminName();
        String result = sb.toString().trim();
        String memberNames = result.substring(0, result.length() - 1);

        if (inviterName.equals(memberNames)){
            if(mDbManager.getUser(mUid).getName().equals(member.get(0).getLn())){
                return mCt.getString(R.string.im_you_scan_join_group);
            }else {
                return inviterName + mCt.getString(R.string.im_scan_code_join_group);
            }
        }

        if (TextUtils.isEmpty(inviterName)){
            inviterName = "admin";
        }

        return String.format(mCt.getString(R.string.im_invite_join_group), inviterName, memberNames);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) throws Exception {
        if (!saveFixGroup(msgEntity)) {
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
    public boolean isInviter(MsgEntity msgEntity) {
        MessageContent messageContent = msgEntity.getMsgContent();
        return mUid.equals(messageContent.getAdminId());
    }

}
