package com.markLove.Xplan.mvp.model;

import com.markLove.Xplan.api.RetrofitApiService;
import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.api.util.RetrofitUtil;
import com.markLove.Xplan.mvp.contract.SearchContract;
import com.markLove.Xplan.mvp.contract.ShopChatContract;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class ShopChatModel extends ShopChatContract.Model {
    private RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getApiServer(RetrofitApiService.class);

    @Override
    public void getMerchantUserList(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.getMerchantUserList(map),requestCallBack);
    }
}
