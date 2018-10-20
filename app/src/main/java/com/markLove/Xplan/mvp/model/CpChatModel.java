package com.markLove.Xplan.mvp.model;

import com.markLove.Xplan.api.RetrofitApiService;
import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.api.util.RetrofitUtil;
import com.markLove.Xplan.mvp.contract.CpChatContract;
import com.markLove.Xplan.mvp.contract.ShopChatContract;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class CpChatModel extends CpChatContract.Model {
    private RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getApiServer(RetrofitApiService.class);

    @Override
    public void focus(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.focus(map),requestCallBack);
    }
}
