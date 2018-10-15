package com.markLove.xplan.bean.msg.body;

import com.markLove.xplan.base.App;
import com.markLove.xplan.config.Constants;
import com.markLove.xplan.bean.msg.Message;
import com.markLove.xplan.utils.DataUtils;
import com.markLove.xplan.utils.PreferencesUtils;

/**
 * Created by luoyunmin on 2017/11/6.
 */

public class OrderMessageBody extends MessageBody {
    String userID;
    String orderType;//0甜蜜付，1支付成功回执,2好友代付
    String orderNumber;
    String orderMessage;

    public OrderMessageBody(Message.Type type, Message.ChatType chatType, String orderType, String orderNumber, String orderMessage) {
        this.orderType = orderType;
        this.orderNumber = orderNumber;
        this.orderMessage = orderMessage;
        this.userID = PreferencesUtils.getInt(App.getInstance(), Constants.ME_USER_ID) + "";
        setType(type);
        setChatType(chatType);
        setDateTime(DataUtils.getDatetime());
    }

    public String getOrderType() {
        return orderType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOrderMessage() {
        return orderMessage;
    }

    public String getUserID() {
        return userID;
    }
}
