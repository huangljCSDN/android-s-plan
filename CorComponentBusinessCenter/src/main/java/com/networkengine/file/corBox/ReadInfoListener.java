package com.networkengine.file.corBox;

import java.util.List;

/**
 * Created by pengpeng on 16/12/28.
 */

public interface ReadInfoListener {
    void onSuccess(List<FileInfo> fileInfos);

    void onFail();
}