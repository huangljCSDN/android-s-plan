package com.networkengine.event;

/**
 * Created by liuhao on 2017/6/8.
 * 修改会话tab 的数量
 */

public class UpdateChatTabMsgCount extends BaseEventBusAction {

    public static final int ADD_ACTION = 0;

    public static final int SET_COUNT_ACTION = 1;

    public static final int SET_EXTENSION_COUNT_ACTION = 2;

    private int action;

    private int count;

    public UpdateChatTabMsgCount(int action, int count) {
        this.action = action;
        this.count = count;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
