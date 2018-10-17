package com.markLove.Xplan.ui.fragment;

import android.view.View;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseFragment;

public class TeamFragment extends BaseFragment {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_team;
    }

    @Override
    protected void init(View view) {

    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
