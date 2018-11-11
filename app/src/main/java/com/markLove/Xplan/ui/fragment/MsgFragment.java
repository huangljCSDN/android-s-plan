package com.markLove.Xplan.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseFragment;
import com.markLove.Xplan.bean.ChatBean;
import com.markLove.Xplan.bean.GoViewBeaan;
import com.markLove.Xplan.bean.MsgBean;
import com.markLove.Xplan.ui.activity.CpChatActivity;
import com.markLove.Xplan.ui.activity.GroupChatActivity;
import com.markLove.Xplan.ui.activity.ShopChatActivity;
import com.markLove.Xplan.ui.activity.SingleChatActivity;
import com.markLove.Xplan.ui.activity.WebViewActivity;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.networkengine.util.LogUtil;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMBoxMessage;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMOfficialMessage;
import com.xsimple.im.engine.IMEngine;

import java.util.ArrayList;
import java.util.List;

public class MsgFragment extends BaseFragment {
    private static final String TAG = "MsgFragment";
    private MyWebView mWebView;
    private DbManager dbManager;
    private long boxChatId;
    private long officialChatId;
    private ChatBean chatBeanForMsgList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void init(View view) {
        mWebView = new MyWebView(getContext());
        LinearLayout mll = view.findViewById(R.id.rootView);
        mll.addView(mWebView);
        dbManager = DbManager.getInstance(getContext());
        initWebSettings();
    }

    /**
     * 设置websetting
     */
    private void initWebSettings() {
        mWebView.addJavascriptInterface(new JSInterface(getActivity()), "xplanfunc");
        mWebView.loadUrl("file:///android_asset/package/main/index.html#/message/native/1");
    }

    public class JSInterface extends BaseJsInterface {

        public JSInterface(Activity mActivity) {
            super(mActivity);
        }

        @JavascriptInterface
        public void goView(String json) {
            LogUtils.i("json=" + json);
            GoViewBeaan goViewBeaan = GsonUtils.json2Bean(json, GoViewBeaan.class);
            startWebViewActivity(goViewBeaan.getUrlPort());
//            startPublishActivity();
        }

        /**
         * 获取消息列表
         *
         * @return
         */
        @JavascriptInterface
        public void getMessageInfo(String json) {
            LogUtils.i(TAG, "getMessageInfo=" + json.toString());
            chatBeanForMsgList = GsonUtils.json2Bean(json, ChatBean.class);
            refreshMsgListData();
        }

