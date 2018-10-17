package com.markLove.Xplan.utils;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

public class FileUtils {
    protected static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 判断SD卡是否可用
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return
     */
    public static long getSDFreeSize() {
        if (isSDCardAvailable()) {
            StatFs statFs = new StatFs(getSDCardPath());

            long blockSize = statFs.getBlockSize();

            long freeBlocks = statFs.getAvailableBlocks();
            return freeBlocks * blockSize;
        }

        return 0;
    }

    public static void delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取SD卡的总容量
     *
     * @return
     */
    public static long getSDAllSize() {
        if (isSDCardAvailable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 统计文件夹文件的大小
     */
    public static long getSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile()) {
                // 获取文件大小
                File[] fl = file.listFiles();
                long ss = 0;
                for (File f : fl)
                    ss += getSize(f);
                return ss;
            } else {
                long ss = (long) file.length();
                return ss; // 单位制bytes
            }
        } else {
            // System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0;
        }
    }

    /**
     * 把bytes转换成MB
     */
    public static String getTrafficStr(long total) {
        DecimalFormat format = new DecimalFormat("##0.0");
        if (total < 1024 * 1024) {
            return format.format(total / 1024f) + "KB";
        } else if (total < 1024 * 1024 * 1024) {
            return format.format(total / 1024f / 1024f) + "MB";
        } else if (total < 1024 * 1024 * 1024 * 1024) {
            return format.format(total / 1024f / 1024f / 1024f) + "GB";
        } else {
            return "统计错误";
        }
    }

    /**
     * 删除文件夹里面的所以文件
     */
    public static void deleteDir(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                } else {
                    deleteDir(files[i]);
                }
            }
        }
    }


    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取外部存储路径
     *
     * @return
     */
    public static String getExternalStorageDirectoryPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 复制assets中的文件到sd卡中
     */
//    private static FileUtils instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private volatile boolean isSuccess;
    private String errorStr;

//    public static FileUtils getInstance(Context context) {
//        if (instance == null)
//            instance = new FileUtils(context);
//        return instance;
//    }

    private FileUtils(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess();
                }
                if (msg.what == FAILED) {
                    callback.onFailed(msg.obj.toString());
                }
            }
        }
    };

    public FileUtils copyAssetsToSD(final String srcPath, final String sdPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                copyAssetsToDst(context, srcPath, sdPath);
                if (isSuccess)
                    handler.obtainMessage(SUCCESS).sendToTarget();
                else
                    handler.obtainMessage(FAILED, errorStr).sendToTarget();
            }
        }).start();
        return this;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    private void copyAssetsToDst(Context context, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(Environment.getExternalStorageDirectory(), dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(Environment.getExternalStorageDirectory(), dstPath);
                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            errorStr = e.getMessage();
            isSuccess = false;
        }
    }

    public interface FileOperateCallback {
        void onSuccess();

        void onFailed(String error);
    }

