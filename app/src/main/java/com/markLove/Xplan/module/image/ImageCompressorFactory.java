package com.markLove.Xplan.module.image;

/**
 * Created by Administrator on 2018/5/3.
 */

public class ImageCompressorFactory {
    private ImageCompressorFactory() {
    }

    private static ImageCompressor mCompressor = ImageCompressor.LUBAN;

    enum ImageCompressor {
        LUBAN
    }

    public static IImageCompressor getCompressor() {
        IImageCompressor compressor = null;
        switch (mCompressor) {
            case LUBAN:
                compressor = LubanImageCompressor.newInstance();
                break;
        }
        return compressor;
    }
}
