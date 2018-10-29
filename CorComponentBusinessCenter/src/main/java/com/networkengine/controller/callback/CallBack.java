package com.networkengine.controller.callback;

/**
 * Created by liuhao on 2017/6/28.
 */

public interface CallBack<Success, Fail> {
    void onSuccess(Success data);

    void onFail(Fail error);

}
