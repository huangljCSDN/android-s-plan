package com.markLove.Xplan.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cjt2325.cameralibrary.JCameraView;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.SinglePickerActivity;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.GoImgLibraryBean;
import com.markLove.Xplan.bean.GoPhotoBean;
import com.markLove.Xplan.bean.GoPhotoFilesBean;
import com.markLove.Xplan.bean.UploadFileBean;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.mvp.contract.FileContract;
import com.markLove.Xplan.mvp.presenter.FilePresenter;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.KeyboardUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;
import com.markLove.Xplan.utils.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.OnCompressListener;

public class WebViewActivity extends BaseActivity<FilePresenter>  implements FileContract.View {
    private MyWebView mWebView;
    private String url;
    GoPhotoBean goPhotoBean;
    GoImgLibraryBean goImgLibraryBean;
    private int type;
    private String localFilePath;
    private ArrayList<Media> mediaList = new ArrayList<>();
    private LinearLayout llEdit;
    private EditText editText;
    private ImageView btnSend;
    //是否是全路径
    private boolean isAll;

    private JSInterface jsInterface;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fullScreen(this);
        StatusBarUtil.StatusBarLightMode(this);
        mWebView = new MyWebView(this);
        LinearLayout mll = findViewById(R.id.rootView);
        mll.addView(mWebView);

        isAll = getIntent().getBooleanExtra("isAll",false);
        url = getIntent().getStringExtra("url");
        jsInterface = new JSInterface(this);
        mWebView.addJavascriptInterface(jsInterface, "xplanfunc");
        if (isAll){
            mWebView.loadUrl(url);
        } else {
            mWebView.loadUrl("file:///android_asset/"+url);
        }

        editText = findViewById(R.id.et_input_msg2);
        llEdit = findViewById(R.id.ll_exit);
        btnSend = findViewById(R.id.btn_send);

        jsInterface.setOnJsInterfaceCallBack(new BaseJsInterface.OnJsInterfaceCallBack() {
            @Override
            public void openInPutText() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llEdit.setVisibility(View.VISIBLE);
                        editText.setVisibility(View.VISIBLE);
                        editText.requestFocus();
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                KeyboardUtils.showKeyboard(editText);
                            }
                        });
                    }
                });
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideKeyboard(editText);
            }
        });
        initSoftKeyboard();
    }

    private void sendCommend(){
        String content = editText.getText().toString().trim();
        HashMap<String,String> map = new HashMap<>();
        map.put("text",content);
        String json = GsonUtils.obj2Json(map);
        mWebView.loadUrl("javascript:openInPutTextFinish("+json+")");
        editText.setText("");
        llEdit.setVisibility(View.GONE);
    }

    //设置软键盘弹起和关闭的监听
    int usableHeightPrevious = 0;
    private void initSoftKeyboard() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int usableHeightNow = computeUsableHeight();
                if (usableHeightNow != usableHeightPrevious) {
                    int usableHeightSansKeyboard = getWindow().getDecorView().getHeight();
                    int heightDifference = usableHeightSansKeyboard - usableHeightNow;
                    if (heightDifference > (usableHeightSansKeyboard / 4)) {
                        llEdit.setVisibility(View.VISIBLE);
                    } else {
                        // 键盘收起
//                        keyboardIsShown = false;
                        sendCommend();
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

    public class JSInterface extends BaseJsInterface {

        public JSInterface(Activity mActivity) {
            super(mActivity);
        }

        @JavascriptInterface
        public void exitApp() {
            App.getInstance().outLogin(WebViewActivity.this);
        }

        @JavascriptInterface
        @Override
        public void goPhoto(String json) {
            goPhotoBean = GsonUtils.json2Bean(json, GoPhotoBean.class);
            type = 1;
            PreferencesUtils.putString(WebViewActivity.this, Constants.TOKEN_KEY,goPhotoBean.getToken());
            startCameraActivity();
        }

        @JavascriptInterface
        @Override
        public void fromImgLibrary(String json) {
            type = 2;
            goImgLibraryBean = GsonUtils.json2Bean(json, GoImgLibraryBean.class);
            PreferencesUtils.putString(WebViewActivity.this,Constants.TOKEN_KEY,goImgLibraryBean.getToken());
            startSinglePickerActivity();
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

    private void startCameraActivity2(){
        Intent intent = new Intent(WebViewActivity.this, CameraActivity.class);
        intent.putExtra("type", JCameraView.BUTTON_STATE_ONLY_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
    }

    private void startSinglePickerActivity() {
        Intent intent = new Intent(this, SinglePickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);
        startActivityForResult(intent, Constants.REQUEST_CODE_PICKER);
    }

    /**
     * 压缩图片
     * @param photos
     */
    private void compressImg(List<String> photos){
        final List<File> files = new ArrayList<>();
        final int size = photos.size();

        if (photos.size() == 0) return;
        top.zibin.luban.Luban.with(this)
                .load(photos)
                .ignoreBy(100)
                .setTargetDir(Constants.LOCAL_IMG_PATH) //缓存路径
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    int count = 0;
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        count ++;
                        files.add(file);
                        if (count == size){
                            mPresenter.upload(files);
                            count =0;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                }).launch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == Constants.REQUEST_CODE_CAMERA) {
                final String path = data.getStringExtra("path");
                Log.i("huang", "path=" + path);
                ArrayList<String> photos = new ArrayList<>();
                photos.add(path);
                compressImg(photos);
                localFilePath = path;
            }
            if (requestCode == Constants.REQUEST_CODE_PICKER) {
                ArrayList<Media> select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                ArrayList<String> photos = new ArrayList<>();
                for (final Media media : select) {
                    LogUtils.i("media", media.toString());
                    if (new File(media.path).exists()){
                        photos.add(media.path);
                        localFilePath = media.path;
                    }
                }
                compressImg(photos);
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

    @Override
    public void downloadSuccess(String json) {

    }

    private void fileToJs(File file,String netUrl,String callBackName){
        GoPhotoFilesBean goPhotoFilesBean = new GoPhotoFilesBean();
        goPhotoFilesBean.setImgName(file.getName());
        goPhotoFilesBean.setImgPath(file.getAbsolutePath());
        goPhotoFilesBean.setUploadData(netUrl);
        PreferencesUtils.putString(this,Constants.ME_HEAD_IMG_URL,netUrl);
        String url = "javascript:"+callBackName+"(" + GsonUtils.obj2Json(goPhotoFilesBean) + ")";
        LogUtils.i("url="+url);
        mWebView.loadUrl("javascript:"+callBackName+"(" + GsonUtils.obj2Json(goPhotoFilesBean) + ")");
    }


    private void finishActivity(){

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

    @Override
    public FilePresenter onCreatePresenter() {
        return new FilePresenter();
    }
}
