package com.markLove.Xplan.ui.widget;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.Recorder;
import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.bean.msg.body.FileMessageBody;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.module.emoji.EmojiOnClickListener;
import com.markLove.Xplan.module.emoji.EmojiUtils;
import com.markLove.Xplan.ui.activity.CameraActivity;
import com.markLove.Xplan.ui.adapter.EmojiPagerAdapter;
import com.markLove.Xplan.ui.dialog.DeleteVoiceDialog;
import com.markLove.Xplan.utils.AudioUtils;
import com.markLove.Xplan.utils.FileUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;
import com.markLove.Xplan.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：created by huanglingjun on 2018/10/16
 * 描述：
 */
public class ChatViewForPublish extends FrameLayout implements View.OnClickListener {

    private EditText etChatSnedMsg;
    private RelativeLayout mRlRecord;
    private TextView mTvRecordTip, mTvRecordTime;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas = new ArrayList<>();
    //    private AudioRecorderButton mAudioRecorderButton;
    private ImageView mAudioRecorderButton;
    private View mAnimView;
    private ArrayList<Media> select;
    private View emojiView;
    private ViewPager vpChatEmoji;
    private ImageView mIvEmoji, mIvRecord;
    private EmojiPointerView llChatEmojiPoint;
    private ImageView tvChatSend;
    private ImageView ivDeleteVoice, ivConfirmVoice;
    private Message voiceMessage;
    private CircleProgressBar circleProgressBar;
    private int totalProgress = 300;
    private int currentProgress;

    int moveY = 0;
    boolean isRecordering = false;
    boolean isEnd = false;
    boolean isPlaying = false;
    boolean isReset = false;
    int voiceDuration;
    boolean meIsLove = false;
    int me_user_id;
    int to_user_id;

    private BaseActivity mActivity;

    public ChatViewForPublish(@NonNull Context context) {
        this(context, null);
    }

    public ChatViewForPublish(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatViewForPublish(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_msg_edit_view_publish, this, true);
        initView();
    }

    private void initView() {
        llChatEmojiPoint = findViewById(R.id.chat_emoji_point);
        mRlRecord = findViewById(R.id.ll_record_sound);
        etChatSnedMsg = findViewById(R.id.et_input_msg);
        mTvRecordTip = findViewById(R.id.tv_record_tip);
        mTvRecordTime = findViewById(R.id.tv_record_time);
        emojiView = findViewById(R.id.chat_emoji_pager);
        vpChatEmoji = findViewById(R.id.chat_emoji_viewpager);
        mIvRecord = findViewById(R.id.iv_record);
        mIvEmoji = findViewById(R.id.iv_emoji);
        tvChatSend = findViewById(R.id.btn_send);
        ivConfirmVoice = findViewById(R.id.iv_confirm_voice);
        ivDeleteVoice = findViewById(R.id.iv_delete_voice);
        etChatSnedMsg.clearFocus();
        mAudioRecorderButton = findViewById(R.id.id_recorder_button);
        circleProgressBar = findViewById(R.id.progress_circle);
        circleProgressBar.setTotalProgress(totalProgress);

        tvChatSend.setEnabled(false);
        findViewById(R.id.fl_sound).setOnClickListener(this);
        findViewById(R.id.fl_camera).setOnClickListener(this);
        findViewById(R.id.fl_pic).setOnClickListener(this);
        findViewById(R.id.fl_emoji).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.iv_confirm_voice).setOnClickListener(this);
        findViewById(R.id.iv_delete_voice).setOnClickListener(this);

        initVoice();
        initEmoji();
        initUI();

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
                if (s.length() >= 500) {
                    ToastUtils.show(getContext(), "最多500个字符", 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_sound:
                if (!mRlRecord.isShown()) {
                    startRecordSound();
                } else {
                    hideRlRecord();
                }
                break;
            case R.id.fl_camera:
                hideRlRecord();
                hideEmojiView();
                startCameraActivity();
                break;
            case R.id.fl_pic:
                hideRlRecord();
                hideEmojiView();
                startPickerActivity();
                break;
            case R.id.fl_emoji:
                showEmojiView();
                break;
            case R.id.btn_send:
                send();
                break;
            case R.id.iv_confirm_voice:
                resetRecordering();
                if (onSendMessageListener != null) {
                    onSendMessageListener.onSendMessage(voiceMessage);
                }
                break;
            case R.id.iv_delete_voice:
                showDeleteRecoredVoiceDialog();
                break;
        }
    }

    public void setActivity(BaseActivity baseActivity) {
        mActivity = baseActivity;
    }

