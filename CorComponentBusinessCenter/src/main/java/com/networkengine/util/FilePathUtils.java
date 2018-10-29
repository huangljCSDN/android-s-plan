package com.networkengine.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * 文件路径工具类
 */
@SuppressWarnings("ALL")
public class FilePathUtils {
    /**
     * 解压包目录
     */
    public static final String UNZIP_PATH_NAME = "/unZip";

    /**
     * 接受的文件目录
     */
    public static final String RECV_FILE = "/recvFile";
    /**
     * 下载的文件的目录
     */
    public static final String DOWN_FILE = "/downFile";

    /**
     * 数据库
     */
    public static final String DB_PATH_NAME = "/db";
    /**
     * 图片
     */
    public static final String IMAGE_PATH_NAME = "/image";
    /**
     * 录音文件
     */
    public static final String RECORD_PATH_NAME = "/record";
    /**
     * 视屏缩略图
     */
    public static final String VIDEO_THUMB_PATH = "/video_thumb";
    /**
     * 视频
     */
    public static final String VIDEO_PATH_NAME = "/video";
    /**
     * log
     */
    public static final String LOG_PATH_NAME = "/log";
    /**
     * 零时文件目录
     */
    public static final String TEMP_PATH_NAME = "/temp";

    /**
     * 收藏文件目录
     */
    public static final String COLLECTION_PATH_NAME = "/collectionRecvFile";
    /**
     * 零时文件目录
     */
    public static final String TEMP_ZIP_PATH_NAME = "/tempzip";
    /**
     * bitmap
     */
    public static final String TEMP_BITMAP = "bitmap";

    /**
     * 皮肤文件目录
     */
    public static final String SKIN_PATH_NAME = "/skin";

    /**
     * 表情文件目录
     */
    public static final String EXPRESSION_PATH_NAME = "/express";
    /**
     * path
     */
    private static String SDCARD_PATH;
    /**
     * 保存的文件名 默认包名
     */
//    public static String SAVE_PATH_NAME;
    /**
     * 单例
     */
    private static final FilePathUtils mPathUtils = null;


