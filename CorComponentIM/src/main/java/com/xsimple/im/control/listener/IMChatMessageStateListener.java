package com.xsimple.im.control.listener;

import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.protocol.IMCommand;

import java.util.List;

/**
 * Created by liuhao on 2017/3/30.
 * 消息回调 ui
 */

public interface IMChatMessageStateListener  {
//    /**
//     * 消息
//     *
//     * @param msgs
//     */
//    void onMsgReceived(List<IMMessage> msgs);

    /**
     *
     * @param msgs
     */
    void onAddMessagerCallBack(List<IMMessage> msgs);

//    /**
//     * 命令消息
//     *
//     * @param instructions
//     */
//    void onOrderReceived(List<IMCommand> instructions);


    /**
     * 发送消息构建本地消息的回调
     *
     * @param message
     */
    void onAddMessagerCallBack(IMMessage message);

    /**
     * 删除消息
     *
     * @param message
     */
    void onDeleteMessageCallBack(IMMessage message);

    /**
     * 修改消息
     */
    void onUpdateMessageCallBack(long localId);

//    /**
//     * 修改消息 通过vid
//     *
//     * @param imMessage
//     */
//    void onUpdateMessageCallBackByVid(IMMessage imMessage);

    /**
     * 批量删除消息
     */
    void onDeleteMessagesCallback(List<IMMessage> imMessageList);


}
