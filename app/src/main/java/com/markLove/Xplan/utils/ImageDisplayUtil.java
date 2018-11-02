package com.markLove.Xplan.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.networkengine.R;
import com.networkengine.database.table.Member;
import com.networkengine.engine.EngineParameter;
import com.networkengine.engine.LogicEngine;
import com.networkengine.networkutil.glide.GlideCacheModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * 图片显示工具类
 * Created by pwy
 */
public class ImageDisplayUtil {
    static ArrayList<String> errUserIcon = new ArrayList<>();

    /**
     * 获取原图路径
     *
     * @param path 路径
     * @return 原图路径
     */
    public static String getOriginalPath(String path) {
        return path.replace("&size=m", "").replace("?size=m&", "?").replace("?size=m", "")
                .replace("&size=s", "").replace("?size=s&", "?").replace("?size=s", "");
    }

    /**
     * 获取中图路径
     *
     * @param path 路径
     * @return 中图路径
     */
    public static String getMPath(String path) {
        path = getOriginalPath(path);
        if (path.contains("?")) {
            path = path + "&size=m";
        } else {
            path = path + "?size=m";
        }
        return path;
    }

    /**
     * 获取小图路径
     *
     * @param path 路径
     * @return 小图路径
     */
    public static String getSPath(String path) {
        path = getOriginalPath(path);
        if (path.contains("?")) {
            path = path + "&size=s";
        } else {
            path = path + "?size=s";
        }
        return path;
    }

    public enum IMAGE_SIZE {
        S, M, L
    }

    /**
     * 设置图片
     *
     * @param imageView      显示控件
     * @param url            图片地址
     * @param sign           标签，用于网络图片清除缓存
     * @param minSize        最小尺寸，大中小
     * @param transformation 转换器
     */
    public static void setImgByUrl(ImageView imageView, String url, String sign, IMAGE_SIZE minSize
            , Transformation<Bitmap> transformation) {
//        setImgByUrl(imageView, url, sign, minSize, R.drawable.ic_default_img, transformation);
    }

    /**
     * 设置图片
     *
     * @param imageView      显示控件
     * @param url            图片地址
     * @param sign           标签，用于网络图片清除缓存
     * @param minSize        最小尺寸，大中小
     * @param defaultImg     默认图
     * @param transformation 转换器
     */
    @SuppressLint("StaticFieldLeak")
    public static void setImgByUrl(final ImageView imageView, String url, String sign, IMAGE_SIZE minSize, final int defaultImg
            , final Transformation<Bitmap> transformation) {
        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            setImgByResource(imageView, defaultImg);
            return;
        }

        if (null == sign) {
            sign = "";
        }

        final StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
//        final Transformation<Bitmap> trans = new Transformation<Bitmap>() {
//            @Override
//            public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
//                if (null == transformation) {
//                    return resource;
//                }
//                return transformation.transform(resource, outWidth, outHeight);
//            }
//
//            @Override
//            public String getId() {
//                return caller.getClassName();
//            }
//        };

        if (url.startsWith(LogicEngine.getMchlUrl()) || url.startsWith(LogicEngine.getMxmUrl())) {
            sign = ""; // 我们平台的图片不存在更新了还用旧的路径的问题
        }

        url = getOriginalPath(url);
        if (!exist(url) && minSize != IMAGE_SIZE.L) { // 大图不存在，并且要求的最小尺寸不是原图，看看中图有没有
            url = getMPath(url);
            if (!exist(url) && minSize == IMAGE_SIZE.S) { // 中图也不存在，并且要求的最小尺寸是小图，换成小图的路径
                url = getSPath(url);
            }
        }

        if (url.startsWith("file://")) {
            url = url.replace("file://", "");
        }
        // 磁盘里已有需要尺寸的图，就直接显示
        String loc = getDiskPath(url);
        if (new File(loc).exists() && TextUtils.isEmpty(sign)) {
//            setImgByLocPath(imageView, loc, sign, defaultImg, trans);
            setImgByLocPath(imageView, loc, sign, defaultImg, null);
            return;
        }

