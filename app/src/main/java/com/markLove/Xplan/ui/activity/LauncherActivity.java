package com.markLove.Xplan.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;

public class LauncherActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_launcher;
    }

    protected void init(Bundle savedInstanceState) {
        fullScreen(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LauncherActivity.this, TestActivity.class);
                LauncherActivity.this.startActivity(intent);
            }
        });
        getPermissions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_register:
                Intent intent2 = new Intent(this, RegisterActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager
                            .PERMISSION_GRANTED) {
            } else{
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                }, 100);
            }
        }
    }


    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
