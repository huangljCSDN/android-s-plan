package com.markLove.Xplan.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.utils.FileUtils;
import com.markLove.Xplan.utils.ToastUtils;


/**
 * Created by luoyunmin on 2017/8/14.
 */

public class ZoomImageActivity extends BaseActivity {

    ImageView zoomImage;
    PopupWindow popupWindow;
    GestureDetector gestureDetector;
    String imgPath;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_zoom_image;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        setTranslucentStatusColor(Color.TRANSPARENT);
        zoomImage = findViewById(R.id.zoom_image);
        initData();
    }

    protected void initData() {
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra("data");
        if (null != data) {
            imgPath = data.getString("imgPath");
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            zoomImage.setImageBitmap(bitmap);
        }
        gestureDetector = new GestureDetector(this, new GestureListener());
        zoomImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        zoomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void showSavePopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_zoom_img_save, null);
        view.findViewById(R.id.zoom_img_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xplan" + File.separator + "img" + File.separator;
                String destPath = Constants.LOCAL_IMG_PATH;
                FileUtils.copyFileToDir(imgPath, destPath);
                ToastUtils.show(ZoomImageActivity.this, "保存成功", 0);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + destPath)));
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.zoom_img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != popupWindow)
                    popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setBackground(0.5f);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(zoomImage, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackground(1.0f);
            }
        });
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }


    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (null != popupWindow && popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                finish();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            showSavePopupWindow();
        }
    }

    private void setBackground(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }
}
