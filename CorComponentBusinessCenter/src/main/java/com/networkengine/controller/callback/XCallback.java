package com.networkengine.controller.callback;

public interface XCallback<T, E extends ErrorResult> {

    void onSuccess(T result);

    void onFail(E error);

}
