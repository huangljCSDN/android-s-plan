package com.xsimple.im.utils;

import android.content.Context;
import android.content.Intent;
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
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.xsimple.im.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


@SuppressWarnings("ALL")
public class ImageUtil {
    public static int MAX_FILE_SIZE = 400 * 1024;
    public static int MAX_WIDTH = 400;
    public static int MAX_HEIGHT = 300;
    public static int OTHER_HEAD_SIZE = 80;    // 头像大小
    public static int HEAD_CORNER_PIX = 8;    // 头像圆角半径

    public static Bitmap defaultHead;
    public static Bitmap discussionHead;

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

        defaultHead = BitmapFactory.decodeResource(ct.getResources(), R.drawable.ic_list_man_circle);
        defaultHead = Bitmap.createScaledBitmap(defaultHead, OTHER_HEAD_SIZE, OTHER_HEAD_SIZE, true); // 压缩
        defaultHead = toRoundCorner(defaultHead, HEAD_CORNER_PIX); // 圆角
        discussionHead = BitmapFactory.decodeResource(ct.getResources(), R.drawable.ic_discuss_group);

//		LogUtil.e("PWY_TEST", "widthPixels:" + dm.widthPixels + ", heightPixels:" + dm.heightPixels + ", density:" + dm.density + ", xdpi:" + dm.xdpi + ", ydpi:" + dm.ydpi);
    }

    /**
     * 把drawable转换成bitmap
     *
     * @param drawable 传入的drawable
     * @return
     */
    public static Bitmap drawabletoBitmap(Drawable drawable) {

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

//	/**
//	 * 把bitmap转换成String
//	 * 
//	 * @param filePath
//	 * @return
//	 */
//	public static String bitmapToString(String filePath) {
//
//		Bitmap bm = getImage(filePath, -1f, -1f);
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
//		byte[] b = baos.toByteArray();
//
//		return Base64.encodeToString(b, Base64.DEFAULT);
//
//	}

//    /**
//     * 计算图片的缩放值
//     *
//     * @param options
//     * @param reqWidth
//     * @param reqHeight
//     * @return
//     */
//    public static int calculateInSampleSize(BitmapFactory.Options options,
//                                            int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            // Calculate ratios of height and width to requested height and
//            // width
//            final int heightRatio = Math.round((float) height
//                    / (float) reqHeight);
//            final int widthRatio = Math.round((float) width / (float) reqWidth);
//
//            // Choose the smallest ratio as inSampleSize value, this will
//            // guarantee
//            // a final image with both dimensions larger than or equal to the
//            // requested height and width.
//            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//        }
//
//        return inSampleSize;
//    }

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

        options.inSampleSize = calculateInSampleSize(options, 320, 480);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        try {

            FileInputStream fileInputStream = new FileInputStream(new File(
                    filePath));
            // bitmap = BitmapFactory.decodeFile(filePath, options);
            // 节省Java层内存
            bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);
        } catch (Exception e) {

        }
        return bitmap;
    }

    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录
     *
     * @return
     */
    public static File getAlbumDir() {
        File dir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取保存 隐患检查的图片文件夹名称
     *
     * @return
     */
    public static String getAlbumName() {
        return "social_image";
    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        if (null == bitmap) {
            return "";
        }
        Bitmap bmpSend = compressImage(bitmap);
        int quality = 100;
        while (quality > 0) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
                bmpSend.compress(CompressFormat.PNG, quality, baos);
                byte[] appicon = baos.toByteArray();// 转为byte数组
                String result = Base64.encodeToString(appicon, Base64.DEFAULT);
                bmpSend.recycle();
                return result;
            } catch (Exception e) {
                System.gc();
                quality -= 10;
            }
        }
        return "";
    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        if (o instanceof String)
            return (String.valueOf(o).trim().length() == 0);
        else if (o instanceof List)
            return ((List) o).isEmpty();
        else if (o instanceof Map)
            return ((Map) o).isEmpty();
        else
            return false;
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
     * 根据文件路径将文件转成base64 Alanqiu-2014/4/28
     *
     * @param path
     * @return
     */
    public static String encodeBase64File(String path) {
        File file = new File(path);
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
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		bitmap.recycle();
        return resizedBitmap;
    }

    private static Bitmap compressImage(Bitmap image) {
//		 return image;
        if (null == image) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.PNG, 100, baos);//
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length > MAX_FILE_SIZE && options > 0) { //
            // 循环判断如果压缩后图片是否大于上限,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.PNG, options, baos);
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//
        // 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap getImage(String srcPath) {
        return getImage(srcPath, -1, -1);
    }

    public static Bitmap getImage(String srcPath, float maxWidth,
                                  float maxHeight) {
        if (null == srcPath) {
            return null;
        }

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        Bitmap bmpRecycle;    // 用于手动回收内存

        newOpts.inJustDecodeBounds = false;
        int w = Math.max(newOpts.outWidth, newOpts.outHeight);
        int h = Math.min(newOpts.outWidth, newOpts.outHeight);

        maxWidth = maxWidth < 0 ? MAX_WIDTH : maxWidth;
        maxHeight = maxHeight < 0 ? MAX_HEIGHT : maxHeight;

        float ww = Math.max(maxWidth, maxHeight);
        float hh = Math.min(maxWidth, maxHeight);
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//		int be = (int) (Math.max(w / ww, h / hh) + 0.5);
        //设置最佳缩放比
        int be = caculateInSampleSize(newOpts, (int) maxHeight, (int) maxWidth);

//		newOpts.inSampleSize = be > 1 ? be : 1;// 设置缩放比例
        newOpts.inSampleSize = be;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        if (null == bitmap) {
            return null;
        }
//		LogUtil.e("PWY_TEST", bitmap.getWidth() + "," + bitmap.getHeight() + "**" + be + "**" + w + "," + h);
//		LogUtil.e("PWY_TEST", ww + "," + hh);
//		if (Math.max(bitmap.getWidth(), bitmap.getHeight()) > ww || Math.min(bitmap.getWidth(), bitmap.getHeight()) > hh) {
//			w = Math.max(bitmap.getWidth(), bitmap.getHeight());
//			h = Math.min(bitmap.getWidth(), bitmap.getHeight());
//			double be2 = Math.max(w * 1.0 / ww, h * 1.0 /hh);
//			bmpRecycle = bitmap;
//			bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() / be2), (int)(bitmap.getHeight() / be2), true);
//			bmpRecycle.recycle();
////			LogUtil.e("PWY_TEST", bitmap.getWidth() + "," + bitmap.getHeight() + "--" + be2 + "--" + w + "," + h);
//		}
        int degree = ImageUtil.readPictureDegree(srcPath);
        if (degree > 0) {
            bitmap = ImageUtil.rotaingImageView(degree, bitmap);
        }
        return bitmap;
        //	return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 从.mp4的url视频中获取第一帧
     *
     * @param url
     * @return
     */
//    public static Bitmap getBitmapFormUrl(String sha) {
//    	String url = IMFileManager.FILE_SERVER
//				+ "file?method=download&path=" + IMFileManager.FILE_FOLDER
//				+ "&access_token=&stat=&version=&sha=" + sha;
//        Bitmap bitmap = null;
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        try {
//            if (Build.VERSION.SDK_INT >= 14) {
//                retriever.setDataSource(url, new HashMap<String, String>());
//            } else {
//                retriever.setDataSource(url);
//            }
//            bitmap = retriever.getFrameAtTime();
//        } catch (RuntimeException ex) {
//            // Assume this is a corrupt video file.
//        } finally {
//            try {
//                retriever.release();
//            } catch (RuntimeException ex) {
//                // Ignore failures while cleaning up.
//            }
//        }
//        return bitmap;
//    }
//	public static Bitmap getImage(Bitmap bmp, float maxWidth,
//			float maxHeight) {
//		if (null == bmp) {
//			return null;
//		}
//
//		int w = bmp.getWidth();
//		int h = bmp.getHeight();
//		
//		maxWidth = maxWidth == -1 ? MAX_WIDTH : maxWidth;// 这里设置高度为800f
//		maxHeight = maxHeight == -1 ? MAX_HEIGHT : maxHeight;// 这里设置宽度为480f
//		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//		int be = 1;// be=1表示不缩放
//		if (w > h && w > maxHeight) {// 如果宽度大的话根据宽度固定大小缩放
//			be = (int) (newOpts.outWidth / maxHeight);
//		} else if (w < h && h > maxWidth) {// 如果高度高的话根据宽度固定大小缩放
//			be = (int) (newOpts.outHeight / maxWidth);
//		}
//		if (be <= 0)
//			be = 1;
//		newOpts.inSampleSize = be;// 设置缩放比例
//		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
//	}

    /**
     * 计算最佳缩放比
     *
     * @param options
     * @param reqheight
     * @param reqwidth
     * @return 返回最佳缩放比例, 用于设置Options的inSampleSize
     * @author panxiaoan
     */
    private static int caculateInSampleSize(BitmapFactory.Options options, int reqheight, int reqwidth) {
        final int height = options.outHeight;

        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqheight || width > reqwidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqheight && (halfWidth / inSampleSize) >= reqwidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

//	private static void setDefaultImage(ImageView iv, Bitmap bmp) {
//		// 设置默认图像
//		if (null != bmp) {
//			iv.setImageBitmap(bmp);
//		} else {
//			iv.setImageResource(R.drawable.loading);
//		}
//	}

//	private static String subFileName(String path, IMAGE_TYPE type) {
//		return MD5Util.encode(path);
//	}

//	public static Bitmap getUserHead(AgoraUser user) {
//		if (null == user) {
//			return defaultHead;
//		}
//		if (user.type != 0) {
//			return discussionHead;
//		}
//
//		Bitmap bmp = BitmapCache.getCache().get(subFileName(user.getImageAddress(), IMAGE_TYPE.ME_HEAD));
//		if (null != bmp && !bmp.isRecycled()) {
//			return bmp;
//		}
//		return defaultHead;
//	}

//	public static void remove(Context ct, String path, IMAGE_TYPE type) {
//
//		if (null == path || "".equals(path)) {
//			return;
//		}
//		String fileName = subFileName(path, type);
//
//		BitmapCache.getCache().remove(fileName);
//
//		String locPath = FilePathUtils.getIntance(ct).getDefaultImageFilePath() + "/"
//				+ fileName;
//		new File(locPath).deleteOnExit();
//	}

//	public static void setImage(ImageView iv, String path, int defaultBmp,
//			IMAGE_TYPE type) {
//		if (null == iv) {
//			return;
//		}
//
//		Bitmap bmp = BitmapFactory.decodeResource(iv.getContext()
//				.getResources(), defaultBmp);
//		setImage(iv, path, bmp, type);
//	}

//	public static void setImage(ImageView iv, String path, Bitmap defaultBmp,
//			IMAGE_TYPE type) {
//		if (null == iv) {
//			return;
//		}
//
//		if (type == IMAGE_TYPE.OTHER_HEAD) {
//			defaultBmp = defaultHead;
//		}
//
//		if (null == path || "".equals(path)) {
//			setDefaultImage(iv, defaultBmp);
//			return;
//		}
//		String fileName = subFileName(path, type);
//		iv.setTag(fileName); // 防止图片错位，异步拉取的头像设到别人的位置
//
//		// 先看缓存里有没有
//		Bitmap bmp = BitmapCache.getCache().get(fileName);
//		if (null != bmp && !bmp.isRecycled()) {
//			iv.setImageBitmap(bmp);
//			return;
//		}
//
//		setDefaultImage(iv, defaultBmp);
//
//		// 网络图片
//		if ((path.startsWith("http") || type == IMAGE_TYPE.OTHER_HEAD)
//				&& fileName.length() > 0) {
//			String locPath = FilePathUtils.getIntance(iv.getContext()).getDefaultImageFilePath() + "/"
//					+ fileName;
//			if (type == IMAGE_TYPE.OTHER_HEAD) {
//				locPath = FilePathUtils.getIntance(iv.getContext()).getContactImageFilePath() + "/" + fileName;
//			}
//			if (!new File(locPath).exists()) { // 本地也没有
////				loadFromServer(path, iv, defaultBmp, type);
//				iv.setTag(subFileName(path, type));
//				new loadFromServerTask(path, iv, defaultBmp, type).execute();
//				return;
//			} else {
//				iv.setTag(subFileName(locPath, type));
//				path = locPath;
//			}
//		}
//
//		// 从SD卡加载
////		loadFromSD(path, iv, defaultBmp, type);
//		new loadFromSDTask(path, iv, defaultBmp, type).execute();
//
//	}

//	public static class loadFromSDTask extends AsyncTask<Void, Void, Bitmap> {
//		private String path;
//		private ImageView iv;
//		private Bitmap defaultBmp;
//		private IMAGE_TYPE type;
//		private String fileName;
//
//		public loadFromSDTask(String path, ImageView iv,
//				Bitmap defaultBmp, IMAGE_TYPE type) {
//			this.path = path;
//			this.iv = iv;
//			this.defaultBmp = defaultBmp;
//			this.type = type;
//			this.fileName = subFileName(path, IMAGE_TYPE.CHAT_IMG_DETAIL); // 从SD卡加载的都都按这格式来截
//		}
//
//		@Override
//		protected Bitmap doInBackground(Void... paramArrayOfParams) {
//			Bitmap bmp = null;
//			if (type == IMAGE_TYPE.ME_HEAD || type == IMAGE_TYPE.OTHER_HEAD) {
//				bmp = getImage(path, 80, 80);
//			} else {
//				bmp = getImage(path);
//			}
//			if (null == bmp) { // 加载不到，只能选择默认头像
//				bmp = defaultBmp;
//			} else if (type == IMAGE_TYPE.OTHER_HEAD) {
//				bmp = toRoundCorner(bmp, HEAD_CORNER_PIX); // 圆角
//			}
//
//			return bmp;
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap result) {
//			if (fileName.equals(iv.getTag())) {
//				iv.setImageBitmap(result);
//			}
//			BitmapCache.getCache().put(fileName, result);
//		};
//
//
//
//	}
//
//	private static void loadFromSD(final String path, final ImageView iv,
//			final Bitmap defaultBmp, final IMAGE_TYPE type) {
//		final String fileName = subFileName(path, IMAGE_TYPE.CHAT_IMG_DETAIL); // 从SD卡加载的都都按这格式来截
//
//		new AsyncTask<Void, Void, Bitmap>() {
//
//			@Override
//			protected Bitmap doInBackground(Void... paramArrayOfParams) {
//				Bitmap bmp = null;
//				if (type == IMAGE_TYPE.ME_HEAD || type == IMAGE_TYPE.OTHER_HEAD) {
//					bmp = getImage(path, 80, 80);
//				} else {
//					bmp = getImage(path);
//				}
//				if (null == bmp) { // 加载不到，只能选择默认头像
//					bmp = defaultBmp;
//				} else if (type == IMAGE_TYPE.OTHER_HEAD) {
//					bmp = toRoundCorner(bmp, HEAD_CORNER_PIX); // 圆角
//				}
//				
//				return bmp;
//			}
//
//			protected void onPostExecute(Bitmap result) {
//				if (fileName.equals(iv.getTag())) {
//					iv.setImageBitmap(result);
//				}
//				BitmapCache.getCache().put(fileName, result);
//			};
//
//		}.execute();
//	}

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

    public static void saveImage(Context context, String path, Bitmap bitmap) {
        if (bitmap == null || path == null || context == null)
            return;

        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 100, fos);
        } catch (Exception e) {

        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block

                }
            }
        }
    }

    //保存图片,压缩成原来的60
    public static void saveImageCompress(Context context, String path, Bitmap bitmap) {
        if (bitmap == null || path == null || context == null)
            return;

        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 60, fos);
        } catch (Exception e) {

        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block

                }
            }
        }
    }

