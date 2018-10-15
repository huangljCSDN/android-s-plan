package com.markLove.xplan.bean.msg.body;

import com.markLove.xplan.bean.msg.Message;
import com.markLove.xplan.utils.DataUtils;

/**
 * Created by luoyunmin on 2017/10/12.
 */

public class DescriptionMessageBody extends MessageBody {

    String fileName;

    public DescriptionMessageBody(Message.Type type, Message.ChatType chatType, String fileName) {
        this.fileName = fileName;
        setType(type);
        setChatType(chatType);
        setDateTime(DataUtils.getDatetime());
    }
}
