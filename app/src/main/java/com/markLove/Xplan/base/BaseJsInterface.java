package com.markLove.Xplan.base;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.cjt2325.cameralibrary.util.LogUtil;
import com.markLove.Xplan.bean.ChatBean;
import com.markLove.Xplan.bean.GoNativeBean;
import com.markLove.Xplan.bean.GoViewBeaan;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.ui.activity.CpChatActivity;
import com.markLove.Xplan.ui.activity.GroupChatActivity;
import com.markLove.Xplan.ui.activity.LauncherActivity;
import com.markLove.Xplan.ui.activity.MainActivity;
import com.markLove.Xplan.ui.activity.ShopChatActivity;
import com.markLove.Xplan.ui.activity.SingleChatActivity;
import com.markLove.Xplan.ui.activity.WebViewActivity;
import com.markLove.Xplan.utils.AppManager;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;

public class BaseJsInterface {

    /**
     * 用户信息
     */
    public static final String USER_INFO_URL = "file:///android_asset/package/main/index.html#/user/native/1";
    /**
     * 组局首页
     */
    public static final String GROUP_INFO_URL = "file:///android_asset/package/main/index.html#/bureau/native/1";
    /**
     * 消息
     */
    public static final String MSG_INFO_URL = "file:///android_asset/package/main/index.html#/message/native/1";
    /**
     * 好玩
     */
    public static final String PLAY_INFO_URL = "file:///android_asset/package/main/index.html#/find/interesting";
    /**
     * 组局成员
     */
    public static final String GROUP_MEMBER_INFO_URL = "file:///android_asset/package/main/index.html#/bureau/member/";
    /**
     * 邀请好友
     */
    public static final String IMVITATION_URL = "file:///android_asset/package/main/index.html#/bureau/invitation/";
    /**
     * 玩伴
     */
    public static final String PLAYMATE_URL = "file:///android_asset/package/main/index.html#/find/playmate";
    /**
     * 180cp
     */
    public static final String CP_URL = "file:///android_asset/package/main/index.html#/find/180cp";
    /**
     * 登录
     */
    public static final String LOGIN_URL = "file:///android_asset/package/main/index.html#/login/password";
    /**
     * 注册
     */
    public static final String REGISTER_URL = "file:///android_asset/package/main/index.html#/login/registration";
    /**
     * 商户成员
     */
    public static final String MERCHANT_MEMBER_URL = "file:///android_asset/package/main/index.html#/find/store/member/";

    private Activity mActivity;
    private GoViewBeaan goViewBeaan;

    public BaseJsInterface(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @JavascriptInterface
    public void toHomePage(String userInfo) {
//            ToastUtils.showLong(LoginActivity.this, "toHomePage");
        LogUtil.i("userInfo= "+userInfo);
        PreferencesUtils.putString(mActivity,PreferencesUtils.KEY_USER,userInfo);
        UserBean userBean = GsonUtils.json2Bean(userInfo,UserBean.class);
        PreferencesUtils.putInt(mActivity,Constants.ME_USER_ID,userBean.getUserInfo().getUserId());
        PreferencesUtils.putString(mActivity,Constants.ME_HEAD_IMG_URL,userBean.getUserInfo().getHeadImageUrl());
        PreferencesUtils.putString(mActivity,Constants.TOKEN_KEY,userBean.getToken());
        Intent intent = new Intent(mActivity,MainActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
        AppManager.getAppManager().finishActivity(LauncherActivity.class);
    }

    @JavascriptInterface
    public void exitApp(String json) {
        App.getInstance().outLogin(mActivity);
    }

    @JavascriptInterface
    public void exitApp() {
        App.getInstance().outLogin(mActivity);
    }

    @JavascriptInterface
    public void toChatRoom(String json) {
        LogUtils.i("huang", "toChatRoom=" + json);
        ChatBean chatBean = GsonUtils.json2Bean(json, ChatBean.class);
        switch (chatBean.getChatType()){
            case 1:
                startGroupChatActivity(chatBean);
                break;
            case 2:
                startShopChatActivity(chatBean);
                break;
            case 3:
                startCpChatActivity(chatBean.getDataId());
                break;
            case 4:
                startSingleChatActivity(chatBean);
                break;
        }
    }

    @JavascriptInterface
    public void goNative(String json) {
        LogUtils.i("huang", "goNative=" + json);
        GoNativeBean goNativeBean = GsonUtils.json2Bean(json,GoNativeBean.class);
        if (goNativeBean == null || goNativeBean.getCallFun().isEmpty()){
            mActivity.finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra("goNativeBean",goNativeBean);
            mActivity.setResult(Activity.RESULT_OK,intent);
            mActivity.finish();
        }
    }

    /**
     * 拍照
     * <p>
     * uploadUrl
     * sCallback photoFinish
     */
    @JavascriptInterface
    public void goPhoto(String json) {
    }

    @JavascriptInterface
    public void fromImgLibrary(String json) {

    }

    /**
     * 定位
     * <p>
     * sCallback 成功回调
     * fCallback 失败回调  "{“status”:”error”,”msg”:”出错”}"
     */
    @JavascriptInterface
    public void getQDLocationInfo(String json) {
//            ToastUtils.showLong(getContext(), "getQDLocationInfo");
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
    }

    /**
     * isTrue     配合goNative的参数callFun一起用，为true：goNative支持返回时调用前端函数
     * urlPort    前端页面路径（路由
     * networkUrl 外网路径
     * isBack     是否显示返回按钮，如果是打开外网链接的话，打开的页面就需要一个返回按钮了，可以悬浮在左下角，点击返回上一个页面
     */
    @JavascriptInterface
    public void goView(String json) {
        LogUtils.i("goView="+json);
        goViewBeaan = GsonUtils.json2Bean(json,GoViewBeaan.class);
        startWebViewActivity(goViewBeaan.getUrlPort());
    }

    /**
     * 改变状态栏颜色
     *
     * @param color
     */
    @JavascriptInterface
    public void updateTintColor(String color) {
    }

    private void startGroupChatActivity(ChatBean chatBean) {
        Intent intent = new Intent(mActivity, GroupChatActivity.class);
        intent.putExtra("chatId", chatBean.getChatId());
        intent.putExtra("dataId", chatBean.getDataId());
        mActivity.startActivityForResult(intent,Constants.REQUEST_CODE_GROUP_CHAT);
    }

    private void startShopChatActivity(ChatBean chatBean) {
        Intent intent = new Intent(mActivity, ShopChatActivity.class);
        intent.putExtra("chatId", chatBean.getChatId());
        intent.putExtra("dataId", chatBean.getChatId());
        mActivity.startActivityForResult(intent,Constants.REQUEST_CODE_MERCHANT_CHAT);
    }

    private void startSingleChatActivity(ChatBean chatBean) {
        Intent intent = new Intent(mActivity, SingleChatActivity.class);
        intent.putExtra("chatId", chatBean.getDataId());
        intent.putExtra("nick_name", chatBean.getDataName());
        intent.putExtra("headImgUrl", chatBean.getHeadImgUrl());
        mActivity.startActivity(intent);
    }

    private void startCpChatActivity(int id) {
        Intent intent = new Intent(mActivity, CpChatActivity.class);
        intent.putExtra("chatId", id);
        mActivity.startActivity(intent);
    }

    private void startWebViewActivity(String url) {
        Intent intent = new Intent(mActivity, WebViewActivity.class);
        intent.putExtra("url", url);
        mActivity.startActivityForResult(intent,Constants.REQUEST_CODE_WEB);
    }
}
