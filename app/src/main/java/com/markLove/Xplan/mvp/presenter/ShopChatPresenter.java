package com.markLove.Xplan.mvp.presenter;

import android.util.Log;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.MerchantBean;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.mvp.contract.SearchContract;
import com.markLove.Xplan.mvp.contract.ShopChatContract;
import com.markLove.Xplan.mvp.model.SearchModel;
import com.markLove.Xplan.mvp.model.ShopChatModel;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class ShopChatPresenter extends ShopChatContract.Presenter {
    private ShopChatModel mModel;

    public ShopChatPresenter(){
        mModel = new ShopChatModel();
    }

    @Override
    public void getMerchantUserList(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.getMerchantUserList(map, new RequestCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean o) {
                getView().hideLoading();
                getView().refreshUI(o);
            }

            @Override
            public void onFail(String result) {
//                Log.i("ShopChatPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }
}
