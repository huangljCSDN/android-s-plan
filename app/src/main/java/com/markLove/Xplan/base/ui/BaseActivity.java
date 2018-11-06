package com.markLove.Xplan.base.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.mvp.BaseView;
import com.markLove.Xplan.utils.AppManager;
import com.markLove.Xplan.utils.MsgUtil;
import com.markLove.Xplan.utils.StatusBarUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by huanglingjun on 2018/5/16.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    public P mPresenter;
    SystemBarTintManager tintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tintManager = new SystemBarTintManager(this);
        getSupportActionBar().hide();
//        setTranslucentStatus();
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
     * 显示删除，非必须实现的方法
     */
    public void showRemove(){};

    public void selectPosition(final int position){};

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
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    /**
     * 结束动画
     */
    protected void endAnimation() {
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_right_out);
    }

    /**
     * 设置状态栏背景状态
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(false);
    }

    /**
     * 设置状态栏颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setTranslucentStatusColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarTintColor(color);
        }
        //如果状态栏为白色，则设置状态栏字体为黑色
        if(color == Color.WHITE){
            StatusBarUtil.StatusBarLightMode(this);
        }
    }

//    /**
//     * 设置状态栏隐藏
//     */
//    public void setTranslucentStatusHide() {
//        tintManager.setStatusBarTintColor(Color.parseColor("#00000000"));
//        root.setFitsSystemWindows(false);
//        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//    }

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
        MsgUtil.showDialog(this);
    }

    @Override
    public void hideLoading() {
        MsgUtil.closeDialog();
    }

    @Override
    public void showTokenExpiredDialog() {
        App.getInstance().onTokenExpires();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error,Toast.LENGTH_SHORT).show();
    }

    public void toast(String content){
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }
}