    /**
     * 发送消息
     */
    public void send() {
        String msg = etChatSnedMsg.getText().toString().trim();
        if (!TextUtils.isEmpty(msg)) {
            Message message;

            message = Message.createTxtMessage(Message.Type.CHAT, me_user_id, to_user_id,"", msg);
            LogUtils.e("fromId=" + message.getFromID() + ",toId=" + message.getToID() + ",msg=" + msg);
            message.setStatus(Message.ChatStatus.SENDING);
//            //判断是否被拉黑
//            judeBlackList(message);
            if (onSendMessageListener != null) {
                onSendMessageListener.onSendMessage(message);
            }
            etChatSnedMsg.setText("");
        } else {
            ToastUtils.showCenter(getContext(), "发送内容不能为空", 0);
        }
    }

    public void showRlRecord() {
        hideEmojiView();
        if (!mRlRecord.isShown()) {
            mIvRecord.setImageResource(R.drawable.ic_voice_selected_blue);
            mRlRecord.setVisibility(View.VISIBLE);
        } else {
            hideRlRecord();
//            setVisibility(GONE);
        }
        closeSoftKeyBoard();
    }

    private void showEmojiView() {
        hideRlRecord();
        if (!emojiView.isShown()) {
            emojiView.setVisibility(View.VISIBLE);
            mIvEmoji.setImageResource(R.drawable.ic_emoji_selected_blue);
        } else {
            hideEmojiView();
//            setVisibility(GONE);
        }
        closeSoftKeyBoard();
    }

    private void hideEmojiView() {
        if (emojiView.isShown()) {
            emojiView.setVisibility(View.GONE);
            mIvEmoji.setImageResource(R.drawable.ic_emoji);
        }
    }

    private void hideRlRecord() {
        if (mRlRecord.isShown()) {
            mRlRecord.setVisibility(View.GONE);
            mIvRecord.setImageResource(R.drawable.ic_sound);
        }
    }

    MyHandler handler = new MyHandler();
    private int startTime;

