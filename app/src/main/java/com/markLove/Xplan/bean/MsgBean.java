package com.markLove.Xplan.bean;

public class MsgBean {

    public long id;
    //消息类型，参考IMChat 里的类型
    public int type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MsgBean{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
