package com.xsimple.im.manager.photo;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by lh on 17/4/12.
 */
public class PhotoModelLoader extends CursorLoader {

    final String[] IMAGE_PROJECTION = {
            Media._ID,
            Media.DATA,
            Media.BUCKET_ID,
            Media.BUCKET_DISPLAY_NAME,
            Media.DATE_ADDED,
            Media.SIZE
    };

    public PhotoModelLoader(Context context) {
        super(context);

        setProjection(IMAGE_PROJECTION);
        setUri(Media.EXTERNAL_CONTENT_URI);
        setSortOrder(Media.DATE_ADDED + " DESC");

        setSelection(
                MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=? " + "or " + MIME_TYPE + "=?");
        String[] selectionArgs;

        selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};

        setSelectionArgs(selectionArgs);
    }


    private PhotoModelLoader(Context context, Uri uri, String[] projection, String selection,
                             String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }


}
