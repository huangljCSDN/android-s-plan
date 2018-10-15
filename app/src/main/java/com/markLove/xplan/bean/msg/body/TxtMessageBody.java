package com.markLove.xplan.bean.msg.body;

import com.markLove.xplan.bean.msg.Message;
import com.markLove.xplan.utils.DataUtils;

/**
 * Created by luoyunmin on 2017/7/5.
 */

public class TxtMessageBody extends MessageBody {

    String msg;

    public TxtMessageBody(Message.Type type, Message.ChatType chatType, String msg) {
        this.msg = msg;
        setType(type);
        setChatType(chatType);
        setDateTime(DataUtils.getDatetime());
    }

    public String getMsg() {
        return msg;
    }
}
