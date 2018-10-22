package com.markLove.Xplan.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.adapter.SpacingDecoration;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.bean.msg.body.FileMessageBody;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.module.emoji.EmojiUtils;
import com.markLove.Xplan.module.image.IImageCompressor;
import com.markLove.Xplan.ui.adapter.PublishMediaGridAdapter;
import com.markLove.Xplan.ui.dialog.ContentEmptyDialog;
import com.markLove.Xplan.ui.dialog.DeleteVoiceDialog;
import com.markLove.Xplan.ui.dialog.ExitEditDialog;
import com.markLove.Xplan.ui.widget.ChatViewForPublish;
import com.markLove.Xplan.utils.AudioUtils;
import com.markLove.Xplan.utils.FileUtils;
import com.markLove.Xplan.utils.ImageUtils;
import com.markLove.Xplan.utils.LogUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 发布动态
 */
public class PublishActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvHead;
    private TextView mTvPublish, mTvOpen;
    private TextView mTvVoice;
    private EditText mEditext;
    private RecyclerView mRecycleView;
    private LinearLayout mLocation;
    private List<Media> mediaList = new ArrayList<>();
    private ChatViewForPublish chatView;
    private PublishMediaGridAdapter gridAdapter;
    private ArrayList<Media> select;
    private ArrayList<Media> photoList = new ArrayList<>();
    private Message voiceMessage;
    private int voiceDuration;
    private MyHandler handler = new MyHandler(this);
    private int startTime;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mIvHead = findViewById(R.id.iv_head);
        mTvPublish = findViewById(R.id.tv_publish);
        mTvOpen = findViewById(R.id.tv_open);
        mEditext = findViewById(R.id.et_content);
        mRecycleView = findViewById(R.id.recyclerView);
        chatView = findViewById(R.id.chatview);
        mTvVoice = findViewById(R.id.tv_voice);

        mTvPublish.setOnClickListener(this);
        mTvOpen.setOnClickListener(this);
        mTvVoice.setOnClickListener(this);
        findViewById(R.id.ll_location).setOnClickListener(this);
        findViewById(R.id.fl_back).setOnClickListener(this);

        Media media = new Media("", "", 0, 999, 0, R.drawable.ic_add_img, "");
        mediaList.add(media);
        initSoftKeyboard();

        chatView.setOnSendMessageListener(new ChatViewForPublish.OnSendMessageListener() {
            @Override
            public void onSendMessage(Message message) {
                //只有录音信息会回调这里
                mTvVoice.setVisibility(View.VISIBLE);
                voiceMessage = message;
                FileMessageBody voiceMessageBody = (FileMessageBody) voiceMessage.getBody();
                String voicePath = "";
                String voicePath1 = Constants.LOCAL_VOICE_PATH + voiceMessageBody.getFileName();
                String voicePath2 = getExternalFilesDir("voice").getAbsolutePath() + File.separator + voiceMessageBody.getFileName();
                if (new File(voicePath1).exists()) {
                    voicePath = voicePath1;
                } else if (new File(voicePath2).exists()) {
                    voicePath = voicePath2;
                }
                voiceDuration = FileUtils.getAmrDuration(voicePath) - 1;
                mTvVoice.setText((voiceDuration > 60 ? 60 : voiceDuration) + "\"");
            }

            @Override
            public void onEmojiMessage(String string, int position) {
                String str = mEditext.getText().toString() + EmojiUtils.emojiValues[position];
                SpannableString spannableString = EmojiUtils.parseEmoji(PublishActivity.this, str);
                mEditext.setText(spannableString);
                mEditext.setSelection(spannableString.length());
            }
        });

        createAdapter();
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_publish:
                publish();
                break;
            case R.id.tv_open:
                break;
            case R.id.ll_location:
                break;
            case R.id.fl_back:
                if (!checkContentIsEmpty()) {
                    showExitEditDialog();
                }
                break;
            case R.id.tv_voice:
                playVoice();
                break;
        }
    }

    void createAdapter() {
        //创建默认的线性LayoutManager
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.addItemDecoration(new SpacingDecoration(4, 10));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecycleView.setHasFixedSize(true);
        //创建并设置Adapter
        ArrayList<Media> medias = new ArrayList<>();
//        ArrayList<Media> select = argsIntent.getParcelableArrayListExtra(PickerConfig.DEFAULT_SELECTED_LIST);
        ArrayList<Media> select = new ArrayList<>();
        int maxSelect = 8;
        long maxSize = 999999;
        gridAdapter = new PublishMediaGridAdapter(medias, this, select, maxSelect, maxSize);
        mRecycleView.setAdapter(gridAdapter);

        gridAdapter.setOnItemClickListener(new PublishMediaGridAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Media data, ArrayList<Media> selectMedias) {
                done(selectMedias);
            }
        });
    }

    public void done(ArrayList<Media> selects) {
        Intent intent = new Intent(this, PublishPreviewActivity.class);
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 9);
        intent.putExtra(PickerConfig.PRE_RAW_LIST, selects);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_PREVIEW);
    }

    private void publish() {
        if (checkContentIsEmpty()) {

        } else {
            showContentEmptyDialog();
        }
    }

    public boolean checkContentIsEmpty() {
        int count = 0;
        if (mEditext.getText().toString().trim().isEmpty()) {
            count++;
        }

        if (mediaList.size() > 1) {
            count++;
        }

        if (count == 0) {
            return true;
        }
        return false;
    }

    private void showExitEditDialog() {
        ExitEditDialog exitEditDialog = new ExitEditDialog(this);
        exitEditDialog.setOnDialogCallBack(new ExitEditDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {
                finish();
            }
        });
        exitEditDialog.show();
    }

    private void showDeleteVoiceDialog() {
        DeleteVoiceDialog deleteVoiceDialog = new DeleteVoiceDialog(this);
        deleteVoiceDialog.setOnDialogCallBack(new DeleteVoiceDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {

            }
        });
        deleteVoiceDialog.show();
    }

    private void showContentEmptyDialog() {
        ContentEmptyDialog contentEmptyDialog = new ContentEmptyDialog(this);
        contentEmptyDialog.show();
    }

    boolean keyboardIsShown = false;
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
                        chatView.setVisibility(View.VISIBLE);
                    } else {
                        // 键盘收起
                        keyboardIsShown = false;
//                        if (!chatView.isShow()){
//                            chatView.hideView();
//                            chatView.setVisibility(View.GONE);
//                        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            LogUtils.i("huang", "requestCode=" + requestCode + "   resultCode=" + resultCode);
            if (requestCode == Constants.REQUEST_CODE_CAMERA) {
                final String path = data.getStringExtra("path");
                LogUtils.i("huang", "path=" + path);
                if (path.contains("mp4")) {
                    photoList.clear();
                    Media media = new Media(path, "", 0, 3, 999, 9999, "");
                    photoList.add(media);
                } else {
                    //视频图片不能同时存在
                    if (photoList.get(0).mediaType == 3) {
                        photoList.clear();
                    }
                    Media media = new Media(path, "", 0, 2, 999, 9999, "");
                    photoList.add(media);
                }
                gridAdapter.setData(photoList);
            }
            if (requestCode == Constants.REQUEST_CODE_PICKER) {
                select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                //视频图片不能同时存在
                if (photoList.get(0).mediaType == 3 && select.size() > 0) {
                    photoList.clear();
                }
                Boolean isOrigin = data.getBooleanExtra(PickerConfig.IS_ORIGIN, false);
                for (final Media media : select) {
                    onImageReturn(null, media.path, isOrigin);
                }
            }

            if (requestCode == Constants.REQUEST_CODE_PREVIEW) {
                photoList = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                gridAdapter.setData(photoList);
            }

            if (resultCode == 102) {
                LogUtils.i("CJT", "video");
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
                    startActivityForResult(new Intent(PublishActivity.this, CameraActivity.class), Constants.REQUEST_CODE_CAMERA);
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
     * 图片返回处理
     *
     * @param uri
     * @param filePath
     * @param isOrigin 是否原图发送
     */
    public void onImageReturn(Uri uri, String filePath, boolean isOrigin) {
//        String filePath = autoCameraUtils.getPath(this, uri);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        final Message imgMsg = Message.createImageMessage(Message.Type.CHAT, 0, 0, fileName, filePath);
        imgMsg.setStatus(Message.ChatStatus.SENDING);

        if (isOrigin) {
            Media media = new Media(filePath, "", 0, 2, 999, 9999, "");
            photoList.add(media);
            gridAdapter.setData(photoList);
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
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Message>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Message message) {
//                            judeBlackList(message);
                            FileMessageBody imgMessageBody = (FileMessageBody) imgMsg.getBody();
                            String outPath = Constants.LOCAL_IMG_PATH + imgMessageBody.getFileName();
                            Media media = new Media(outPath, "", 0, 2, 999, 9999, "");
                            photoList.add(media);
                            gridAdapter.setData(photoList);
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

    private void playVoice() {
        if (voiceMessage == null) return;
        FileMessageBody voiceMessageBody = (FileMessageBody) voiceMessage.getBody();
        String voicePath = "";
        String voicePath1 = Constants.LOCAL_VOICE_PATH + voiceMessageBody.getFileName();
        String voicePath2 = this.getExternalFilesDir("voice").getAbsolutePath() + File.separator + voiceMessageBody.getFileName();
        if (new File(voicePath1).exists()) {
            voicePath = voicePath1;
        } else if (new File(voicePath2).exists()) {
            voicePath = voicePath2;
        }
        AudioUtils.getInstance().play(voicePath, new AudioUtils.PlayStatusListener() {
            @Override
            public void playEnd() {
                mTvVoice.setText((voiceDuration > 60 ? 60 : voiceDuration) + "\"");
            }

            @Override
            public void playStart() {
                startTime = voiceDuration;
                handler.sendEmptyMessageDelayed(1,1000);
            }
        });
    }


    class MyHandler extends Handler {
        WeakReference<PublishActivity> mActivity;

        public MyHandler(PublishActivity chatActivity) {
            mActivity = new WeakReference<PublishActivity>(chatActivity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    startTime --;
                    mActivity.get().setRecorderingTime(startTime);
                    handler.removeMessages(1);
                    if (startTime > 0){
                        handler.sendEmptyMessageDelayed(1,1000);
                    }
                    break;
            }
        }
    }

    private void setRecorderingTime(int time){
        if (time > 0){
            mTvVoice.setText(time + "\"");
        } else {
            mTvVoice.setText(0 + "\"");
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        super.onDestroy();
    }
}

