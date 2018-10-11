package com.markLove.xplan.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.xplan.R;
import com.markLove.xplan.base.mvp.BasePresenter;
import com.markLove.xplan.base.ui.BaseActivity;
import com.markLove.xplan.bean.Recorder;
import com.markLove.xplan.recorder.MediaManager;
import com.markLove.xplan.recorder.RecorderAdapter;
import com.markLove.xplan.recorder.view.AudioRecorderButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE_PERMISSION = 100; //权限申请自定义码
    private final int REQUEST_CODE_CAMERA = 101; //相机
    private final int REQUEST_CODE_PICKER = 102; //相册

    private ImageView imageView;

    private ListView mListView;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas =new ArrayList<>();

    private AudioRecorderButton mAudioRecorderButton;
    private View mAnimView;

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
        findViewById(R.id.fl_sound).setOnClickListener(this);
        findViewById(R.id.fl_camera).setOnClickListener(this);
        findViewById(R.id.fl_pic).setOnClickListener(this);
        findViewById(R.id.fl_emoji).setOnClickListener(this);
        imageView = findViewById(R.id.image);

        mListView = findViewById(R.id.id_listview);
        mAudioRecorderButton = findViewById(R.id.id_recorder_button);

        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                //每完成一次录音
                Recorder recorder = new Recorder(seconds,filePath);
                mDatas.add(recorder);
                //更新adapter
                mAdapter.notifyDataSetChanged();
                //设置listview 位置
                mListView.setSelection(mDatas.size()-1);
            }
        });
        setListViewAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_sound:

                break;
            case R.id.fl_camera:
                getPermissions();
                break;
            case R.id.fl_pic:
                startPickerActivity();
                break;
            case R.id.fl_emoji:
                break;
        }
    }

    ArrayList<Media> select;

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
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                this.startActivityForResult(new Intent(GroupChatActivity.this, CameraActivity.class), REQUEST_CODE_CAMERA);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(GroupChatActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
            }
        } else {
            this.startActivityForResult(new Intent(GroupChatActivity.this, CameraActivity.class), REQUEST_CODE_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("huang","requestCode="+requestCode+"   resultCode="+resultCode);
        if (requestCode == REQUEST_CODE_CAMERA) {
            Log.i("CJT", "picture");
            final String path = data.getStringExtra("path");
            Log.i("huang","path="+path);
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

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
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
                    startActivityForResult(new Intent(GroupChatActivity.this, CameraActivity.class), REQUEST_CODE_CAMERA);
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    Uri getUri(String path){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return   FileProvider.getUriForFile(this, this.getPackageName()+ ".dmc", new File(path));
        }else {
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
     * @param path
     */
    private void playVideo(String path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(getUri(path), "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (isIntentAvailable(GroupChatActivity.this, intent)) {
            startActivity(intent);
        } else {
            Toast.makeText(GroupChatActivity.this,getString(com.dmcbig.mediapicker.R.string.cant_play_video), Toast.LENGTH_SHORT).show();
        }
    }


    private void setListViewAdapter(){
        mAdapter = new RecorderAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //如果第一个动画正在运行， 停止第一个播放其他的
                if (mAnimView != null) {
                    mAnimView.setBackgroundResource(R.drawable.adj);
                    mAnimView = null;
                }
                //播放动画
                mAnimView = view.findViewById(R.id.id_recorder_anim);
                mAnimView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable animation = (AnimationDrawable) mAnimView.getBackground();
                animation.start();

                //播放音频  完成后改回原来的background
                MediaManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAnimView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });
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

}
