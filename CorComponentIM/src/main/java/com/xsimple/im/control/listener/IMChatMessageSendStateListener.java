package com.xsimple.im.control.listener;

/**
 * Created by liuhao on 2018/4/13.
 * 消息发送接受状态回调函数
 */

public interface IMChatMessageSendStateListener {
    /**
     * 发送消息成功回调
     *
     * @param localId
     */
    void onSendMessageSuccessCallBack(long localId);

    /**
     * 发送失败回调
     *
     * @param localId
     */
    void onSendMessageFaileCallBack(long localId);
}
