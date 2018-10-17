package com.markLove.Xplan.module.image;

import android.app.Activity;

import java.util.List;


/**
 * Created by Administrator on 2018/5/31.
 */

public class ImageViewerManager {
    private static final ImageViewerManager ourInstance = new ImageViewerManager();

    public static ImageViewerManager getInstance() {
        return ourInstance;
    }

    private ImageViewerManager() {
    }

    public void launchImageViewer(Activity activity, List<String> urls, int position) {
//        launchImageViewer(activity, urls, position, false, null);
    }

//    public void launchImageViewer(Activity activity, List<String> urls, int position, boolean showShare, ImageViewer.OnShareListener listener) {
//        ImageViewer.from(activity)
//                .setUrls(urls)
//                .setIndex(position)
//                .setShowShare(showShare)
//                .setOnShareListener(listener)
//                .launch();
//    }

}
