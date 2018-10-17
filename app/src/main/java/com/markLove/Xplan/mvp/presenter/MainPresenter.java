package com.markLove.Xplan.mvp.presenter;

import android.util.Log;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.PostQueryInfo;
import com.markLove.Xplan.mvp.contract.MainContract;
import com.markLove.Xplan.mvp.model.MainModel;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class MainPresenter extends MainContract.Presenter {
    private MainModel mModel;

    public MainPresenter(){
        mModel = new MainModel();
    }

    /**
     * 请求数据
     * @param type
     * @param postid
     */
    public void getData(String type, String postid){
        if (!isAttach()) return;
        getView().showLoading();

        mModel.searchRx(type, postid, new RequestCallBack<PostQueryInfo>() {
            @Override
            public void onSuccess(PostQueryInfo o) {
                getView().hideLoading();
                getView().refreshUI(o);
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
