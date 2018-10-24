package com.markLove.Xplan.mvp.presenter;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.mvp.contract.UserOperationContract;
import com.markLove.Xplan.mvp.model.UserOperationModel;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class UserOperationPresenter extends UserOperationContract.Presenter {
    private UserOperationModel mModel;

    public UserOperationPresenter(){
        mModel = new UserOperationModel();
    }

    @Override
    public void focus(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.focus(map, new RequestCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean o) {
                getView().hideLoading();
                getView().onFocus(o);
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
