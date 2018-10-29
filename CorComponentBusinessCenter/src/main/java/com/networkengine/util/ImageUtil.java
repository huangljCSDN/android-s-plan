package com.networkengine.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


@SuppressWarnings("ALL")
public class ImageUtil {
    public static int MAX_FILE_SIZE = 400 * 1024;
    public static int MAX_WIDTH = 320;
    public static int MAX_HEIGHT = 480;
    public static int OTHER_HEAD_SIZE = 80;    // 头像大小
    public static int HEAD_CORNER_PIX = 8;    // 头像圆角半径


    /**
     * CHAT_IMG:聊天的图片，400*300。 OTHER_HEAD:头像，除自己，80*80。 ME_HEAD:自己的头像，120*120。
     */
    public enum IMAGE_TYPE {
        OTHER_HEAD, ME_HEAD, CHAT_IMG, CHAT_IMG_DETAIL
    }

    ;

    public static void init(Context ct) {
        DisplayMetrics dm = ct.getResources().getDisplayMetrics();
        MAX_WIDTH = dm.widthPixels / 4;
        MAX_HEIGHT = dm.heightPixels / 4;
    }

    /**
     * 把drawable转换成bitmap
     *
     * @param drawable 传入的drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Context context, Drawable drawable) {

        if (drawable == null) {
            return null;
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);

        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 把bitmap转换成String
     *
     * @param bm
     * @param quality
     * @return
     */
    public static String bitmapToString(Bitmap bm, int quality) {
        if (bm == null) {
            return "";
        }
        if (quality <= 0 || quality > 100) {
            return "";
        }
        byte[] b = bitmapToByte(bm, quality);

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 字节转bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        byte[] bytes = null;
        try {
            bytes = string.getBytes("UTF-8");
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        } catch (UnsupportedEncodingException e) {
            Log.e("hh", "UnsupportedEncodingException " + e.getMessage());
        }
        return null;
    }


