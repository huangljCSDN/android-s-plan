package com.networkengine.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by pengpeng on 17/4/6.
 */

public class MsgNotice {
    private String virtualMsgId;
    private String totalNumber;
    private String unReadNumber;

    public String getVirtualMsgId() {
        return this.virtualMsgId;
    }

    public void setVirtualMsgId(String virtualMsgId) {
        this.virtualMsgId = virtualMsgId;
    }

    public String getTotalNumber() {
        return this.totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getUnReadNumber() {
        return this.unReadNumber;
    }

    public void setUnReadNumber(String unReadNumber) {
        this.unReadNumber = unReadNumber;
    }

}
