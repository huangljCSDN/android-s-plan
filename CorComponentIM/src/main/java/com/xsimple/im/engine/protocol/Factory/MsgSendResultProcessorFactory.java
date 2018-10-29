package com.xsimple.im.engine.protocol.Factory;

import android.content.Context;

import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.IMMsgRequestEntity;
import com.xsimple.im.engine.protocol.IMSendResultEntity;
import com.xsimple.im.engine.protocol.processpr.MsgParameterProcessor;
import com.xsimple.im.engine.protocol.processpr.MsgResultProcessor;
import com.xsimple.im.engine.protocol.processpr.Processor;

import java.util.Map;

/**
 * Created by liuhao on 2017/5/9.
 */

public class MsgSendResultProcessorFactory extends ProcessorFactory<String, IMSendResultEntity, IMMessage> {


    public MsgSendResultProcessorFactory(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected void initProcessorMap(Map<String, Processor<IMSendResultEntity, IMMessage>> processorMap) {

        MsgResultProcessor msgProcessor = new MsgResultProcessor(mCt, mDbManager);

        processorMap.put(IMSendResultEntity.IM_SEND_RESULT_KEY, msgProcessor);

    }

    @Override
    public boolean canCreate(String key) {
        return super.canCreate(key);
    }
}
