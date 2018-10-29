package com.xsimple.im.db.datatable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liuhao on 2017/5/5.
 */
@Entity(nameInDb = "im_msg_call_new")
public class IMCallInfo {
    @Id(autoincrement = true)
    private Long id;
    /**
     * 通话时间
     */
    private long callTime;
    /**
     * 房间号
     */
    private String homeid;
    /**
     * 操作type
     */
    private String optionType;
    /**
     * 操作id
     */
    private String optionId;
    /**
     * 聊天类型
     * 0 单聊
     * 1 群聊
     * 跟IMMessage  的聊天类型不一样
     * IMMessage的聊天类型  代表 消息的类型
     * call 的type 表示在单聊的消息，但是，这条视频语音通话消息可能是群聊中发出给个人的通话消息
     * <p>
     * IMMessage的聊天类型   IMCallInfo的聊天类型  意义
     * <p>
     * 0  单聊               0                    单聊通话，消息类型的发送对象为单人
     * -                    1                    多人通话，但是消息类型的发送对象为单人
     * <p>
     * 1 群组               1                   多人通话，但是消息类型的发送对象为群组
     * <p>
     * 2 讨论组                1                   多人通话，但是消息类型的发送对象为讨论组
     */
    private int callType;

    /**
     * 对于 callType 为 1 的情况，需要设置group id
     */
    private String gid;
    /**
     * 附属消息
     */
    private String callMsg;










    @Generated(hash = 432824759)
    public IMCallInfo(Long id, long callTime, String homeid, String optionType,
            String optionId, int callType, String gid, String callMsg) {
        this.id = id;
        this.callTime = callTime;
        this.homeid = homeid;
        this.optionType = optionType;
        this.optionId = optionId;
        this.callType = callType;
        this.gid = gid;
        this.callMsg = callMsg;
    }
    @Generated(hash = 1244448181)
    public IMCallInfo() {
    }










    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getCallTime() {
        return this.callTime;
    }
    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }
    public String getHomeid() {
        return this.homeid;
    }
    public void setHomeid(String homeid) {
        this.homeid = homeid;
    }
    public String getOptionType() {
        return this.optionType;
    }
    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }
    public String getOptionId() {
        return this.optionId;
    }
    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }
    public int getCallType() {
        return this.callType;
    }
    public void setCallType(int callType) {
        this.callType = callType;
    }
    public String getGid() {
        return this.gid;
    }
    public void setGid(String gid) {
        this.gid = gid;
    }
    public String getCallMsg() {
        return this.callMsg;
    }
    public void setCallMsg(String callMsg) {
        this.callMsg = callMsg;
    }
}
