package com.networkengine.controller.callback;

public interface XCacheCallback<T> extends XCallback<T, ErrorResult> {

    /**
     * 缓存加载回调
     *
     * @param t
     */
    void onLoaderCache(T t);
}
