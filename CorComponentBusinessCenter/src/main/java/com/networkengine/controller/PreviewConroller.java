package com.networkengine.controller;

import android.util.Log;

import com.networkengine.entity.PreviewResult;
import com.networkengine.httpApi.Api;
import com.networkengine.httpApi.PreviewApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuhao on 2017/3/2.
 */

public class PreviewConroller {


    private PreviewApiService mAPreviewApiService;

    public interface IPreviewConroller {

        void onSuccess(String htmlPath);

        void onFail(String msg);
    }

    public PreviewConroller() {
        mAPreviewApiService = Api.preService();
    }

    /**
     * 获取预览html
     *
     * @param map
     * @param iPreviewConroller
     */
    public void getPreViewHtml(Map<String, String> map, final IPreviewConroller iPreviewConroller) {
        if (map == null)
            return;
        if (iPreviewConroller == null)
            return;
        Call<PreviewResult> resultCall = mAPreviewApiService.getPreviewResult(map);
        resultCall.enqueue(new Callback<PreviewResult>() {
            @Override
            public void onResponse(Call<PreviewResult> call, Response<PreviewResult> response) {
                if (response == null || response.body() == null) {
                    iPreviewConroller.onFail("response is null");
                    return;
                }
                Log.e("preview", response.toString());
                PreviewResult previewResult = response.body();
                if (previewResult == null) {
                    iPreviewConroller.onFail("PreviewResult is null");
                    return;
                }
                if (previewResult.getStatus().equals("error")) {
                    iPreviewConroller.onFail("PreviewResult return error");
                    return;
                }
                iPreviewConroller.onSuccess(previewResult.getHtmlUrl());

            }

            @Override
            public void onFailure(Call<PreviewResult> call, Throwable t) {
                iPreviewConroller.onFail(t.getMessage());
            }
        });
    }
}

