package com.networkengine.httpApi;

import com.networkengine.entity.PreviewResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by liuhao on 2017/3/2.
 */

public interface PreviewApiService {


    /**
     * 文件预览
     *
     * @param map
     * @return
     */
    @POST("office/doc2Html")
    Call<PreviewResult> getPreviewResult(@QueryMap() Map<String, String> map);

}
