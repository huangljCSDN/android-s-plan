package com.xsimple.im.control.listener;


/**
 * Created by liuhao on 2017/4/7.
 * 文件传输的回调
 */

public interface FileTransferListener {

    /**
     * 回调进度
     *
     * @param localId
     */
    void onFileTransferLoading(long localId);

    /**
     * 传输成功
     *
     * @param localId
     */
    void onFileTransferSuccess(long localId);

    /**
     * 传输失败
     *
     * @param localId
     */
    void onFileTransferFailed(long localId);

    /**
     * 传输暂停
     *
     * @param localId
     */
    void onFileTransferOnPause(long localId);

    /**
     * 开始传输
     *
     * @param localId
     */
    void onFileTransferOnStart(long localId);
}
