package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.xsimple.im.db.DbManager;

/**
 * Created by pengpeng on 17/5/9.
 */

public class CmdGroupReadProcessor extends CmdSingleReadProcessor {
    public CmdGroupReadProcessor(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }
}
