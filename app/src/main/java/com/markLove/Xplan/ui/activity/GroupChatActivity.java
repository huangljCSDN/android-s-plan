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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjt2325.cameralibrary.util.FileUtil;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.ChatUser;
import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.bean.msg.body.FileMessageBody;
import com.markLove.Xplan.bean.msg.body.TxtMessageBody;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.db.DBDao;
import com.markLove.Xplan.module.image.IImageCompressor;
import com.markLove.Xplan.mvp.contract.ChatView;
import com.markLove.Xplan.mvp.presenter.ChatPresenter;
import com.markLove.Xplan.mvp.presenter.impl.ChatPresenterImpl;
import com.markLove.Xplan.ui.adapter.GroupChatMessageAdapter;
import com.markLove.Xplan.ui.widget.MorePopWindow;
import com.markLove.Xplan.ui.widget.RemoveMsgDialog;
import com.markLove.Xplan.ui.widget.ReportDialog;
import com.markLove.Xplan.ui.widget.ResendMsgDialog;
import com.markLove.Xplan.ui.widget.SignUpTipDialog;
import com.markLove.Xplan.utils.AudioUtils;
import com.markLove.Xplan.utils.DataUtils;
import com.markLove.Xplan.utils.DensityUtils;
import com.markLove.Xplan.utils.ImageUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;
import com.markLove.Xplan.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 组局聊天室
 */
public class GroupChatActivity extends BaseActivity implements View.OnClickListener ,ChatView{
    private ArrayList<Media> select;
    private FrameLayout mFlMore;
    private RecyclerView rlChatMsgList;
    private RelativeLayout mRlTitleBar;
    private RelativeLayout mRlCancel;
    private RelativeLayout mRlRemove;
    private com.markLove.Xplan.ui.widget.ChatView chatView;

    private TextView mTvJoinCount,mTvPlace,mTvTime,mTvRemark;
    private Button mBtnJoin;
    private TextView mTvInvitation; //邀请好友

    ChatPresenter chatPresenter;
    GroupChatMessageAdapter chatMessageAdapter;
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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_group_chat;
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rlChatMsgList = findViewById(R.id.chat_msg_list);
        mFlMore = findViewById(R.id.fl_more);
        mRlTitleBar = findViewById(R.id.rl_title_bar);
        mRlCancel = findViewById(R.id.rl_cancel);
        mRlRemove = findViewById(R.id.rl_remove);

        mTvJoinCount = findViewById(R.id.tv_jion_count);
        mTvPlace = findViewById(R.id.tv_place);
        mTvTime = findViewById(R.id.tv_time);
        mTvRemark = findViewById(R.id.tv_remark);
        mBtnJoin = findViewById(R.id.btn_enter);
        mTvInvitation = findViewById(R.id.tv_invitation);

