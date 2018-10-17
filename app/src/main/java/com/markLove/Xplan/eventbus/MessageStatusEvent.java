package com.markLove.Xplan.eventbus;

/**
 * Created by luoyunmin on 2017/8/15.
 */

public class MessageStatusEvent {
    private String msgID;
    private int status;

    public MessageStatusEvent(String msgID, int status) {
        this.msgID = msgID;
        this.status = status;
    }

    public int getState() {
        return status;
    }

    public String getMsgID() {
        return msgID;
    }
}
