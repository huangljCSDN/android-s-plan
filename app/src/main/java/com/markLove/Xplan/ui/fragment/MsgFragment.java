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
import com.markLove.Xplan.bean.ChatBean;
import com.markLove.Xplan.ui.activity.CpChatActivity;
import com.markLove.Xplan.ui.activity.GroupChatActivity;
import com.markLove.Xplan.ui.activity.ShopChatActivity;
import com.markLove.Xplan.ui.activity.SingleChatActivity;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;

public class MsgFragment extends BaseFragment {
    private WebView mWebView;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void init(View view) {
        mWebView = new WebView(getContext());
        LinearLayout mll = view.findViewById(R.id.rootView);
        //避免内存泄露，采用动态添加的方式

//        mWebView = findViewById(R.id.webView);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(layoutParams);
        mll.addView(mWebView);
        initWebSettings();
    }

    /**
     * 设置websetting
     */
    private void initWebSettings(){
        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(false);
        //开启JavaScript支持
        settings.setJavaScriptEnabled(true);
        // 支持缩放
        settings.setSupportZoom(true);
        mWebView.addJavascriptInterface(new JSInterface(), "xplanfunc");
        mWebView.loadUrl("file:///android_asset/package/main/index.html#/message/native/1");

    }

    public class JSInterface extends Object {

        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void toChatRoom(String json) {
            LogUtils.i("huang", "toChatRoom=" + json);
            ChatBean chatBean = GsonUtils.json2Bean(json, ChatBean.class);
            switch (chatBean.getChatType()){
                case 1:
                    startGroupChatActivity(chatBean.getChatId());
                    break;
                case 2:
                    startShopChatActivity(chatBean.getChatId());
                    break;
                case 3:
                    startCpChatActivity(chatBean.getChatId());
                    break;
                case 4:
                    startSingleChatActivity(chatBean.getChatId());
                    break;
            }
        }
    }

    private void startShopChatActivity(int id) {
        Intent intent = new Intent(getContext(), ShopChatActivity.class);
        intent.putExtra("chatId", id);
        startActivity(intent);
    }

    private void startGroupChatActivity(int id) {
        Intent intent = new Intent(getContext(), GroupChatActivity.class);
        intent.putExtra("chatId", id);
        startActivity(intent);
    }

    private void startSingleChatActivity(int id) {
        Intent intent = new Intent(getContext(), SingleChatActivity.class);
        intent.putExtra("chatId", id);
        startActivity(intent);
    }

    private void startCpChatActivity(int id) {
        Intent intent = new Intent(getContext(), CpChatActivity.class);
        intent.putExtra("chatId", id);
        startActivity(intent);
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
        mWebView.removeAllViews();
        mWebView.stopLoading();
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.destroy();
    }


    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