        findViewById(R.id.fl_more).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.iv_remove).setOnClickListener(this);
        findViewById(R.id.btn_enter).setOnClickListener(this);
        addPersonHeadPhoto();

        chatView = findViewById(R.id.chatView);
        chatView.setActivity(this);
        chatView.setOnSendMessageListener(new com.markLove.Xplan.ui.widget.ChatView.OnSendMessageListener() {
            @Override
            public void onSendMessage(Message message) {
                judeBlackList(message);
            }
        });

        rlChatMsgList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = DensityUtils.dip2px(GroupChatActivity.this, 10f);
                outRect.top = DensityUtils.dip2px(GroupChatActivity.this, 10f);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_more:
                showMoreDialog();
                break;
            case R.id.tv_cancel:
                cancelRemove();
                break;
            case R.id.iv_remove:
                showRemoveDialog();
                break;
            case R.id.btn_enter:
                signUp();
                break;
        }
    }

    protected void initData() {
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
        me_user_id = PreferencesUtils.getInt(this, Constants.ME_USER_ID);
        LogUtils.d("me_user_id=" + me_user_id);
//        tvChatUser.setText(nickName);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatMessageAdapter = new GroupChatMessageAdapter(this, new ArrayList<Message>());
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
//        tvChatSendPrice.setText(gold + "");
//        chatPresenter.getHistory(me_user_id, to_user_id);
//        getGiftList();
        getChatOpen();
    }

    /**
     * 报名
     */
    private void signUp(){
        //已报名
        mBtnJoin.setText(getString(R.string.joined));
        mBtnJoin.setTextColor(getColor(R.color.color_333333));
        mBtnJoin.setBackgroundResource(R.drawable.bg_joined);
        mBtnJoin.setClickable(false);

        SignUpTipDialog signUpTipDialog = new SignUpTipDialog(this);
        signUpTipDialog.setCanceledOnTouchOutside(true);
        signUpTipDialog.show();
    }

    /**
     * 设置组局状态
     * @param startTime
     */
    private void checkTime(long startTime){
       long currentTime =  System.currentTimeMillis();
       long goingTime = currentTime - startTime;

       if (goingTime > 0){
           long hour = goingTime / 3600000;
           if (hour > 8){
               mBtnJoin.setText(getString(R.string.finished));
               mBtnJoin.setTextColor(getColor(R.color.white));
               mBtnJoin.setBackgroundResource(R.drawable.bg_joined);
               mBtnJoin.setClickable(false);
           } else {
               mBtnJoin.setText(getString(R.string.going));
               mBtnJoin.setTextColor(getColor(R.color.white));
               mBtnJoin.setBackgroundResource(R.drawable.bg_joined);
               mBtnJoin.setClickable(false);
           }
       }
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

    GroupChatMessageAdapter.FailMessageResend failMessageResend = new GroupChatMessageAdapter.FailMessageResend() {
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
        DBDao.getDbDao(this).insertMessage(me_user_id, message);
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
        final Message imgMsg = Message.createImageMessage(Message.Type.CHAT, me_user_id, to_user_id, fileName, filePath);
        imgMsg.setStatus(Message.ChatStatus.SENDING);

        if (isOrigin) {
            judeBlackList(imgMsg);
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
        } else super.onBackPressed();
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
        try {
            if (chatMessageAdapter.getItemCount() > 0) {
                Message msg = chatMessageAdapter.getItemMsg(chatMessageAdapter.getItemCount() - 1);
                msg = DBDao.getDbDao(this).queryLastMessage(me_user_id, to_user_id, msg.getMsgID());
                ChatUser chatUser = new ChatUser();
                chatUser.setNickName(nickName);
                chatUser.setUserID(to_user_id);
                chatUser.setHeadImgUrl(headImgUrl);
                chatUser.setChatTime(DataUtils.getDateTime(msg.getMsgTime() != 0 ? msg.getMsgTime() : System.currentTimeMillis()));
                chatUser.setLastMsgId(msg.getMsgID());
                chatUser.setUnreadCount(0);
                chatUser.setLastMsgChatType(msg.getChatType());
                switch (msg.getChatType()) {
                    case TXT:
                        TxtMessageBody txtMessageBody = (TxtMessageBody) msg.getBody();
                        chatUser.setLastMsgContent(txtMessageBody.getMsg());
                        break;
                    case IMAGE:
                        chatUser.setLastMsgContent("[图片]");
                        break;
                    case VOICE:
                        chatUser.setLastMsgContent("[语音]");
                        break;
                    case LOVE:
                        chatUser.setLastMsgContent("[情侣邀请]");
                        break;
                    case SUPERLIKE:
                        chatUser.setLastMsgContent("[超喜欢]");
                        break;
                    case GIFT:
                        chatUser.setLastMsgContent("[礼物]");
                        break;
                    case IMAGE_DESC:
                        chatUser.setLastMsgContent("[图片]");
                        break;
                    case VOICE_DESC:
                        chatUser.setLastMsgContent("[语音]");
                        break;
                    default:
                        chatUser.setLastMsgContent("");
                        break;
                }
                DBDao.getDbDao(this).insertChatUser(me_user_id, chatUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != chatPresenter) {
            chatPresenter.onPause();
        }
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
        super.onDestroy();
    }


    private void addPersonHeadPhoto() {
//        for (int i = 0; i < 10; i++) {
//            View view = LayoutInflater.from(this).inflate(R.layout.item_min_head, null);
//            ImageView imageHead = view.findViewById(R.id.iv_item_head);
//            //显示圆形的imageview
//            RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//                    .skipMemoryCache(true);//不做内存缓存
//
//            Glide.with(this).load(R.drawable.icon).apply(mRequestOptions).into(imageHead);
//
//            mLlPersons.addView(view);
//        }
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
                    startActivityForResult(new Intent(GroupChatActivity.this, CameraActivity.class), Constants.REQUEST_CODE_CAMERA);
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

    /**
     * 显示更多弹窗
     */
    private void showMoreDialog() {
        MorePopWindow morePopWindow = new MorePopWindow(this, getString(R.string.report_group)
                , getString(R.string.exit_group));
        morePopWindow.showAtBottom(mFlMore);
        morePopWindow.setOnDialogCallBack(new MorePopWindow.OnDialogCallBack() {
            @Override
            public void onReportCallBack() {
                showReportDialog();
            }

            @Override
            public void onExitCallBack() {
                finish();
            }
        });
    }

    /**
     * 显示举报框
     */
    private void showReportDialog() {
        ReportDialog reportDialog = new ReportDialog(this);
        reportDialog.setOnDialogCallBack(new ReportDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {
                toast(content);
            }
        });

        reportDialog.show();
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

}
