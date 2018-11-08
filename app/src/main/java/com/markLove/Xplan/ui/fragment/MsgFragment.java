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
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMBoxMessage;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMOfficialMessage;
import com.xsimple.im.engine.IMEngine;

import java.util.ArrayList;
import java.util.List;

public class MsgFragment extends BaseFragment {
    private MyWebView mWebView;
    private DbManager dbManager;
    private long boxChatId;
    private long officialChatId;

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

        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void toChatRoom(String json) {
            LogUtils.i("huang", "toChatRoom=" + json);
            ChatBean chatBean = GsonUtils.json2Bean(json, ChatBean.class);
            switch (chatBean.getChatType()) {
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
            LogUtils.i("json=" + json);
            GoViewBeaan goViewBeaan = GsonUtils.json2Bean(json, GoViewBeaan.class);
            startWebViewActivity(goViewBeaan.getUrlPort());
//            startPublishActivity();
        }

        /**
         * 获取所有的盒子消息
         */
        @JavascriptInterface
        private String getBoxMessageList() {
            List<IMBoxMessage> imBoxMessageList = dbManager.loadBoxIMMessage(boxChatId);
            return GsonUtils.obj2Json(imBoxMessageList);
        }

        /**
         * 获取所有的官方消息方法
         */
        @JavascriptInterface
        private String getOfficialMessageList() {
            List<IMOfficialMessage> imOfficialMessageList = dbManager.loadOfficialIMMessage(officialChatId);
            return GsonUtils.obj2Json(imOfficialMessageList);
        }

        /**
         * 获取消息列表
         * @return
         */
        @JavascriptInterface
        private String getMessageInfo() {
            return initMsgData();
        }

        /**
         * 删除会话
         * @return
         */
        @JavascriptInterface
        private void deleteChat(String json) {
            MsgBean msgBean = GsonUtils.json2Bean(json,MsgBean.class);
            if (msgBean.type == IMChat.SESSION_GROUP_CLUSTER || msgBean.type == IMChat.SESSION_PERSON){
                dbManager.deleteIMChat(msgBean.getId());
            } else if (msgBean.type == IMChat.SESSION_BOX_MSG){
                dbManager.deleteBoxMessage(msgBean.getId());
            } else if (msgBean.type == IMChat.SESSION_OFFICIAL_MSG){
                dbManager.deleteOfficialMessage(msgBean.getId());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.loadUrl("javascript:setMessageInfo(" + initMsgData() + ")");
    }

    /**
     * 初始化列表消息
     *
     * @return
     */
    private String initMsgData() {
        IMEngine mImEngine = IMEngine.getInstance(getContext());
        List<IMChat> chats = mImEngine.getChats(App.getInstance().getUserId());
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
            if (chat.getType() == IMChat.SESSION_GROUP_CLUSTER || chat.getType() == IMChat.SESSION_PERSON) {
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
        return GsonUtils.obj2Json(chats);
    }

    private void test() {
        IMBoxMessage imBoxMessage = new IMBoxMessage(Long.parseLong("0"), Long.parseLong("0"), "366930", (long) 213123, false, false, "box", "", "你被提出组局了");
        LogUtils.i("huang", GsonUtils.obj2Json(imBoxMessage));
        IMOfficialMessage imOfficialMessage = new IMOfficialMessage((long) 0, (long) 0, "356665", "das15564789.png", "http://www.baidu.com", (long) 1515588845, false, false, "official", "这是标题", "给你送了大礼包了");
        LogUtils.i("huang", GsonUtils.obj2Json(imOfficialMessage));
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
        LogUtils.i("huang", "chats=" + GsonUtils.obj2Json(chats));
    }

    private void startWebViewActivity(String url) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivityForResult(intent, 200);
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
