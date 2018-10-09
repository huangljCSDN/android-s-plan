package com.markLove.xplan.model;

import com.markLove.xplan.api.RetrofitApiService;
import com.markLove.xplan.api.util.RequestCallBack;
import com.markLove.xplan.api.util.RetrofitUtil;
import com.markLove.xplan.api.util.RxObserver;
import com.markLove.xplan.bean.PostQueryInfo;
import com.markLove.xplan.contract.MainContract;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class MainModel extends MainContract.Model {
    private RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getApiServer(RetrofitApiService.class);

    @Override
    public void searchRx(String type, String postid, RequestCallBack requestCallBack) {
        retrofitApiService.searchRx(type,postid)
                .timeout(50, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<PostQueryInfo>(requestCallBack));
//        requestData(retrofitApiService.searchRx(type,postid),requestCallBack);
    }
}
