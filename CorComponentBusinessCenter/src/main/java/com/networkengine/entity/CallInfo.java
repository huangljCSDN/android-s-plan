package com.networkengine.entity;

/**
 * Created by liuhao on 2017/5/5.
 */

public class CallInfo {

    private long callId;

    /**
     * 房间号
     */
    private String homeid;
    /**
     * 操作消息的id
     */
    private String optionId;
    /**
     * 通话消息的类型
     */
    private String optionType;

    /**
     * 通话信息的类型
     * 0 单人 1 多人
     */
    private int callType;
    /**
     * 讨论组，群组聊天的 group id
     */
    private String gid;


    public String getHomeid() {
        return homeid;
    }

    public void setHomeid(String homeid) {
        this.homeid = homeid;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }


    public long getCallId() {
        return callId;
    }

    public void setCallId(long callId) {
        this.callId = callId;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}
