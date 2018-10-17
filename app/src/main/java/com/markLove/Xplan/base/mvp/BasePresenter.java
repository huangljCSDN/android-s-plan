package com.markLove.Xplan.base.mvp;

import java.lang.ref.SoftReference;

/**
 * Created by huanglingjun on 2018/9/20.
 */
public abstract class BasePresenter<V> {

    public SoftReference<V> mViewRef;

    //绑定视图
    public void attachView(V view) {
        mViewRef = new SoftReference<>(view);
    }

    //解绑
    public void detachView() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public V getView() {
        return mViewRef.get();
    }

    public boolean isAttach() {
        return null != mViewRef && null != mViewRef.get();
    }
}
