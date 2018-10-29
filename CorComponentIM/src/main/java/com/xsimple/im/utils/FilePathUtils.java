package com.xsimple.im.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.networkengine.entity.FileInfo;
import com.xsimple.im.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.networkengine.util.DateUtil;

/**
 * 文件路径工具类
 *
 * @author ouyangbo
 */
@SuppressWarnings("ALL")
public class FilePathUtils {

    public static final String UNZIP_PATH_NAME = "unZip";
    public static final String DB_PATH_NAME = "db";
    public static final String IMAGE_PATH_NAME = "/image";
    public static final String RECORD_PATH_NAME = "/record";
    public static final String VIDEO_THUMB_PATH = "/video_thumb";
    public static final String VIDEO_PATH_NAME = "/video";
    public static final String LOG_PATH_NAME = "log";

    public static final String TEMP_PATH_NAME = "/temp";

    private static String cachePath;

    private static String SDCARD_PATH;
    public static String SAVE_PATH_NAME;
    private static final FilePathUtils mPathUtils = null;

    /**
     * 得到缓存路径 1，如果有sd卡 就是sd空间 2，没有sd卡 就是手机系统分配空间
     *
     * @param context
     */
    private FilePathUtils(Context context) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            SDCARD_PATH = context.getExternalCacheDir().getPath();
            // SDCARD_PATH = context.getFilesDir().getPath();

        } else {
            SDCARD_PATH = context.getCacheDir().getPath();
        }
        SAVE_PATH_NAME = context.getPackageName();

        File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static FilePathUtils getIntance(Context context) {
        if (mPathUtils == null) {
            return new FilePathUtils(context);
        }
        return mPathUtils;

    }

    public static String getBasePath() {
        return SDCARD_PATH + "/" + SAVE_PATH_NAME;
    }


    /**
     * 接受的文件的保存路径
     *
     * @return
     */
    public String getRecvFilePath() {

        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME + "/recvFile");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    /**
     * 下载文件的保存路径
     *
     * @return
     */
    public String getDownFilePath() {

        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME + "/downFile");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return "";
    }


    /**
     * 获取SD卡目录下相对应包名程序下的文件保存的图片的路径
     *
     * @param context
     * @return
     */
    public String getDefaultPicturePath() {
        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME + IMAGE_PATH_NAME);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    /**
     * 获取SD卡目录下相对应包名程序下的录音保存的路径
     *
     * @param context
     * @return
     */
    public String getDefaultRecordPath() {
        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME + RECORD_PATH_NAME);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return "";
    }


    /**
     * 获取零时文件目录
     *
     * @return
     */
    public String getDefaultTempPath() {
        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME + TEMP_PATH_NAME);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return "";
    }


    /**
     * 获取SD卡目录下相对应包名程序下的录制视频保存的路径
     *
     * @param context
     * @return
     */
    public String getDefaultVideoFilePath() {
        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME + VIDEO_PATH_NAME);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return null;
    }


    /**
     * 获取SD卡目录下相对应包名程序下的录制视频截图保存的路径
     *
     * @param context
     * @return
     */
    public String getDefaultVideoThumbPath() {
        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME + VIDEO_THUMB_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return null;
    }


    /**
     * 获取SD卡目录下的相对应包名程序的二维码的图片路径
     *
     * @return
     */
    public String getDefaultQrCordPath() {
        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME + "/qrcode");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return null;
    }


    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static int getFileIcon(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return R.drawable.icon_other;
        }
        String ext = "";
        if (fileName.contains(".")) {
            ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        if ("doc".equals(ext) || "docx".equals(ext) || "rtf".equals(ext) || "wps".equalsIgnoreCase(ext)) {
            return R.drawable.icon_word;
        } else if ("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext) || "csv".equalsIgnoreCase(ext)) {
            return R.drawable.icon_excel;
        } else if ("ppt".equalsIgnoreCase(ext) || "pptx".equalsIgnoreCase(ext)) {
            return R.drawable.icon_ppt;
        } else if ("xml".equalsIgnoreCase(ext) || "html".equalsIgnoreCase(ext)) {
            return R.drawable.icon_ebook;
        } else if ("pdf".equalsIgnoreCase(ext)) {
            return R.drawable.icon_pdf;
        } else if ("txt".equalsIgnoreCase(ext)) {
            return R.drawable.icon_ebook;
        } else if ("png".equalsIgnoreCase(ext) || "jpg".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext) || "bmp".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)) {
            return R.drawable.icon_image;
        } else if ("swf".equalsIgnoreCase(ext) || "rmvb".equalsIgnoreCase(ext) || "avi".equalsIgnoreCase(ext) || "mp4".equalsIgnoreCase(ext) || "rm".equalsIgnoreCase(ext) || "mkv".equalsIgnoreCase(ext) || "3gp".endsWith(ext)) {
            return R.drawable.icon_video;
        } else if ("amr".equalsIgnoreCase(ext) || "wav".equalsIgnoreCase(ext) || "mp3".equalsIgnoreCase(ext)) {
            return R.drawable.icon_music;
        } else if ("rar".equalsIgnoreCase(ext) || "zip".equalsIgnoreCase(ext) || "7z".equalsIgnoreCase(ext)) {
            return R.drawable.icon_zip;
        } else {
            return R.drawable.icon_other;
        }
    }

    public static String getFileType(Context context, String fileName) {
        String ext = "";
        if (fileName.contains(".")) {
            ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        if ("doc".equals(ext) || "docx".equals(ext) || "rtf".equals(ext) || "wps".equalsIgnoreCase(ext)) {
            return "WORD";
        } else if ("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext)) {
            return "EXCEL";
        } else if ("ppt".equalsIgnoreCase(ext) || "pptx".equalsIgnoreCase(ext)) {
            return "PPT";
        } else if ("xml".equalsIgnoreCase(ext) || "html".equalsIgnoreCase(ext)) {
            return "XML";
        } else if ("pdf".equalsIgnoreCase(ext)) {
            return "PDF";
        } else if ("txt".equalsIgnoreCase(ext)) {
            return "TEXT";
        } else if ("png".equalsIgnoreCase(ext) || "jpg".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext) || "bmp".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)) {
            return context.getResources().getString(R.string.business_item_picture);
        } else if ("swf".equalsIgnoreCase(ext) || "rmvb".equalsIgnoreCase(ext) || "avi".equalsIgnoreCase(ext) || "mp4".equalsIgnoreCase(ext) || "rm".equalsIgnoreCase(ext) || "mkv".equalsIgnoreCase(ext) || "3gp".endsWith(ext)) {
            return context.getResources().getString(R.string.business_item_video);
        } else if ("amr".equalsIgnoreCase(ext) || "wav".equalsIgnoreCase(ext) || "mp3".equalsIgnoreCase(ext)) {
            return context.getResources().getString(R.string.business_item_voice);
        } else if ("rar".equalsIgnoreCase(ext) || "zip".equalsIgnoreCase(ext) || "7z".equalsIgnoreCase(ext) || "tar".equalsIgnoreCase(ext)) {
            return context.getResources().getString(R.string.business_item_package);
        } else {
            return context.getResources().getString(R.string.business_item_other);
        }
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
    private static String getMIMEType(File file) {

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

    public static String getMIMEType(String path) {
        String type = "*/*";
        File file = new File(path);
        if (file == null || file.exists())
            return type;
        return getMIMEType(file);
    }

    public static String getMIMETypeByName(String fName) {
        String type = "*/*";
        if (TextUtils.isEmpty(fName)) {
            return type;
        }
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
     * 获取文件后缀
     *
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        if (file == null) {
            return "";
        }
        String name = file.getName();
        String type = name.substring(name.lastIndexOf(".") + 1, name.length());
        return type;
    }


    /**
     * 判断文件能否通过本地应用打开
     *
     * @return
     */
    public static boolean icCanOpenByLocal(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (isAudio(file)) {
            return true;
        }
        if (isVideo(file)) {
            return true;
        }

        return false;
    }

    /**
     * 判断文件能否通过本地应用打开
     *
     * @return
     */
    public static boolean icCanOpenByLocal(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        String mimeTypeByName = getMIMETypeByName(name);
        if (mimeTypeByName.startsWith("audio/")) {
            return true;
        }
        if (mimeTypeByName.startsWith("video/")) {
            if ("video/quicktime".equals(mimeTypeByName)) {
                return false;
            }
            return true;
        }

        return false;
    }


    public static void openFile(final Context ct, File file) {

//        if (null == file || !file.exists()) {
//            Toast.makeText(ct
//                    , ct.getString(R.string.business_file_does_not_exist)
//                    , Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }
//        MediaResource mediaResource = new MediaResource();
//        FileInfo fileInfo = new FileInfo();
//        fileInfo.setPath(file.getPath());
//        fileInfo.setName(file.getName());
//        fileInfo.setSize(file.length() + "");
//        mediaResource.setFileInfo(fileInfo);
//        if (icCanOpenByLocal(file.getName()) && isVideo(file)) {
//            VideoPlayActivity.startMe(ct, mediaResource.toJosn());
//            return;
//        }
//        if (icCanOpenByLocal(file.getName()) && isAudio(file)) {
//            MediaPlayUtils.getMediaPlayUtils(ct).play(file.getPath(), new MediaPlayUtils.OnCompletionListener() {
//                @Override
//                public void onCompletion() {
//
//                }
//            });
//            // AudioPlayActivity.startMe(ct, mediaResource.toJosn());
//            return;
//        }
//
//
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        // 设置intent的Action属性
//        intent.setAction(Intent.ACTION_VIEW);
//        // 获取文件file的MIME类型
//        String type = getMIMEType(file);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        // 设置intent的data和Type属性。
//        intent.setDataAndType(/* uri */getUri(ct, file), type);
//        if (intent.resolveActivity(ct.getPackageManager()) != null) {
//            // 跳转
//            ct.startActivity(intent);
//        } else {
//            Toast.makeText(ct, ct.getString(R.string.no_resolve_activity), Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * @param ct   不要乱改代码，有一个界面，播放的样式不一样
     * @param file
     */
    public static void openFiles(final Context ct, File file) {

//        if (null == file || !file.exists()) {
//            Toast.makeText(ct
//                    , ct.getString(R.string.business_file_does_not_exist)
//                    , Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }
//        MediaResource mediaResource = new MediaResource();
//        FileInfo fileInfo = new FileInfo();
//        fileInfo.setPath(file.getPath());
//        fileInfo.setName(file.getName());
//        fileInfo.setSize(file.length() + "");
//        mediaResource.setFileInfo(fileInfo);
//        if (icCanOpenByLocal(file.getName()) && isVideo(file)) {
//            VideoPlayActivity.startMe(ct, mediaResource.toJosn());
//            return;
//        }
//        if (icCanOpenByLocal(file.getName()) && isAudio(file)) {
//
//            AudioPlayActivity.startMe(ct, mediaResource.toJosn());
//            return;
//        }
//
//
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        // 设置intent的Action属性
//        intent.setAction(Intent.ACTION_VIEW);
//        // 获取文件file的MIME类型
//        String type = getMIMEType(file);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        // 设置intent的data和Type属性。
//        intent.setDataAndType(/* uri */getUri(ct, file), type);
//        if (intent.resolveActivity(ct.getPackageManager()) != null) {
//            // 跳转
//            ct.startActivity(intent);
//        } else {
//            Toast.makeText(ct, ct.getString(R.string.no_resolve_activity), Toast.LENGTH_SHORT).show();
//        }
    }


    /**
     * 7.0以后系统获取Uri方法有变化，改用FileProvider
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        } else {
            return Uri.fromFile(file);
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
            {".csv", "application/vnd.ms-excel"},
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
            {".amr", "audio/x-mpeg"},
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
            {".prop", "text/plain"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
            {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"}, {"", "*/*"}
    };


    public static boolean isAudio(File file) {
        if (null == file) {
            return false;
        }
        return getMIMEType(file).startsWith("audio/");
    }

    public static boolean isVideo(File file) {
        if (null == file) {
            return false;
        }
        return getMIMEType(file).startsWith("video/");
    }


    public static boolean isPicture(File file) {
        if (null == file) {
            return false;
        }
        return getMIMEType(file).startsWith("image/");
    }


    /**
     * 日志保存目录
     *
     * @return
     */
    public String getDefaultLogPath() {
        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/" + SAVE_PATH_NAME + "/log");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return null;
    }

//    /**
//     * 日志保存目录
//     *
//     * @return
//     */
//    public static String getDefaultLogFileName(Context mContext) {
//        return getDefaultLogPath() + "/" + mContext.getPackageName() + "-"
//                + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-"
//                + MsgSyncCenter.getInstance(mContext).getHttpUser() + "-" + Util.getDeviceId(mContext) + ".txt";
//
//    }

    public static List<String> getExtSDCardPaths() {
        List<String> paths = new ArrayList<String>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED)
                && extFile.exists() && extFile.isDirectory()
                && extFile.canWrite()) {
            paths.add(extFile.getAbsolutePath());
        }
        try {
            // obtain executed result of command line code of 'mount', to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                paths.add(mountPath);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block

        }
        return paths;
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
     * 拼接文件名字
     *
     * @param muid
     * @param type
     * @return
     */
    public static String getFileName(String muid, String type) {
        return String.format("%s%s%s%s%s", DateUtil.curTime2Str(), "_", muid, ".", type);
    }

