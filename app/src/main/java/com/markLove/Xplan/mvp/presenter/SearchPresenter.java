package com.markLove.Xplan.mvp.presenter;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.mvp.contract.SearchContract;
import com.markLove.Xplan.mvp.model.SearchModel;
import com.markLove.Xplan.utils.LogUtils;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class SearchPresenter extends SearchContract.Presenter {
    private SearchModel mModel;

    public SearchPresenter(){
        mModel = new SearchModel();
    }

    @Override
    public void getNearMerchant(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.getNearMerchant(map, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(Object json) {
                LogUtils.i("SearchPresenter",json.toString());
                getView().hideLoading();
                getView().refreshMerchantList(json.toString());
            }

            @Override
            public void onFail(String result) {
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }

    @Override
    public void getNearUser(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.getNearUser(map, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(Object json) {
                LogUtils.i("SearchPresenter",json.toString());
                getView().hideLoading();
                getView().refreshUserList(json.toString());
            }

            @Override
            public void onFail(String result) {
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }

    @Override
    public void getMerchantUserList(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.getMerchantUserList(map, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(Object json) {
                LogUtils.i("SearchPresenter",json.toString());
                getView().hideLoading();
                getView().refreshUserList(json.toString());
            }

            @Override
            public void onFail(String result) {
                LogUtils.i("SearchPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }
}