    private FilePathUtils(Context context, String rootPath) {


        SDCARD_PATH = rootPath;
//        SAVE_PATH_NAME = context.getPackageName();

//        File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME);
        File file = new File(SDCARD_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 得到缓存路径 1，如果有sd卡 就是sd空间 2，没有sd卡 就是手机系统分配空间
     */
    public static FilePathUtils getSDIntance() {
        Context context = CoracleSdk.getCoracleSdk().getContext();
        if (mPathUtils == null) {
            String rootPath = "";
            if (SDCardUtils.isSDCardEnable()) {

                rootPath = context.getExternalCacheDir().getPath();
            } else {
                rootPath = context.getCacheDir().getPath();
            }
            return new FilePathUtils(context, rootPath);
        }
        return mPathUtils;

    }

    /**
     * 获取缓存的目录空间
     *
     * @param context
     * @return
     */
    public static FilePathUtils getCacheIntance(Context context) {
        if (mPathUtils == null) {
            String path = context.getCacheDir().getPath();

            return new FilePathUtils(context, path);
        }
        return mPathUtils;

    }

    /**
     * 获取目录地址
     *
     * @return
     */
    public String getBasePath() {

//        return String.format("%s%s%s", SDCARD_PATH, "/", SAVE_PATH_NAME);
        return SDCARD_PATH;
    }


    /**
     * 创建跟目录下的子目录
     *
     * @return
     */
    public String mkdirsSubFile(String subdirectory) {
        if (TextUtils.isEmpty(subdirectory)) {
            return "";
        }
        if (!subdirectory.startsWith("/")) {
            subdirectory = String.format("%s%s", "/", subdirectory);
        }
        File file = new File(getBasePath() + subdirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();

    }

    /**
     * 创建跟目录下的子目录
     *
     * @return
     */
    public File mkdirsSubFiles(String subdirectory) {
        if (TextUtils.isEmpty(subdirectory)) {
            return null;
        }
        if (!subdirectory.startsWith("/")) {
            subdirectory = String.format("%s%s", "/", subdirectory);
        }
        File file = new File(getBasePath() + subdirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;

    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean fileIsExists(String path) {
        if (path == null || path.trim().length() <= 0) {
            return false;
        }
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 递归删除文件及文件夹
     */
    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists())
            return true;

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles != null && childFiles.length > 0) {
                for (int i = 0; i < childFiles.length; i++) {
                    deleteFile(childFiles[i]);
                }
            }
        }

        return file.delete();
    }


    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String formatFileLen(long size) {
        if (size >= 1024 * 1024 * 1024) {
            return String.format("%.1fG", (size * 1.0) / (1024 * 1024 * 1024));
        }
        if (size > 1024 * 1024) {
            return String.format("%.1fM", (size * 1.0) / (1024 * 1024));
        }
        if (size > 1024) {
            return String.format("%.1fK", (size * 1.0) / 1024);
        }
        return size + "B";
    }


    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private static String getFileType(File file) {

        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if ("".equals(end)) {
            return type;
        }
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 获取文件类型
     *
     * @param path
     * @return
     */
    public static String getFileType(String path) {
        String type = "*/*";
        File file = new File(path);
        if (file == null || file.exists())
            return type;
        return getFileType(file);
    }

    /**
     * 调用系统工具，打开文件
     *
     * @param ct
     * @param file
     */
    public static void openFile(Context ct, File file) {
    	try{
			if (null == file || !file.exists()) {
	            Toast.makeText(ct, "文件不存在！", Toast.LENGTH_SHORT).show();
	            return;
	        }
	        Intent intent = new Intent();
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        // 设置intent的Action属性
	        intent.setAction(Intent.ACTION_VIEW);
	        // 获取文件file的MIME类型
	        String type = getFileType(file);
	        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	        // 设置intent的data和Type属性。
	        intent.setDataAndType(/* uri */getUri(ct, file), type);
	        if (intent.resolveActivity(ct.getPackageManager()) != null){
            // 跳转
            ct.startActivity(intent);
	        }else {
//	            Toast.makeText(ct, ct.getString(R.string.no_resolve_activity), Toast.LENGTH_SHORT).show();
	        }
    	}catch(Exception e){
    		Toast.makeText(ct, "找不到对应的文件类型", Toast.LENGTH_SHORT).show();
    	}
    }


    private final static String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {".amr", "audio/amr"},
            {"", "*/*"},
    };

    private static List<String> mImgTyepLists;

    public static boolean isImag(String type) {

        if (mImgTyepLists == null) {
            mImgTyepLists = new ArrayList<>();
            mImgTyepLists.add(".jpg");
            mImgTyepLists.add(".png");
            mImgTyepLists.add(".bmp");
            mImgTyepLists.add(".gif");
            mImgTyepLists.add(".jpeg");
        }

        if (mImgTyepLists.contains(type)) {

            return true;
        }

        return false;


    }

    private static List<String> mMedioTyepLists;

    public static boolean isMedio(String type) {

        if (mMedioTyepLists == null) {
            mMedioTyepLists = new ArrayList<>();
            mMedioTyepLists.add(".3gp");
            mMedioTyepLists.add(".mp4");
            mMedioTyepLists.add(".3gp");
            mMedioTyepLists.add(".m3u");
            mMedioTyepLists.add(".m4a");
            mMedioTyepLists.add(".m4b");
            mMedioTyepLists.add(".m4p");
            mMedioTyepLists.add(".m4u");
            mMedioTyepLists.add(".m4v");
            mMedioTyepLists.add(".mov");
            mMedioTyepLists.add(".mp2");
            mMedioTyepLists.add(".mp3");
            mMedioTyepLists.add(".mp4");
            mMedioTyepLists.add(".mpe");
            mMedioTyepLists.add(".mpeg");
            mMedioTyepLists.add(".mpg");
            mMedioTyepLists.add(".mpg4");
            mMedioTyepLists.add(".mpga");
            mMedioTyepLists.add(".ogg");
            mMedioTyepLists.add(".rmvb");
            mMedioTyepLists.add(".wav");
            mMedioTyepLists.add(".wmv");

        }

        if (mMedioTyepLists.contains(type)) {

            return true;
        }

        return false;
    }

    private static List<String> mDocumentLists;


    public static boolean isDocument(String type) {
        if (mDocumentLists == null) {
            mDocumentLists = new ArrayList<>();
            mDocumentLists.add(".c");
            mDocumentLists.add(".conf");
            mDocumentLists.add(".cpp");
            mDocumentLists.add(".docx");
            mDocumentLists.add(".xls");
            mDocumentLists.add(".xlsx");
            mDocumentLists.add(".h");
            mDocumentLists.add(".htm");
            mDocumentLists.add(".html");
            mDocumentLists.add(".java");
            mDocumentLists.add(".js");
            mDocumentLists.add(".log");
            mDocumentLists.add(".pdf");
            mDocumentLists.add(".png");
            mDocumentLists.add(".pps");
            mDocumentLists.add(".ppt");
            mDocumentLists.add(".pptx");
            mDocumentLists.add("prop");
            mDocumentLists.add(".sh");
            mDocumentLists.add(".wps");
            mDocumentLists.add(".z");
        }

        if (mDocumentLists.contains(type)) {

            return true;
        }

        return false;

    }


    public static boolean isPicture(File file) {
        if (null == file) {
            return false;
        }
        return getFileType(file).startsWith("image/");
    }

    /**
     * file转byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * byte[]转file
     *
     * @param buf
     * @param filePath
     * @param fileName
     */
    public static boolean byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static File getCacheDir(Context context, String uniqueName) {
        String cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获得当前应用默认的解压路径
     */
    public static String getDefaultUnzipFile() {
        Context context = CoracleSdk.getCoracleSdk().getContext();
        File unZipDir = new File(context.getFilesDir(), "unZip");
        if (!unZipDir.exists()) {
            unZipDir.mkdirs();
        }
        return unZipDir.getAbsolutePath();
    }

    /**
     * 解压压缩文件
     *
     * @param zipPath
     * @param toPath
     * @return
     */
    public static boolean unZipResourcePackage(File zipFile, String descDir) {
        Log.e("workspace", "==================start==================");
        Log.e("workspace", "unZipResourcePackage zipFile : " + zipFile.getPath() + " , exist : " + zipFile.exists());
        Log.e("workspace", "unZipResourcePackage descDir : " + descDir + " , exist : " + new File(descDir).exists());

        boolean success = true;
        try {
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            ZipFile zip = new ZipFile(zipFile);
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
                // 过滤平台路径问题
                outPath = (descDir + zipEntryName).replaceAll("\\\\", "/");
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
            Log.e("workspace", "==================end==================");
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    /**
     * 获取目录下的所有文件
     *
     * @param path
     * @return
     */
    public static File[] getFileListByPath(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            return file.listFiles();
        } else {
            return new File[0];
        }
    }

    /**
     * 复制文件
     *
     * @param file
     * @param dirPath
     * @return
     */
    public boolean instFileToAPPDirectory(File file, String subdirectory, String newName) {
        if (file == null || !file.exists() || TextUtils.isEmpty(subdirectory) || TextUtils.isEmpty(newName)) {
            return false;
        }
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        String path = String.format("%s%s%s", mkdirsSubFile(subdirectory), "/", newName);
        File newFile = new File(path);
        try {
            newFile.createNewFile();
            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(newFile);
            byte[] temp = new byte[4096];
            int len = -1;
            while ((len = inputStream.read(temp)) != -1) {
                outputStream.write(temp, 0, len);
                outputStream.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * base 64 保存文件
     *
     * @param base64Str
     * @param file
     */
    public boolean instFileToAPPDirectory(String base64Str, String subdirectory, String newName) {
        if (TextUtils.isEmpty(base64Str) || TextUtils.isEmpty(subdirectory) || TextUtils.isEmpty(newName)) {
            return false;
        }

        byte[] bitmapArray = Base64.decode(base64Str, Base64.DEFAULT);
        OutputStream out = null;
        String path = String.format("%s%s%s", mkdirsSubFile(subdirectory), "/", newName);
        File newFile = new File(path);
        try {
            newFile.createNewFile();
            out = new FileOutputStream(newFile);
            out.write(bitmapArray);
            out.flush();
        } catch (Exception e) {
            LogUtil.e("", e);
            return false;
        } finally {

            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogUtil.e("", e);
            }
        }
        return true;
    }

    /**
     * 7.0以后系统获取Uri方法有变化，改用FileProvider
     * @param context
     * @param file
     * @return
     */
    public static Uri getUri(Context context, File file){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//            return FileProvider.getUriForFile(context, context.getPackageName()+".fileProvider", file);
        }else {
            return Uri.fromFile(file);
        }
        return Uri.fromFile(file);
    }
}
