package com.markLove.Xplan.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.StatusBarUtil;

public class UserInfoActivity extends BaseActivity {
    private MyWebView mWebView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarColor(this,R.color.white);
        StatusBarUtil.StatusBarLightMode(this);
        mWebView = new MyWebView(this);
        LinearLayout mll = findViewById(R.id.rootView);
        mll.addView(mWebView);
        String userId = getIntent().getStringExtra("userId");
        if (TextUtils.isEmpty(userId)){
            mWebView.loadUrl("file:///android_asset/package/main/index.html#/user/native/1");
        } else {
            mWebView.loadUrl("file:///android_asset/package/main/index.html#/user/native/1/"+userId);
        }
        mWebView.addJavascriptInterface(new BaseJsInterface(this), "xplanfunc");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.onDestroy();
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
