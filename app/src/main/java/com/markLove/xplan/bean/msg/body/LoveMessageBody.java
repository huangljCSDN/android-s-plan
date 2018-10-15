package com.markLove.xplan.bean.msg.body;


import com.markLove.xplan.bean.msg.Message;
import com.markLove.xplan.utils.DataUtils;

/**
 * Created by luoyunmin on 2017/7/13.
 */

public class LoveMessageBody extends MessageBody {

    private int status;

    public LoveMessageBody(int status) {
        this.status = status;
        setChatType(Message.ChatType.LOVE);
        setType(Message.Type.CHAT);
        setDateTime(DataUtils.getDatetime());
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
