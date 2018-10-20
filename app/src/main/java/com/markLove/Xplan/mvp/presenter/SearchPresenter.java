package com.markLove.Xplan.mvp.presenter;

import android.util.Log;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.MerchantBean;
import com.markLove.Xplan.bean.PostQueryInfo;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.mvp.contract.MainContract;
import com.markLove.Xplan.mvp.contract.SearchContract;
import com.markLove.Xplan.mvp.model.MainModel;
import com.markLove.Xplan.mvp.model.SearchModel;

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

        mModel.getNearMerchant(map, new RequestCallBack<MerchantBean>() {
            @Override
            public void onSuccess(MerchantBean merchantBean) {
                getView().hideLoading();
                getView().refreshMerchantList(merchantBean);
            }

            @Override
            public void onFail(String result) {
                Log.i("MainPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }

    @Override
    public void getNearUser(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.getNearUser(map, new RequestCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean o) {
                getView().hideLoading();
                getView().refreshUserList(o);
            }

            @Override
            public void onFail(String result) {
                Log.i("MainPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }
}
