package com.networkengine.entity;

import java.util.List;

/**
 * Created by liuhao on 2018/6/20.
 */

public class MsgRequestMultipleEntity {

    protected List<MsgRequestEntity> msgList;

    public List<MsgRequestEntity> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MsgRequestEntity> msgList) {
        this.msgList = msgList;
    }
}
