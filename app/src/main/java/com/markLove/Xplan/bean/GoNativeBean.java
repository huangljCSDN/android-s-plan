package com.markLove.Xplan.bean;

import java.io.Serializable;

public class GoNativeBean implements Serializable {


    /**
     * callFun : refreshBureauList
     * param : 2
     */
    private String callFun;
    private int param;
    //2表示退出聊天室
    private int closeView;

    public int getCloseView() {
        return closeView;
    }

    public void setCloseView(int closeView) {
        this.closeView = closeView;
    }

    public void setCallFun(String callFun) {
        this.callFun = callFun;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public String getCallFun() {
        return callFun;
    }

    public int getParam() {
        return param;
    }
}