    /**
     * bitmap转字节
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bitmap, int quality) {
        if (bitmap == null) {
            return null;
        }
        if (quality <= 0 || quality > 100) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, quality, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /**
     * 字节转bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap byteToBitmap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }


    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param imagesrc
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        Bitmap bitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = caculateInSampleSize(options, 320, 480);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        try {

            FileInputStream fileInputStream = new FileInputStream(new File(
                    filePath));

            bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);
        } catch (Exception e) {

        }
        return bitmap;
    }


    /**
     * 将多个Bitmap合并成一个图片。
     *
     * @param int    将多个图合成多少列
     * @param Bitmap ... 要合成的图片
     * @return
     */
    public static Bitmap combineBitmaps(int columns, List<Bitmap> bitmaps) {
        if (columns <= 0 || bitmaps == null || bitmaps.size() == 0) {
            throw new IllegalArgumentException(
                    "Wrong parameters: columns must > 0 and bitmaps.length must > 0.");
        }
        int maxWidthPerImage = 0;
        int maxHeightPerImage = 0;
        for (Bitmap b : bitmaps) {
            //
            maxWidthPerImage = maxWidthPerImage > b.getWidth() ? maxWidthPerImage
                    : b.getWidth();
            //
            maxHeightPerImage = maxHeightPerImage > b.getHeight() ? maxHeightPerImage
                    : b.getHeight();
        }
        int rows = 0;
        if (columns >= bitmaps.size()) {
            rows = 1;
            columns = bitmaps.size();
        } else {
            rows = bitmaps.size() % columns == 0 ? bitmaps.size() / columns
                    : bitmaps.size() / columns + 1;
        }
        Bitmap newBitmap = Bitmap.createBitmap(columns * maxWidthPerImage, rows
                * maxHeightPerImage, Config.RGB_565);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.parseColor("#3c3c3c"));
        int count = 1;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                int index = x * columns + y;
                if (index >= bitmaps.size()) {
                    break;
                }
                if (count <= 9) {
                    newBitmap = mixtureBitmap(newBitmap, bitmaps.get(index),
                            new PointF(y * maxWidthPerImage, x
                                    * maxHeightPerImage));
                    count++;
                }

            }
        }
        return newBitmap;
    }

    /**
     * 将多个Bitmap合并成一个图片。
     *
     * @param int    将多个图合成多少列
     * @param Bitmap ... 要合成的图片
     * @return
     */
    public static Bitmap combineBitmaps(int columns, Bitmap... bitmaps) {
        if (columns <= 0 || bitmaps == null || bitmaps.length == 0) {
            throw new IllegalArgumentException(
                    "Wrong parameters: columns must > 0 and bitmaps.length must > 0.");
        }
        int maxWidthPerImage = 0;
        int maxHeightPerImage = 0;
        for (Bitmap b : bitmaps) {
            maxWidthPerImage = maxWidthPerImage > b.getWidth() ? maxWidthPerImage
                    : b.getWidth();
            maxHeightPerImage = maxHeightPerImage > b.getHeight() ? maxHeightPerImage
                    : b.getHeight();
        }
        int rows = 0;
        if (columns >= bitmaps.length) {
            rows = 1;
            columns = bitmaps.length;
        } else {
            rows = bitmaps.length % columns == 0 ? bitmaps.length / columns
                    : bitmaps.length / columns + 1;
        }
        Bitmap newBitmap = Bitmap.createBitmap(columns * maxWidthPerImage, rows
                * maxHeightPerImage, Config.RGB_565);

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                int index = x * columns + y;
                if (index >= bitmaps.length)
                    break;
                newBitmap = mixtureBitmap(newBitmap, bitmaps[index],
                        new PointF(y * maxWidthPerImage, x * maxHeightPerImage));
            }
        }
        return newBitmap;
    }

    /**
     * Mix two Bitmap as one.
     *
     * @param bitmapOne
     * @param bitmapTwo
     * @param point     where the second bitmap is painted.
     * @return
     */
    public static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
                                       PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(),
                first.getHeight(), Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newBitmap;
    }

    /**
     * 根据文件路径将文件转成base64
     *
     * @param path
     * @return
     */
    public static String encodeBase64File(String path) {
        if (TextUtils.isEmpty(path))
            return "";
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        byte[] buffer = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            fileInputStream.read(buffer);
            fileInputStream.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {

        }
        return "";

    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }
        File file = new File(path);
        if (!file.exists()) {
            return -1;
        }

        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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

        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImage(int angle, Bitmap bitmap) {

        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }


    /**
     * 获取图片
     *
     * @param srcPath
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap getImage(String srcPath, int maxWidth,
                                  int maxHeight) {
        if (null == srcPath) {
            return null;
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        maxWidth = maxWidth < 0 ? MAX_WIDTH : maxWidth;
        maxHeight = maxHeight < 0 ? MAX_HEIGHT : maxHeight;

        newOpts.inSampleSize = caculateInSampleSize(newOpts, maxHeight, maxWidth);

        newOpts.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        if (null == bitmap) {
            return null;
        }
        int degree = ImageUtil.readPictureDegree(srcPath);
        if (degree > 0) {
            bitmap = ImageUtil.rotaingImage(degree, bitmap);
        }
        return bitmap;
    }


    /**
     * 转为圆角图片
     *
     * @param bitmap 原图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    /**
     * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
     * 官网：获取压缩后的图片
     *
     * @param res
     * @param resId
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = caculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 获取压缩后的图片
     *
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filepath,
                                                     int maxWidth, int maxHeight) {
        if (TextUtils.isEmpty(filepath)) {
            return null;
        }
        File file = new File(filepath);
        if (!file.exists()) {
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        maxWidth = maxWidth < 0 ? MAX_WIDTH : maxWidth;
        maxHeight = maxHeight < 0 ? MAX_HEIGHT : maxHeight;
        options.inSampleSize = caculateInSampleSize(options, maxWidth,
                maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    /**
     * 压缩
     *
     * @param bitmap
     * @param quality
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                       int quality, int reqWidth, int reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, quality, baos);
        byte[] data = baos.toByteArray();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = caculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }


    /**
     * 压缩
     *
     * @param bitmap
     * @param quality
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFroPath(String path,
                                                    int quality, int maxWidth, int maxHeight) {

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, quality, baos);
        byte[] data = baos.toByteArray();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        maxWidth = maxWidth < 0 ? MAX_WIDTH : maxWidth;
        maxHeight = maxHeight < 0 ? MAX_HEIGHT : maxHeight;
        options.inSampleSize = caculateInSampleSize(options, maxWidth,
                maxHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }


    /**
     * 计算压缩比例值(改进版 by touch_ping)
     * <p>
     * 当前2>3>4...倍压缩
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度O
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static int caculateInSampleSize(BitmapFactory.Options options,
                                           int reqWidth, int reqHeight) {
        StringBuffer buffer = new StringBuffer();
        final int picheight = options.outHeight;
        final int picwidth = options.outWidth;

        buffer.append("picheight 原高：");
        buffer.append(picheight);
        buffer.append("  picwidth 原宽：");
        buffer.append(picwidth);
        int targetheight = picheight;
        int targetwidth = picwidth;
        int inSampleSize = 1;

        if (targetheight > reqHeight || targetwidth > reqWidth) {
            while (targetheight >= reqHeight
                    && targetwidth >= reqWidth) {
                inSampleSize += 1;
                targetheight = picheight / inSampleSize;
                targetwidth = picwidth / inSampleSize;
            }
        }
        buffer.append("targetheight 压缩后高：");
        buffer.append(targetheight);
        buffer.append("  targetwidth 压缩后宽：");
        buffer.append(targetwidth);

        buffer.append("  inSampleSize 压缩比：");
        buffer.append(inSampleSize);
        LogUtil.d("图片压缩》》》" + buffer.toString());

        return inSampleSize;
    }

    public static void saveMobileMatrixImage(String imagePath) {
//
        int degree = readPictureDegree(imagePath);
        // 角度为0 代表不需要旋转图片
        if (degree == 0 || degree == -1) {
            return;
        }
        Bitmap bMapRotate;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[32 * 1024];
        options.inPreferredConfig = Config.RGB_565;
        Bitmap bMap;
        bMap = BitmapFactory.decodeFile(imagePath, options);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        bMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), bMap.getHeight(), matrix, true);
        FileOutputStream out;
        try {
            File imgFile = new File(imagePath);
            out = new FileOutputStream(imgFile);
            bMapRotate.compress(CompressFormat.JPEG, 100, out);
            if (bMapRotate != null) {
                bMapRotate.recycle();
                bMapRotate = null;
            }
        } catch (FileNotFoundException e) {
            LogUtil.e("error", e);
        }
    }


    public static boolean insertImageToMedia(Context context, File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 保存方法
     */
    public static void saveBitmap(Bitmap bitmap, int quality, String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(CompressFormat.PNG, quality, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
