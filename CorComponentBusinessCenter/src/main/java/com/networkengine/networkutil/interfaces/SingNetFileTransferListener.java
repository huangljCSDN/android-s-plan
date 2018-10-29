package com.networkengine.networkutil.interfaces;


import com.networkengine.entity.FileSubPackage;

/**
 * Created by liuhao on 2017/4/7.
 * 文件传输的回调
 * 单线程的回掉
 */

public interface SingNetFileTransferListener {

    /**
     * 回调进度
     *
     * @param
     */
    void onFileTransferLoading(FileSubPackage packages);

    /**
     * 传输成功
     *
     * @param
     */
    void onFileTransferSuccess(FileSubPackage packages);

    /**
     * 传输失败
     *
     * @param
     */
    void onFileTransferFailed(FileSubPackage packages);

}
