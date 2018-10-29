package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.xsimple.im.db.DbManager;

/**
 * Created by pengpeng on 17/5/8.
 */

public class CmdFixGroupAdminProcessor extends CmdGroupAdminProcessor {
    public CmdFixGroupAdminProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }
}
