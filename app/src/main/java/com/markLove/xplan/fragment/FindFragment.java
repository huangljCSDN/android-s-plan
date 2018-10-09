package com.markLove.xplan.fragment;

import android.view.View;

import com.markLove.xplan.R;
import com.markLove.xplan.base.mvp.BasePresenter;
import com.markLove.xplan.base.ui.BaseFragment;

public class FindFragment extends BaseFragment {

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_find;
    }

    @Override
    protected void init(View view) {

    }
}