    class MyHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    postRecorderingMessage();
                    setRecorderingTime();
                    break;
                case 1:
                    startTime--;
                    setRecorderingTime(startTime);
                    handler.removeMessages(1);
                    if (startTime > 0) {
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
        }
    }

    /**
     * 开始录音
     */
    public void startRecordering() {
        currentProgress = 0;
        circleProgressBar.setProgress(0);
        circleProgressBar.setVisibility(VISIBLE);
        isReset = false;
        mTvRecordTip.setText(getString(R.string.recoreding_voice));
        mAudioRecorderButton.setImageResource(R.drawable.ic_recoreding);
    }

    /**
     * 停止录音
     */
    public void stopRecordering() {
        isEnd = true;
        isRecordering = false;
        circleProgressBar.setVisibility(GONE);
        mTvRecordTip.setText(getString(R.string.recoreding_voice_completed));
        ivDeleteVoice.setVisibility(VISIBLE);
        ivConfirmVoice.setVisibility(VISIBLE);
        mAudioRecorderButton.setImageResource(R.drawable.ic_recoreded);
        handler.removeMessages(0);
    }

    /**
     * 重置录音
     */
    public void resetRecordering() {
        isReset = true;
        isEnd = false;
        handler.removeMessages(1);
        mTvRecordTip.setText(getString(R.string.click_recording));
        ivDeleteVoice.setVisibility(GONE);
        ivConfirmVoice.setVisibility(GONE);
        mTvRecordTime.setText("0s");
        mAudioRecorderButton.setImageResource(R.drawable.ic_recored);
        if (isPlaying) {
            AudioUtils.getInstance().stop();
            isPlaying = false;
        }
    }

    /**
     * 开始播放录音
     */
    public void startPlayRecordering() {
        isPlaying = true;
        mTvRecordTip.setText(getString(R.string.playing_voice));
        mAudioRecorderButton.setImageResource(R.drawable.ic_recoreding);
    }

    /**
     * 播放结束
     */
    public void endPlayRecordering() {
        isPlaying = false;
        if (!isReset) {
            mTvRecordTip.setText(getString(R.string.playing_voice_end));
            mAudioRecorderButton.setImageResource(R.drawable.ic_recoreded);
            mTvRecordTime.setText(voiceDuration + "s");
            handler.removeMessages(1);
        }
    }

    private String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    /**
     * 取消录音
     */
    public void cancelRecordring() {
        isEnd = true;
        mTvRecordTime.setText("0s");
        mTvRecordTime.setVisibility(View.GONE);
        mTvRecordTip.setText(getString(R.string.click_recording));
    }

    public void postRecorderingMessage() {
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    public void setVoiceState() {
        if (isRecordering) {
            mTvRecordTip.setText(getString(R.string.cancel_send));
        } else {
            mTvRecordTip.setText(getString(R.string.loosen_send));
        }
    }

    public void setRecorderingTime() {
        long timeInterval = AudioUtils.getInstance().getCurrentTimeInterval();
        if (timeInterval >= AudioUtils.COUNTDOWN_VOICE_TIME && timeInterval < AudioUtils.MAX_VOICE_TIME_LAGER) {
        } else if (AudioUtils.getInstance().getCurrentTimeInterval() >= AudioUtils.MAX_VOICE_TIME_LAGER) {
            isEnd = true;
        }
//        mTvRecordTime.setText(showTimeCount(AudioUtils.getInstance().getCurrentTimeInterval() * 1000) +"s");
        mTvRecordTime.setText(AudioUtils.getInstance().getCurrentTimeInterval() + "s");
        currentProgress += 1;
        circleProgressBar.setProgress(currentProgress);
        if (isEnd) {
            if (isRecordering) {
                AudioUtils.getInstance().stopRecording();
            } else {
                AudioUtils.getInstance().cancelRecording();
            }
        }
    }

    private void setRecorderingTime(int time) {
        if (time > 0) {
            mTvRecordTime.setText(time + "s");
        } else {
            mTvRecordTime.setText(0 + "s");
        }
    }

    public String showTimeCount(long time) {
        System.out.println("time=" + time);
        if (time >= 360000000) {
            return "00:00:00";
        }
        String timeCount = "";
        long hourc = time / 3600000;
        String hour = "0" + hourc;
        System.out.println("hour=" + hour);
        hour = hour.substring(hour.length() - 2, hour.length());
        System.out.println("hour2=" + hour);

        long minuec = (time - hourc * 3600000) / (60000);
        String minue = "0" + minuec;
        System.out.println("minue=" + minue);
        minue = minue.substring(minue.length() - 2, minue.length());
        System.out.println("minue2=" + minue);

        long secc = (time - hourc * 3600000 - minuec * 60000) / 1000;
        String sec = "0" + secc;
        System.out.println("sec=" + sec);

        sec = sec.substring(sec.length() - 2, sec.length());
        System.out.println("sec2=" + sec);
        timeCount = sec;
        System.out.println("timeCount=" + timeCount);
        return timeCount;
    }

    //初始化语音
    private void initVoice() {
        mAudioRecorderButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final String recorderingPermission = Manifest.permission.RECORD_AUDIO;
                final String storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                //6.0以前无论是否有权限都是返回true
                if (ContextCompat.checkSelfPermission(getActivity(), recorderingPermission) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(), storagePermission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_PERMISSION_TWO);
                } else if (ContextCompat.checkSelfPermission(getActivity(), recorderingPermission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, Constants.REQUEST_CODE_PERMISSION_TWO);
                } else if (ContextCompat.checkSelfPermission(getActivity(), storagePermission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_PERMISSION_TWO);
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (!isEnd) {
                                if (isRecordering) {
                                    isEnd = true;
                                    isRecordering = false;
                                    AudioUtils.getInstance().stopRecording();
                                } else {
                                    AudioUtils.getInstance().startRecording(audioRecoderListener);
                                }
                            } else {
                                playVoice();
                            }
                            break;

                        default:
                            break;
                    }

                }
                return true;
            }
        });
    }

    AudioUtils.AudioRecoderListener audioRecoderListener = new AudioUtils.AudioRecoderListener() {
        @Override
        public void recoderFail() {
            ToastUtils.showCenter(App.getInstance(), "录音无效，请检查录音权限", 0);
            stopRecordering();
        }

        @Override
        public void recoderStart() {
            isRecordering = true;
            //开启一个时间监听,每隔一秒获取一次时间
            postRecorderingMessage();
            isEnd = false;
            startRecordering();
        }

        @Override
        public void recoderEnd() {
            handler.removeMessages(0);
            stopRecordering();
            //结束录音后发送这条消息
            if (AudioUtils.getInstance().getTimeInterval() < 1000) {
                ToastUtils.showCenter(getActivity(), "录制时间过短", 0);
                FileUtils.delete(AudioUtils.getInstance().getFilePath());
                resetRecordering();
                return;
            }
            String voicePath = AudioUtils.getInstance().getFilePath();
            File file = new File(voicePath);
            if (file.exists() && file.length() > 0) {
                String voiceName = voicePath.substring(voicePath.lastIndexOf("/") + 1, voicePath.length());
                voiceMessage = Message.createVoiceMessage(Message.Type.CHAT, me_user_id, to_user_id,"", voiceName, voicePath);
                voiceMessage.setStatus(Message.ChatStatus.SENDING);
//                judeBlackList(voiceMessage);
            } else {
                ToastUtils.showCenter(getActivity(), "录音无效，请检查录音权限", 0);
                FileUtils.delete(AudioUtils.getInstance().getFilePath());
            }
        }

        @Override
        public void recoderCancel() {
            cancelRecordring();
        }
    };


    /**
     * 开始录音
     */
    private void startRecordSound() {
        if (mRlRecord.getVisibility() == View.VISIBLE) {
            mRlRecord.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                        .PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    showRlRecord();
                } else {
                    //不具有获取权限，需要进行权限申请
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,
                    }, 103);
                }
            } else {
                showRlRecord();
            }
        }
    }

    /**
     * 启动相册
     */
    private void startPickerActivity() {
        Intent intent = new Intent(getActivity(), PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);//default image and video (Optional)
        long maxSize = 188743680L;//long long long
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 9);  //default 40 (Optional)
        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, select); // (Optional)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager
                            .PERMISSION_GRANTED) {
                getActivity().startActivityForResult(intent, Constants.REQUEST_CODE_PICKER);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                }, Constants.REQUEST_CODE_PERMISSION_ONE);
            }
        } else {
            getActivity().startActivityForResult(intent, Constants.REQUEST_CODE_PICKER);
        }
    }

    /**
     * 启动相机
     */
    private void startCameraActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                getActivity().startActivityForResult(new Intent(getActivity(), CameraActivity.class), Constants.REQUEST_CODE_CAMERA);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, Constants.REQUEST_CODE_PERMISSION_ONE);
            }
        } else {
            getActivity().startActivityForResult(new Intent(getActivity(), CameraActivity.class), Constants.REQUEST_CODE_CAMERA);
        }
    }


    Uri getUri(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".dmc", new File(path));
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
        if (isIntentAvailable(getContext(), intent)) {
            getActivity().startActivity(intent);
        } else {
            Toast.makeText(getContext(), getString(com.dmcbig.mediapicker.R.string.cant_play_video), Toast.LENGTH_SHORT).show();
        }
    }

    public BaseActivity getActivity() {
        if (mActivity != null) {
            return mActivity;
        }
        return (BaseActivity) getContext();
    }


    protected void initUI() {

        meIsLove = PreferencesUtils.getBoolean(getContext(), Constants.USER_IS_LOVES_INFO_KEY);
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
                    ToastUtils.show(getContext(), "最多200个字符", 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void hideView() {
        hideEmojiView();
        hideRlRecord();
    }

    public boolean isShow() {
        if (emojiView.isShown() || mRlRecord.isShown()) {
            return true;
        }
        return false;
    }

    //初始化Emoji
    private void initEmoji() {
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, EmojiUtils.emojiIcons);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        vpChatEmoji.setLayoutManager(linearLayoutManager);
        EmojiPagerAdapter emojiPagerAdapter = new EmojiPagerAdapter(getContext(), list);
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
//                    SpannableString spannableString = EmojiUtils.parseEmoji(getContext(), str);
//                    etChatSnedMsg.setText(spannableString);
//                    etChatSnedMsg.setSelection(spannableString.length());
                    if (onSendMessageListener != null) {
                        onSendMessageListener.onEmojiMessage(str, position);
                    }
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

    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }


    /**
     * 发送消息回调接口
     */
    public interface OnSendMessageListener {
        void onSendMessage(Message message);

        void onEmojiMessage(String str, int position);
    }

    public OnSendMessageListener onSendMessageListener;

    public void setOnSendMessageListener(OnSendMessageListener onSendMessageListener) {
        this.onSendMessageListener = onSendMessageListener;
    }

    private void closeSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getActivity().getCurrentFocus() != null) {
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void playVoice() {
        if (isPlaying) {
            AudioUtils.getInstance().stop();
            return;
        }
        if (voiceMessage == null) return;
        FileMessageBody voiceMessageBody = (FileMessageBody) voiceMessage.getBody();
        String voicePath = "";
        String voicePath1 = Constants.LOCAL_VOICE_PATH + voiceMessageBody.getFileName();
        String voicePath2 = getContext().getExternalFilesDir("voice").getAbsolutePath() + File.separator + voiceMessageBody.getFileName();
        if (new File(voicePath1).exists()) {
            voicePath = voicePath1;
        } else if (new File(voicePath2).exists()) {
            voicePath = voicePath2;
        }
        voiceDuration = FileUtils.getAmrDuration(voicePath) - 1;
        startTime = voiceDuration;
        AudioUtils.getInstance().play(voicePath, new AudioUtils.PlayStatusListener() {
            @Override
            public void playEnd() {
                endPlayRecordering();
            }

            @Override
            public void playStart() {
                startPlayRecordering();
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        });
    }

    private void showDeleteRecoredVoiceDialog() {
        DeleteVoiceDialog deleteVoiceDialog = new DeleteVoiceDialog(getContext());
        deleteVoiceDialog.setOnDialogCallBack(new DeleteVoiceDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {
                resetRecordering();
            }
        });
        deleteVoiceDialog.show();
    }
}
