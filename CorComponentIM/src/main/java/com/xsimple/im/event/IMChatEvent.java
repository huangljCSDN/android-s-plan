package com.xsimple.im.event;

import com.networkengine.event.BaseEventBusAction;

/**
 * Created by liuhao on 2017/5/23.
 */

public class IMChatEvent extends BaseEventBusAction {

    private int mCode;

    private String sessionType;

    private String sessionId;

    /**
     * 添加添加成员
     */
    public static final int ADD_GROUP_MEMBER = 1000;

    /**
     * 移除成员
     */
    public static final int REMOVE_GROUP_MEMBER = 1001;
    /**
     * 别人移除我
     */
    public static final int REMOVE_GEOUP_MYSELF = 1002;

    /**
     * 修改群名称
     */
    public static final int UPDATE_GROPU_NAME = 1003;

    /**
     * 群解散
     */
    public static final int GROUP_DISSOLVE = 1004;

    public static final int GROUP_UPDATE_MANAGER = 1005;

    public IMChatEvent() {

    }

    public IMChatEvent(int code) {
        this.mCode = code;
    }

    public IMChatEvent(int code, String sessionId) {
        this.mCode = code;
        this.sessionId = sessionId;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int mCode) {
        this.mCode = mCode;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
