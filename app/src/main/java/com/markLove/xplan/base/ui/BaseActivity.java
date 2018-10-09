package com.markLove.xplan.base.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.markLove.xplan.R;
import com.markLove.xplan.base.mvp.BasePresenter;
import com.markLove.xplan.base.mvp.BaseView;
import com.markLove.xplan.utils.AppManager;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    public P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(getContentViewId());

        AppManager.getAppManager().addActivity(this);
        mPresenter = onCreatePresenter();
        if (mPresenter != null){
            mPresenter.attachView(this);
        }
        init(savedInstanceState);
    }

    /**
     * 获取显示view的xml文件ID
     *
     * @return xml布局文件
     */
    protected abstract int getContentViewId();

    /**
     * 初始化应用程序，设置一些初始化数据都获取数据等操作
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 初始化Activity标题与返回按键
     *
     * @param title 标题内容
     */
    protected void setActivityTitle(String title) {
//        TextView content = (TextView) findViewById(R.id.tv_title_title);
//        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_back);
//        if (content != null) {
//            content.setText(title);
//        }
//        if (ivBack != null) {
//            ivBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }
//        // 获得状态栏高度
//        TextView vStatusBar = (TextView) findViewById(R.id.v_fill_statusBar);
//        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        int statusBarHeight = this.getResources().getDimensionPixelSize(resourceId);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            vStatusBar.setHeight(statusBarHeight);
//            vStatusBar.setVisibility(View.VISIBLE);
//        } else {
//            vStatusBar.setVisibility(View.GONE);
//        }
    }

    /**
     * 结束动画
     */
    protected void endAnimation() {
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_right_out);
    }


    /**
     * 设置回退动画
     */
    @Override
    public void finish() {
        super.finish();
        endAnimation();
    }

    /**
     * 绑定presenter
     */
    public abstract P onCreatePresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error,Toast.LENGTH_SHORT).show();
    }
}
