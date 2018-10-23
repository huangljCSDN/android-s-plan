package com.markLove.Xplan.mvp.model;

import com.markLove.Xplan.api.RetrofitApiService;
import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.api.util.RetrofitUtil;
import com.markLove.Xplan.mvp.contract.PublishContract;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class PublishModel extends PublishContract.Model {
    private RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getApiServer(RetrofitApiService.class);

    @Override
    public void addLocus(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.addLocus(map),requestCallBack);
    }
}
