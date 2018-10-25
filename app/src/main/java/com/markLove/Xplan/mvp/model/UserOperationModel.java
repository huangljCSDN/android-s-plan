package com.markLove.Xplan.mvp.model;

import com.markLove.Xplan.api.RetrofitApiService;
import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.api.util.RetrofitUtil;
import com.markLove.Xplan.mvp.contract.UserOperationContract;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class UserOperationModel extends UserOperationContract.Model {
    private RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getApiServer(RetrofitApiService.class);

    @Override
    public void focus(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.focus(map),requestCallBack);
    }

    @Override
    public void isBlackList(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.isBlackList(map),requestCallBack);
    }
}
