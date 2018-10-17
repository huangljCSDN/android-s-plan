package com.markLove.Xplan.bean.msg.body;

import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.utils.DataUtils;

/**
 * Created by luoyunmin on 2017/7/5.
 */

public class LoginMessageBody extends MessageBody {

    String token;

    public LoginMessageBody(String token) {
        super();
        this.token = token;
        setType(Message.Type.LOGIN);
        setChatType(Message.ChatType.NULL);
        setDateTime(DataUtils.getDatetime());
    }

}
