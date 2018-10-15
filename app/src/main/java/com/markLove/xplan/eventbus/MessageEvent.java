package com.markLove.xplan.eventbus;


import com.markLove.xplan.bean.msg.Message;

/**
 * Created by luoyunmin on 2017/7/3.
 */

public class MessageEvent {
    private Message msg;

    public MessageEvent(Message msg) {
        this.msg = msg;
    }

    public Message getMsg() {
        return msg;
    }
}
