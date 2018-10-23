package com.markLove.Xplan.mvp.model;

import com.markLove.Xplan.api.RetrofitApiService;
import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.api.util.RetrofitUtil;
import com.markLove.Xplan.mvp.contract.FileContract;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class FileModel extends FileContract.Model {
    private RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getApiServer(RetrofitApiService.class);

    @Override
    public void upload(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.upload(map),requestCallBack);
    }

    @Override
    public void download(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.download(map),requestCallBack);
    }
}
