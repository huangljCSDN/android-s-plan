package com.markLove.Xplan.bean.msg.body;

import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.utils.DataUtils;

/**
 * Created by luoyunmin on 2017/8/8.
 */

public class GiftMessageBody extends MessageBody {
    private int giftId;

    public GiftMessageBody(int giftID, Message.ChatType chatType, Message.Type type) {
        this.giftId = giftID;
        setChatType(chatType);
        setType(type);
        setDateTime(DataUtils.getDatetime());
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }
}
