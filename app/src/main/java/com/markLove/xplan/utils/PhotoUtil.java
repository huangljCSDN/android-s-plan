package com.markLove.xplan.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.Toast;

import com.markLove.xplan.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoUtil {

    /**
     * 图片质量压缩
     */
    private static final int quality = 90;
    private static final int OUTPUT_X_DEFAULT = 150, OUTPUT_X_HD = 720, OUTPUT_Y_DEFAULT = 150, OUTPUT_Y_HD = 720;

    /**
     * 创建文件
     */
    public static File newPhotoFile() {
        File dir = FilePathUtils.getSDCardImagePath();
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "photo" + timeStamp + ".jpg";
        File image = new File(dir, fileName);
        return image;
    }

    /**
     * 拍照Intent
     */
    public static void takePhotoIntent(Activity ac, int resultCode, File file) {
        //File file = newPhotoFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        ac.startActivityForResult(intent, resultCode);
        //return file;
    }

    /**
     * 选择照片Intent
     */
    public static File choosePhotoIntent(Activity ac, int resultCode) {
        File file = newPhotoFile();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ac.startActivityForResult(intent, resultCode);
        return file;
    }


    /**
     * 剪切图标Intent
     *
     * @param activity    调用剪切功能的Activity
     * @param uri         待剪切的文件Uri
     * @param requestCode 请求码
     * @return iconFile 剪切后的图标文件
     * @author XieWenjun
     */
    public static File cropPhotoIntent(Activity activity, Uri uri, int requestCode) {
        return cropPhotoIntent(activity, uri, requestCode, OUTPUT_X_DEFAULT, OUTPUT_Y_DEFAULT);
    }

    /**
     * 剪切图标Intent(一切为了搞清头像)
     *
     * @param activity    调用剪切功能的Activity
     * @param uri         待剪切的文件Uri
     * @param requestCode 请求码
     * @return iconFile 剪切后的图标文件
     * @author XieWenjun
     */
    public static File cropPhotoIntent4HD(Activity activity, Uri uri, int requestCode) {
        return cropPhotoIntent(activity, uri, requestCode, OUTPUT_X_HD, OUTPUT_Y_HD);
    }

    public static File cropPhotoIntent(Activity activity, Uri uri, int requestCode, int outputX, int outputY) {
        //华为手机上如果设置为1：1，裁剪框会默认为圆形
        return cropPhotoIntent(activity, uri, requestCode, 9999, 9998, outputX, outputY);
    }

    public static File cropPhotoIntent(Activity activity, Uri uri, int requestCode, int aspectX, int aspectY, int outputX, int outputY) {
        File file = newPhotoFile();
        Uri newUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);             // 保留比例
        intent.putExtra("aspectX", aspectX); // x方向上的比例
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("output", newUri);
        intent.putExtra("outputX", outputX);        // 裁剪区 宽
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scaleUpIfNeeded", true);//解决图片放大到一定比例裁剪出现黑边
        activity.startActivityForResult(intent, requestCode);
        return file;
    }

    /**
     * 修改图片(压缩比例+压缩质量)
     */
    // TODO 超长图，普通比例，超小图
    public static synchronized void fixPhoto(Context context, String filePath, String filePath2, int quality,
                                             PhotoFixCallbackListener fixCallbackListener) {
        int reqHeight = 800;
        int reqWidth = 480;

        int degree;
        BitmapFactory.Options options;
        int height;
        int width;

        Bitmap bitmap;

        // 旋转角度
        degree = readPhotoDegree(filePath);

        // 图片宽高
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        height = options.outHeight;
        width = options.outWidth;
        if (degree == 90 || degree == 270) {
            height = options.outWidth;
            width = options.outHeight;
        } else {
            height = options.outHeight;
            width = options.outWidth;
        }

        // 计算压缩比例
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateZoomSize(width, height, reqWidth, reqHeight);
        // 读取压缩比例之后的Bitmap到内存中
        bitmap = BitmapFactory.decodeFile(filePath, options);

        // 旋转图片
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // 保存到File
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(filePath2));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);

        if (fixCallbackListener != null) {
            fixCallbackListener.onFixed();
        }

        // 回收
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
            bitmap = null;
        }
    }

    /**
     * 拍照后的处理
     *
     * @param filePath 需要处理的文件路径,同时为处理后的文件路径(拍照出入的文件，所以处理之后也保存在拍照文件中)
     */
    public static void fixPhoto(Context context, String filePath, PhotoFixCallbackListener fixCallbackListener) {
        fixPhoto(context, filePath, filePath, quality, fixCallbackListener);
    }

    /**
     * 选择图片后的处理
     *
     * @param context
     * @param uri      需要处理的文件Uri()
     * @param filePath 保存处理后的文件路径 (选择图片不需要输入，得到的是Uri,所以需要一个新文件，保存处理后的图片)
     */
    public static void fixPhoto(Context context, Uri uri, String filePath, PhotoFixCallbackListener fixCallbackListener) {
        fixPhoto(context, getFilePath4Uri(context, uri), filePath, quality, fixCallbackListener);
    }

