package com.markLove.Xplan.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.cjt2325.cameralibrary.util.LogUtil;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.AppManager;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.PreferencesUtils;

public class LoginActivity extends BaseContractActivity {
    private MyWebView mWebView;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fullScreen(this);
        mWebView = new MyWebView(this);

        LinearLayout mll = findViewById(R.id.rootView);
        mll.addView(mWebView);
        initWebSettings();

        mWebView.requestFocus(View.FOCUS_DOWN);
    }

    /**
     * 设置websetting
     */
    private void initWebSettings(){

        mWebView.setWebChromeClient(new WebChromeClient(){
            // For Android 3.0-
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                showPicDialog();
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                showPicDialog();
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                showPicDialog();
            }

            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                showPicDialog();
                return true;
            }
        });
        mWebView.addJavascriptInterface(new BaseJsInterface(this), "xplanfunc");
        mWebView.loadUrl("file:///android_asset/package/main/index.html#/login/password");
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()){
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.onDestroy();
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    public void setBitmap(String imagePath) {
//        Uri uri = Uri.fromFile(new File(imagePath));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mUploadCallbackAboveL.onReceiveValue(new Uri[]{uri});
//        } else {
//            mUploadMessage.onReceiveValue(uri);
//        }
    }
}
