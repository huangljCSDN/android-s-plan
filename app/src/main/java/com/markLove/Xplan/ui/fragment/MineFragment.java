package com.markLove.Xplan.ui.fragment;

import android.view.View;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseFragment;

public class MineFragment extends BaseFragment {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(View view) {

    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
