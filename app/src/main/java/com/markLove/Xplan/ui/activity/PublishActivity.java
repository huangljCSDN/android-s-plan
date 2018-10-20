package com.markLove.Xplan.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.ui.dialog.ContentEmptyDialog;
import com.markLove.Xplan.ui.dialog.DeleteVoiceDialog;
import com.markLove.Xplan.ui.dialog.ExitEditDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布动态
 */
public class PublishActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvHead;
    private TextView mTvPublish,mTvOpen;
    private EditText mEditext;
    private RecyclerView mRecycleView;
    private LinearLayout mLocation;
    private List<Media> mediaList = new ArrayList<>();

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

        mTvPublish.setOnClickListener(this);
        mTvOpen.setOnClickListener(this);
        findViewById(R.id.ll_location).setOnClickListener(this);
        findViewById(R.id.fl_back).setOnClickListener(this);

        Media media = new Media("","",0,999,0,R.drawable.ic_add_img,"");
        mediaList.add(media);
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_publish:
                publish();
                break;
            case R.id.tv_open:
                break;
            case R.id.ll_location:
                break;
            case R.id.fl_back:
                if (!checkContentIsEmpty()){
                    showExitEditDialog();
                }
                break;
        }
    }

    private void publish(){
        if (checkContentIsEmpty()){

        }else {
            showContentEmptyDialog();
        }
    }

    public boolean checkContentIsEmpty(){
        int count = 0;
        if (mEditext.getText().toString().trim().isEmpty()){
            count ++;
        }

        if (mediaList.size() > 1){
            count ++;
        }

        if (count == 0){
            return true;
        }
        return false;
    }

    private void showExitEditDialog(){
        ExitEditDialog exitEditDialog = new ExitEditDialog(this);
        exitEditDialog.setOnDialogCallBack(new ExitEditDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {
                finish();
            }
        });
        exitEditDialog.show();
    }

    private void showDeleteVoiceDialog(){
        DeleteVoiceDialog deleteVoiceDialog = new DeleteVoiceDialog(this);
        deleteVoiceDialog.setOnDialogCallBack(new DeleteVoiceDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {

            }
        });
        deleteVoiceDialog.show();
    }

    private void showContentEmptyDialog(){
        ContentEmptyDialog contentEmptyDialog = new ContentEmptyDialog(this);
        contentEmptyDialog.show();
    }
}
