package com.markLove.xplan.module.image;

import android.content.Context;
import android.util.Log;

import com.markLove.xplan.utils.MeUtils;

import java.io.File;

/**
 * Created by Administrator on 2018/5/3.
 */

public class BaseImageCompressor implements IImageCompressor {
    private static final String TAG = "BaseImageCompressor";
    protected Context mContext;
    protected String mDestImageDir;
    protected String[] mSrcImageFilePaths;
    protected OnImageCompressListener mListener;

    protected BaseImageCompressor() {
    }

    @Override
    public IImageCompressor init(Context context, String destImageDir, String... srcImageFilePaths) {
        mContext = context;
        mDestImageDir = destImageDir;
        File destFileDir = new File(mDestImageDir);
        if (!destFileDir.exists()) {
            destFileDir.mkdirs();
        }
        mSrcImageFilePaths = srcImageFilePaths;
        return this;
    }

    @Override
    public IImageCompressor setOnImageCompressListener(OnImageCompressListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void startCompress() {
        checkParams();
    }

    private void checkParams() {
        if (mListener == null) {
            throw new RuntimeException("请设置图片压缩的监听OnImageCompressListener");
        }

        if (mContext == null) {
            if (mListener != null) {
                Log.e(TAG, "Context不能为空");
                String msg = "未进行初始化";
                mListener.onCompressError(msg);
            }
        }

        if (MeUtils.stringsIsEmpty(mSrcImageFilePaths)) {
            if (mListener != null) {
                String msg = "压缩源文件的路径不能为空";
                mListener.onCompressError(msg);
            }
        }
    }

    @Override
    public void syncStartCompress() {
        checkParams();
    }
}
