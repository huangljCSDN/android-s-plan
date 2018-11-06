package com.markLove.Xplan.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.bean.msg.body.FileMessageBody;
import com.markLove.Xplan.bean.msg.body.TxtMessageBody;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.module.image.IImageCompressor;
import com.markLove.Xplan.mvp.contract.ChatView;
import com.markLove.Xplan.mvp.contract.UserOperationContract;
import com.markLove.Xplan.mvp.presenter.ChatPresenter;
import com.markLove.Xplan.mvp.presenter.UserOperationPresenter;
import com.markLove.Xplan.mvp.presenter.impl.ChatPresenterImpl;
import com.markLove.Xplan.ui.adapter.CpChatMessageAdapter;
import com.markLove.Xplan.ui.dialog.CareOtherDialog;
import com.markLove.Xplan.ui.dialog.ExitRoomDialog;
import com.markLove.Xplan.ui.dialog.OtherExitRoomDialog;
import com.markLove.Xplan.ui.dialog.RemoveMsgDialog;
import com.markLove.Xplan.ui.dialog.ResendMsgDialog;
import com.markLove.Xplan.utils.AudioUtils;
import com.markLove.Xplan.utils.DensityUtils;
import com.markLove.Xplan.utils.ImageUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;
import com.markLove.Xplan.utils.ToastUtils;
import com.networkengine.controller.callback.RouterCallback;
import com.networkengine.database.table.Member;
import com.networkengine.engine.LogicEngine;
import com.networkengine.entity.MemEntity;
import com.xsimple.im.control.IMChatLogic;
import com.xsimple.im.control.MessagerLoader;
import com.xsimple.im.control.iable.IIMChatLogic;
import com.xsimple.im.control.listener.IMChatCallBack;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMGroupRemark;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.IMEngine;
import com.xsimple.im.engine.protocol.IMCommand;
import com.xsimple.im.event.ExitGroupEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 三分钟情侣聊天
 */
public class CpChatActivity extends BaseActivity<UserOperationPresenter> implements View.OnClickListener, ChatView, UserOperationContract.View,IMChatCallBack {
    private ArrayList<Media> select;

    private TextView mTvTime,mTvCare,mTvTitle;
    private RecyclerView rlChatMsgList;
    private RelativeLayout mRlTitleBar;
    private RelativeLayout mRlCancel;
    private RelativeLayout mRlRemove;
    private com.markLove.Xplan.ui.widget.ChatView chatView;
    private int time = 180;
    MyHandler myHandler;

