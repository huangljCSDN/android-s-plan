package com.networkengine.networkutil.glide;

import android.content.Context;


import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

import com.networkengine.util.FilePathUtils;
import com.networkengine.util.MD5Util;


/**
 * 自定义缓存策略
 * Created by pwy on 2018/5/23.
 */
public class GlideCacheModule implements GlideModule {
    public static final String DISK_PATH = FilePathUtils.getSDIntance().mkdirsSubFile(FilePathUtils.IMAGE_PATH_NAME);

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskLruCacheFactory(DISK_PATH, 500 * 1024 * 1024));
        builder.setMemoryCache(new LruResourceCache((int) (Runtime.getRuntime().maxMemory() / 4)));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
