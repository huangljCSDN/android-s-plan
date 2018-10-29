package com.networkengine.controller.callback;

import com.networkengine.entity.MchlBaseResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by liuhao on 2018/5/25.
 */

public class MchlSyncCoracleCallback<T> {

    private ErrorResult mErrorResult;

    public ErrorResult getErrorResult() {

        return mErrorResult;
    }

    public void setErrorResult(ErrorResult mErrorResult) {
        this.mErrorResult = mErrorResult;
    }

    /**
     * 同步解析
     *
     * @param call
     * @return
     */
    public T execute(Call<MchlBaseResult<T>> call) {
        try {
            Response<MchlBaseResult<T>> baseResultResponse = call.execute();
            if (baseResultResponse == null) {
                setErrorResult(ErrorResult.error(ErrorResult.ERROR_REQUEST));
                return null;
            }
            if (!baseResultResponse.isSuccessful()) {
                setErrorResult(ErrorResult.error(ErrorResult.ERROR_REQUEST));
                return null;
            }
            MchlBaseResult<T> baseResult = baseResultResponse.body();

            if (baseResult == null) {
                setErrorResult(ErrorResult.error(ErrorResult.ERROR_DATA_EMPTY));
                return null;
            }
            if (MchlBaseResult.STATE_CODE.CODE_FAILED.equals(baseResult.getCode())) {
                setErrorResult(ErrorResult.error(ErrorResult.SERVER_UNSUCCESS_CODE, baseResult.getMsg()));

                return null;
            }
            setErrorResult(ErrorResult.error(ErrorResult.SERVER_SUCCESS_CODE, baseResult.getMsg()));
            return baseResult.getData();

        } catch (IOException e) {
            setErrorResult(ErrorResult.error(e));
        }
        return null;
    }

}