//    /**
//     * 获得当前应用默认的解压路径
//     *
//     * @param context
//     * @return
//     */
//    public static String getDefaultUnzipFile(Context context) {
//        return getPath(context, UNZIP_PATH_NAME);
//    }

//    /**
//     * 获取SD卡目录下相对应包名程序下的数据库的路径
//     *
//     * @param context
//     * @return
//     */
//    public static String getDefaultDataBasePath(Context context) {
//        return getPath(context, DB_PATH_NAME);
//    }


//
//    /**
//     * 获取SD卡目录下相对应包名程序下的拍照保存的图片的路径
//     *
//     * @param context
//     * @return
//     */
//    public static String getDefaultImagePath(Context context) {
//        return getPath(context, IMAGE_PATH_NAME);
//    }

//    /**
//     * 获取SD卡目录下相对应包名程序下的录音保存的图片的路径
//     *
//     * @param context
//     * @return
//     */
//    public static String getDefaultRecordPath(Context context) {
//        return getPath(context, RECORD_PATH_NAME);
//    }
//
//    /**
//     * 获取SD卡目录下相对应包名程序下的视频保存的图片的路径
//     *
//     * @param context
//     * @return
//     */
//    public static String getDefaultVideoPath(Context context) {
//        return getPath(context, VIDEO_PATH_NAME);
//    }
//
//    /**
//     * 日志保存目录
//     *
//     * @return
//     */
//    public static String getDefaultLogPath(Context context) {
//        return getPath(context, LOG_PATH_NAME);
//    }

