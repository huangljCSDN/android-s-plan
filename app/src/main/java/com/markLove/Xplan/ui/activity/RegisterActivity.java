package com.markLove.Xplan.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.SinglePickerActivity;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;

import java.util.ArrayList;

public class RegisterActivity extends BaseActivity {
    private ImageView imageView;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        imageView = findViewById(R.id.icon);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, SinglePickerActivity.class);
                RegisterActivity.this.startActivityForResult(intent,200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200){
            ArrayList<Media>  select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            Boolean isOrigin = data.getBooleanExtra(PickerConfig.IS_ORIGIN, false);
            for (final Media media : select) {
                Log.i("media", media.toString());
                Uri mediaUri = Uri.parse("file://" + media.path);
                Glide.with(this)
                        .load(mediaUri)
                        .into(imageView);
            }
        }
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
