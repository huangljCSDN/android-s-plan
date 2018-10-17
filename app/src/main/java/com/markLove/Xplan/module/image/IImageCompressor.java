package com.markLove.Xplan.module.image;

import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public interface IImageCompressor {
    IImageCompressor init(Context context, String destImageDir, String... srcImageFilePaths);

    IImageCompressor setOnImageCompressListener(OnImageCompressListener listener);

    void startCompress();

    void syncStartCompress();

    interface OnImageCompressListener {
        /**
         * 开始进行压缩
         *
         * @param msg
         */
        void onCompressStart(String msg);

        /**
         * 压缩完成
         *
         * @param destFilePaths
         */
        void onCompressComplete(List<String> destFilePaths);

        /**
         * 压缩出现错误
         *
         * @param msg
         */
        void onCompressError(String msg);
    }
}
