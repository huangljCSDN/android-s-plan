package com.markLove.xplan.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.xplan.R;
import com.markLove.xplan.base.mvp.BasePresenter;
import com.markLove.xplan.base.ui.BaseActivity;
import com.markLove.xplan.bean.Recorder;
import com.markLove.xplan.bean.msg.Message;
import com.markLove.xplan.config.Constants;
import com.markLove.xplan.module.emoji.EmojiOnClickListener;
import com.markLove.xplan.module.emoji.EmojiUtils;
import com.markLove.xplan.module.recorder.MediaManager;
import com.markLove.xplan.module.recorder.view.AudioRecorderButton;
import com.markLove.xplan.mvp.contract.ChatView;
import com.markLove.xplan.mvp.presenter.ChatPresenter;
import com.markLove.xplan.mvp.presenter.impl.ChatPresenterImpl;
import com.markLove.xplan.ui.adapter.ChatMessageAdapter;
import com.markLove.xplan.ui.adapter.EmojiPagerAdapter;
import com.markLove.xplan.ui.widget.EmojiPointerView;
import com.markLove.xplan.ui.widget.MorePopWindow;
import com.markLove.xplan.ui.widget.ReportDialog;
import com.markLove.xplan.utils.BigDecimalUtil;
import com.markLove.xplan.utils.DensityUtils;
import com.markLove.xplan.utils.LogUtils;
import com.markLove.xplan.utils.PreferencesUtils;
import com.markLove.xplan.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopChatActivity extends BaseActivity implements View.OnClickListener,ChatView{
    private final int REQUEST_CODE_PERMISSION_ONE = 100; //权限申请自定义码
    private final int REQUEST_CODE_PERMISSION_TWO = 103; //权限申请自定义码
    private final int REQUEST_CODE_CAMERA = 101; //相机
    private final int REQUEST_CODE_PICKER = 102; //相册
    private ImageView imageView;
    private ListView mListView;
    private EditText etChatSnedMsg;
    private RelativeLayout mRlRecord;
    private TextView mTvRecordTip,mTvRecordTime;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas = new ArrayList<>();
    private AudioRecorderButton mAudioRecorderButton;
    private View mAnimView;
    private ArrayList<Media> select;
    private TextView mTvSend;
    private View emojiView;
    private ViewPager vpChatEmoji;
    private ImageView mIvEmoji;
    private EmojiPointerView llChatEmojiPoint;
    private TextView tvChatSend;

    private TextView mTvShopName, mTvAllPersonCount;
    private LinearLayout mLlPersons;
    private FrameLayout mFlMore;
    private RecyclerView rlChatMsgList;

    ChatPresenter chatPresenter;
    ChatMessageAdapter chatMessageAdapter;
    String nickName;
    String headImgUrl;
//    AutoCameraUtils autoCameraUtils;
    int me_user_id;
    int to_user_id;
    int moveY = 0;
    boolean isRecordering = false;
    boolean isEnd = false;
    boolean isLikeAndUser = false;
    boolean toUserIDIsLove = false;
    boolean meIsLove = false;
    boolean isBlackUser = false;
    boolean keyboardIsShown = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shop_chat;
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        findViewById(R.id.fl_sound).setOnClickListener(this);
        findViewById(R.id.fl_camera).setOnClickListener(this);
        findViewById(R.id.fl_pic).setOnClickListener(this);
        findViewById(R.id.fl_emoji).setOnClickListener(this);
        imageView = findViewById(R.id.image);

//        mListView = findViewById(R.id.id_listview);
        rlChatMsgList = findViewById(R.id.chat_msg_list);
        
        
        llChatEmojiPoint = findViewById(R.id.chat_emoji_point);
        mRlRecord = findViewById(R.id.ll_record_sound);
        etChatSnedMsg = findViewById(R.id.et_input_msg);
        mTvRecordTip = findViewById(R.id.tv_record_tip);
        mTvRecordTime = findViewById(R.id.tv_record_time);
        mTvSend = findViewById(R.id.btn_send);
        emojiView = findViewById(R.id.chat_emoji_pager);
        vpChatEmoji = findViewById(R.id.chat_emoji_viewpager);
        mIvEmoji = findViewById(R.id.iv_emoji);
        tvChatSend = findViewById(R.id.btn_send);
        etChatSnedMsg.clearFocus();
        mAudioRecorderButton = findViewById(R.id.id_recorder_button);
        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                //每完成一次录音
                Recorder recorder = new Recorder(seconds, filePath);
                mDatas.add(recorder);
                //更新adapter
                mAdapter.notifyDataSetChanged();
                //设置listview 位置
//                mListView.setSelection(mDatas.size() - 1);
            }
        });

        mAudioRecorderButton.setOnRecordindStateChangListener(new AudioRecorderButton.OnRecordingStateChangListener() {
            @Override
            public void onStateChang(int state) {
                switch (state){
                    case AudioRecorderButton.STATE_NORMAL:
                        mTvRecordTip.setText(getString(R.string.pressed_say));
                        mTvRecordTime.setVisibility(View.GONE);
                        break;
                    case AudioRecorderButton.STATE_WANT_TO_CANCEL:
                        mTvRecordTip.setText(getString(R.string.loosen_send));
                        break;
                    case AudioRecorderButton.STATE_RECORDING:
                        mTvRecordTime.setVisibility(View.VISIBLE);
                        mTvRecordTip.setText(getString(R.string.cancel_send));
                        break;
                    case AudioRecorderButton.STATE_TO_SHORT:
                        mTvRecordTip.setText(getString(R.string.pressed_say));
                        mTvRecordTime.setVisibility(View.GONE);
                        toast("录制时间过短");
                        break;
                }
            }

            @Override
            public void onTimeChang(float time) {
                mTvRecordTime.setText(BigDecimalUtil.floatToTime(time));
            }
        });
        setListViewAdapter();

        mTvShopName = findViewById(R.id.tv_shop_name);
        mTvAllPersonCount = findViewById(R.id.tv_person_count);
        mLlPersons = findViewById(R.id.ll_person_count);
        mFlMore = findViewById(R.id.fl_more);
        findViewById(R.id.fl_more).setOnClickListener(this);
        mTvShopName.setText("重庆火锅");
        mTvAllPersonCount.setText("21314124人");
        addPersonHeadPhoto();

        initUI();
    }
    
    

    private void addPersonHeadPhoto() {
        for (int i = 0; i < 10; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_min_head, null);
            ImageView imageHead = view.findViewById(R.id.iv_item_head);
            //显示圆形的imageview
            RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                    .skipMemoryCache(true);//不做内存缓存

            Glide.with(this).load(R.drawable.icon).apply(mRequestOptions).into(imageHead);

            mLlPersons.addView(view);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_sound:
                startRecordSound();
                break;
            case R.id.fl_camera:
                startCameraActivity();
                break;
            case R.id.fl_pic:
                startPickerActivity();
                break;
            case R.id.fl_more:
                showMoreDialog();
                break;
            case R.id.fl_emoji:
                if (emojiView.isShown()) {
                    mIvEmoji.setImageResource(R.drawable.ic_emoji);
                    emojiView.setVisibility(View.GONE);
                } else {
                    emojiView.setVisibility(View.VISIBLE);
                    mIvEmoji.setImageResource(R.mipmap.chat_emoij_select);
                    mRlRecord.setVisibility(View.GONE);
                }
        }
    }

    /**
     * 显示更多弹窗
     */
    private void showMoreDialog() {
        MorePopWindow morePopWindow = new MorePopWindow(this,getString(R.string.report_chat_room)
                ,getString(R.string.exit_chat_room));
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

    /**
     * 开始录音
     */
    private void startRecordSound() {
        if (mRlRecord.getVisibility() == View.VISIBLE) {
            mRlRecord.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                        .PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    showRlRecord();
                } else {
                    //不具有获取权限，需要进行权限申请
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,
                    }, 103);
                }
            } else {
                showRlRecord();
            }
        }
    }

    private void showRlRecord(){
        if (emojiView.isShown()){
            emojiView.setVisibility(View.GONE);

        }
        mRlRecord.setVisibility(View.VISIBLE);
    }

    /**
     * 启动相册
     */
    private void startPickerActivity() {
        Intent intent = new Intent(this, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE_VIDEO);//default image and video (Optional)
        long maxSize = 188743680L;//long long long
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 15);  //default 40 (Optional)
        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, select); // (Optional)
        this.startActivityForResult(intent, REQUEST_CODE_PICKER);
    }

    /**
     * 启动相机
     */
    private void startCameraActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                this.startActivityForResult(new Intent(ShopChatActivity.this, CameraActivity.class), REQUEST_CODE_CAMERA);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(ShopChatActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_ONE);
            }
        } else {
            this.startActivityForResult(new Intent(ShopChatActivity.this, CameraActivity.class), REQUEST_CODE_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.i("huang", "requestCode=" + requestCode + "   resultCode=" + resultCode);
            if (requestCode == REQUEST_CODE_CAMERA) {
                Log.i("CJT", "picture");
                final String path = data.getStringExtra("path");
                Log.i("huang", "path=" + path);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(path));

                Uri mediaUri = Uri.parse("file://" + path);

                Glide.with(this)
                        .load(mediaUri)
                        .into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(path);
                    }
                });
            }
            if (requestCode == REQUEST_CODE_PICKER) {
                select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                Log.i("select", "select.size" + select.size());
                for (final Media media : select) {
                    Log.i("media", media.path);
                    Log.e("media", "s:" + media.size);
//                imageView.setImageURI(Uri.parse(media.path));

                    Uri mediaUri = Uri.parse("file://" + media.path);

                    Glide.with(this)
                            .load(mediaUri)
                            .into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playVideo(media.path);
                        }
                    });
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
        if (requestCode == REQUEST_CODE_PERMISSION_ONE) {
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
                    startActivityForResult(new Intent(ShopChatActivity.this, CameraActivity.class), REQUEST_CODE_CAMERA);
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == REQUEST_CODE_PERMISSION_TWO) {
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
                    showRlRecord();
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    Uri getUri(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(this, this.getPackageName() + ".dmc", new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    /**
     * 检查是否有可以处理的程序
     *
     * @param context
     * @param intent
     * @return
     */
    private boolean isIntentAvailable(Context context, Intent intent) {
        List resolves = context.getPackageManager().queryIntentActivities(intent, 0);
        return resolves.size() > 0;
    }

    /**
     * 调取播放视频的工具
     *
     * @param path
     */
    private void playVideo(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(getUri(path), "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (isIntentAvailable(ShopChatActivity.this, intent)) {
            startActivity(intent);
        } else {
            Toast.makeText(ShopChatActivity.this, getString(com.dmcbig.mediapicker.R.string.cant_play_video), Toast.LENGTH_SHORT).show();
        }
    }


    private void setListViewAdapter() {
//        mAdapter = new RecorderAdapter(this, mDatas);
//        mListView.setAdapter(mAdapter);
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //如果第一个动画正在运行， 停止第一个播放其他的
//                if (mAnimView != null) {
//                    mAnimView.setBackgroundResource(R.drawable.adj);
//                    mAnimView = null;
//                }
//                //播放动画
//                mAnimView = view.findViewById(R.id.id_recorder_anim);
//                mAnimView.setBackgroundResource(R.drawable.play_anim);
//                AnimationDrawable animation = (AnimationDrawable) mAnimView.getBackground();
//                animation.start();
//
//                //播放音频  完成后改回原来的background
//                MediaManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        mAnimView.setBackgroundResource(R.drawable.adj);
//                    }
//                });
//            }
//        });
    }

    /**
     * 根据生命周期 管理播放录音
     */
    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }


    protected void initUI() {

//        setTranslucentStatusColor(Color.WHITE);
        chatPresenter = new ChatPresenterImpl();
        chatPresenter.setView(this);
        initSoftKeyboard();
//        initVoice();
        initEmoji();
        rlChatMsgList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                chatMessageAdapter.dismissItemPopup();
                closeSoftKeyBoard();
//                llChatVoice.setVisibility(View.GONE);
//                ivChatVoice.setImageResource(R.mipmap.chat_voice_normal);
                emojiView.setVisibility(View.GONE);
//                ivChatEmoij.setImageResource(R.mipmap.chat_emoij_normal);
//                llChatGift.setVisibility(View.GONE);
//                ivChatGift.setImageResource(R.mipmap.chat_gift_normal);
                return false;
            }
        });
        meIsLove = PreferencesUtils.getBoolean(this, Constants.USER_IS_LOVES_INFO_KEY);
        etChatSnedMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tvChatSend.setEnabled(true);
                } else {
                    tvChatSend.setEnabled(false);
                }
                if (s.length() >= 200) {
                    ToastUtils.show(ShopChatActivity.this, "最多200个字符", 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initData();
    }

    //初始化Emoji
    private void initEmoji() {
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, EmojiUtils.emojiIcons);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        vpChatEmoji.setLayoutManager(linearLayoutManager);
        EmojiPagerAdapter emojiPagerAdapter = new EmojiPagerAdapter(this, list);
        emojiPagerAdapter.setEmojiClickListener(emojiClickListener);
        vpChatEmoji.setAdapter(emojiPagerAdapter);
        vpChatEmoji.setVisibility(View.VISIBLE);
        emojiView.setVisibility(View.GONE);
        llChatEmojiPoint.setCount(emojiPagerAdapter.getCount());
        llChatEmojiPoint.setSelectPoint(1);
        llChatEmojiPoint.invalidate();
        vpChatEmoji.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                llChatEmojiPoint.setSelectPoint(i + 1);
                llChatEmojiPoint.invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
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
//            finish();
//        } else {
//            to_user_id = Integer.parseInt(bundle.getString("user_id"));
//            nickName = bundle.getString("nick_name");
//            headImgUrl = bundle.getString("head_img_url");
//        }
        me_user_id = PreferencesUtils.getInt(this, Constants.ME_USER_ID);
        LogUtils.d("me_user_id="+me_user_id);
//        tvChatUser.setText(nickName);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatMessageAdapter = new ChatMessageAdapter(this, new ArrayList<Message>());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
        String url = FileUtil.saveBitmap("haha",bitmap);
//        chatMessageAdapter.setToHeadImgUrl(headImgUrl);
        chatMessageAdapter.setToHeadImgUrl(url);
        chatMessageAdapter.setFromHeadImgUrl(PreferencesUtils.getString(this, Constants.ME_HEAD_IMG_URL));
        chatMessageAdapter.setFailMessageResend(failMessageResend);
        manager.setStackFromEnd(false);
        rlChatMsgList.setLayoutManager(manager);
        rlChatMsgList.setAdapter(chatMessageAdapter);

        chatMessageAdapter.registerAdapterDataObserver(adapterDataObserver);
        rlChatMsgList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = DensityUtils.dip2px(ShopChatActivity.this, 10f);
                outRect.top = DensityUtils.dip2px(ShopChatActivity.this, 10f);
            }
        });
