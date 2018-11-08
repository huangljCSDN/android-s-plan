package com.markLove.Xplan.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.SinglePickerActivity;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.GoImgLibraryBean;
import com.markLove.Xplan.bean.GoPhotoBean;
import com.markLove.Xplan.bean.GoPhotoFilesBean;
import com.markLove.Xplan.bean.UploadFileBean;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.mvp.contract.FileContract;
import com.markLove.Xplan.mvp.presenter.FilePresenter;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.AppManager;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity<FilePresenter> implements FileContract.View {
    private MyWebView mWebView;
    GoPhotoBean goPhotoBean;
    GoImgLibraryBean goImgLibraryBean;
    private int type;
    private String localFilePath;
    private ArrayList<Media> mediaList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fullScreen(this);
        mWebView = new MyWebView(this);

        LinearLayout mll = findViewById(R.id.rootView);
        mll.addView(mWebView);
        initWebSettings();

        mWebView.requestFocus(View.FOCUS_DOWN);
    }

    /**
     * 设置websetting
     */
    private void initWebSettings(){
        
        mWebView.addJavascriptInterface(new JSInterface(this), "xplanfunc");
        mWebView.loadUrl(BaseJsInterface.LOGIN_URL);
    }

    public class JSInterface extends BaseJsInterface {

        public JSInterface(Activity mActivity) {
            super(mActivity);
        }

        /**
         * 拍照
         * <p>
         * uploadUrl
         * sCallback photoFinish
         */
        @JavascriptInterface
        @Override
        public void goPhoto(String json) {
            goPhotoBean = GsonUtils.json2Bean(json, GoPhotoBean.class);
            type = 1;
            PreferencesUtils.putString(LoginActivity.this,Constants.TOKEN_KEY,goPhotoBean.getToken());
            startCameraActivity();
        }

        /**
         * 调用相册，视频
         * <p>
         * uploadUrl  后端提供的上传接口名
         * selectType 选择内容：1-图片、2-视频、3图片和视频
         * backType   选择方式-单选（single）、多选(multi) 注：如果是多选，则返回数组
         * sCallback  photoFinish
         */
        @JavascriptInterface
        @Override
        public void fromImgLibrary(String json) {
            type = 2;
            goImgLibraryBean = GsonUtils.json2Bean(json, GoImgLibraryBean.class);
            PreferencesUtils.putString(LoginActivity.this,Constants.TOKEN_KEY,goImgLibraryBean.getToken());
            startSinglePickerActivity();
        }

        /**
         * 获取用户信息
         *
         * @param userInfo
         */
        @JavascriptInterface
        @Override
        public void toHomePage(String userInfo) {
//            ToastUtils.showLong(LoginActivity.this, "toHomePage");
            LogUtil.i("userInfo= "+userInfo);
            PreferencesUtils.putString(LoginActivity.this,PreferencesUtils.KEY_USER,userInfo);
            UserBean userBean = GsonUtils.json2Bean(userInfo,UserBean.class);
            PreferencesUtils.putInt(LoginActivity.this,Constants.ME_USER_ID,userBean.getUserInfo().getUserId());
            PreferencesUtils.putString(LoginActivity.this,Constants.ME_HEAD_IMG_URL,userBean.getUserInfo().getHeadImageUrl());
            PreferencesUtils.putString(LoginActivity.this,Constants.TOKEN_KEY,userBean.getToken());
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            LoginActivity.this.startActivity(intent);
            AppManager.getAppManager().finishActivity(LauncherActivity.class);
            finish();
        }
    }

    /**
     * 启动相机
     */
    private void startCameraActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                startCameraActivity2();
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, Constants.REQUEST_CODE_PERMISSION_ONE);
            }
        } else {
            startCameraActivity2();
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()){
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.onDestroy();
    }

    /**
     * 上传文件
     */
    private void uploadFile() {
        if (mediaList.isEmpty()) return;
        List<File> files = new ArrayList<>();
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
        LogUtils.i("huang", files.toString());
        mPresenter.upload(files);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == Constants.REQUEST_CODE_CAMERA) {
                final String path = data.getStringExtra("path");
                Log.i("huang", "path=" + path);
                Media media = new Media(path, "", 0, 1, 999, 9999, "");
                mediaList.clear();
                mediaList.add(media);
                localFilePath = path;
                uploadFile();
            }
            if (requestCode == Constants.REQUEST_CODE_PICKER) {
                ArrayList<Media> select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                for (final Media media : select) {
                    Log.i("media", media.toString());
                    Media media1 = new Media(media.path, "", 0, 1, 999, 9999, "");
                    mediaList.clear();
                    mediaList.add(media1);
                    localFilePath = media.path;
                }
                uploadFile();
            }
        }
    }

    @Override
    public void uploadSuccess(UploadFileBean bean) {
        hideLoading();
        ArrayList<String> paths = new ArrayList<>();
        if (bean.getList() != null && !bean.getList().isEmpty()){
            for (UploadFileBean.FileBean fileBean : bean.getList()) {
                paths.add(fileBean.getPath());
            }
        }

        LogUtils.i("huang","localFilePath="+localFilePath);
        if (type == 1 && goPhotoBean != null){
            File file = new File(localFilePath);
            String netUrl = paths.get(0);
            if (!file.exists()){
                try {
                    file.createNewFile();
                    fileToJs(file,netUrl,goPhotoBean.getsCallback());
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                fileToJs(file,netUrl,goPhotoBean.getsCallback());
            }
        } else if (type == 2 && goImgLibraryBean != null){
            File file = new File(localFilePath);
            String netUrl = paths.get(0);
            if (!file.exists()){
                try {
                    file.createNewFile();
                    fileToJs(file,netUrl,goImgLibraryBean.getsCallback());
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                fileToJs(file,netUrl,goImgLibraryBean.getsCallback());
            }
        }
    }

    private void fileToJs(File file,String netUrl,String callBackName){
        GoPhotoFilesBean goPhotoFilesBean = new GoPhotoFilesBean();
        goPhotoFilesBean.setImgName(file.getName());
        goPhotoFilesBean.setImgPath(file.getAbsolutePath());
        goPhotoFilesBean.setUploadData(netUrl);
        String url = "javascript:"+callBackName+"(" + GsonUtils.obj2Json(goPhotoFilesBean) + ")";
        LogUtils.i("url="+url);
        mWebView.loadUrl("javascript:"+callBackName+"(" + GsonUtils.obj2Json(goPhotoFilesBean) + ")");
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
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    startCameraActivity2();
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startCameraActivity2(){
        Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
        intent.putExtra("type", JCameraView.BUTTON_STATE_ONLY_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
    }

    private void startSinglePickerActivity() {
        Intent intent = new Intent(this, SinglePickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);
        startActivityForResult(intent, Constants.REQUEST_CODE_PICKER);
    }

    @Override
    public void downloadSuccess(String json) {

    }

    @Override
    public FilePresenter onCreatePresenter() {
        return new FilePresenter();
    }
}
