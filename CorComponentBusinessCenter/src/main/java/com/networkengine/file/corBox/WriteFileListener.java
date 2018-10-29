package com.networkengine.file.corBox;

/**
 * 外部调用的写入监听
 * Created by pengpeng on 16/12/28.
 */
public interface WriteFileListener {
    void onSuccess(String path, FileInfo fileInfo);

    void onFail();
}