//        chatPresenter = new ChatPresenterImpl();
//        chatPresenter.setView(this);
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

    EmojiOnClickListener emojiClickListener = new EmojiOnClickListener() {
        @Override
        public void onEmojiItemClick(int emojiIcon) {
            if (emojiIcon != R.mipmap.emoji_delect) {
                int position = -1;
                for (int i = 0; i < EmojiUtils.emojiIcons.length; i++) {
                    if (EmojiUtils.emojiIcons[i] == emojiIcon) {
                        position = i;
                        break;
                    }
                }
                if (position >= 0 && position < EmojiUtils.emojiIcons.length) {
                    String str = etChatSnedMsg.getText().toString() + EmojiUtils.emojiValues[position];
                    SpannableString spannableString = EmojiUtils.parseEmoji(ShopChatActivity.this, str);
                    etChatSnedMsg.setText(spannableString);
                    etChatSnedMsg.setSelection(spannableString.length());
                }
            } else {
                int keyCode = KeyEvent.KEYCODE_DEL;
                KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
                etChatSnedMsg.onKeyDown(keyCode, keyEventDown);
                etChatSnedMsg.onKeyUp(keyCode, keyEventUp);
            }
        }
    };

    int usableHeightPrevious = 0;
    
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
                        // 键盘弹出
