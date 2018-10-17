package com.markLove.Xplan.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.markLove.Xplan.R;
import com.markLove.Xplan.config.Constants;

import java.io.File;

/**
 * Created by hs on 2018/8/21.
 */

public class ImageLoaderUtils {


    /**
     * 图像显示
     *
     * @param context
     * @param picUrl
     * @param imageView
     */
    public static void displayCircle(Context context, String picUrl, ImageView imageView,int errRes,int placeholdeRes) {

        if (TextUtils.isEmpty(picUrl)) {
            imageView.setBackgroundResource(R.drawable.icon_loading_default);
            return;
        }
        if (!picUrl.startsWith("http"))
            picUrl = Constants.BASE_IMG_URL+picUrl;

        Glide.with(context).load(picUrl)
                .apply(RequestOptions.placeholderOf(placeholdeRes))
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.errorOf(errRes))
                .into(imageView);
    }

    /**
     * 图像显示
     *
     * @param context
     * @param picUrl
     * @param imageView
     */
    public static void display(Context context, String picUrl, ImageView imageView) {

        if (TextUtils.isEmpty(picUrl)) {
            imageView.setBackgroundResource(R.drawable.icon_loading_default);
            return;
        }
        if (!picUrl.startsWith("http"))
            picUrl = Constants.BASE_IMG_URL+picUrl;

        Glide.with(context).load(picUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.errorOf(R.drawable.icon_loading_default))
                .into(imageView);
    }


    /**
     * 图像显示
     *
     * @param context
     * @param imageView
     */
    public static void display(Context context, File res, ImageView imageView, int width, int height) {

        Glide.with(context).load(res)
                .apply(RequestOptions.placeholderOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.errorOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.overrideOf(width,height))
                .into(imageView);
    }


    /**
     * 图像显示
     *
     * @param context
     * @param imageView
     */
    public static void display(Context context, int res, ImageView imageView,int width,int height) {

        Glide.with(context).load(res)
                .apply(RequestOptions.placeholderOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.errorOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.overrideOf(width,height))
                .into(imageView);
    }


    /**
     * 图像显示
     *
     * @param context
     * @param picUrl
     * @param imageView
     */
    public static void display(Context context, String picUrl, ImageView imageView,int width,int height) {

        String loadImageUrl;
        if(picUrl != null && !picUrl.startsWith("http")){
            loadImageUrl = Constants.BASE_IMG_URL+picUrl;
        }else{
            loadImageUrl = picUrl;
        }

        Glide.with(context).load(loadImageUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.errorOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.overrideOf(width,height))
                .into(imageView);
    }

    /**
     * 圆形图像显示
     *
     * @param context
     * @param picUrl
     * @param imageView
     */
    public static void displayCircle(Context context, String picUrl, ImageView imageView) {
        if (TextUtils.isEmpty(picUrl)){
            imageView.setBackgroundResource(R.drawable.icon_loading_default);
            return;
        }
        if (!picUrl.startsWith("http")) {
            picUrl = Constants.BASE_IMG_URL + picUrl;
        }

        Glide.with(context).load(R.drawable.icon)
                .apply(RequestOptions.placeholderOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.errorOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

}
