package com.networkengine.file.corBox;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 沙箱文件信息
 * Created by pengpeng on 16/12/28.
 */

public class FileInfo implements Comparator<String> {

    public static final String SEPARATOR = "_";

    private static final String INFO_SUFFIX = "_inf";

    private static final String PATH_SEPARATOR = "/";

    private static final String REPLACE_PATH_SEPARATOR = "%2F";

    private String mPath;

    private String mType;

    private long mSize;

    private String mTime;

    private List<String> mDataKeys = new ArrayList<>();

    private  int mFileType;
    private  long mLastModified;
    private  String mName;

    public FileInfo(String path, String type, long size, String time) {
        this(path, type, size, time, new ArrayList<String>());
    }

    public FileInfo(String path, int fileType, long size, long lastModified,String name) {
        this.mPath = path;
        this.mFileType = fileType;
        this.mSize = size;
        this.mLastModified = lastModified;
        this.mName = name;
    }

    public FileInfo(String path, String type, long size, String time, List<String> dataKeys) {
        this.mPath = path;
        this.mType = type;
        this.mSize = size;
        this.mTime = time;
        this.mDataKeys = dataKeys;

    }

    public String getmPath() {
        return mPath;
    }

    public String getmType() {
        return mType;
    }

    public long getmSize() {
        return mSize;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public void setmDataKeys(List<String> mDataKeys) {
        this.mDataKeys = mDataKeys;
    }

    public static String decoderPath(String intoPath) {
        if (intoPath == null) {
            return intoPath;
        }
        return intoPath.replaceAll(PATH_SEPARATOR, REPLACE_PATH_SEPARATOR);
    }

    public static String encoderPath(String oPath) {
        if (oPath == null) {
            return oPath;
        }
        return oPath.replaceAll(PATH_SEPARATOR, REPLACE_PATH_SEPARATOR);
    }

    public static String getInfoPath(String path) {
        return TextUtils.isEmpty(path) ? path : String.format("%s%s", path, INFO_SUFFIX);
    }

    public String getInfoPath() {
        return getInfoPath(mPath);
    }

    public List<String> getSortList() {
        sort();
        return mDataKeys;
    }

    public void sort() {
        Collections.sort(mDataKeys, this);
    }

    @Override
    public int compare(String lhs, String rhs) {

        if (TextUtils.isEmpty(lhs) || TextUtils.isEmpty(rhs)) {
            return 0;
        }

        int lhsSeparatorIndex = lhs.lastIndexOf(SEPARATOR) + 1;
        int rhsSeparatorIndex = rhs.lastIndexOf(SEPARATOR) + 1;
        String lhsSuffix = lhs.substring(lhsSeparatorIndex);
        String rhsSuffix = rhs.substring(rhsSeparatorIndex);

        int lhsIndex = Integer.parseInt(lhsSuffix);
        int rhsIndex = Integer.parseInt(rhsSuffix);
        if (lhsIndex > rhsIndex) {
            return 1;
        } else if (lhsIndex < rhsIndex) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "mPath='" + mPath + '\'' +
                ", mType=" + mType +
                ", mSize=" + mSize +
                ", mTime='" + mTime + '\'' +
                ", mDataKeys=" + mDataKeys +
                '}';
    }
}