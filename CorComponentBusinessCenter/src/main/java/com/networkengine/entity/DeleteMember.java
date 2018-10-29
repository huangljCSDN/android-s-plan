package com.networkengine.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author panxiaoan
 * date 2017/4/6.
 */
public class DeleteMember {
    private String ln;
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
