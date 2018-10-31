package com.networkengine.database.entity;

/**
 * 作者：created by huanglingjun on 2018/10/31
 * 描述：
 */
public class IMMessageBean {
    public MsgContent msgContent;
    public int group_id;
    public int sender;

    public IMMessageBean(MsgContent msgContent, int group_id, int sender) {
        this.msgContent = msgContent;
        this.group_id = group_id;
        this.sender = sender;
    }

    public MsgContent getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(MsgContent msgContent) {
        this.msgContent = msgContent;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "IMMessageBean{" +
                "msgContent=" + msgContent +
                ", group_id=" + group_id +
                ", sender=" + sender +
                '}';
    }
}
