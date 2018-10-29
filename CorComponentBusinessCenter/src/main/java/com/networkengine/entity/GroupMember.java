package com.networkengine.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * author panxiaoan
 * date 2017/4/6.
 * desc 创建或者邀请人进入讨论组，消息中会有一个mems字段，里面包含被邀请人列表，单个成员用GroupMember表示
 */
public class GroupMember {
    //我TM也不知道这个字段什么意思。。。后端就这么传过来的
    protected String ln;
    //用户名
    private String un;

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

}
