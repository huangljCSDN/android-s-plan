package com.markLove.Xplan.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import com.cjt2325.cameralibrary.JCameraView;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.SinglePickerActivity;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.ui.BaseFragment;
import com.markLove.Xplan.bean.GoImgLibraryBean;
import com.markLove.Xplan.bean.GoPhotoBean;
import com.markLove.Xplan.bean.GoPhotoFilesBean;
import com.markLove.Xplan.bean.GoViewBeaan;
import com.markLove.Xplan.bean.UploadFileBean;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.mvp.contract.FileContract;
import com.markLove.Xplan.mvp.presenter.FilePresenter;
import com.markLove.Xplan.ui.activity.CameraActivity;
import com.markLove.Xplan.ui.activity.PublishActivity;
import com.markLove.Xplan.ui.widget.MyWebView;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.OnCompressListener;

public class MineFragment extends BaseFragment<FilePresenter> implements FileContract.View{
    private MyWebView mWebView;
    private GoViewBeaan goViewBeaan;

    GoPhotoBean goPhotoBean;
    GoImgLibraryBean goImgLibraryBean;
    private int type;
    private String localFilePath;
    private ArrayList<Media> mediaList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(View view) {
        mWebView = new MyWebView(getContext());
        LinearLayout mll = view.findViewById(R.id.rootView);
        mll.addView(mWebView);
        initWebSettings();
    }

    /**
     * 设置websetting
     */
    private void initWebSettings(){
        mWebView.addJavascriptInterface(new JSInterface(getActivity()), "xplanfunc");
        mWebView.loadUrl("file:///android_asset/package/main/index.html#/user/native/1");

    }

    // 继承自Object类
    public class JSInterface  extends BaseJsInterface {

        public JSInterface(Activity mActivity) {
            super(mActivity);
        }

        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void toPublishPage(String json) {
            //{"chatType":1,"chatId":1}
            LogUtils.i("huang", "toPublishPage="+json);
            startPublishActivity();
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
            startSinglePickerActivity();
        }
    }


    private void startPublishActivity(){
        Intent intent = new Intent(getContext(),PublishActivity.class);
        startActivityForResult(intent,Constants.REQUEST_CODE_PUBLISH);
    }

    //    @Override
//    public void onBackPressed() {
//        if (mWebView.canGoBack()){
//            mWebView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.onDestroy();
    }


    @Override
    public FilePresenter onCreatePresenter() {
        return new FilePresenter();
    }

    private void startSinglePickerActivity() {
        Intent intent = new Intent(getActivity(), SinglePickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);
        startActivityForResult(intent, Constants.REQUEST_CODE_PICKER);
    }

    /**
     * 启动相机
     */
    private void startCameraActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                startCameraActivity2();
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, Constants.REQUEST_CODE_PERMISSION_ONE);
            }
        } else {
            startCameraActivity2();
        }
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

     /**
      * 压缩图片
     * @param photos
     */
    private void compressImg(List<String> photos, boolean isOrigin){
        final List<File> files = new ArrayList<>();
        final int size = photos.size();

        if (photos.size() == 0) return;
            top.zibin.luban.Luban.with(getContext())
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == Constants.REQUEST_CODE_CAMERA) {
                final String path = data.getStringExtra("path");
                Log.i("huang", "path=" + path);
                ArrayList<String> photos = new ArrayList<>();
                photos.add(path);
                compressImg(photos,false);
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
                compressImg(photos,false);
            }

            if (requestCode == Constants.REQUEST_CODE_PUBLISH){
                //刷新轨迹列表
                mWebView.loadUrl("javascript:refreshUserLocus()");
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
        PreferencesUtils.putString(getActivity(),Constants.ME_HEAD_IMG_URL,netUrl);
        String url = "javascript:"+callBackName+"(" + GsonUtils.obj2Json(goPhotoFilesBean) + ")";
        LogUtils.i("url="+url);
        mWebView.loadUrl("javascript:"+callBackName+"(" + GsonUtils.obj2Json(goPhotoFilesBean) + ")");
    }

    private void startCameraActivity2(){
        Intent intent = new Intent(getContext(), CameraActivity.class);
        intent.putExtra("type", JCameraView.BUTTON_STATE_ONLY_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
    }

    @Override
    public void downloadSuccess(String json) {

    }
}
