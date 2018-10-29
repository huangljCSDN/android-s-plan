package com.xsimple.im.control.listener;

import com.xsimple.im.db.datatable.IMMessage;

/**
 * Created by liuhao on 2017/4/19.
 * 聊天界面菜单点击
 */

public interface ChatMenuClickListener {
    /**
     * 复制
     */
    void onCopyMessage(IMMessage imMessage);

    /**
     * 转发
     */
    void onTransmitMessage(IMMessage imMessage);

    /**
     * 收藏
     */
    void onFavoritesmessage(IMMessage imMessage);

    /**
     * 撤回
     */
    void onRevocationMessage(IMMessage imMessage);

    /**
     * 删除
     */
    void onDeleteMessage(IMMessage imMessage);

    /**
     * 删除附件
     *
     * @param fid
     */
    void onDeleteFile(Long fid);

    /**
     * 多选
     */
    void onChooserMoreMessage(IMMessage imMessage, boolean flag);

    /**
     * 添加表情
     */
    void onChooserAddExpress(IMMessage imMessage);

    /**
     * 回复
     */
    void onReply(IMMessage imMessage);

    /**
     * 分享
     */
    void onShare(IMMessage imMessage);
}
