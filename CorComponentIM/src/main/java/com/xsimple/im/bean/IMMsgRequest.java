package com.xsimple.im.bean;

import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.IMMsgRequestEntity;

/**
 * Created by liuhao on 2018/4/12.
 */

public class IMMsgRequest {

    private IMMsgRequestEntity mMsgRequestEntity;

    private IMMessage mIMessage;

    public IMMsgRequest(IMMsgRequestEntity mMsgRequestEntity, IMMessage mIMessage) {
        this.mMsgRequestEntity = mMsgRequestEntity;
        this.mIMessage = mIMessage;
    }

    public IMMsgRequestEntity getMsgRequestEntity() {
        return mMsgRequestEntity;
    }

    public IMMessage getIMessage() {
        return mIMessage;
    }
}
