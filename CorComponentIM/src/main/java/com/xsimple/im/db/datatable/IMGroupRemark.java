package com.xsimple.im.db.datatable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liuhao on 2018/5/2.
 */
@Entity(nameInDb = "im_msg_group_remark_new")
public class IMGroupRemark {


    @Id(autoincrement = true)
    private Long rId;

    /**
     * 公告的所属群组
     */
    private String groupId;
    /**
     * 公告所属的群组的名字
     */
    private String groupName;

    /**
     * 表所属用户id
     */
    private String uId;
    /**
     * 公告服务器id
     */
    private String id;

    /**
     * 公告创建者id
     */
    private String userId;
    /**
     * 公告创建者
     */
    private String createName;
    /**
     * 公告title
     */
    private String title;
    /**
     * 公告内容
     */
    private String content;
    /**
     * 公告创建时间
     */
    private String createDatetime;
    /**
     * 公告创建时间 long 用于排序
     */
    private long sendTime;
    /**
     * 是否已读
     */
    private boolean read;



    @Generated(hash = 497100252)
    public IMGroupRemark() {
    }

    @Generated(hash = 238416443)
    public IMGroupRemark(Long rId, String groupId, String groupName, String uId,
            String id, String userId, String createName, String title,
            String content, String createDatetime, long sendTime, boolean read) {
        this.rId = rId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.uId = uId;
        this.id = id;
        this.userId = userId;
        this.createName = createName;
        this.title = title;
        this.content = content;
        this.createDatetime = createDatetime;
        this.sendTime = sendTime;
        this.read = read;
    }

    public Long getRId() {
        return this.rId;
    }

    public void setRId(Long rId) {
        this.rId = rId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUId() {
        return this.uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateName() {
        return this.createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public boolean getRead() {
        return this.read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }


}
