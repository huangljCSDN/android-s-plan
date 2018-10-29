package com.xsimple.im.engine.protocol;

import com.networkengine.entity.IMSendResult;
import com.networkengine.entity.IMSendResultDetail;
import com.networkengine.entity.IMSendResultMultipleDetail;

/**
 * Created by liuhao on 2017/5/10.
 */

public class IMSendResultEntity extends IMSendResult implements IProcessorParameter<String> {

    public static final String IM_SEND_RESULT_KEY = "IM_SEND_RESULT_KEY";


    public long localId;

    public IMSendResultEntity(long localId) {
        this.localId = localId;
    }


    public IMSendResultEntity(long localId, IMSendResult imSendResult) {
        this.localId = localId;
        setCode(imSendResult.getCode());
        setMsg(imSendResult.getMsg());
        setData(new IMSendResultDetail(imSendResult.getData().getSendTime(), imSendResult.getData().getVirtualMsgId()));
    }

    public IMSendResultEntity(long localId, IMSendResultMultipleDetail imSendResult) {
        this.localId = localId;
        setCode("0");
        setMsg("");
        setData(imSendResult);
    }


    @Override
    public String getKey() {
        return IM_SEND_RESULT_KEY;
    }
}
