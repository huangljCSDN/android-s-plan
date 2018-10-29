package com.xsimple.im.engine.protocol;

import com.networkengine.entity.IMSendMultipleResult;
import com.networkengine.entity.IMSendResultMultipleDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhao on 2018/6/21.
 */

public class IMSendResultMultipleEntity implements IProcessorParameter<String> {


    private List<IMSendResultEntity> mImSendResultEntities = new ArrayList<>();

    public IMSendResultMultipleEntity(List<Long> msgLocalId) {

        for (long id : msgLocalId) {
            mImSendResultEntities.add(new IMSendResultEntity(id));
        }

    }

    public IMSendResultMultipleEntity(List<Long> msgLocalId, IMSendMultipleResult sendResult) {
        if ("1".equals(sendResult.getCode())) {
            for (long id : msgLocalId) {
                mImSendResultEntities.add(new IMSendResultEntity(id));
            }
            return;
        }
        List<IMSendResultMultipleDetail> data = sendResult.getData();
        if (data == null || data.isEmpty()) {
            for (long id : msgLocalId) {
                mImSendResultEntities.add(new IMSendResultEntity(id));
            }
            return;
        }
        for (int i = 0; i < msgLocalId.size(); i++) {
            Long id = msgLocalId.get(i);
            IMSendResultMultipleDetail imSendResultMultipleDetail = data.get(i);
            IMSendResultEntity imSendResultEntity = new IMSendResultEntity(id, imSendResultMultipleDetail);
            mImSendResultEntities.add(imSendResultEntity);
        }
    }

    public List<IMSendResultEntity> getImSendResultEntities() {

        return mImSendResultEntities;
    }

    @Override
    public String getKey() {

        return IMSendResultEntity.IM_SEND_RESULT_KEY;
    }
}
