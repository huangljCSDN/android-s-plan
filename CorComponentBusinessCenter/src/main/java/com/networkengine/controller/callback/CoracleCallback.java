package com.networkengine.controller.callback;

import com.networkengine.entity.BaseResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuhao on 2018/5/15.
 */

public abstract class CoracleCallback<T> implements Callback<BaseResult<T>> {


    @Override
    public void onResponse(Call<BaseResult<T>> call, Response<BaseResult<T>> response) {
        if (response == null) {
            onFailed(ErrorResult.error(ErrorResult.ERROR_REQUEST));
            return;
        }
        if (!response.isSuccessful()) {
            onFailed(ErrorResult.error(ErrorResult.ERROR_REQUEST));
            return;
        }
        BaseResult<T> body = response.body();
        if (body == null) {
            onFailed(ErrorResult.error(ErrorResult.ERROR_DATA_EMPTY));
            return;
        }
        if (body.getRes() == BaseResult.CODE_FAILED) {

            onFailed(ErrorResult.error(ErrorResult.SERVER_UNSUCCESS_CODE,body.getMsg()));
            return;
        }
        onSuccess(body.getData());

    }

    @Override
    public void onFailure(Call<BaseResult<T>> call, Throwable t) {
        onFailed(ErrorResult.error(t));
    }

    /**
     * 请求成功
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 请求失败
     *
     * @param errorResult
     */
    public abstract void onFailed(ErrorResult errorResult);


}