//    /**
//     * 使用Apache工具包解压缩zip文件
//     *
//     * @param sourceFilePath 指定的解压缩文件地址
//     * @param targetDirPath  指定的解压缩目录地址
//     */
//    public static void unCompressFile(String sourceFilePath, String targetDirPath, OnUncompressFileCallBack callBack) {
//        ZipFile zf = null;
//        try {
//            BufferedInputStream bis;
//            zf = new ZipFile(sourceFilePath, "GBK");
//            Enumeration e = zf.getEntries();
//            while (e.hasMoreElements()) {
//                org.apache.tools.zip.ZipEntry ze = (org.apache.tools.zip.ZipEntry) e.nextElement();
//                String entryName = ze.getName();
//                String path = targetDirPath + "/" + entryName;
//                if (ze.isDirectory()) {
//                    System.out.println("正在创建解压目录 - " + entryName);
//                    File decompressDirFile = new File(path);
//                    if (!decompressDirFile.exists()) {
//                        decompressDirFile.mkdirs();
//                    }
//                } else {
//                    System.out.println("正在创建解压文件 - " + entryName);
//                    String fileDir = path.substring(0, path.lastIndexOf("/"));
//                    File fileDirFile = new File(fileDir);
//                    if (!fileDirFile.exists()) {
//                        fileDirFile.mkdirs();
//                    }
//                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetDirPath + "/" + entryName));
//                    bis = new BufferedInputStream(zf.getInputStream(ze));
//                    byte[] readContent = new byte[1024];
//                    int readCount = bis.read(readContent);
//                    while (readCount != -1) {
//                        bos.write(readContent, 0, readCount);
//                        readCount = bis.read(readContent);
//                    }
//                    bos.close();
//                }
//            }
//            callBack.onSuccess();
//        } catch (Exception e) {
//            e.printStackTrace();
//            callBack.onFailed(e);
//        } finally {
//            if (null != zf) {
//                try {
//                    zf.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public interface OnUncompressFileCallBack {
        void onSuccess();

        void onFailed(Exception e);
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public boolean copyFolder(String oldPath, String newPath) {
        boolean isok = true;
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            //如果不存在则 return出去
            if (!a.exists()) {
                return false;
            }
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            isok = false;
        }
        return isok;
    }

    /**
     * 将字节转成特定的文件,转化成功返回true失败返回false
     *
     * @param bytes    文件的字节
     * @param filePath 文件存储路径
     * @param fileName 文件名字
     */
    public static boolean outputFile(byte[] bytes, String filePath, String fileName) {
        final String storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        boolean isSuccess = false;
        File file = new File(filePath + File.separator + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                isSuccess = false;
                e.printStackTrace();
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            isSuccess = false;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    isSuccess = false;
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 得到amr的时长
     *
     * @param file
     * @return amr文件时间长度
     * @throws IOException
     */
    public static int getAmrDuration(File file) {
        try {
            long duration = -1;
            int[] packedSize = {12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0,
                    0, 0};
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                long length = file.length();// 文件的长度
                int pos = 6;// 设置初始位置
                int frameCount = 0;// 初始帧数
                int packedPos = -1;

                byte[] datas = new byte[1];// 初始数据值
                while (pos <= length) {
                    randomAccessFile.seek(pos);
                    if (randomAccessFile.read(datas, 0, 1) != 1) {
                        duration = length > 0 ? ((length - 6) / 650) : 0;
                        break;
                    }
                    packedPos = (datas[0] >> 3) & 0x0F;
                    pos += packedSize[packedPos] + 1;
                    frameCount++;
                }

                duration += frameCount * 20;// 帧数*20
            } finally {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
            return (int) ((duration / 1000) + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到amr的时长
     *
     * @param filePath
     * @return amr文件时间长度
     * @throws IOException
     */
    public static int getAmrDuration(String filePath) {
        try {
            long duration = -1;
            int[] packedSize = {12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0,
                    0, 0};
            RandomAccessFile randomAccessFile = null;
            try {
                File file = new File(filePath);
                randomAccessFile = new RandomAccessFile(file, "r");
                long length = file.length();// 文件的长度
                int pos = 6;// 设置初始位置
                int frameCount = 0;// 初始帧数
                int packedPos = -1;

                byte[] datas = new byte[1];// 初始数据值
                while (pos <= length) {
                    randomAccessFile.seek(pos);
                    if (randomAccessFile.read(datas, 0, 1) != 1) {
                        duration = length > 0 ? ((length - 6) / 650) : 0;
                        break;
                    }
                    packedPos = (datas[0] >> 3) & 0x0F;
                    pos += packedSize[packedPos] + 1;
                    frameCount++;
                }

                duration += frameCount * 20;// 帧数*20
            } finally {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
            return (int) ((duration / 1000) + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 复制文件目录
     *
     * @param srcDir  要复制的源目录 eg:/mnt/sdcard/DB
     * @param destDir 复制到的目标目录 eg:/mnt/sdcard/db/
     * @return
     */
    public static boolean copyDir(String srcDir, String destDir) {
        File sourceDir = new File(srcDir);
        //判断文件目录是否存在
        if (!sourceDir.exists()) {
            return false;
        }
        //判断是否是目录
        if (sourceDir.isDirectory()) {
            File[] fileList = sourceDir.listFiles();
            File targetDir = new File(destDir);
            //创建目标目录
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            //遍历要复制该目录下的全部文件
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {//如果如果是子目录进行递归
                    copyDir(fileList[i].getPath() + "/",
                            destDir + fileList[i].getName() + "/");
                } else {//如果是文件则进行文件拷贝
                    copyFile(fileList[i].getPath(), destDir + "/" + fileList[i].getName());
                }
            }
            return true;
        } else {
            copyFileToDir(srcDir, destDir);
            return true;
        }
    }


    /**
     * 复制文件（非目录）
     *
     * @param srcFile  要复制的源文件
     * @param destFile 复制到的目标文件
     * @return
     */
    private static boolean copyFile(String srcFile, String destFile) {
        try {
            InputStream streamFrom = new FileInputStream(srcFile);
            OutputStream streamTo = new FileOutputStream(destFile);
            byte buffer[] = new byte[1024];
            int len;
            while ((len = streamFrom.read(buffer)) > 0) {
                streamTo.write(buffer, 0, len);
            }
            streamFrom.close();
            streamTo.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    /**
     * 把文件拷贝到某一目录下
     *
     * @param srcFile
     * @param destDir
     * @return
     */
    public static boolean copyFileToDir(String srcFile, String destDir) {
        File fileDir = new File(destDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String destFile = destDir + "/" + new File(srcFile).getName();
        try {
            InputStream streamFrom = new FileInputStream(srcFile);
            OutputStream streamTo = new FileOutputStream(destFile);
            byte buffer[] = new byte[1024];
            int len;
            while ((len = streamFrom.read(buffer)) > 0) {
                streamTo.write(buffer, 0, len);
            }
            streamFrom.close();
            streamTo.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    /**
     * 移动文件目录到某一路径下
     *
     * @param srcFile
     * @param destDir
     * @return
     */
    public static boolean moveFile(String srcFile, String destDir) {
        //复制后删除原目录
        if (copyDir(srcFile, destDir)) {
            deleteFile(new File(srcFile));
            return true;
        }
        return false;
    }

    /**
     * 删除文件（包括目录）
     *
     * @param delFile
     */
    public static void deleteFile(File delFile) {
        //如果是目录递归删除
        if (delFile.isDirectory()) {
            File[] files = delFile.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
        } else {
            delFile.delete();
        }
        //如果不执行下面这句，目录下所有文件都删除了，但是还剩下子目录空文件夹
        delFile.delete();
    }
}
