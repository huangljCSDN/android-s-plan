package com.markLove.Xplan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.GoNativeBean;
import com.markLove.Xplan.ui.fragment.GroupFragment;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;
import com.markLove.Xplan.utils.StatusBarUtil;

public class WebViewActivity extends BaseActivity {
    private MyWebView mWebView;
    private String url;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fullScreen(this);
        StatusBarUtil.StatusBarLightMode(this);
        mWebView = new MyWebView(this);
        LinearLayout mll = findViewById(R.id.rootView);
        mll.addView(mWebView);

        url = getIntent().getStringExtra("url");
        mWebView.addJavascriptInterface(new JSInterface(this), "xplanfunc");
        mWebView.loadUrl("file:///android_asset/"+url);
    }

    // 继承自Object类
    public class JSInterface extends BaseJsInterface {

        public JSInterface(Activity mActivity) {
            super(mActivity);
        }

        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void goNative(String json) {
            //{"chatType":1,"chatId":1}
            LogUtils.i("huang", "goNative=" + json);
            GoNativeBean goNativeBean = GsonUtils.json2Bean(json,GoNativeBean.class);
            if (goNativeBean == null || goNativeBean.getCallFun().isEmpty()){
                finish();
            } else {
                Intent intent = new Intent();
                intent.putExtra("goNativeBean",goNativeBean);
                setResult(RESULT_OK,intent);
                finish();
            }
        }

        @JavascriptInterface
        public void exitApp() {
            App.getInstance().outLogin(WebViewActivity.this);
        }
    }

    private void finishActivity(){

    }

//    @Override
//    public void onBackPressed() {
//        if (mWebView.canGoBack()){
//            mWebView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
