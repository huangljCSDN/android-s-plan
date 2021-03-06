package com.markLove.Xplan.mvp.contract;


import com.markLove.Xplan.bean.msg.Message;

import java.util.List;

/**
 * Created by luoyunmin on 2017/6/29.
 */

public interface ChatView extends LoadDataView {

    void showHistoryMessage(List<Message> historyMessageList);

    void addOneMessage(Message msg);

    void updataMessage(String msgID, int errorCode);
}
