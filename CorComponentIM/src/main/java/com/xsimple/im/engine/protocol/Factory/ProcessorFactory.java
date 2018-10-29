package com.xsimple.im.engine.protocol.Factory;

import android.content.Context;

import com.xsimple.im.db.DbManager;
import com.xsimple.im.engine.protocol.IProcessorParameter;
import com.xsimple.im.engine.protocol.processpr.Processor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengpeng on 17/5/4.
 */

public abstract class ProcessorFactory<K, Parameter extends IProcessorParameter<K>, Result> {


    Map<K, Processor<Parameter, Result>> mProcessorMap = new HashMap<>();

    DbManager mDbManager;

    Context mCt;

    public ProcessorFactory(Context mCt, DbManager mDbManager) {
        this.mCt = mCt;
        this.mDbManager = mDbManager;
        initProcessorMap(mProcessorMap);
    }

    public Processor<Parameter, Result> create(K key){
        return mProcessorMap.get(key);
    }

    public boolean canCreate(K key){
        return mProcessorMap.keySet().contains(key);
    }

    protected abstract void initProcessorMap(Map<K, Processor<Parameter, Result>> processorMap);
}
