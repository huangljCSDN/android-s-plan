package com.markLove.xplan.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.markLove.xplan.base.App;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 文件路径工具类
 *
 * @author ouyangbo
 */
public class FilePathUtils {
    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static File getSDCardImagePath() {
        String path = getSDCardPath() + "/" + App.getInstance().getPackageName() + "/image/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getSDCardQRCodePath() {
        String path = getSDCardPath() + "/" + App.getInstance().getPackageName() + "/qrcode/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址。
     */
    private static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private static File getCacheDir(Context context, String uniqueName) {
        String cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获得当前应用默认的解压路径
     *
     * @return
     */
    public static String getDefaultUnzipFile() {
        File cacheDir = getCacheDir(App.getInstance(), "unZip");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }


    /**
     * 获取SD卡目录下相对应包名程序下的文件保存的图片的路径
     */
    public static String getDefaultFilePath() {
        File cacheDir = getDiskCacheDir(App.getInstance(), "file");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 获取SD卡目录下相对应包名程序下的拍照保存的图片的路径
     */
    public static String getDefaultImageFilePath() {
        File cacheDir = getDiskCacheDir(App.getInstance(), "image");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    public static String getSDCardImageCachePath() {
        File cacheDir = getDiskCacheDir(App.getInstance(), "image");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 日志保存目录
     *
     * @return
     */
    public static String getDefaultLogPath() {
        File cacheDir = getDiskCacheDir(App.getInstance(), "log");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    public static void deleteAllCacel() {
        try {
            String cachePath;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    && !Environment.isExternalStorageRemovable()) {
                cachePath = App.getInstance().getExternalCacheDir().getPath();
            } else {
                cachePath = App.getInstance().getCacheDir().getPath();
            }
            deletefile(cachePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param delpath String
     * @return boolean
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean deletefile(String delpath) throws Exception {
        try {
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "/" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                        System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    } else if (delfile.isDirectory()) {
                        deletefile(delpath + "/" + filelist[i]);
                    }
                }
                System.out.println(file.getAbsolutePath() + "删除成功");
                file.delete();
            }

        } catch (FileNotFoundException e) {
        }
        return true;
    }

    /**
     * 删除文件[夹]<br/>
     * 如果是文件夹, 递归删除文件夹及目录下所有文件
     *
     * @param file 文件或文件夹
     */
    public static void delete(File file) {
        if (file == null) return;

        if (file.isDirectory()) {
            File[] fileArr = file.listFiles();
            for (File f : fileArr) {
                delete(f);  // 递归删除
            }

        }
        file.delete();
    }

    /**
     * 删除文件[夹]<br/>
     * 如果是文件夹, 递归删除文件夹及目录下所有文件
     *
     * @param filePath 文件或文件夹路径
     */
    public static void delete(String filePath) {
        delete(new File(filePath));
    }

    // 0430----图片压缩后保存到原路径的方法

    /**
     * compressBySize(压缩图片的方法)
     *
     * @param pathName     图片路径
     * @param targetWidth  压缩后图片的宽
     * @param targetHeight 压缩后图片的高
     * @return Bitmap对象
     */
    public static Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    /**
     * 存储进SD卡
     * saveFile(保存压缩后图片到存储中的方法)
     *
     * @param bm bitmap对象
     * @throws Exception
     */
    public static void saveFileImg(Bitmap bm, String filePath) throws Exception {
        File dirFile = new File(filePath);
        // 检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete(); // 删除原图片
        }
        File myCaptureFile = new File(filePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        // 100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    public static void bitmapToFile(Bitmap bitmap, File file) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (Exception e) {
            Ln.e(e);
        }
    }

}