//    /**
//     * 获得SD卡上缓存目录下文件夹路径
//     *
//     * @param context
//     * @return
//     */
//    public static String getPath(Context context, String dirName) {
//        File cacheDir = getDiskCacheDir(context, dirName);
//
//        if (SDCardUtils.getAvailableExternalMemorySize() == 0) {
//            ToastUtil.showToast(context, "手机存储空间不足！");
//        }
//
//        if (!cacheDir.exists()) {
//            cacheDir.mkdirs();
//        }
//        return cacheDir.getAbsolutePath();
//    }

//    /**
//     * 根据传入的uniqueName获取硬盘缓存的路径地址。
//     */
//    @SuppressLint("NewApi")
//    private static File getDiskCacheDir(Context context, String uniqueName) {
//        if (TextUtils.isEmpty(cachePath) || !new File(cachePath).exists()) {
//            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
//                    && !Environment.isExternalStorageRemovable()) {
////				cachePath = context.getExternalCacheDir().getPath();//context会为null
//                cachePath = AppContext.getInstance().getExternalCacheDir().getPath();
//            } else {
////				cachePath = context.getCacheDir().getPath();
//                cachePath = AppContext.getInstance().getCacheDir().getPath();
//            }
//        }
//        return new File(cachePath + File.separator + uniqueName);
//    }

    public String getCollectionRecvFilePath() {

        if (SDCARD_PATH != null) {
            File file = new File(SDCARD_PATH + "/collectionRecvFile");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    /**
     * 打开文件和图片的方法
     */
    public static void openImage(Context mCt, String localPath) {
//        MediaResource mediaResource = new MediaResource();
//        mediaResource.setNetPath(localPath);
//        ArrayList<MediaResource> list = new ArrayList<MediaResource>();
//        list.add(mediaResource);
//        IMImageViewPagerActivity.startMe(mCt, list, 0, null);

    }
    /**
     * 创建跟目录下的子目录
     *
     * @return
     */
    public static String mkdirsSubFile(String subdirectory) {
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

}
