package com.xsimple.im.engine.protocol;

import com.networkengine.entity.GetMsgsEntity;

/**
 * Created by pengpeng on 17/5/5.
 */

public class MsgEntity extends GetMsgsEntity implements IProcessorParameter<String> {
    /**
     * 是否是历史消息
     */
    private boolean isHistoryMsg = false;

    public MsgEntity(GetMsgsEntity orgMsgEntity) {
//        this.setMId(orgMsgEntity.getMId());
//        this.setIsDisturb(orgMsgEntity.getIsDisturb());
//        this.setMsgID(orgMsgEntity.getMsgID());
//        this.setMsgVersion(orgMsgEntity.getMsgVersion());
//        this.setSenderId(orgMsgEntity.getSenderId());
//        this.setSendTime(orgMsgEntity.getSendTime());
//        this.setVirtualMsgId(orgMsgEntity.getVirtualMsgId());
        this.setParam(orgMsgEntity.getParam());
        this.setMsgContent(orgMsgEntity.getMsgContent());
    }

    public MsgEntity(GetMsgsEntity orgMsgEntity, boolean isHistoryMsg) {
        this(orgMsgEntity);
        this.isHistoryMsg = isHistoryMsg;
    }


    @Override
    public String getKey() {
        return getMsgContent().getType();
    }

    public boolean isHistoryMsg() {
        return isHistoryMsg;
    }

    public void setHistoryMsg(boolean historyMsg) {
        isHistoryMsg = historyMsg;
    }
}