        /**
         * 删除会话
         *
         * @return
         */
        @JavascriptInterface
        public void deleteChat(String json) {
            MsgBean msgBean = GsonUtils.json2Bean(json, MsgBean.class);
            if (msgBean.type == IMChat.SESSION_GROUP_CLUSTER || msgBean.type == IMChat.SESSION_GROUP_DISCUSSION
                    || msgBean.type == IMChat.SESSION_PERSON) {
                dbManager.deleteIMChat(msgBean.getId());
            } else if (msgBean.type == IMChat.SESSION_BOX_MSG) {
                dbManager.deleteBoxMessage(msgBean.getId());
            } else if (msgBean.type == IMChat.SESSION_OFFICIAL_MSG) {
                dbManager.deleteOfficialMessage(msgBean.getId());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMsgListData();
    }

    /**
     * 刷新消息列表数据
     */
    private void refreshMsgListData() {
        if (chatBeanForMsgList != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:" + chatBeanForMsgList.getsCallback() + "(" + getMsgData() + ")");
                }
            });
        }
    }

    /**
     * 初始化列表消息
     *
     * @return
     */
    private String getMsgData() {
        IMEngine mImEngine = IMEngine.getInstance(getContext());
        List<IMChat> chats = mImEngine.getChats(App.getInstance().getUserId());
        if (chats == null || chats.size() <= 0) return "";

        //将盒子消息移到第一位
        for (int i = 0; i < chats.size(); i++) {
            IMChat chat = chats.get(i);
            if (chat.getType() == IMChat.SESSION_BOX_MSG && i != 0) {
                chats.add(0, chats.remove(i));
                break;
            }
        }
        //将官方消息移到第二位
        if (chats.size() > 1) {
            for (int i = 0; i < chats.size(); i++) {
                IMChat chat = chats.get(i);
                if (chat.getType() == IMChat.SESSION_OFFICIAL_MSG && i != 1) {
                    chats.add(1, chats.remove(i));
                    break;
                }
            }
        }

        //获取会话最后一条消息
        for (IMChat chat : chats) {
            if (chat.getType() == IMChat.SESSION_GROUP_CLUSTER || chat.getType() == IMChat.SESSION_GROUP_DISCUSSION
                    || chat.getType() == IMChat.SESSION_PERSON) {
                List<IMMessage> list = dbManager.loadLastMessage(chat.getId());
                chat.setIMMessages(list);
            } else if (chat.getType() == IMChat.SESSION_BOX_MSG) {
                boxChatId = chat.getId();
                List<IMBoxMessage> list = dbManager.loadLastBoxMessage(chat.getId());
                chat.setIMBoxMessage(list);
            } else if (chat.getType() == IMChat.SESSION_OFFICIAL_MSG) {
                officialChatId = chat.getId();
                List<IMOfficialMessage> list = dbManager.loadLastOfficialMessage(chat.getId());
                chat.setIMOfficialMessage(list);
            }
        }
        LogUtils.i(TAG, "chats=" + GsonUtils.obj2Json(chats));
        return GsonUtils.obj2Json(chats);
    }

    private void test() {
        IMBoxMessage imBoxMessage = new IMBoxMessage(Long.parseLong("0"), Long.parseLong("0"), "366930", (long) 213123, false, false, "box", "", "你被提出组局了");
        LogUtils.i(TAG, GsonUtils.obj2Json(imBoxMessage));
        IMOfficialMessage imOfficialMessage = new IMOfficialMessage((long) 0, (long) 0, "356665", "das15564789.png", "http://www.baidu.com", (long) 1515588845, false, false, "official", "这是标题", "给你送了大礼包了");
        LogUtils.i(TAG, GsonUtils.obj2Json(imOfficialMessage));
        IMEngine mImEngine = IMEngine.getInstance(getContext());
        List<IMChat> chats = mImEngine.getChats(App.getInstance().getUserId());
        for (IMChat chat : chats) {
            if (chat.getType() == IMChat.SESSION_GROUP_CLUSTER || chat.getType() == IMChat.SESSION_PERSON) {
                List<IMMessage> list = IMEngine.getInstance(getContext()).getDbManager().loadLastMessage(chat.getId());
                chat.setIMMessages(list);
            } else if (chat.getType() == IMChat.SESSION_BOX_MSG) {
                List<IMBoxMessage> list = IMEngine.getInstance(getContext()).getDbManager().loadLastBoxMessage(chat.getId());
                chat.setIMBoxMessage(list);
            } else if (chat.getType() == IMChat.SESSION_OFFICIAL_MSG) {
                List<IMOfficialMessage> list = IMEngine.getInstance(getContext()).getDbManager().loadLastOfficialMessage(chat.getId());
                chat.setIMOfficialMessage(list);
            }
        }
        List<IMOfficialMessage> officialMessages = new ArrayList<>();
        officialMessages.add(imOfficialMessage);
        List<IMBoxMessage> imBoxMessages = new ArrayList<>();
        imBoxMessages.add(imBoxMessage);
        chats.get(1).setIMOfficialMessage(officialMessages);
        chats.get(0).setIMBoxMessage(imBoxMessages);
        LogUtils.i(TAG, "chats=" + GsonUtils.obj2Json(chats));
    }

    private void startWebViewActivity(String url) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivityForResult(intent, 200);
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