        setImgByResource(imageView, defaultImg);
        imageView.setTag(url);
        new AsyncTask<String, Void, String>() {
            String originalUrl;
            String sign;

            @Override
            protected String doInBackground(String... url) {
                originalUrl = url[0];
                sign = url[1];
                try {
                    File file = dowloadPic(imageView.getContext(), originalUrl, sign);

                    if (originalUrl.equals(getOriginalPath(originalUrl))) { // 下载的是原图，删中图和小图
                        new File(getDiskPath(getMPath(originalUrl))).delete();
                        new File(getDiskPath(getSPath(originalUrl))).delete();
                    } else if (originalUrl.equals(getMPath(originalUrl))) { // 下载的是中图，删小图
                        new File(getDiskPath(getSPath(originalUrl))).delete();
                    }

                    if (originalUrl.equals(imageView.getTag())) {
                        return file.getAbsolutePath();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                setImgByLocPath(imageView, s, sign, defaultImg, null);
                super.onPostExecute(s);
            }
        }.execute(url, sign);
    }

    public static File dowloadPic(Context context, String originalUrl, String sign) throws ExecutionException, InterruptedException {
        String signUrl = originalUrl;
        if (!TextUtils.isEmpty(sign)) {
            // 加个参数，改变图片的路径
            if (originalUrl.contains("?")) {
                signUrl = originalUrl + "&glideSign=" + sign;
            } else {
                signUrl = originalUrl + "?glideSign=" + sign;
            }
        }

        GlideUrl glideUrl = new GlideUrl(signUrl);
        if (originalUrl.startsWith(LogicEngine.getMchlUrl()) || originalUrl.startsWith(LogicEngine.getMxmUrl())) { // 我们的文件服务器才加上头参数
            EngineParameter engineParameter = LogicEngine.getInstance().getEngineParameter();
            Member member = LogicEngine.getInstance().getUser();
            LazyHeaders lazyHeaders = new LazyHeaders.Builder()
                    .addHeader("X-xSimple-appKey", engineParameter.appKey)
                    .addHeader("User-Agent", engineParameter.userAgent)
                    .addHeader("X-xSimple-LoginName", TextUtils.isEmpty(member.getId()) ? "" : member.getId())
                    .addHeader("X-xSimple-AuthToken", TextUtils.isEmpty(member.getUserToken()) ? "" : member.getUserToken())
                    .addHeader("X-xSimple-SysCode", TextUtils.isEmpty(member.getUserSystem()) ? "" : member.getUserSystem())
                    .addHeader("X-xSimple-SysUserID", TextUtils.isEmpty(member.getUserId()) ? TextUtils.isEmpty(member.getId()) ? "" : member.getId() : member.getUserId())
                    .build();
            glideUrl = new GlideUrl(signUrl, lazyHeaders);
        }

        FutureTarget<File> future = Glide.with(context)
                .load(glideUrl)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        File cacheFile = future.get();
        File targetFile = new File(getDiskPath(originalUrl));
        if (targetFile.exists()) {
            targetFile.delete(); // 如果已存在，删除
        }
        cacheFile.renameTo(targetFile);
        return targetFile;
    }

    /**
     * 显示磁盘上的图片, 不允许改成public
     *
     * @param imageView      图片控件
     * @param locPath        文件路径
     * @param sign           签名，用于更新缓存
     * @param defaultImg     默认图
     * @param transformation 转换
     */
    private static void setImgByLocPath(ImageView imageView, String locPath, String sign, int defaultImg
            , final Transformation<Bitmap> transformation) {
        imageView.setTag(null);
        try {
            // 页页关闭时再显示图片的话，会闪退，直接抛弃这异常
            if (-1 == defaultImg) {
                Glide.with(imageView.getContext())
                        .load(locPath)
                        .into(imageView);
            } else {
                Glide.with(imageView.getContext())
                        .load(locPath)
                        .into(imageView);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 显示资源文件
     *
     * @param imageView 图片控件
     * @param rid       资源ID
     */
    public static void setImgByResource(ImageView imageView, int rid) {
        if (imageView == null) {
            return;
        }
        try {
            if (-1 == rid) {
                imageView.setImageDrawable(null);
            } else {
                imageView.setImageResource(rid);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 显示默认图
     *
     * @param imageView 图片控件
     */
    public static void setDefaultImg(ImageView imageView) {
//        setImgByResource(imageView, R.drawable.ic_default_img);
    }


    /**
     * 文件是否存在
     *
     * @param uri 路径
     * @return 是否存在
     */
    public static boolean exist(String uri) {
        return null != uri && new File(getDiskPath(uri)).exists();

    }

    /**
     * 获取SD卡缓存路径
     *
     * @param uri 路径
     * @return SD文件
     */
    public static String getDiskPath(String uri) {
        if (null == uri || !uri.startsWith("http")) {
            return uri;
        } else {
            return GlideCacheModule.DISK_PATH + "/" + uri.hashCode() + ".jpg";
        }
    }

    /**
     * @param context
     * @param path
     * @param imgName
     * @param imgSize
     * @return
     */
    private static boolean savePhotoToAlbum(Context context, String path, String imgName, long imgSize) {
        ContentResolver mContentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, context.getResources().getString(R.string.app_name) + "_PIC_" + imgName);
        values.put(MediaStore.Images.Media.DESCRIPTION, context.getResources().getString(R.string.app_name) + "_PIC_" + imgName);
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, imgName);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        values.put(MediaStore.MediaColumns.SIZE, imgSize);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        //插入
        Uri insert = mContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        OutputStream imageOut = null;
        try {
            imageOut = mContentResolver.openOutputStream(insert);

            Bitmap bitmap = BitmapFactory.decodeFile(path);

            if (bitmap == null) {
                return false;
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);

        } catch (FileNotFoundException e) {

        } finally {
            try {
                if (imageOut != null) {
                    imageOut.close();
                }
            } catch (IOException e) {

            }
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, insert));
        return true;
    }
}




