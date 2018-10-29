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
 * Created by pengpeng on 17/5/5.
 */

public class CmdGroupAddProcessor extends CmdGroupProcessor {

    public CmdGroupAddProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member){
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
        String inviterName = isInviter(msgEntity) ? mCt.getString(R.string.im_you): messageContent.getInviter_name();
        String result = sb.toString().trim();
        String memberNames = result.substring(0, result.length() - 1);

        try{
            if (inviterName.equals(memberNames)){
                if(mDbManager.getUser(mUid).getName().equals(member.get(0).getLn())){
                    return mCt.getString(R.string.im_you_scan_join_discussion_group);
                }else {
                    return inviterName + mCt.getString(R.string.im_scan_code_join_discussion_group);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return String.format(mCt.getString(R.string.im_invite_join_discussion_group), inviterName, memberNames);
    }

    @Override
    protected boolean processChatAndGroup(MsgEntity msgEntity) {
        if(!saveGroup(msgEntity)){
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
