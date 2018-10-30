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
import com.markLove.Xplan.bean.GoViewBeaan;
import com.markLove.Xplan.ui.activity.CpChatActivity;
import com.markLove.Xplan.ui.activity.GroupChatActivity;
import com.markLove.Xplan.ui.activity.ShopChatActivity;
import com.markLove.Xplan.ui.activity.SingleChatActivity;
import com.markLove.Xplan.ui.activity.WebViewActivity;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;

public class MsgFragment extends BaseFragment {
    private MyWebView mWebView;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_msg;
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

        @JavascriptInterface
        public void goView(String json) {
            LogUtils.i("json="+json);
            GoViewBeaan goViewBeaan = GsonUtils.json2Bean(json,GoViewBeaan.class);
            startWebViewActivity(goViewBeaan.getUrlPort());
//            startPublishActivity();
        }
    }

    private void startWebViewActivity(String url) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivityForResult(intent,200);
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
        mWebView.onDestroy();
    }


    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
