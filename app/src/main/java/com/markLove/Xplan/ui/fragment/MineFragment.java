package com.markLove.Xplan.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseFragment;
import com.markLove.Xplan.bean.GoViewBeaan;
import com.markLove.Xplan.ui.activity.PublishActivity;
import com.markLove.Xplan.ui.activity.WebViewActivity;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;

public class MineFragment extends BaseFragment {
    private MyWebView mWebView;
    private GoViewBeaan goViewBeaan;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(View view) {
        mWebView = new MyWebView(getContext());
        LinearLayout mll = view.findViewById(R.id.rootView);
        mll.addView(mWebView);
        initWebSettings();
    }

    /**
     * 设置websetting
     */
    private void initWebSettings(){
        mWebView.addJavascriptInterface(new JSInterface(), "xplanfunc");
        mWebView.loadUrl("file:///android_asset/package/main/index.html#/user/native/1");

    }

    // 继承自Object类
    public class JSInterface extends Object {

        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void toPublishPage() {
            //{"chatType":1,"chatId":1}
            LogUtils.i("huang", "toPublishPage=");
            startPublishActivity();
        }

        @JavascriptInterface
        public void goView(String json) {
            LogUtils.i("json="+json);
            goViewBeaan = GsonUtils.json2Bean(json,GoViewBeaan.class);
            startWebViewActivity(goViewBeaan.getUrlPort());
//            startPublishActivity();
        }
    }

    private void startWebViewActivity(String url) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivityForResult(intent,200);
    }

    private void startPublishActivity(){
        Intent intent = new Intent(getContext(),PublishActivity.class);
        startActivityForResult(intent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        LogUtils.i("huang","requestCode="+requestCode);
        if (requestCode == 100){
            //刷新轨迹列表
            mWebView.loadUrl("javascript:refreshUserLocus()");
        }
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
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
