package com.xsimple.im.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.networkengine.util.LogUtil;

/**
 * Created by liuhao on 2017/4/14.
 */

public class ThumbnailUtils {
    /**
     * @param videoPath    视频文件地址
     * @param thumbnailath 缩略图地址
     * @param listner      回调
     */
    public static void getThumbnail(String videoPath, String thumbnailath, final GetThumbnailListner listner) {
        new ThumbnailTask() {
            @Override
            protected void onPostExecute(Boolean boo) {
                if (listner != null) {
                    listner.onThumbnail(boo);
                }
            }
        }.execute(videoPath, thumbnailath);
    }

    /**
     * @param videoPath
     * @return
     */
    private static Bitmap getVideoThumbnail(String videoPath) {
        if (TextUtils.isEmpty(videoPath)) {
            return null;
        }
        if (!new File(videoPath).exists()) {
            return null;
        }
        try {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(videoPath);
            Bitmap bitmap = media.getFrameAtTime();
            return bitmap;
        } catch (Exception e) {
            LogUtil.e("getVideoThumbnail >>> " + e.getMessage());
        }

        return null;
    }

    /**
     * 工作线程
     */
    static class ThumbnailTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            if (TextUtils.isEmpty(params[0]) || TextUtils.isEmpty(params[1])) {
                return false;
            }
            File file = new File(params[0]);
            if (!file.exists()) {
                return false;
            }
            Bitmap videoThumbnail = getVideoThumbnail(params[0]);
            if (videoThumbnail == null)
                return false;
            File f = new File(params[1]);

            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
                videoThumbnail.compress(Bitmap.CompressFormat.PNG, 90, fOut);
                fOut.flush();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (fOut != null) {
                        fOut.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface GetThumbnailListner {
        void onThumbnail(Boolean boo);
    }
}
