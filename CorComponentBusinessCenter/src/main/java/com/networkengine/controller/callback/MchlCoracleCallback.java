package com.networkengine.controller.callback;

import com.networkengine.entity.MchlBaseResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuhao on 2018/5/15.
 */

public abstract class MchlCoracleCallback<T> implements Callback<MchlBaseResult<T>> {


    @Override
    public void onResponse(Call<MchlBaseResult<T>> call, Response<MchlBaseResult<T>> response) {
        if (response == null) {
            onFailed(ErrorResult.error(ErrorResult.ERROR_REQUEST));
            return;
        }
        if (!response.isSuccessful()) {
            onFailed(ErrorResult.error(ErrorResult.ERROR_REQUEST));
            return;
        }
        MchlBaseResult<T> body = response.body();
        if (body == null) {
            onFailed(ErrorResult.error(ErrorResult.ERROR_DATA_EMPTY));
            return;
        }
        if (MchlBaseResult.STATE_CODE.CODE_FAILED.equals(body.getCode())) {

            onFailed(ErrorResult.error(ErrorResult.SERVER_UNSUCCESS_CODE, body.getMsg()));
            return;
        }

        onSuccess(body.getData(), ErrorResult.error(ErrorResult.SERVER_SUCCESS_CODE, body.getMsg()));

    }

    @Override
    public void onFailure(Call<MchlBaseResult<T>> call, Throwable t) {
        onFailed(ErrorResult.error(t));
    }

    /**
     * 请求成功
     *
     * @param t
     */
    public abstract void onSuccess(T t, ErrorResult errorResult);

    /**
     * 请求失败
     *
     * @param errorResult
     */
    public abstract void onFailed(ErrorResult errorResult);


}
