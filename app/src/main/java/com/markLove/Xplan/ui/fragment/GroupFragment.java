package com.markLove.Xplan.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.SinglePickerActivity;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseFragment;
import com.markLove.Xplan.bean.ChatBean;
import com.markLove.Xplan.bean.GoNativeBean;
import com.markLove.Xplan.bean.GoViewBeaan;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.ui.activity.GroupChatActivity;
import com.markLove.Xplan.ui.activity.PublishActivity;
import com.markLove.Xplan.ui.activity.WebViewActivity;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.ToastUtils;

public class GroupFragment extends BaseFragment {
    private MyWebView mWebView;
    private GoViewBeaan goViewBeaan;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_team;
    }

    @Override
    protected void init(View view) {
        mWebView = new MyWebView(getContext());
        LinearLayout mll = view.findViewById(R.id.rootView);
        //避免内存泄露，采用动态添加的方式
        mll.addView(mWebView);
        initWebSettings();
    }

    /**
     * 设置websetting
     */
    private void initWebSettings() {
        mWebView.addJavascriptInterface(new BaseJsInterface(getActivity()), "xplanfunc");

        mWebView.loadUrl(BaseJsInterface.GROUP_INFO_URL);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("huang","onActivityResult---->"+data);
        if (data == null) return;
        if (requestCode == Constants.REQUEST_CODE_WEB && resultCode == getActivity().RESULT_OK) {
            if (goViewBeaan != null && goViewBeaan.isIsTrue()){
                GoNativeBean goNativeBean = (GoNativeBean) data.getSerializableExtra("goNativeBean");
                mWebView.loadUrl("javascript:"+goNativeBean.getCallFun()+"(" + goNativeBean.getParam() + ")");
            }
        }
        //组局聊天室返回
        if (requestCode == Constants.REQUEST_CODE_GROUP_CHAT && resultCode == getActivity().RESULT_OK) {
            //当组局解散，刷新组局
            mWebView.loadUrl("javascript:refreshBureauList()");
        }
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
