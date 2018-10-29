package com.networkengine.file.corBox;


import java.io.File;

/**
 * 外部调用的读取监听
 * Created by pengpeng on 16/12/28.
 */
public interface ReadFileListener {
    void onSuccess(File file);

    void onFail();
}