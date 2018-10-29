package com.xsimple.im.manager.photo;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.xsimple.im.bean.DirectoryModel;
import com.xsimple.im.bean.PhotoModel;
import com.xsimple.im.utils.FilePathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * Created by lh on 17/4/12.
 */
public class MediaStoreHelper {


    public static void getPhotoDirs(Activity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    /**
     * 加载图片
     *
     * @param activity
     * @param args
     * @param resultCallback
     */
    public static void getPhotoDirs(FragmentActivity activity, Bundle args, MediaStoreHelper.PhotosResultCallback resultCallback) {
        activity.getLoaderManager()
                .initLoader(0, args, new MediaStoreHelper.PhotoDirLoaderCallbacks(activity, resultCallback));
    }


    /**
     * 加载图片
     */
    private static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private PhotosResultCallback resultCallback;

        public PhotoDirLoaderCallbacks(Context context, PhotosResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoModelLoader(context);
        }


        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;

            List<DirectoryModel> directories = new ArrayList<>();


            while (data.moveToNext()) {

                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long size = data.getInt(data.getColumnIndexOrThrow(SIZE));
                if (size < 1) {
                    continue;
                }
                if (!FilePathUtils.fileIsExists(path)) {
                    continue;
                }
                File file = new File(path);

                String parentName = file.getParentFile().getName();

                PhotoModel PhotoModel = new PhotoModel();

                DirectoryModel directoryModel = new DirectoryModel();

                directoryModel.setCoverPath(path);
                directoryModel.setName(parentName);
                directoryModel.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                directoryModel.setId(bucketId);

                PhotoModel.setId(imageId);
                PhotoModel.setOriginalPath(path);
                PhotoModel.setSize(size);
                PhotoModel.setName(name);

                if (!directories.contains(directoryModel)) {
                    directoryModel.addPhoto(PhotoModel);
                    directories.add(directoryModel);
                } else {
                    directories.get(directories.indexOf(directoryModel)).addPhoto(PhotoModel);
                }

            }

            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
            data.close();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    public interface PhotosResultCallback {
        void onResultCallback(List<DirectoryModel> directoryModel);
    }

}
