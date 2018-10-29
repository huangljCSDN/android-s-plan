package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.xsimple.im.db.DbManager;

/**
 * Created by pengpeng on 17/5/9.
 */

public class CmdGroupWithdrawalProcessor extends CmdSingleWithdrawalProcessor {
    public CmdGroupWithdrawalProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }
}