//	public static class loadFromServerTask extends AsyncTask<Void, Void, Bitmap> {
//		private String path;
//		private ImageView iv;
//		private Bitmap defaultBmp;
//		private IMAGE_TYPE type;
//		private String fileName;
//
//		public loadFromServerTask(String path, ImageView iv,
//				Bitmap defaultBmp, IMAGE_TYPE type) {
//			this.path = path;
//			this.iv = iv;
//			this.defaultBmp = defaultBmp;
//			this.type = type;
//			this.fileName = subFileName(path, type); // 从SD卡加载的都都按这格式来截
//		}
//
//		@Override
//		protected Bitmap doInBackground(Void... paramArrayOfParams) {
//			Bitmap bmp = null;
////			if (type == IMAGE_TYPE.OTHER_HEAD) {
////				bmp = UserManager.getInstance(iv.getContext()).getUserIcon(
////						path);
////			} else {
//			try {
//
//				HttpGet get = new HttpGet(path);
//				HttpResponse response = Http.getHttpClient().execute(get, Http.getHttpContext());
//				InputStream inStream = response.getEntity().getContent();
//
//				String path = FilePathUtils.getIntance(iv.getContext()).getDefaultImageFilePath() + "/"
//						+ fileName;
//				saveImage(iv.getContext(), path, inStream);
//				bmp = getImage(path);
//
//
//			} catch (Exception e) {
//			}
////			}
//			if (null == bmp) { // 网络加不到，只能选择默认头像
//				bmp = defaultBmp;
//			} else {
//				if (type == IMAGE_TYPE.OTHER_HEAD) {
//					bmp = Bitmap.createScaledBitmap(bmp, OTHER_HEAD_SIZE, OTHER_HEAD_SIZE, true); // 压缩
//					String path = FilePathUtils.getIntance(iv.getContext()).getContactImageFilePath() + "/"
//							+ fileName;
//					saveImage(iv.getContext(), path, bmp); // 切圆角前先保存
//					bmp = toRoundCorner(bmp, HEAD_CORNER_PIX); // 圆角
//				}
//			}
//			return bmp;
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap result) {
//			if (fileName.equals(iv.getTag())) {
//				iv.setImageBitmap(result);
//			}
//			BitmapCache.getCache().put(fileName, result);
//		};
//
//
//	}

    private static void saveImage(Context ct, String locPath, InputStream is) {

        FileOutputStream fos = null;
        try {
            File file = new File(locPath);
            if (file.exists()) {
                file.delete();
            }
            int l;
            fos = new FileOutputStream(file);
            byte[] tmp = new byte[2048];
            while ((l = is.read(tmp)) != -1) {
                fos.write(tmp, 0, l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 质量压缩
     *
     * @param image
     * @param maxkb
     * @return
     */
    public static Bitmap compressBitmap(Bitmap image, int maxkb) {
        //L.showlog(压缩图片);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
//      Log.i(test,原始大小 + baos.toByteArray().length);
        while (baos.toByteArray().length / 1024 > maxkb) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
//
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
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

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
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
                                                     int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                       int reqWidth, int reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] data = baos.toByteArray();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 计算压缩比例值(改进版 by touch_ping)
     * <p>
     * 原版2>4>8...倍压缩
     * 当前2>3>4...倍压缩
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int picheight = options.outHeight;
        final int picwidth = options.outWidth;

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

        return inSampleSize;
    }

    /**
     * bitmap转字节
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }


//	private static void loadFromServer(final String path, final ImageView iv,
//			final Bitmap defaultBmp, final IMAGE_TYPE type) {
//		final String fileName = subFileName(path, type);
//
//		new AsyncTask<Void, Void, Bitmap>() {
//
//			@Override
//			protected Bitmap doInBackground(Void... paramArrayOfParams) {
//				Bitmap bmp = null;
//				if (type == IMAGE_TYPE.OTHER_HEAD) {
//					bmp = UserManager.getInstance(iv.getContext()).getUserIcon(
//							path);
//				} else {
//					try {
//						URL m = new URL(path);
//						InputStream inStream = (InputStream) m.getContent();
//						bmp = BitmapFactory.decodeStream(inStream);
//					} catch (Exception e) {
//						LogUtil.exception(e);
//					}
//				}
//				if (null == bmp) { // 网络加不到，只能选择默认头像
//					bmp = defaultBmp;
//				} else {
//					if (type == IMAGE_TYPE.OTHER_HEAD) {
//						bmp = Bitmap.createScaledBitmap(bmp, OTHER_HEAD_SIZE, OTHER_HEAD_SIZE, true); // 压缩
//						String path = FilePathUtils.getContactImageFilePath() + "/"
//								+ fileName;
//						saveImage(iv.getContext(), path, bmp); // 切圆角前先保存
//						bmp = toRoundCorner(bmp, HEAD_CORNER_PIX); // 圆角
//					} else {
//						String path = FilePathUtils.getDefaultImageFilePath() + "/"
//								+ fileName;
//						saveImage(iv.getContext(), path, bmp);
//					}
//				}
//				return bmp;
//			}
//
//			protected void onPostExecute(Bitmap result) {
//				if (fileName.equals(iv.getTag())) {
//					iv.setImageBitmap(result);
//				}
//				BitmapCache.getCache().put(fileName, result);
//			};
//
//		}.execute();
//	}
}
