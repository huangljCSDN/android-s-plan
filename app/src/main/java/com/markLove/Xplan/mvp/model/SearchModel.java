package com.markLove.Xplan.mvp.model;

import com.markLove.Xplan.api.RetrofitApiService;
import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.api.util.RetrofitUtil;
import com.markLove.Xplan.api.util.RxObserver;
import com.markLove.Xplan.bean.PostQueryInfo;
import com.markLove.Xplan.mvp.contract.MainContract;
import com.markLove.Xplan.mvp.contract.SearchContract;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class SearchModel extends SearchContract.Model {
    private RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getApiServer(RetrofitApiService.class);

    @Override
    public void getNearMerchant(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.getNearMerchant(map),requestCallBack);
    }

    @Override
    public void getNearUser(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.getNearUser(map),requestCallBack);
    }

    @Override
    public void getMerchantUserList(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.getMerchantUserList(map),requestCallBack);
    }
}
