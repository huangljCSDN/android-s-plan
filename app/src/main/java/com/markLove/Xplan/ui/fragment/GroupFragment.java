package com.markLove.Xplan.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.SinglePickerActivity;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseFragment;
import com.markLove.Xplan.bean.ChatBean;
import com.markLove.Xplan.bean.GoViewBeaan;
import com.markLove.Xplan.ui.activity.GroupChatActivity;
import com.markLove.Xplan.ui.activity.WebViewActivity;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.ToastUtils;

import java.util.ArrayList;

public class GroupFragment extends BaseFragment {
    private WebView mWebView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_team;
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
    private void initWebSettings() {
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
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 支持缩放
        settings.setSupportZoom(false);

        mWebView.addJavascriptInterface(new JSInterface(), "xplanfunc");

        mWebView.loadUrl("file:///android_asset/package/main/index.html#/bureau/native/1");

    }

    // 继承自Object类
    public class JSInterface extends Object {

        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void toChatRoom(String json) {
            //{"chatType":1,"chatId":1}
            LogUtils.i("huang", "toChatRoom=" + json);
            GroupFragment.this.startGroupChatActivity(json);

//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mWebView.loadUrl("javascript:photoFinish(" + "1" + ")");
//                }
//            });
        }

        @JavascriptInterface
        public void goNative(String callFun) {
            ToastUtils.showLong(getContext(), "goNative");
        }

        /**
         * 拍照
         * <p>
         * uploadUrl
         * sCallback photoFinish
         */
        @JavascriptInterface
        public void goPhoto(String json) {
            ToastUtils.showLong(getContext(), "goPhoto");
        }

        /**
         * 视频
         * <p>
         * uploadUrl
         * sCallback videoFinish
         */
        @JavascriptInterface
        public void goVideo(String json) {
            ToastUtils.showLong(getContext(), "goVideo");

        }

        /**
         * 录音
         * <p>
         * uploadUrl
         * sCallback videoFinish
         */
        @JavascriptInterface
        public void goRecord(String json) {
            ToastUtils.showLong(getContext(), "goRecord");

        }

        /**
         * 调用相册，视频
         * <p>
         * uploadUrl  后端提供的上传接口名
         * selectType 选择内容：1-图片、2-视频、3图片和视频
         * backType   选择方式-单选（single）、多选(multi) 注：如果是多选，则返回数组
         * sCallback  photoFinish
         */
        @JavascriptInterface
        public void fromImgLibrary(String json) {
            ToastUtils.showLong(getContext(), "fromImgLibrary");

            if (1 == Integer.parseInt("111")) {
                Intent intent = new Intent(getActivity(), SinglePickerActivity.class);
                intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);
                getActivity().startActivityForResult(intent, 200);
            }
        }

        /**
         * 打电话
         *
         * @param phoneNumber
         */
        @JavascriptInterface
        public void openPhone(String phoneNumber) {
            ToastUtils.showLong(getContext(), "openPhone");
        }

        /**
         * 定位
         * <p>
         * sCallback 成功回调
         * fCallback 失败回调  "{“status”:”error”,”msg”:”出错”}"
         */
        @JavascriptInterface
        public void getQDLocationInfo(String json) {
            ToastUtils.showLong(getContext(), "getQDLocationInfo");
        }

        /**
         * 下载
         * <p>
         * fileInfo
         * fCallback downLoadFinish
         * type      "IMAGE"
         */
        @JavascriptInterface
        public void goDownload(String json) {
            ToastUtils.showLong(getContext(), "goDownload");
        }

        /**
         * isTrue     配合goNative的参数callFun一起用，为true：goNative支持返回时调用前端函数
         * urlPort    前端页面路径（路由
         * networkUrl 外网路径
         * isBack     是否显示返回按钮，如果是打开外网链接的话，打开的页面就需要一个返回按钮了，可以悬浮在左下角，点击返回上一个页面
         */
        @JavascriptInterface
        public void goView(String json) {
            LogUtils.i("json="+json);
            GoViewBeaan goViewBeaan = GsonUtils.json2Bean(json,GoViewBeaan.class);
            startWebViewActivity(goViewBeaan.getUrlPort());
        }

        private void startWebViewActivity(String url) {
            Intent intent = new Intent(getContext(), WebViewActivity.class);
            intent.putExtra("url", url);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        /**
         * 改变状态栏颜色
         *
         * @param color
         */
        @JavascriptInterface
        public void updateTintColor(String color) {
            ToastUtils.showLong(getContext(), "updateTintColor");
//            StatusBarUtil.setStatusBarColor(getActivity(),color);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            ArrayList<Media> select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            Boolean isOrigin = data.getBooleanExtra(PickerConfig.IS_ORIGIN, false);
            for (final Media media : select) {
                Log.i("media", media.toString());
                Uri mediaUri = Uri.parse("file://" + media.path);
//                Glide.with(this)
//                        .load(mediaUri)
//                        .into(imageView);
            }
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

    private void startGroupChatActivity(final String json) {

        ChatBean chatBean = GsonUtils.json2Bean(json, ChatBean.class);
        Intent intent = new Intent(getContext(), GroupChatActivity.class);
        intent.putExtra("chatId", chatBean.getChatId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}