//                        if (emojiView.isShown()) {
//                            emojiView.setVisibility(View.GONE);
//                            ivChatEmoij.setImageResource(R.mipmap.chat_emoij_normal);
//                        }
//                        
//                        if (llChatVoice.isShown()) {
//                            llChatVoice.setVisibility(View.GONE);
//                            ivChatVoice.setImageResource(R.mipmap.chat_voice_normal);
//                        }
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

    ChatMessageAdapter.FailMessageResend failMessageResend = new ChatMessageAdapter.FailMessageResend() {
        @Override
        public void failResend(Message msg) {
            closeSoftKeyBoard();
            resendMessage(msg);
        }
    };

    private void resendMessage(final Message resendMessage) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_chat_msg_fail, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
        contentView.findViewById(R.id.popup_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updataMessage(resendMessage.getMsgID(), Message.ChatStatus.SENDING.ordinal());
                judeBlackList(resendMessage);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.popup_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.N) {
            int height = DensityUtils.dip2px(this, 180);
            popupWindow.setHeight(height);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, getWindow().getDecorView().getHeight() - height);
        } else {
            popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        }
        popupWindow.update();
    }

    private void judeBlackList(final Message msg) {
        if (null != msg) {
            int sendCount = PreferencesUtils.getInt(this, Constants.SEND_MESSAGE_COUNT + to_user_id, 0);
            //原计数+1
            PreferencesUtils.putInt(this, Constants.SEND_MESSAGE_COUNT + to_user_id, ++sendCount);
            addOneMessage(msg);
        }
        StringBuilder sb = new StringBuilder(Constants.POST_JUDGE_BLACK_LIST);
        sb.append("?ToUserId=" + to_user_id);
        sb.append("&Token=" + PreferencesUtils.getString(this, Constants.TOKEN_KEY));
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

    @Override
    public void showHistoryMessage(List<Message> historyMessageList) {
        
    }

    @Override
    public void addOneMessage(Message msg) {

    }

    @Override
    public void updataMessage(String msgID, int errorCode) {

    }
}
