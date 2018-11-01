package com.markLove.Xplan.base;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

import com.markLove.Xplan.ui.fragment.GroupFragment;
import com.markLove.Xplan.utils.LogUtils;
import com.networkengine.entity.MyFileFavorite;

public class BaseJsInterface {

    private Activity mActivity;

    public BaseJsInterface(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @JavascriptInterface
    public void exitApp(String json) {
        App.getInstance().outLogin(mActivity);
    }
}