//	public static void fixPhotoList(final Context context, final ArrayList<String> files, final ArrayList<String> results, final int quality,
//			final PhotoListFixCallbackListener fixCallbackListener) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				for (int i = 0; i < files.size(); i++) {
//					fixPhoto(context, files.get(i), results.get(i), quality, null);
//				}
//				fixCallbackListener.onFixed();
//			}
//		}).start();
//	}

    // 计算图片的缩放值
    public static int calculateZoomSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        return calculateZoomSize(width, height, reqWidth, reqHeight);
    }

    public static int calculateZoomSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {  // 如果图片的宽高 都小于 请求宽高,不压缩比例
            if (width > height) {// 横图，压缩到高度和屏幕一样
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else { // 长图或方图，压缩到宽度和屏幕一样
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            final float totalPixels = width * height;   // 压缩比例前的像素点
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;  // 计划压缩比例后的像素点*2 (720 * 1280 * 2)
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }

        // 原来的压缩方法
        /*if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}*/

        return inSampleSize;
    }

    // 读取图片旋转的角度
    public static int readPhotoDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    // 由uri得到文件路径
    public static String getFilePath4Uri(Context context, Uri uri) {
        String filePath = null;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex("_data"));
        }
        return filePath;
    }

    public interface PhotoFixCallbackListener {
        void onFixed();
    }

    public interface PhotoListFixCallbackListener {
        void onFixed();
    }

    /** 保存 图片 */
//	public static void saveFile(Context context, String imageUrl) {
//		try {
//			String imagePath = ImageLoadUtil.getPath4Url(context, imageUrl);
//			File hdFile = new File(imagePath);
//			String savedPath = Environment.getExternalStorageDirectory() + "/com.temaijie/Photo/"
//					+ ImageLoadUtil.getName4Url(imageUrl) + ".jpg";
//			File savedFile = new File(savedPath);
//			if (!savedFile.getParentFile().exists()) {
//				savedFile.getParentFile().mkdirs();
//			}
//			if (savedFile.exists()) {
//				Toast.makeText(context, "保存路径:" + savedPath, Toast.LENGTH_SHORT).show();
//				return;
//			}
//			hdFile.renameTo(savedFile);
//			if (savedFile.exists()) {
//				Toast.makeText(context, "保存路径:" + savedPath, Toast.LENGTH_SHORT).show();
//			} else {
//				Toast.makeText(context, "抱歉！保存失败，请重新保存!", Toast.LENGTH_SHORT).show();
//			}
//		} catch (Throwable e) {
//			Toast.makeText(context, "抱歉！保存失败，请重新保存!", Toast.LENGTH_SHORT).show();
//		}
//	}

    /**
     * 读取本地图片文件，默认最小宽度和最小高度
     *
     * @param context
     * @param pathName 文件路径
     * @return bitmap
     * @author XieWenjun
     */
    public static Bitmap decodeFile(Context context, String pathName) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int reqWidth = dm.widthPixels;
        int reqHight = dm.heightPixels;
        return decodeFile(context, pathName, reqWidth, reqHight);
    }

    /**
     * 读取本地图片文件，自定义最小宽度和最小高度
     *
     * @param context
     * @param pathName  文件路径
     * @param reqWidth  自定义最小宽度
     * @param reqHeight 自定义最小高度
     * @return bitmap
     * @author XieWenjun
     */
    public static Bitmap decodeFile(Context context, String pathName, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateZoomSize(options, reqWidth, reqHeight);// 计算图片的缩放值
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    /**
     * 删除图片
     *
     * @param path 文件路径
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        deleteTempFile(file);
    }

    /**
     * 删除图片
     *
     * @param file 文件
     */
    public static void deleteTempFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        bmp.recycle();

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 图片质量压缩
     *
     * @param image
     * @param quality 压缩质量比率 100 为不压缩, 90为压缩到90%
     * @return
     */
    public static Bitmap compress(Bitmap image, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
    }

    // TODO 应该整理成BitmapUtil
    // 图片质量压缩
    public static Bitmap compressImage(Bitmap image, int limitKB) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > limitKB) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();// 重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;// 每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
//        return bitmap;

        ByteArrayInputStream isBm = new ByteArrayInputStream(compressImageReturnByte(image, limitKB));// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
    }

    // 图片质量压缩
    public static byte[] compressImageReturnByte(Bitmap image, int limitKB) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > limitKB) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        byte[] result = baos.toByteArray();
        try {
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Ln.e("#compressImageReturnByte" + (result.length / 1024));    // 压缩后的大小
        return result;
    }

    /**
     * 保存view为图片<br/>
     * 关键逻辑参考: MPAndroidChart [Chart.java # saveToPath()]
     * ChenRen / 2016年 8月 1日 星期一 10时58分00秒 CST
     * @param viewGroup view的外层ViewGroup
     */
    public static boolean saveViewToImg(Context context, ViewGroup viewGroup, String fileName) {
        boolean isSaveSuccess = true;
        try {
            int width = viewGroup.getWidth();
            int height = viewGroup.getHeight();
            Drawable bgDrawable = viewGroup.getBackground();

            // Define a bitmap with the same size as the view
            Bitmap returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            // Bind a canvas to it
            Canvas canvas = new Canvas(returnedBitmap);
            // Get the view's background
            if (bgDrawable != null)
                // has background drawable, then draw it on the canvas
                bgDrawable.draw(canvas);
            else
                // does not have background drawable, then draw white background on
                // the canvas
                canvas.drawColor(Color.WHITE);
            // draw the view on the canvas
            viewGroup.draw(canvas);


            File targetDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), context.getString(R.string.app_name));
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            File file = new File(targetDir, fileName);
            FilePathUtils.bitmapToFile(returnedBitmap, file);

            // 通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            Toast.makeText(context, "已保存到相册", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Ln.e(e);
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
            isSaveSuccess = false;
        }
        return isSaveSuccess;
    }


    public static int dip2px(Context context, int value){
        float scaleing=context.getResources().getDisplayMetrics().density;
        return (int) (value*scaleing+0.5f);
    }
    public static int px2dip(Context context, int value){
        float scaling=context.getResources().getDisplayMetrics().density;
        return (int) (value/scaling+0.5f);
    }
}
