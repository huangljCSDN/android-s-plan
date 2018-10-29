package com.xsimple.im.event;

import com.networkengine.event.BaseEventBusAction;

/**
 * Created by liuhao on 2018/4/25.
 */

public class IMGroupFileEvent extends BaseEventBusAction {

    private int event;

    public IMGroupFileEvent(int event) {
        this.event = event;
    }

    public int getEvent() {
        return event;
    }
}
