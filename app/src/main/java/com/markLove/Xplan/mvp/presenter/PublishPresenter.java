package com.markLove.Xplan.mvp.presenter;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.mvp.contract.PublishContract;
import com.markLove.Xplan.mvp.model.PublishModel;
import com.markLove.Xplan.utils.LogUtils;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class PublishPresenter extends PublishContract.Presenter {
    private PublishModel mModel;

    public PublishPresenter(){
        mModel = new PublishModel();
    }

    @Override
    public void addLocus(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.addLocus(map, new RequestCallBack<BaseBean<Object>>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
                LogUtils.i("PublishPresenter",baseBean.toString());
                getView().hideLoading();
                getView().refreshUI(baseBean.toString());
            }

            @Override
            public void onFail(String result) {
                LogUtils.i("PublishPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }
}
