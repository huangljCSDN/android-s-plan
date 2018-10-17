package com.markLove.Xplan.bean.msg.body;

import com.markLove.Xplan.base.App;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.PreferencesUtils;

/**
 * Created by luoyunmin on 2017/7/5.
 */
public class MessageBody {
    String userName;
    Message.Type type;
    Message.ChatType chatType;
    String dateTime;

    public MessageBody() {
        userName = PreferencesUtils.getString(App.getInstance(), Constants.NICK_NAME_KEY, "");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Message.Type getType() {
        return type;
    }

    public void setType(Message.Type type) {
        this.type = type;
    }

    public Message.ChatType getChatType() {
        return chatType;
    }

    public void setChatType(Message.ChatType chatType) {
        this.chatType = chatType;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    //确定消息内容的长度
    public int getLength() {
        return toString().getBytes().length;
    }

    //将消息内容转化成byte数组
    public byte[] toBytes() {
        return toString().getBytes();
    }

    @Override
    public String toString() {
        return GsonUtils.obj2Json(this);
    }
}
