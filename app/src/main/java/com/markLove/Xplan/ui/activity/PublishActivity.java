package com.markLove.Xplan.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.adapter.MediaGridAdapter;
import com.dmcbig.mediapicker.adapter.SpacingDecoration;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.module.emoji.EmojiUtils;
import com.markLove.Xplan.ui.adapter.PublishMediaGridAdapter;
import com.markLove.Xplan.ui.dialog.ContentEmptyDialog;
import com.markLove.Xplan.ui.dialog.DeleteVoiceDialog;
import com.markLove.Xplan.ui.dialog.ExitEditDialog;
import com.markLove.Xplan.ui.widget.ChatView;
import com.markLove.Xplan.ui.widget.ChatViewForPublish;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

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
    private ChatViewForPublish chatView;
    private PublishMediaGridAdapter gridAdapter;

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

        mTvPublish.setOnClickListener(this);
        mTvOpen.setOnClickListener(this);
        findViewById(R.id.ll_location).setOnClickListener(this);
        findViewById(R.id.fl_back).setOnClickListener(this);

        Media media = new Media("","",0,999,0,R.drawable.ic_add_img,"");
        mediaList.add(media);
        initSoftKeyboard();

        chatView.setOnSendMessageListener(new ChatViewForPublish.OnSendMessageListener() {
            @Override
            public void onSendMessage(Message message) {

            }

            @Override
            public void onEmojiMessage(String string,int position) {
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

    void createAdapter() {
        //创建默认的线性LayoutManager
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, PickerConfig.GridSpanCount);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.addItemDecoration(new SpacingDecoration(PickerConfig.GridSpanCount, PickerConfig.GridSpace));
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
}
