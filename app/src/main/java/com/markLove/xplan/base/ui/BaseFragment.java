package com.markLove.xplan.base.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.markLove.xplan.base.mvp.BasePresenter;
import com.markLove.xplan.base.mvp.BaseView;
import com.markLove.xplan.utils.MsgUtil;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {
    private boolean isFirstVisible = true;
    /**
     * 标识该Fragment是否是第一次不可见
     **/
    private boolean isFirstInvisible = true;
    /**
     * 标识视图是否加载完成
     **/
    private boolean isPrepared;

    public P mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = onCreatePresenter();
        if (mPresenter != null){
            mPresenter.attachView(this);
        }

        init(view);
        initPrepare();
    }

    /**
     * 绑定presenter
     */
    public abstract P onCreatePresenter();

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 实现Fragment的懒加载
     *
     * @param isVisibleToUser 标示Fragment对用户是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    /**
     * 获取布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 做初始化视图和事件
     *
     * @param view
     */
    protected abstract void init(View view);

    /**
     * Fragment第一次可见的时候调用此方法
     * (Fragment装载在ViewPager中时需要调用)
     */
    protected void onFirstUserVisible() {
    }

    /**
     * Fragment除第一次其他次可见的时候调用此方法
     */
    protected void onUserVisible() {
    }

    /**
     * Fragment第一次不可见的时候调用此方法
     */
    private void onFirstUserInvisible() {
    }

    /**
     * Fragment除第一次其他次不可见的时候调用此方法
     */
    protected void onUserInvisible() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        MsgUtil.showDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        MsgUtil.closeDialog();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
    }
}
