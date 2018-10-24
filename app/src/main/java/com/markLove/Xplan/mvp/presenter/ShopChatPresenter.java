package com.markLove.Xplan.mvp.presenter;

import android.util.Log;

import com.cjt2325.cameralibrary.util.LogUtil;
import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.bean.MerchantBean;
import com.markLove.Xplan.bean.MerchantInfoBean;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.mvp.contract.SearchContract;
import com.markLove.Xplan.mvp.contract.ShopChatContract;
import com.markLove.Xplan.mvp.model.SearchModel;
import com.markLove.Xplan.mvp.model.ShopChatModel;
import com.markLove.Xplan.utils.LogUtils;

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
    public void getUsersByGroup(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.getUsersByGroup(map, new RequestCallBack<BaseBean<Object>>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
                LogUtils.i("ShopChatPresenter",baseBean.toString());
                getView().hideLoading();
//                getView().refreshUsersByGroup();
            }

            @Override
            public void onFail(String result) {
                LogUtils.i("ShopChatPresenter",result);
//                Log.i("ShopChatPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }

    @Override
    public void getMerchantInfo(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.getMerchantInfo(map, new RequestCallBack<BaseBean<MerchantInfoBean>>() {
            @Override
            public void onSuccess(BaseBean<MerchantInfoBean> baseBean) {
                LogUtils.i("ShopChatPresenter",baseBean.toString());
                getView().hideLoading();
                getView().refreshMerchantInfo(baseBean.Data);
            }

            @Override
            public void onFail(String result) {
                LogUtils.i("ShopChatPresenter",result);
//                Log.i("ShopChatPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }
}
