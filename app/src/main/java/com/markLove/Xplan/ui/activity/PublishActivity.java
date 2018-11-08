package com.markLove.Xplan.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import com.amap.api.location.AMapLocation;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.adapter.SpacingDecoration;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.UploadFileBean;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.bean.msg.body.FileMessageBody;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.module.emoji.EmojiUtils;
import com.markLove.Xplan.module.image.IImageCompressor;
import com.markLove.Xplan.mvp.contract.FileContract;
import com.markLove.Xplan.mvp.contract.PublishContract;
import com.markLove.Xplan.mvp.presenter.FilePresenter;
import com.markLove.Xplan.mvp.presenter.PublishPresenter;
import com.markLove.Xplan.ui.adapter.PublishMediaGridAdapter;
import com.markLove.Xplan.ui.dialog.ContentEmptyDialog;
import com.markLove.Xplan.ui.dialog.ExitEditDialog;
import com.markLove.Xplan.ui.widget.ChatViewForPublish;
import com.markLove.Xplan.utils.AudioUtils;
import com.markLove.Xplan.utils.FileUtils;
import com.markLove.Xplan.utils.ImageLoaderUtils;
import com.markLove.Xplan.utils.ImageUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;
import com.markLove.Xplan.utils.StatusBarUtil;
import com.markLove.Xplan.utils.ToastUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class PublishActivity extends BaseActivity<PublishPresenter> implements View.OnClickListener, PublishContract.View, FileContract.View {
    private ImageView mIvHead;
    private ImageView mIvLocation;
    private TextView mTvPublish, mTvOpen;
    private TextView mTvVoice;
    private TextView mTvLocation;
    private EditText mEditext;
    private RecyclerView mRecycleView;
    private LinearLayout mLocation;
    private ArrayList<Media> mediaList = new ArrayList<>();
    private ChatViewForPublish chatView;
    private PublishMediaGridAdapter gridAdapter;
    private ArrayList<Media> select;
    private Message voiceMessage;
    private int voiceDuration;
    private int videoDuration;
    private MyHandler handler = new MyHandler(this);
    private FilePresenter filePresenter;
    private int startTime;
    private int visible;
    //0:文字 1：图片，2：视频(第一个文件是视频，第二个是缩略图)，3：语音
    private int type;
    private String city = "";
    private UserBean meUserBean;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarColor(this, R.color.white);
        StatusBarUtil.StatusBarLightMode(this);
        mIvHead = findViewById(R.id.iv_head);
        mTvPublish = findViewById(R.id.tv_publish);
        mTvOpen = findViewById(R.id.tv_open);
        mEditext = findViewById(R.id.et_content);
        mRecycleView = findViewById(R.id.recyclerView);
        chatView = findViewById(R.id.chatview);
        mTvVoice = findViewById(R.id.tv_voice);
        mTvLocation = findViewById(R.id.tv_location);
        mIvLocation = findViewById(R.id.iv_location);

        mTvPublish.setOnClickListener(this);
        mTvOpen.setOnClickListener(this);
        mTvVoice.setOnClickListener(this);
        findViewById(R.id.ll_location).setOnClickListener(this);
        findViewById(R.id.fl_back).setOnClickListener(this);

        initSoftKeyboard();
        setListener();
        createAdapter();
        meUserBean = App.getInstance().getUserBean();

        filePresenter = new FilePresenter();
        filePresenter.attachView(this);

        ImageLoaderUtils.displayCircle(this, PreferencesUtils.getString(this, Constants.ME_HEAD_IMG_URL), mIvHead);
    }

    private void setListener() {
        chatView.setOnSendMessageListener(new ChatViewForPublish.OnSendMessageListener() {
            @Override
            public void onSendMessage(Message message) {
                //只有录音信息会回调这里
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
                voiceDuration = FileUtils.getAmrDuration(voicePath);
                mTvVoice.setText((voiceDuration > 60 ? 60 : voiceDuration - 1) + "\"");
                type = 3;
                mediaList.clear();
                Media media = new Media(voicePath, "", 0, 3, 999, 9999, "");
                mediaList.add(media);
                mTvPublish.setTextColor(getColor(R.color.color_30efec));
                mRecycleView.setVisibility(View.GONE);
                mTvVoice.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEmojiMessage(String string, int position) {
                String str = mEditext.getText().toString() + EmojiUtils.emojiValues[position];
                SpannableString spannableString = EmojiUtils.parseEmoji(PublishActivity.this, str);
                mEditext.setText(spannableString);
                mEditext.setSelection(spannableString.length());
            }
        });
    }


    @Override
    public PublishPresenter onCreatePresenter() {
        return new PublishPresenter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_publish:
                uploadFile();
                break;
            case R.id.tv_open:
                startSetPermissionActivity();
                break;
            case R.id.ll_location:
                getLocation();
                break;
            case R.id.fl_back:
                if (!checkContentIsEmpty()) {
                    showExitEditDialog();
                } else {
                    finish();
                }
                break;
            case R.id.tv_voice:
                playVoice();
                break;
        }
    }

    private void getLocation() {
        AMapLocation aMapLocation = App.getInstance().getaMapLocation();
        if (aMapLocation != null) {
            city = aMapLocation.getCity();
            mTvLocation.setText(city);
            mTvLocation.setTextColor(getColor(R.color.color_30efec));
            mIvLocation.setSelected(true);
        } else {
            ToastUtils.showShort(this, "获取定位失败");
        }
    }

    private void startSetPermissionActivity() {
        Intent intent = new Intent(this, SetPermissionActivity.class);
        intent.putExtra("visible", visible);
        startActivityForResult(intent, Constants.REQUEST_CODE_VISIBLE);
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
        int maxSelect = 9;
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

    /**
     * 校验发布的内容是否为空
     *
     * @return
     */
    public boolean checkContentIsEmpty() {
        if (mEditext.getText().toString().trim().isEmpty() && mediaList.size() <= 0 &&
                voiceMessage == null) {
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
            //拍照
            if (requestCode == Constants.REQUEST_CODE_CAMERA) {
                final String path = data.getStringExtra("path");
                LogUtils.i("huang", "path=" + path);
                if (path.contains("mp4")) {
                    mediaList.clear();
                    Media media = new Media(path, "", 0, 2, 999, 9999, "");
                    mediaList.add(media);
                    videoDuration = FileUtils.getAmrDuration(path);
                    type = 2;
                } else {
                    //视频、语音、图片不能同时存在
                    if (!mediaList.isEmpty()) {
                        if (mediaList.get(0).mediaType == 2 || mediaList.get(0).mediaType == 3) {
                            mediaList.clear();
                        }
                    }
                    Media media = new Media(path, "", 0, 1, 999, 9999, "");
                    mediaList.add(media);
                    type = 1;
                }
                gridAdapter.setData(mediaList);
                mTvPublish.setTextColor(getColor(R.color.color_30efec));
                mRecycleView.setVisibility(View.VISIBLE);
                mTvVoice.setVisibility(View.GONE);
            }
            //相册
            if (requestCode == Constants.REQUEST_CODE_PICKER) {
                select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                if (select != null && !select.isEmpty()) {
                    //视频、语音、图片不能同时存在
                    if (!mediaList.isEmpty()) {
                        if ((mediaList.get(0).mediaType == 2 || mediaList.get(0).mediaType == 3) && select.size() > 0) {
                            mediaList.clear();
                        }
                    }
                    //是否原图
//                    Boolean isOrigin = data.getBooleanExtra(PickerConfig.IS_ORIGIN, false);
                    Boolean isOrigin = true;
                    for (final Media media : select) {
                        onImageReturn(null, media.path, isOrigin);
                    }
                    type = 1;
                    mTvPublish.setTextColor(getColor(R.color.color_30efec));
                    mRecycleView.setVisibility(View.VISIBLE);
                    mTvVoice.setVisibility(View.GONE);
                }
            }
            //已选图片预览
            if (requestCode == Constants.REQUEST_CODE_PREVIEW) {
                mediaList = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                if (mediaList.isEmpty()) {
                    mTvPublish.setTextColor(getColor(R.color.color_333333));
                    mRecycleView.setVisibility(View.GONE);
                    mTvVoice.setVisibility(View.GONE);
                }
                gridAdapter.setData(mediaList);
            }
            //设置是否可见
            if (requestCode == Constants.REQUEST_CODE_VISIBLE) {
                visible = data.getIntExtra("visible", 0);
                if (visible == 0) {
                    mTvOpen.setText(getString(R.string.open));
                } else {
                    mTvOpen.setText(getString(R.string.only_me_see));
                }
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
        final Message imgMsg = Message.createImageMessage(Message.Type.CHAT, 0, 0,"", fileName, filePath);
        imgMsg.setStatus(Message.ChatStatus.SENDING);
        isOrigin = true;
        LogUtils.i("huang","filePath="+filePath);
        if (isOrigin) {
            Media media = new Media(filePath, "", 0, 1, 999, 9999, "");
            mediaList.add(media);
            gridAdapter.setData(mediaList);
        } else {
            Observable.create(new ObservableOnSubscribe<Message>() {
                @Override
                public void subscribe(final ObservableEmitter<Message> emitter) throws Exception {
                    final FileMessageBody imgMessageBody = (FileMessageBody) imgMsg.getBody();
                    final String outPath = Constants.LOCAL_IMG_PATH + imgMessageBody.getFileName();
                    LogUtils.i("huang", "outPath=" + outPath);
                    ImageUtils.compressImageInPath(imgMessageBody.getFilePath(), Constants.LOCAL_IMG_PATH, new IImageCompressor.OnImageCompressListener() {
                        @Override
                        public void onCompressStart(String msg) {

                        }

                        @Override
                        public void onCompressComplete(List<String> destFilePaths) {
                            if (destFilePaths != null && destFilePaths.size() > 0) {
                                LogUtils.i("huang", "destFilePaths=" + destFilePaths.get(0));
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
                            FileMessageBody imgMessageBody = (FileMessageBody) imgMsg.getBody();
//                            String outPath = Constants.LOCAL_IMG_PATH + imgMessageBody.getFileName();
                            Media media = new Media(imgMessageBody.getFilePath(), "", 0, 1, 999, 9999, "");
                            mediaList.add(media);
                            gridAdapter.setData(mediaList);
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
                mTvVoice.setText((voiceDuration > 60 ? 60 : voiceDuration - 1) + "\"");
            }

            @Override
            public void playStart() {
                startTime = voiceDuration - 1;
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        });
    }

    @Override
    public void uploadSuccess(UploadFileBean bean) {
        addLocus(bean.getList());
    }

    /**
     * 上传文件
     */
    private void uploadFile() {
        if (!checkContentIsEmpty()) {
            if (type != 0) {
                List<File> files = new ArrayList<>();
                if (type != 2) {
                    for (Media media : mediaList) {
                        String path = media.path;
                        File file = new File(path);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                                files.add(file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            files.add(new File(path));
                        }
                    }
                } else {
                    Media media = mediaList.get(0);
                    String path = media.path;
                    Bitmap bitmap = FileUtils.getVideoThumb2(path);
                    File videoFile = new File(path);
                    String videoImagPath = FileUtils.bitmap2File(bitmap, "VIDEO_IMG_" + System.currentTimeMillis());
                    files.add(videoFile);
                    files.add(new File(videoImagPath));
                }
                LogUtils.i("huang", files.toString());
                filePresenter.upload(files);
            } else {
                addLocus(null);
            }
        } else {
            showContentEmptyDialog();
        }
    }

    /**
     * 网络请求:发布
     */
    private void addLocus(ArrayList<UploadFileBean.FileBean> fileBeanArrayList) {
        ArrayList<String> paths = new ArrayList<>();
        if (fileBeanArrayList != null && !fileBeanArrayList.isEmpty()){
            for (UploadFileBean.FileBean fileBean : fileBeanArrayList) {
                paths.add(fileBean.getPath());
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("path", paths.toString());
        //1：图片，2：视频(第一个文件是视频，第二个是缩略图)，3：语音
        map.put("type", String.valueOf(type));
        //上传视频/语音时长
        if (type == 2) {
            map.put("duration", String.valueOf(videoDuration));
        } else if (type == 3) {
            map.put("duration", String.valueOf(voiceDuration));
        } else {
            map.put("duration", "0");
        }
        map.put("content", mEditext.getText().toString().trim());
        map.put("address", city);
        map.put("visible", String.valueOf(visible));
        mPresenter.addLocus(map);
    }

    @Override
    public void downloadSuccess(String json) {

    }

    @Override
    public void refreshUI(String json) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
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
                    startTime--;
                    mActivity.get().setRecorderingTime(startTime);
                    handler.removeMessages(1);
                    if (startTime > 0) {
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
        }
    }

    private void setRecorderingTime(int time) {
        if (time > 0) {
            mTvVoice.setText(time + "\"");
        } else {
            mTvVoice.setText(0 + "\"");
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        chatView.onDestroy();
        filePresenter.detachView();
        super.onDestroy();
    }
}

