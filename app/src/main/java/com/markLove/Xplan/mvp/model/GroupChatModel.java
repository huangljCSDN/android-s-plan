package com.markLove.Xplan.mvp.model;

import com.markLove.Xplan.api.RetrofitApiService;
import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.api.util.RetrofitUtil;
import com.markLove.Xplan.mvp.contract.GroupChatContract;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class GroupChatModel extends GroupChatContract.Model {
    private RetrofitApiService retrofitApiService = RetrofitUtil.getInstance().getApiServer(RetrofitApiService.class);

    @Override
    public void joinGroup(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.joinGroup(map),requestCallBack);
    }

    @Override
    public void participateGroup(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.participateGroup(map),requestCallBack);
    }

    @Override
    public void applyGroup(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.applyGroup(map),requestCallBack);
    }

    @Override
    public void groupDetails(Map<String, String> map, RequestCallBack requestCallBack) {
        requestData(retrofitApiService.groupDetails(map),requestCallBack);
    }
}
