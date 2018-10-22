package com.markLove.Xplan.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;

/**
 * 设置动态可见权限
 */
public class SetPermissionActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rlALL,rlMe;
    private ImageView ivSelectedAll,ivSelectedMe;
    private int visible;
    private String typeString ;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_permission;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ivSelectedAll = findViewById(R.id.iv_selected);
        ivSelectedMe = findViewById(R.id.iv_selected2);

        findViewById(R.id.rl_all).setOnClickListener(this);
        findViewById(R.id.rl_me).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.fl_back).setOnClickListener(this);

        visible = getIntent().getIntExtra("visible",0);
        if (visible == 0){
            ivSelectedAll.setVisibility(View.VISIBLE);
            ivSelectedMe.setVisibility(View.GONE);
        } else {
            ivSelectedAll.setVisibility(View.GONE);
            ivSelectedMe.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_all:
                ivSelectedAll.setVisibility(View.VISIBLE);
                ivSelectedMe.setVisibility(View.GONE);
                visible = 0;
                break;
            case R.id.rl_me:
                ivSelectedAll.setVisibility(View.GONE);
                ivSelectedMe.setVisibility(View.VISIBLE);
                visible = 1;
                break;
            case R.id.fl_back:
                finishActivity();
                break;
        }
    }

    private void finishActivity(){
        Intent intent = new Intent();
        intent.putExtra("visible",visible);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }
}
