package com.markLove.Xplan.mvp.presenter;

import android.util.Log;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.mvp.contract.GroupChatContract;
import com.markLove.Xplan.mvp.model.GroupChatModel;

import java.util.Map;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public class GroupChatPresenter extends GroupChatContract.Presenter {
    private GroupChatModel mModel;

    public GroupChatPresenter(){
        mModel = new GroupChatModel();
    }

    @Override
    public void joinGroup(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.joinGroup(map, new RequestCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean o) {
                getView().hideLoading();
                getView().onJoinGroup(o);
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
    public void participateGroup(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.participateGroup(map, new RequestCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean o) {
                getView().hideLoading();
                getView().onParticipateGroup(o);
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
