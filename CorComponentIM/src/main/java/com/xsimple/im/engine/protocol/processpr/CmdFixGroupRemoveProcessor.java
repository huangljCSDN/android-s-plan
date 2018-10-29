package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;

import com.networkengine.entity.GroupMember;
import com.networkengine.entity.MessageContent;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdFixGroupRemoveProcessor extends CmdGroupRemoveProcessor {

    public CmdFixGroupRemoveProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {
        if (member == null || member.isEmpty())
            return "";

        MessageContent content = msgEntity.getMsgContent();
        StringBuilder sb = new StringBuilder();
        for (GroupMember groupMember : member) {
            boolean isMyself = mUid.equals(groupMember.getLn());
            if (isMyself) {
                sb.insert(0, mCt.getString(R.string.im_you));
            } else {
                sb.append(groupMember.getUn() + "，");
            }
        }

        String tmpRemover = TextUtils.isEmpty(content.getMasterName()) ? content.getAdminName() : content.getMasterName();
        String remover = isInviter(msgEntity) ? mCt.getString(R.string.im_you): tmpRemover;
        String result = sb.toString().trim();
        String memberNames = result.substring(0, result.length() - 1);

        return String.format(mCt.getString(R.string.im_remove_group), remover, memberNames);
    }

    @Override
    public boolean isInviter(MsgEntity msgEntity) {
        MessageContent messageContent = msgEntity.getMsgContent();
        return mUid.equals(messageContent.getAdminId());
    }
}
