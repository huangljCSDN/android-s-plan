package com.markLove.xplan.ui.activity;

import android.os.Bundle;

import com.markLove.xplan.R;
import com.markLove.xplan.base.mvp.BasePresenter;
import com.markLove.xplan.base.ui.BaseActivity;

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
