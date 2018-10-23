package com.markLove.Xplan.mvp.presenter;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.mvp.contract.MainContract;
import com.markLove.Xplan.mvp.model.MainModel;
import com.markLove.Xplan.utils.LogUtils;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class MainPresenter extends MainContract.Presenter {
    private MainModel mModel;

    public MainPresenter(){
        mModel = new MainModel();
    }

    @Override
    public void updateUserPlace(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.updateUserPlace(map, new RequestCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                LogUtils.i("MainPresenter",baseBean.toString());
                getView().hideLoading();
//                getView().refreshUI(baseBean);
            }

            @Override
            public void onFail(String result) {
                LogUtils.i("MainPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }
}
