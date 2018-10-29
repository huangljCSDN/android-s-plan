package com.xsimple.im.control.iable;


import com.xsimple.im.db.datatable.IMMessage;

import java.util.List;

/**
 * Created by liuhao on 2017/3/30.
 * 用界面刷新
 */

public interface IMChatUiRefresh {

    void onRefreshItem(long localId);

    void onRefresfItemAdd(IMMessage imMessage);

    void onRefresfItemAddList(List<IMMessage> list);

   // void onRefreshItem(IMMessage oldT, IMMessage newT);

    void onRefreshItemDelete(IMMessage imMessage);

   // void onRefreshByVid(IMMessage imMessage);

    //void onRefreshItemByFileState(long localId);

    void onRefreshItemOnFileTransferLoading(long localId);

    void onRefreshClear();
}
