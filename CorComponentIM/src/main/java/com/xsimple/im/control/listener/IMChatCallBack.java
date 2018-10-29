package com.xsimple.im.control.listener;


import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMMessage;

/**
 * Created by liuhao on 2017/6/2.
 * 回调 ui
 */

public interface IMChatCallBack extends IMChatMessageStateListener, FileTransferListener, IMChatMessageSendStateListener {
    /**
     * 设置at 视图
     *
     * @param atString
     */
    void setAtView(String atString);

    /**
     * 显示听筒面板
     */
    void showVoiceHFOrHook();

    /**
     * 影藏听筒面板
     */
    void hideVoiceHFOrHook();

    /**
     * 播放吓下一条短语音
     *
     * @param posstion
     */
    void playNextMedia(int posstion);

    //改变页面状态，根据IMChatLogic中的字段决定是否为选择模式
    void changeSelectedMode(IMMessage imMessage, boolean isSelectedMode);

    /**
     * 展示groupremark
     *
     * @param imGroupRemark
     */
    void showGroupRemarkDialog(IMGroupRemark imGroupRemark);
    /**
     * 回复
     * @param imMessage
     */
    void onReply(IMMessage imMessage);

    /**
     * 滑动到指定的消息
     * @param msgId
     */
    void scrollToMsg(String msgId);

}