    ChatPresenter chatPresenter;
    CpChatMessageAdapter chatMessageAdapter;
    String nickName;
    String headImgUrl;
    //    AutoCameraUtils autoCameraUtils;
    int me_user_id;
    int to_user_id;
    boolean isEnd = false;
    boolean isLikeAndUser = false;
    boolean isBlackUser = false;
    boolean keyboardIsShown = false;
    int usableHeightPrevious = 0;
    /**
     * 是否关注了
     */
    private boolean isCared;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_cp_chat;
    }

    @Override
    public UserOperationPresenter onCreatePresenter() {
        return new UserOperationPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fullScreen(this);
        rlChatMsgList = findViewById(R.id.chat_msg_list);
        mRlTitleBar = findViewById(R.id.rl_title_bar);
        mRlCancel = findViewById(R.id.rl_cancel);
        mRlRemove = findViewById(R.id.rl_remove);
        mTvTime = findViewById(R.id.tv_time);
        mTvCare = findViewById(R.id.tv_care);
        mTvTitle = findViewById(R.id.title);

        findViewById(R.id.fl_back).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.iv_remove).setOnClickListener(this);
        findViewById(R.id.tv_care).setOnClickListener(this);

        chatView = findViewById(R.id.chatView);
        chatView.setActivity(this);
        chatView.setOnSendMessageListener(new com.markLove.Xplan.ui.widget.ChatView.OnSendMessageListener() {
            @Override
            public void onSendMessage(Message message) {
                if (message.getChatType() == Message.ChatType.TXT){
                    TxtMessageBody txtMessageBody = (TxtMessageBody) message.getBody();
                    mImChatControl.sendMessage(IMMessage.CONTENT_TYPE_TXT,txtMessageBody.getMsg());
                } else {
                    FileMessageBody fileMessageBody = (FileMessageBody) message.getBody();
                    sendVoice(fileMessageBody.getFilePath());
                }
            }
        });

        rlChatMsgList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = DensityUtils.dip2px(CpChatActivity.this, 10f);
                outRect.top = DensityUtils.dip2px(CpChatActivity.this, 10f);
            }
        });

        rlChatMsgList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                chatMessageAdapter.dismissItemPopup();
                closeSoftKeyBoard();
                chatView.hideView();
                return false;
            }
        });
        initSoftKeyboard();
        initData();
        //倒计时
        mTvTime.setText(time+"s");
        myHandler = new MyHandler();
        myHandler.sendEmptyMessageDelayed(1,1000);
        initIm();
    }

    @Override
    public void onFocus(BaseBean baseBean) {
        if (!isFinish){
            isCared = true;
            mTvCare.setText(getString(R.string.cared));
            mTvCare.setBackground(getDrawable(R.drawable.bg_care_gray));
            mTvCare.setClickable(false);
        } else {
            finish();
        }
    }

    @Override
    public void isBlackSuccess(BaseBean baseBean) {

    }

    @Override
    public void isBlackError(String baseBean) {

    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    if (time > 0){
                        if (time == 60){
                            mTvCare.setVisibility(View.VISIBLE);
                        }
                        myHandler.removeMessages(1);
                        myHandler.sendEmptyMessageDelayed(1,1000);
                        time --;
                        mTvTime.setText(time + "s");
                    } else {
                        myHandler.removeMessages(1);
                        if(!isCared){
                            showCareDialog();
                        } else {
                            finish();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                cancelRemove();
                break;
            case R.id.iv_remove:
                showRemoveDialog();
                break;
            case R.id.fl_back:
                showExitDialog();
                break;
            case R.id.tv_care:
                requestFocus();
                break;
        }
    }

    private void requestFocus() {
        Map<String, String> map = new HashMap<>();
        map.put("targetUserId", String.valueOf(to_user_id));
        map.put("type", "1");
        mPresenter.focus(map);
    }

    protected void initData() {
        mTvTitle.setText("找不到话题");
        boolean isLove = PreferencesUtils.getBoolean(this, Constants.USER_IS_LOVES_INFO_KEY);
        if (isLove) {
            int femaleID = PreferencesUtils.getInt(this, Constants.FEMALE_USER_ID, 0);
            int maleID = PreferencesUtils.getInt(this, Constants.MALE_USER_ID, 0);
            if (femaleID != 0 && maleID != 0) {
                if ((femaleID == me_user_id && maleID == to_user_id) || (femaleID == to_user_id && maleID == me_user_id)) {
                    isLikeAndUser = true;
                }
            }
        }
        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("data");
//        if (null == bundle) {
////            ToastUtils.showCenter(this, "无效的接受者", 0);
//            finish();o
//        } else {
//            to_user_id = Integer.parseInt(bundle.getString("user_id"));
//            nickName = bundle.getString("nick_name");
//            headImgUrl = bundle.getString("head_img_url");
//        }
        to_user_id = getIntent().getIntExtra("chatId", 0);
        me_user_id = PreferencesUtils.getInt(this, Constants.ME_USER_ID);
        LogUtils.d("me_user_id=" + me_user_id);
        LogUtils.d("to_user_id=" + to_user_id);
//        tvChatUser.setText(nickName);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatMessageAdapter = new CpChatMessageAdapter(this, new ArrayList<Message>());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        String url = FileUtil.saveBitmap("haha", bitmap);
//        chatMessageAdapter.setToHeadImgUrl(headImgUrl);
        chatMessageAdapter.setToHeadImgUrl(url);
        chatMessageAdapter.setFromHeadImgUrl(PreferencesUtils.getString(this, Constants.ME_HEAD_IMG_URL));
        chatMessageAdapter.setFailMessageResend(failMessageResend);
        manager.setStackFromEnd(false);
        rlChatMsgList.setLayoutManager(manager);
        rlChatMsgList.setAdapter(chatMessageAdapter);

        chatMessageAdapter.registerAdapterDataObserver(adapterDataObserver);


        chatPresenter = new ChatPresenterImpl();
        chatPresenter.setView(this);
        UserBean userBean = App.getInstance().getUserBean();
        int sex = userBean.getUserInfo().getSex();
        if (sex == 1){
            chatMessageAdapter.setBetweenUserSex(1,2);
        } else {
            chatMessageAdapter.setBetweenUserSex(2,1);
        }
        chatView.setId(me_user_id,to_user_id);
//        tvChatSendPrice.setText(gold + "");
//        chatPresenter.getHistory(me_user_id, to_user_id);
//        getGiftList();
        getChatOpen();
    }

    private void getChatOpen() {
//        Map<String, String> params = new HashMap<>();
//        params.put("Token", PreferencesUtils.getString(this, Constants.TOKEN_KEY));
//        OkGoHttpUtils.get(Constants.GET_JUDE_CHAT_OPEN, params, new OkGoHttpCallBack() {
//        @Override
//        public void onSuccess(String s, Call call, Response response) {
//            ChatOpen chatOpen = GsonUtils.json2Bean(s, ChatOpen.class);
//            if (chatOpen.isHaveData()) {
//                ChatOpen.DtBean dtBean = chatOpen.getDt().get(0);
//                if ("1".equals(dtBean.getChatOpen())) {
//                    PreferencesUtils.putBoolean(ShopChatActivity.this, Constants.CHAT_OPEN_KEY, true);
//                } else {
//                    PreferencesUtils.putBoolean(ShopChatActivity.this, Constants.CHAT_OPEN_KEY, false);
//                }
//            }
//        }
//    });
    }

    RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            rlChatMsgList.post(new Runnable() {
                @Override
                public void run() {
                    if (null != rlChatMsgList) {
                        rlChatMsgList.smoothScrollToPosition(chatMessageAdapter.getItemCount());
                    }
                }
            });
        }
    };

    CpChatMessageAdapter.FailMessageResend failMessageResend = new CpChatMessageAdapter.FailMessageResend() {
        @Override
        public void failResend(Message msg) {
            closeSoftKeyBoard();
            resendMessage(msg);
        }
    };

    /**
     * 重新发送消息
     * @param resendMessage
     */
    private void resendMessage(final Message resendMessage) {
        ResendMsgDialog resendMsgDialog = new ResendMsgDialog(this);
        resendMsgDialog.setOnMenuClickListener(new ResendMsgDialog.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                updataMessage(resendMessage.getMsgID(), Message.ChatStatus.SENDING.ordinal());
                judeBlackList(resendMessage);
            }
        });
        resendMsgDialog.show();
    }

    /**
     * 显示历史消息
     * @param historyMessageList
     */
    @Override
    public void showHistoryMessage(final List<Message> historyMessageList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatMessageAdapter.addAll(historyMessageList);
            }
        });
    }

    /**
     * 添加一条消息
     * @param msg
     */
    @Override
    public void addOneMessage(final Message msg) {
        if ((msg.getToID() == me_user_id && msg.getFromID() == to_user_id)
                || (msg.getToID() == to_user_id && msg.getFromID() == me_user_id)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != chatMessageAdapter) {
                        chatMessageAdapter.addOne(msg);
                    }
                }
            });
        }
    }

    @Override
    public void updataMessage(final String msgID, final int errorCode) {
        if (Message.ChatStatus.values()[errorCode] == Message.ChatStatus.SUCCESS) {
            if (null != chatMessageAdapter) {
                Message message = chatMessageAdapter.getMessageByID(msgID);
                if (message != null) {
                    if (message.getFromID() == PreferencesUtils.getInt(this, Constants.ME_USER_ID) && (message.getChatType() == Message.ChatType.IMAGE || message.getChatType() == Message.ChatType.VOICE) && message.getStatus() == Message.ChatStatus.SUCCESS) {
                        Message.ChatType chatType = message.getChatType() == Message.ChatType.IMAGE ? Message.ChatType.IMAGE_DESC : Message.ChatType.VOICE_DESC;
                        FileMessageBody fileMessageBody = (FileMessageBody) message.getBody();
                        sendMessage(Message.createDescriptionMessage(message.getFromID(), message.getToID(), Message.Type.CHAT, chatType, fileMessageBody.getFileName()));
                    }
                }
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != chatMessageAdapter) {
                    try {
                        int position = chatMessageAdapter.getItemPositionByMsgID(msgID);
                        if (position >= 0) {
                            Message.ChatStatus status = Message.ChatStatus.values()[errorCode];
                            List<Message.ChatStatus> payloads = new ArrayList<Message.ChatStatus>();
                            payloads.add(status);
                            chatMessageAdapter.notifyItemRangeChanged(position, 1, status);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 判断是否被拉黑
     *
     * @param msg
     */
    private void judeBlackList(final Message msg) {
        if (null != msg) {
            int sendCount = PreferencesUtils.getInt(this, Constants.SEND_MESSAGE_COUNT + to_user_id, 0);
            //原计数+1
            PreferencesUtils.putInt(this, Constants.SEND_MESSAGE_COUNT + to_user_id, ++sendCount);
            addOneMessage(msg);
        }
        //暂时不判断拉黑逻辑，直接发送
        sendMessage(msg);

//        StringBuilder sb = new StringBuilder(Constants.POST_JUDGE_BLACK_LIST);
//        sb.append("?ToUserId=" + to_user_id);
//        sb.append("&Token=" + PreferencesUtils.getString(this, Constants.TOKEN_KEY));
//        OkGoHttpUtils.post(sb.toString(), new OkGoHttpCallBack() {
//            @Override
//            public void onSuccess(String s, Call call, Response response) {
//                try {
//                    InfoResultData infoResultData = GsonUtils.json2Bean(s, InfoResultData.class);
//                    if (infoResultData.getStatus() == 200) {
//                        double data = (Double) infoResultData.getData();
//                        if (data == 1) {
//                            //已经被拉黑
//                            chatMessageAdapter.setBlack(true);
//                            isBlackUser = true;
//                            if (null != msg && (msg.getChatType() != Message.ChatType.GIFT || msg.getChatType() != Message.ChatType.SUPERLIKE)) {
//                                msg.setStatus(Message.ChatStatus.REJECTED);
//                                updataMessage(msg.getMsgID(), Message.ChatStatus.REJECTED.ordinal());
//                                int me_user_id = PreferencesUtils.getInt(App.getInstance(), Constants.ME_USER_ID);
//                                DBDao.getDbDao(App.getInstance()).insertMessage(me_user_id, msg);
//                            }
//                        } else {
//                            chatMessageAdapter.setBlack(false);
//                            isBlackUser = false;
//                            if (null != msg) {
////                                if (msg.getChatType() == Message.ChatType.SUPERLIKE) {
////                                    sendSuperLikeDecuctionMoney(msg, 1);
////                                } else {
//                                sendMessage(msg);
////                                }
//                            }
//                        }
////                        initBecomeCouple();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Call call, Response response, Exception e) {
//                super.onError(call, response, e);
//                if (null != msg) {
//                    int sendCount = PreferencesUtils.getInt(ShopChatActivity.this, Constants.SEND_MESSAGE_COUNT + to_user_id, 0);
//                    //原计数-1
//                    PreferencesUtils.putInt(ShopChatActivity.this, Constants.SEND_MESSAGE_COUNT + to_user_id, --sendCount);
//                    updataMessage(msg.getMsgID(), Message.ChatStatus.FAIL.ordinal());
//                    int me_user_id = PreferencesUtils.getInt(App.getInstance(), Constants.ME_USER_ID);
//                    DBDao.getDbDao(App.getInstance()).insertMessage(me_user_id, msg);
//                }
//            }
//        });
    }

    /**
     * 发送消息
     *
     * @param message
     */
    private void sendMessage(Message message) {
//        DBDao.getDbDao(this).insertMessage(me_user_id, message);
        boolean isOpen = PreferencesUtils.getBoolean(this, Constants.CHAT_OPEN_KEY, true);
        String token = PreferencesUtils.getString(this, Constants.TOKEN_KEY);
        LogUtils.d("------->token=" + token + ",isOpen=" + isOpen);
        if (isOpen) {
//            ChatClient.getInstance().sendMessage(token, message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rlChatMsgList.scrollToPosition(chatMessageAdapter.getItemCount());
                }
            });
        } else {
            if (message.getChatType() == Message.ChatType.LOVE) {
                PreferencesUtils.putLong(this, Constants.BECOME_COUPLE_TIME_KEY + to_user_id, 0);
            }
            ToastUtils.showCenter(this, "该服务正在紧急维护当中，请稍后再试", 0);
            updataMessage(message.getMsgID(), Message.ChatStatus.FAIL.ordinal());
        }
    }

    /**
     * 图片返回处理
     *
     * @param uri
     * @param filePath
     * @param isOrigin 是否原图发送
     */
    public void onImageReturn(Uri uri, String filePath, boolean isOrigin) {
//        String filePath = autoCameraUtils.getPath(this, uri);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        final Message imgMsg = Message.createImageMessage(Message.Type.CHAT, me_user_id, to_user_id,"", fileName, filePath);
        imgMsg.setStatus(Message.ChatStatus.SENDING);
        isOrigin = true;
        if (isOrigin) {
            sendImage(filePath);
//            judeBlackList(imgMsg);
        } else {
            Observable.create(new ObservableOnSubscribe<Message>() {
                @Override
                public void subscribe(final ObservableEmitter<Message> emitter) throws Exception {
                    final FileMessageBody imgMessageBody = (FileMessageBody) imgMsg.getBody();
                    final String outPath = Constants.LOCAL_IMG_PATH + imgMessageBody.getFileName();
                    ImageUtils.compressImageInPath(imgMessageBody.getFilePath(), Constants.LOCAL_IMG_PATH, new IImageCompressor.OnImageCompressListener() {
                        @Override
                        public void onCompressStart(String msg) {

                        }

                        @Override
                        public void onCompressComplete(List<String> destFilePaths) {
                            if (destFilePaths != null && destFilePaths.size() > 0) {
                                imgMessageBody.setFilePath(outPath);
                                emitter.onNext(imgMsg);
                                emitter.onComplete();
                            }
                        }

                        @Override
                        public void onCompressError(String msg) {

                        }
                    });
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Observer<Message>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Message message) {
                            judeBlackList(message);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        if (keyboardIsShown || chatView.isShow()) {
            closeSoftKeyBoard();
            chatView.hideView();
        } else {
            showExitDialog();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        ChatClient.getInstance().isNotification = false;
//        ChatClient.getInstance().currentlyID = to_user_id;
        Log.v("MAIN", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != chatPresenter) {
            chatPresenter.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        ChatClient.getInstance().isNotification = true;
        Log.v("MAIN", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        try {
//            if (chatMessageAdapter.getItemCount() > 0) {
//                Message msg = chatMessageAdapter.getItemMsg(chatMessageAdapter.getItemCount() - 1);
//                msg = DBDao.getDbDao(this).queryLastMessage(me_user_id, to_user_id, msg.getMsgID());
//                ChatUser chatUser = new ChatUser();
//                chatUser.setNickName(nickName);
//                chatUser.setUserID(to_user_id);
//                chatUser.setHeadImgUrl(headImgUrl);
//                chatUser.setChatTime(DataUtils.getDateTime(msg.getMsgTime() != 0 ? msg.getMsgTime() : System.currentTimeMillis()));
//                chatUser.setLastMsgId(msg.getMsgID());
//                chatUser.setUnreadCount(0);
//                chatUser.setLastMsgChatType(msg.getChatType());
//                switch (msg.getChatType()) {
//                    case TXT:
//                        TxtMessageBody txtMessageBody = (TxtMessageBody) msg.getBody();
//                        chatUser.setLastMsgContent(txtMessageBody.getMsg());
//                        break;
//                    case IMAGE:
//                        chatUser.setLastMsgContent("[图片]");
//                        break;
//                    case VOICE:
//                        chatUser.setLastMsgContent("[语音]");
//                        break;
//                    case LOVE:
//                        chatUser.setLastMsgContent("[情侣邀请]");
//                        break;
//                    case SUPERLIKE:
//                        chatUser.setLastMsgContent("[超喜欢]");
//                        break;
//                    case GIFT:
//                        chatUser.setLastMsgContent("[礼物]");
//                        break;
//                    case IMAGE_DESC:
//                        chatUser.setLastMsgContent("[图片]");
//                        break;
//                    case VOICE_DESC:
//                        chatUser.setLastMsgContent("[语音]");
//                        break;
//                    default:
//                        chatUser.setLastMsgContent("");
//                        break;
//                }
//                DBDao.getDbDao(this).insertChatUser(me_user_id, chatUser);
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (null != chatPresenter) {
//            chatPresenter.onPause();
//        }
    }

    @Override
    protected void onDestroy() {
        chatMessageAdapter.unregisterAdapterDataObserver(adapterDataObserver);
//        if (null != handler) {
//            handler.removeCallbacksAndMessages(null);
//        }
        if (null != chatPresenter) {
            chatPresenter.onDestory();
        }
        chatView.onDestroy();
        AudioUtils.getInstance().destory();
        myHandler.removeCallbacksAndMessages(null);
        myHandler = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.i("huang", "requestCode=" + requestCode + "   resultCode=" + resultCode);
            if (requestCode == Constants.REQUEST_CODE_CAMERA) {
                final String path = data.getStringExtra("path");
                Log.i("huang", "path=" + path);
//                Uri mediaUri = Uri.parse("file://" + path);
//                Glide.with(this)
//                        .load(mediaUri)
//                        .into(imageView);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        playVideo(path);
//                    }
//                });
                onImageReturn(null, path, true);
            }
            if (requestCode == Constants.REQUEST_CODE_PICKER) {
                select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                Boolean isOrigin = data.getBooleanExtra(PickerConfig.IS_ORIGIN, false);
                for (final Media media : select) {
                    Log.i("media", media.toString());
                    onImageReturn(null, media.path, isOrigin);
                }
            }
            if (resultCode == 102) {
                Log.i("CJT", "video");
                String path = data.getStringExtra("path");
            }
            if (resultCode == 103) {
                Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_PERMISSION_ONE) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    startCameraActivity2();
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == Constants.REQUEST_CODE_PERMISSION_TWO) {
            int size2 = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size2++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size2++;
                }
                if (size2 == 0) {
//                    showRlRecord();
                    chatView.showRlRecord();
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startCameraActivity2(){
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("type",JCameraView.BUTTON_STATE_ONLY_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
    }

    private boolean isFinish;
    /**
     * 显示关注弹窗
     */
    private void showCareDialog() {
        CareOtherDialog careOtherDialog = new CareOtherDialog(this);
        careOtherDialog.setOnDialogCallBack(new CareOtherDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(int type) {
                //1 关注 2 取消
                if (type == 1){
                    isFinish = true;
                    requestFocus();
                } else {
                    finish();
                }
            }
        });
        careOtherDialog.setCanceledOnTouchOutside(false);
        careOtherDialog.show();
    }

    /**
     * 显示退出提示按钮
     */
    private void showExitDialog() {
        ExitRoomDialog exitRoomDialog = new ExitRoomDialog(this);
        exitRoomDialog.setOnDialogCallBack(new ExitRoomDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {
                finish();
            }
        });
        exitRoomDialog.show();
    }

    /**
     * 显示他人退出提示框
     */
    private void showOtherDialog() {
        OtherExitRoomDialog otherExitRoomDialog = new OtherExitRoomDialog(this);
        otherExitRoomDialog.setOnDialogCallBack(new OtherExitRoomDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {
                finish();
            }
        });
        otherExitRoomDialog.setCanceledOnTouchOutside(false);
        otherExitRoomDialog.show();
    }

    //设置软键盘弹起和关闭的监听
    private void initSoftKeyboard() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int usableHeightNow = computeUsableHeight();
                if (usableHeightNow != usableHeightPrevious) {
                    int usableHeightSansKeyboard = getWindow().getDecorView().getHeight();
                    int heightDifference = usableHeightSansKeyboard - usableHeightNow;
                    if (heightDifference > (usableHeightSansKeyboard / 4)) {
                        keyboardIsShown = true;
                        chatView.hideView();
                    } else {
                        // 键盘收起
                        keyboardIsShown = false;
                    }
                    getWindow().getDecorView().requestLayout();
                    usableHeightPrevious = usableHeightNow;
                }
            }
        });
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    private void closeSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示删除按钮
     */
    public void showRemove() {
        closeSoftKeyBoard();
        chatView.setVisibility(View.GONE);
        chatView.hideView();
        mRlTitleBar.setVisibility(View.GONE);
        mRlCancel.setVisibility(View.VISIBLE);
        mRlRemove.setVisibility(View.VISIBLE);
    }

    PopupWindow removePopup;

    /**
     * 选择要删除的内容
     */
    public void showRemoveDialog() {
        if (!chatMessageAdapter.hasSelectRemovePostion()) {
            RemoveMsgDialog removeMsgDialog = new RemoveMsgDialog(this);
            removeMsgDialog.setOnMenuClickListener(new RemoveMsgDialog.OnMenuClickListener() {
                @Override
                public void onMenuClick() {
                    chatMessageAdapter.selectRemove(2);
                    if (null != removePopup) removePopup.dismiss();
                    mRlCancel.setVisibility(View.GONE);
                    mRlRemove.setVisibility(View.GONE);
                    mRlTitleBar.setVisibility(View.VISIBLE);
                    chatView.setVisibility(View.VISIBLE);
                }
            });
            removeMsgDialog.show();
        } else {
            ToastUtils.showCenter(this, "请选择要删除的内容", 0);
        }
    }

    /**
     * 取消删除
     */
    public void cancelRemove() {
        chatMessageAdapter.selectRemove(0);
        mRlCancel.setVisibility(View.GONE);
        mRlRemove.setVisibility(View.GONE);
        chatView.setVisibility(View.VISIBLE);
        mRlTitleBar.setVisibility(View.VISIBLE);
    }

    public void selectPosition(final int position) {
        rlChatMsgList.post(new Runnable() {
            @Override
            public void run() {
                rlChatMsgList.smoothScrollToPosition(position);
            }
        });
    }


    //--------------------------新im---------------------------//
    /**
     * 聊天控制器
     */
    private IIMChatLogic mImChatControl;

    /**
     * Im　消息引擎
     */
    private IMEngine mImEngine;
    private MessagerLoader mMessagerLoader;
    /**
     * 会话
     */
    private IMChat mImChat;
    private List<IMMessage> mMessagesList;

    private static final String EXTRA_TARGET = "EXTRA_TARGET";
    private static final String EXTRA_SCROLL_TO_MESSAGE = "EXTRA_SCROLL_TO_MESSAGE";
    String chatId; //聊天室id

    /**
     * 直接跳转到某条记录的消息virtualId
     */
    public static final String ACTION_SCROLL_TO_MESSAGE = "im_virtual_id";

    /**
     * 打开聊天界面
     *
     * @param context     上下文
     * @param memEntity   对象
     * @param scrollToMsg 打开界面同时跳转到指定消息
     */
    public static void startMe(Context context, MemEntity memEntity, String scrollToMsg, RouterCallback callback) {
//        routerCallback = callback;
        Intent intent = getStartIntent(context, memEntity);
        intent.putExtra(EXTRA_SCROLL_TO_MESSAGE, scrollToMsg);
        context.startActivity(intent);
    }

    /**
     * 获取跳转Intent
     *
     * @param context   上下文
     * @param memEntity 对象
     * @return 跳转Intent
     */
    public static Intent getStartIntent(Context context, MemEntity memEntity) {
        Intent intent = new Intent(context, ShopChatActivity.class);
        intent.putExtra(EXTRA_TARGET, memEntity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private void initIm(){
        EventBus.getDefault().register(this);
        initMember();
        chatId = String.valueOf(to_user_id);
//        chatId = "369101"; //刘杰的
//        chatId = "369105";
        mImEngine = IMEngine.getInstance(this);

        final MemEntity memEntity = new MemEntity(chatId,"",0);
        mImChatControl = new IMChatLogic.Build() {
            @Override
            public MemEntity setTargetMem() {
                return memEntity;
            }

            @Override
            public IMChatCallBack setIMChatCallBack() {
                return CpChatActivity.this;
            }


            @Override
            public int setRegistCode() {
                return IMEngine.IMENGINE_RIGIST_CODE_CHAT_ACYIVITY;
            }

            @Override
            public String setEventCode() {
                return IMEngine.EVENT_RIGIST_CODE_CHAT_ACYIVITY;
            }

        }.build(this);
        //异常处理
        if (mImChatControl == null) {
            Toast.makeText(this, "启动聊天页面失败", Toast.LENGTH_SHORT).show();
            LogUtils.e("聊天器控制器 is null");
            finish();
            return;
        }
        if (mImChatControl.getMyUid() == null) {
            LogUtils.e("启动聊天页面失败>>>>用户为空----> 自己");
            finish();
            return;
        }

//        if (memEntity.getType() != 0) {
//            IMGroup imGroup = mImChatControl.getIMGroup();
//            if (imGroup == null) {
//                LogUtils.e("启动聊天页面失败>>>>群主为空---->");
//                finish();
//                return;
//            }
//        }
        //设置草稿
//        IMChat imChat = mChatControl.getIMChat();
//        if (imChat != null) {
//            mImChatViewHolder.setDrafts(imChat.getDrafts());
//        }

        mImChat = mImChatControl.getIMChat();
        //刷新会话
        if (mImChat == null) {
            mImChat = mImChatControl.getOrCreateChat();
        }

        LogUtils.i("hhuang",mImChat.toString());
        mMessagerLoader = new MessagerLoader(this, mImChat, mImChatControl);
        //未读数量
//        mUnReadCount = mImChat.getUnReadCount();
//        //获取未读和＠我的消息的本地id的集合
//        unReadOrAitMeIdlist = mMessagerLoader.getUnReadOrAitMeIdlist();
//        //修改消息的阅读状态 发送服务器请求
//        mImChatControl.updataUnReadCount(mMessagerLoader.getCacheMessage());
        //添加第一次加载的数据
        mMessagerLoader.getFirstLoadMessager();
        this.mMessagesList = mMessagerLoader.getShowMessage();
        //刷新布局
        chatMessageAdapter.setImChatControl(mImChatControl);
        chatMessageAdapter.setImData(mMessagesList);
        chatMessageAdapter.notifyDataSetChanged();
        chatView.initEvent(mImChatControl,mImEngine,mMessagerLoader);
    }

    private void initMember(){
        if (LogicEngine.getInstance().getUser() == null){
            String userToken = PreferencesUtils.getString(this,PreferencesUtils.KEY_USER_TOKEN);
            UserBean userBean = App.getInstance().getUserBean();
            Member member = new Member();
            member.setId(String.valueOf(userBean.getUserInfo().getUserId()));
            member.setUserId(String.valueOf(userBean.getUserInfo().getUserId()));
            member.setUserName(userBean.getUserInfo().getNickName());
            member.setUserToken(userToken);
            LogicEngine.getInstance().setUser(member);
        }
    }

    private void sendVoice(String path){
        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
            return;
        }
        List<String> list = new ArrayList<>();
        list.add(path);
        mImChatControl.uploadLocalFiles(list, IMMessage.CONTENT_TYPE_SHORT_VOICE);
    }

    /**
     * 发送照片
     */
    private void sendImage(String path) {
        if (TextUtils.isEmpty(path))
            return;
        File file = new File(path);
        if (!file.exists())
            return;
        mImChatControl.singUploadLocalFiles(path, IMMessage.CONTENT_TYPE_IMG);

//        if (IMMessage.CONTENT_TYPE_IMG.equals(type)) {
//
//        } else {
//            //压缩
////            compressVide(path, type);
//            // mChatControl.singUploadLocalFiles(path, type);
//        }
    }

    /**
     * 发送视频
     * @param path
     */
    private void sendVideo(String path) {
        if (TextUtils.isEmpty(path))
            return;
        File file = new File(path);
        if (!file.exists())
            return;
        mImChatControl.singUploadLocalFiles(path, IMMessage.CONTENT_TYPE_VIDEO);
    }

    @Override
    public void setAtView(String atString) {

    }

    @Override
    public void showVoiceHFOrHook() {

    }

    @Override
    public void hideVoiceHFOrHook() {

    }

    @Override
    public void playNextMedia(int posstion) {

    }

    @Override
    public void changeSelectedMode(IMMessage imMessage, boolean isSelectedMode) {

    }

    @Override
    public void showGroupRemarkDialog(IMGroupRemark imGroupRemark) {

    }

    @Override
    public void onReply(IMMessage imMessage) {

    }

    @Override
    public void scrollToMsg(String msgId) {

    }

    @Override
    public void onFileTransferLoading(long localId) {

    }

    @Override
    public void onFileTransferSuccess(long localId) {
        LogUtils.i("huang", "onFileTransferSuccess=");
        chatMessageAdapter.refreshMessageStatus(localId,IMMessage.STATUS_SUCCESS,IMMessage.STATUS_SUCCESS);
    }

    @Override
    public void onFileTransferFailed(long localId) {
        LogUtils.i("huang", "onFileTransferFailed=");
        chatMessageAdapter.refreshMessageStatus(localId,IMMessage.STATUS_FAIL,IMMessage.STATUS_FAIL);
        ToastUtils.showLong(this,R.string.send_fail);
    }

    @Override
    public void onFileTransferOnPause(long localId) {

    }

    @Override
    public void onFileTransferOnStart(long localId) {

    }

    @Override
    public void onSendMessageSuccessCallBack(long localId) {
        LogUtils.i("huang", "onSendMessageSuccessCallBack=");
        chatMessageAdapter.refreshMessageStatus(localId,IMMessage.STATUS_SUCCESS,IMMessage.STATUS_SUCCESS);
    }

    @Override
    public void onSendMessageFaileCallBack(long localId) {
        LogUtils.i("huang", "onSendMessageFaileCallBack=");
        chatMessageAdapter.refreshMessageStatus(localId,IMMessage.STATUS_FAIL,IMMessage.STATUS_DEFAULT);
        ToastUtils.showLong(this,R.string.send_fail);
    }


    @Override
    public void onAddMessagerCallBack(List<IMMessage> msgs) {
        LogUtils.i("huang","onAddMessagerCallBack="+msgs.toString());
        onRefresfItemAddList(msgs);
    }

    @Override
    public void onAddMessagerCallBack(IMMessage message) {

        LogUtils.i("huang","onAddMessagerCallBack2="+message.toString());
        message.refresh();
        mMessagerLoader.addMessager(message);
        chatMessageAdapter.addOneImData(message);
    }

    @Override
    public void onDeleteMessageCallBack(IMMessage message) {
        chatMessageAdapter.deleteMessage(message.getLocalId());
    }

    @Override
    public void onUpdateMessageCallBack(long localId) {

    }

    @Override
    public void onDeleteMessagesCallback(List<IMMessage> imMessageList) {

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onTranspondMsgToCurrent(TranspondToCurrentEvent transpondToCurrentEvent) {
//        //mProcessorFactory.processorAddMsg(transpondToCurrentEvent.getMessage());
//        mImChatViewHolder.onRefresfItemAdd(transpondToCurrentEvent.getMessage());
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onTranspondMsgToCurrentSuccess(TranspondToCurrentSuccessEvent event) {
//        // mProcessorFactory.processorUpdateMsg(event.getOld(), event.getNew());
//        mImChatViewHolder.onRefreshItem(event.getOld().getLocalId());
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exitGroup(ExitGroupEvent e) {
        //删除会话
        IMChat imChat = mImChatControl.getIMChat();
        if (imChat != null) {
            imChat.delete();
        }
        finish();
    }

    public void onRefresfItemAddList(List<IMMessage> list) {
        if (list == null || list.isEmpty())
            return;
        //刷新会话
        if (mImChat == null) {
            mImChat = mImChatControl.getOrCreateChat();
        }
        mImChat.refresh();
        mImChatControl.updataUnReadCount(list);
        addItem(list,true);
    }

    /**
     * 添加视图
     */
    private void addItem(List<IMMessage> list, boolean isBottom) {
        if (list == null || list.isEmpty())
            return;

        mMessagerLoader.addMessager(list, isBottom);
        chatMessageAdapter.addListImData(list);

//        if (isBottom) {
//            mLinearLayoutManager.scrollToPositionWithOffset(0, 100);
//        }

    }

}
