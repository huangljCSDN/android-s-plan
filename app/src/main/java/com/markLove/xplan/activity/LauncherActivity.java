package com.markLove.xplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.markLove.xplan.R;
import com.markLove.xplan.base.mvp.BasePresenter;
import com.markLove.xplan.base.ui.BaseActivity;

public class LauncherActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected int getContentViewId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                Intent intent = new Intent(this,GroupChatActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register:
                break;
        }
    }
}
