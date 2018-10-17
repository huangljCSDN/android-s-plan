package com.markLove.Xplan.api.util;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public interface RequestCallBack<T> {
    void onSuccess(T o);

    void onFail(String result);
}
