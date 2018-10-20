package com.markLove.Xplan.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.utils.Ln;
import com.markLove.Xplan.utils.PhotoUtil;
import com.markLove.Xplan.ui.dialog.ChoosePicDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by huanglingjun on 2017/11/8.
 */

public class BaseContractActivity extends BaseActivity implements ChoosePicDialog.OnMenuClickListener{
    protected static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 10000;
    protected static final String TAG="BaseContractActivity";
    // 拍照的file, 剪切的file
    protected File photoFile, cropFile;
    protected int photoCount;

    /**
     * request code 获取相片
     */
    protected static final int REQUEST_CODE_GALLERY = 1408;
    protected static final int REQUEST_CODE_PHOTO = 1421;
    /**
     * 剪切相片
     */
    protected static final int REQUEST_CODE_CROP = 1423;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    public void onMenuClick(int menuType) {
        if (menuType == ChoosePicDialog.GALLERY) {
            goPickerActivity();

        } else if (menuType == ChoosePicDialog.TAKE_PHOTO) {
            // 拍照
            photoFile = PhotoUtil.newPhotoFile();
            PhotoUtil.takePhotoIntent(this, REQUEST_CODE_PHOTO, photoFile);
        }
    }

    ArrayList<Media> select;
    void goPickerActivity(){
        Intent intent =new Intent(this, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE,PickerConfig.PICKER_IMAGE_VIDEO);//default image and video (Optional)
        long maxSize=188743680L;//long long long
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE,maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT,15);  //default 40 (Optional)
        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST,select); // (Optional)
        this.startActivityForResult(intent,REQUEST_CODE_GALLERY);
    }

    protected void showPicDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE2);
            } else {
                showMenuDialog();
            }
        } else {
            showMenuDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMenuDialog();
            } else {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                onGalleryResult(resultCode, data);
                break;
            case REQUEST_CODE_PHOTO:
                onPhotoResult(resultCode, data);
                break;
            case REQUEST_CODE_CROP:
                onCropResult(resultCode, data);
                break;
        }
    }

    /**
     * 拍照
     *
     * @param resultCode
     * @param data
     */
    protected void onPhotoResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && photoFile != null) {
            //指定了url存储相片，所以data为空
//            compressImage(photoFile.getAbsolutePath());
            setBitmap(photoFile.getAbsolutePath());
        } else {
            Ln.e("拍照失败");
        }
    }


    /**
     * 选择相片
     *
     * @param resultCode
     * @param data
     */
    protected void onGalleryResult(int resultCode, Intent data) {
        if (data != null){
            select=data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            Log.i("select","select.size"+select.size());
            for(Media media:select){
                Log.i("media",media.path);
                Log.e("media","s:"+media.size);
//                imageView.setImageURI(Uri.parse(media.path));
                setBitmap(media.path);

//                Uri mediaUri = Uri.parse("file://" + media.path);
//
//                Glide.with(this)
//                        .load(mediaUri)
//                        .into(imageView);
            }
        }
//        if (resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//            if (fuckingCrop) {
//                if (outputX > 0 && outputY > 0 && aspectX > 0 && aspectY > 0) {
//                    cropFile = PhotoUtil.cropPhotoIntent(this, uri, REQUEST_CODE_CROP, aspectX, aspectY, outputX, outputY);
//                } else {
//                    cropFile = PhotoUtil.cropPhotoIntent(this, uri, REQUEST_CODE_CROP);
//                }
//            } else {
//                cropFile = new File(getFilePathFromContentUri(context, uri));
//                onCropResult(RESULT_OK, null);
//            }
//        } else {
//            Ln.e("相片选择失败");
//        }
    }

    /**
     * 剪切结果
     *
     * @param resultCode
     * @param data
     */
    protected void onCropResult(int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            onCropResultSuccess(data);
//        } else {
//            onCropResultFail(data);
//            Ln.e("图片剪切失败");
//        }
    }

    /**
     * 压缩图片
     *
     * @param filePath
     */
    public void compressImage(String filePath) {
        synchronized (BaseContractActivity.class){
//            Luban.get(this)
//                    .load(new File(filePath))                     //传人要压缩的图片
//                    .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
//                    .setCompressListener(new OnCompressListener() { //设置回调
//
//                        @Override
//                        public void onStart() {
////                        BriefLoadingDialog.showLoading(NewUploadImgActivity.this);
//                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
//                        }
//
//                        @Override
//                        public void onSuccess(File file) {
//                            // TODO 压缩成功后调用，返回压缩后的图片文件
////                        processFilePath(file.getAbsolutePath());
//                            onSuccessCompress(file);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            // TODO 当压缩过去出现问题时调用
//                            BriefLoadingDialog.dismissLoading();
//                        }
//                    }).launch();    //启动压缩
        }
    }

    /**
     * 设置图片回调表单
     * @param imagePath
     */
    protected void setBitmap(String imagePath){

    }

    /**
     * 选择完图片回调
     * @param photoList
     */
    protected void onSuccessSelectPhoto(List<String> photoList){

    }

    /**
     * 显示选择菜单
     */
    protected void showMenuDialog() {
        showMenuDialog(-1);
    }

    /**
     * 显示选择菜单
     *
     * @param action 当前动作标识(比如:设置头像、设置背景)  用于同一个activity中多个地方要上传图片的时候
     */
    protected void showMenuDialog(int action) {
        showMenuDialog(new ChoosePicDialog(), action);
    }

    /**
     * 显示选择菜单
     *
     * @param avatarDialog 要显示的dialog
     * @param action       当前动作标识(比如:设置头像、设置背景)  用于同一个activity中多个地方要上传图片的时候
     */
    protected void showMenuDialog(ChoosePicDialog avatarDialog, int action) {
        avatarDialog.setOnMenuClickListener(this).show(getFragmentManager(), this.getClass().getName());
    }

}
