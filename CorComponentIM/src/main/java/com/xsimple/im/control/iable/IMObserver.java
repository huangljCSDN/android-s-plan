package com.xsimple.im.control.iable;

import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.db.datatable.IMMessage;

import java.util.List;

/**
 * Created by pengpeng on 17/3/30.
 */

public interface IMObserver {

    void onMsgReceived(List<IMMessage> msgs);

    void onOrderReceived(List<IMCommand> orders);

}
