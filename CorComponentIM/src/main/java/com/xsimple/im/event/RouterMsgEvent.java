package com.xsimple.im.event;

import com.networkengine.event.BaseEventBusAction;

/**
 * Created by liuhao on 2018/5/10.
 */

public class RouterMsgEvent extends BaseEventBusAction {

    private String localMsgId;

    public RouterMsgEvent(String localMsgId) {
        this.localMsgId = localMsgId;
    }

    public String getLocalMsgId() {
        return localMsgId;
    }

    public void setLocalMsgId(String localMsgId) {
        this.localMsgId = localMsgId;
    }
}
