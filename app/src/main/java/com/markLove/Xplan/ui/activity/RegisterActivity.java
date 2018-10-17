package com.markLove.Xplan.ui.activity;

import android.os.Bundle;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;

public class RegisterActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
