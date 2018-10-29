package com.xsimple.im.engine.protocol.processpr;

import android.content.Context;

import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.IMEngine;

import java.util.List;

/**
 * Created by pengpeng on 17/5/4.
 */

public abstract class Processor<Parameter, Result> {

    protected Context mCt;

    protected DbManager mDbManager;

    protected String mUid = IMEngine.getInstance(mCt).getMyId();

    public Processor(Context mCt, DbManager mDbManager) {
        this.mCt = mCt;
        this.mDbManager = mDbManager;
    }

//    public abstract IMMessage transform(Parameter parameter);

    public abstract Result process(Parameter parameter) throws Exception;

    public abstract List<Result> processList(List<Parameter> parameterList) throws Exception;


    public int fixGroupOrGroup(String type) {

        if (type.startsWith("FIX")) {
            return IMMessage.TYPE_GROUP;
        }

        return IMMessage.TYPE_DISCUSS;
    }

}
