package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.networkengine.entity.GroupMember;
import com.xsimple.im.R;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.engine.protocol.MsgEntity;

import java.util.List;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdFixGroupUpdateProcessor extends CmdGroupUpdateProcessor {
    public CmdFixGroupUpdateProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected String getMsgContent(MsgEntity msgEntity, List<GroupMember> member) {

        String groupName = getGname(msgEntity);
        if (TextUtils.isEmpty(groupName)) {
            groupName = "";
        }
        return String.format("%s%s", mCt.getString(R.string.im_change_group_name), groupName);
    }
}
