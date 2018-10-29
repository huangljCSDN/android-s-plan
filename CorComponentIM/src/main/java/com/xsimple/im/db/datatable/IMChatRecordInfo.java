package com.xsimple.im.db.datatable;

import com.xsimple.im.db.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 聊天记录
 */
@Entity(nameInDb = "im_msg_chat_record_info_new")
public class IMChatRecordInfo {

    @Id(autoincrement = true)
    private Long rId;
    /**
     * 标题
     */
    private String title;
    /**
     * 未展开详情时显示的内容
     */
    private String content;
    /**
     * 合并转发人的ID，显示时后台需要从该人的表里查消息
     */
    private String receiverId;
    /**
     * 消息virtualMsgId
     */
    @Convert(columnType = String.class, converter = StringConverter.class)
    private ArrayList<String> msgIds;
    @Generated(hash = 353872134)
    public IMChatRecordInfo(Long rId, String title, String content,
            String receiverId, ArrayList<String> msgIds) {
        this.rId = rId;
        this.title = title;
        this.content = content;
        this.receiverId = receiverId;
        this.msgIds = msgIds;
    }
    @Generated(hash = 369718145)
    public IMChatRecordInfo() {
    }
    public Long getRId() {
        return this.rId;
    }
    public void setRId(Long rId) {
        this.rId = rId;
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
    public ArrayList<String> getMsgIds() {
        return this.msgIds;
    }
    public void setMsgIds(ArrayList<String> msgIds) {
        this.msgIds = msgIds;
    }
    public String getReceiverId() {
        return this.receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }


}
