package com.markLove.Xplan.mvp.presenter;

import android.util.Log;

import com.markLove.Xplan.api.util.RequestCallBack;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.bean.GroupDetailBean;
import com.markLove.Xplan.mvp.contract.GroupChatContract;
import com.markLove.Xplan.mvp.model.GroupChatModel;
import com.markLove.Xplan.utils.LogUtils;

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

        mModel.joinGroup(map, new RequestCallBack<BaseBean<Object>>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
                LogUtils.i("GroupChatPresenter",baseBean.toString());
                getView().hideLoading();
//                getView().onJoinGroup(baseBean);
            }

            @Override
            public void onFail(String result) {
                LogUtils.i("GroupChatPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }

    @Override
    public void exitGroup(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.exitGroup(map, new RequestCallBack<BaseBean<Object>>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
                LogUtils.i("GroupChatPresenter",baseBean.toString());
                getView().hideLoading();
                getView().onExitGroup(baseBean);
            }

            @Override
            public void onFail(String result) {
                LogUtils.i("GroupChatPresenter",result);
                getView().hideLoading();
                getView().showError("退出组局失败!");
            }
        });
    }

    @Override
    public void participateGroup(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.participateGroup(map, new RequestCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                LogUtils.i("GroupChatPresenter",baseBean.toString());
                getView().hideLoading();
                getView().onParticipateGroup(baseBean);
            }

            @Override
            public void onFail(String result) {
                Log.i("GroupChatPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }

    @Override
    public void applyGroup(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.applyGroup(map, new RequestCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                LogUtils.i("GroupChatPresenter",baseBean.toString());
                getView().hideLoading();
                getView().onApplyGroup(baseBean);
            }

            @Override
            public void onFail(String result) {
                Log.i("GroupChatPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }

    @Override
    public void groupDetails(Map<String, String> map) {
        if (!isAttach()) return;
        getView().showLoading();

        mModel.groupDetails(map, new RequestCallBack<BaseBean<GroupDetailBean>>() {
            @Override
            public void onSuccess(BaseBean<GroupDetailBean> baseBean) {
                LogUtils.i("GroupChatPresenter",baseBean.toString());
                getView().hideLoading();
                getView().onGroupDetail(baseBean.Data);
            }

            @Override
            public void onFail(String result) {
                Log.i("GroupChatPresenter",result);
                getView().hideLoading();
                getView().showError(result);
            }
        });
    }
}
