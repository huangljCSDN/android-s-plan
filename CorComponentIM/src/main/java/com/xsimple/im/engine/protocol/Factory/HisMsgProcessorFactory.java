package com.xsimple.im.engine.protocol.Factory;

import android.content.Context;

import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.MsgEntity;
import com.xsimple.im.engine.protocol.processpr.HisMsgProcessor;
import com.xsimple.im.engine.protocol.processpr.Processor;

import java.util.Map;

/**
 * Created by pengpeng on 17/5/4.
 */

public class HisMsgProcessorFactory extends ProcessorFactory<String, MsgEntity, IMMessage> {

    public HisMsgProcessorFactory(Context mCt, DbManager mDbManager) {
        super(mCt, mDbManager);
    }

    @Override
    protected void initProcessorMap(Map<String, Processor<MsgEntity, IMMessage>> processorMap) {

        HisMsgProcessor msgProcessor = new HisMsgProcessor(mCt, mDbManager);
        processorMap.put(IMMessage.CONTENT_TYPE_TXT, msgProcessor);
        processorMap.put(IMMessage.CONTENT_TYPE_FUN, msgProcessor);
        processorMap.put(IMMessage.CONTENT_TYPE_REPLY, msgProcessor);
        processorMap.put(IMMessage.CONTENT_TYPE_RECORD, msgProcessor);
        processorMap.put(IMMessage.CONTENT_TYPE_IMG, msgProcessor);
        processorMap.put(IMMessage.CONTENT_TYPE_FILE, msgProcessor);
        processorMap.put(IMMessage.CONTENT_TYPE_MAP, msgProcessor);
        processorMap.put(IMMessage.CONTENT_TYPE_SHORT_VOICE, msgProcessor);
        processorMap.put(IMMessage.CONTENT_TYPE_VIDEO, msgProcessor);

        //  processorMap.put(IMMessage.CONTENT_TYPE_VOICE_CHAT, msgProcessor);
        //processorMap.put(IMMessage.CONTENT_TYPE_VIDEO_CHAT, msgProcessor);
        //  processorMap.put(IMMessage.CONTENT_TYPE_REJECT, msgProcessor);
        //  processorMap.put(IMMessage.CONTENT_TYPE_CANCEL, msgProcessor);

    }

    @Override
    public boolean canCreate(String key) {
        return super.canCreate(key);
    }


}
