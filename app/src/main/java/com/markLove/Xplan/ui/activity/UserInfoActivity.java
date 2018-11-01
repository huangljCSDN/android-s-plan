package com.markLove.Xplan.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.ui.fragment.GroupFragment;
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
        mWebView.addJavascriptInterface(new BaseJsInterface(this), "xplanfunc");
        mWebView.loadUrl("file:///android_asset/package/main/index.html#/user/native/1");
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
