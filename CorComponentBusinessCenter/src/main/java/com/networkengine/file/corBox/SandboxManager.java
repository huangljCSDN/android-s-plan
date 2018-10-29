package com.networkengine.file.corBox;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.coracle.box.CoracleBox;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.networkengine.AsyncUtil.Marker;

/**
 * 沙箱管理器
 */
public class SandboxManager {

    private static final String TAG = "SandboxManager";

    private static final String DIRECTORY_KEY = "directory";

    private static final String DE_BOX_PWD = "cor_box_pwd";

    private static final int PART_SIZE = 512 * 1024;

    private static final String DIRECTORY_SEPARATOR = ",";

    private CoracleBox mBox;

    private Marker<WriteTask> mMarker;

    private Map<String, WriteResult> mBoxTransResult;

    /**
     * 封装了写入结果，对应回调和数据键集合
     */
    private class WriteResult {
        private WriteCallback callback;
        private boolean result;
        private ArrayList<String> dataKeys = new ArrayList<>();

        public WriteResult(WriteCallback callback, boolean result) {
            this.callback = callback;
            this.result = result;

        }

        /**
         * 添加数据键
         *
         * @param dataKey 数据键
         */
        void addDataKey(String dataKey) {
            if (dataKeys == null) {
                dataKeys = new ArrayList<>();
            }
            dataKeys.add(dataKey);
        }

        /**
         * 获得数据键
         *
         * @return
         */
        List<String> getDataKeys() {
            if (dataKeys == null) {
                return new ArrayList<>();
            }
            return dataKeys;
        }
    }

    /**
     * 文件写入的异步任务
     */
    private abstract class WriteTask extends AsyncTask<String, Integer, Boolean> {

        /**
         * 文件的绝对路径
         */
        String mPath;

        /**
         * 数据包的key（绝对路径＋"_"+包下标）
         */
        String mPartPath;

        @Override
        protected Boolean doInBackground(String... params) {
            if (params == null || params.length != 2) {
                return false;
            }
            mPath = params[0];
            mPartPath = String.format("%s%s%s", params[0], FileInfo.SEPARATOR, params[1]);
            return doInBackground();
        }

        /**
         * 具体的写入操作
         *
         * @return
         */
        abstract Boolean doInBackground();
    }

    /**
     * 写入回调
     */
    private interface WriteCallback {
        void onSuccess(List<String> dataKeys);

        void onFail();
    }

    /**
     * 沙箱管理器初始化
     *
     * @param ct
     * @param appkey
     * @return
     */
    private CoracleBox initeBox(Context ct, String appkey) {
        if (mBox == null) {
            mBox = new CoracleBox(ct, appkey, DE_BOX_PWD);
        }

        /*
        * 同步集合
        * */
        mBoxTransResult = Collections.synchronizedMap(new HashMap<String, WriteResult>());

        /*
        * 记录器
        * */
        mMarker = new Marker<WriteTask>() {
            @Override
            public boolean compare(WriteTask internal, WriteTask external) {
                if (internal == null || external == null) {
                    return false;
                }

                /*
                * 判断相同标记对象的条件（文件包数据键）
                * */
                return internal.mPartPath.equals(external.mPartPath);
            }
        };
        return mBox;
    }

    /**
     * 构造
     *
     * @param ct
     */
    public SandboxManager(Context ct, String appkey) {
        initeBox(ct, appkey);
    }

    /**
     * 沙箱是否存在某文件
     *
     * @param path 文件绝对路径
     * @return
     */
    public Boolean exists(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return getFileKeys().contains(path);
    }

    /**
     * 获取文件详情
     *
     * @param path 文件路径
     * @return 文件详情
     */
    public FileInfo getFileInfo(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        if (!getFileKeys().contains(path)) {
            return null;
        }

        try {

            String infoPath = FileInfo.getInfoPath(path);

            String infoJson = mBox.readString(FileInfo.encoderPath(infoPath));

            if (infoJson == null) {
                return null;
            }

            FileInfo fileInfo = new Gson().fromJson(infoJson, FileInfo.class);
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 软删除所有沙箱文件
     *
     * @return
     */
    public boolean softClearAllFile() {
        if (mBox == null) {
            return false;
        }
        try {
            mBox.writeData(DIRECTORY_KEY, "");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得文件列表
     *
     * @param readInfoListener
     * @return
     */
    public boolean getAllFile(final ReadInfoListener readInfoListener) {

        if (readInfoListener == null) {
            return false;
        }

        new AsyncTask<Void, Void, List<FileInfo>>() {
            @Override
            protected List<FileInfo> doInBackground(Void... params) {
                List<String> paths = getFileKeys();

                List<FileInfo> fileInfos = new ArrayList<>();
                if (paths == null || paths.isEmpty()) {
                    return null;
                }

                for (String path : paths) {

                    FileInfo fileInfo = getFileInfo(path);

                    if (fileInfo == null) {
                        continue;
                    }

                    fileInfos.add(fileInfo);
                }

                return fileInfos;
            }

            @Override
            protected void onPostExecute(List<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                if (fileInfos == null) {
                    readInfoListener.onFail();
                    return;
                }

                readInfoListener.onSuccess(fileInfos);
            }
        }.execute();

        return true;
    }

    /**
     * 从沙箱读取文件
     *
     * @param path             文件路径
     * @param readFileListener 文件读取监听
     * @return
     */
    public boolean readFile(String path, ReadFileListener readFileListener) {

        FileInfo fileInfo = getFileInfo(path);

        if (fileInfo == null) {
            return false;
        }

        if (TextUtils.isEmpty(fileInfo.getmPath())) {
            return false;
        }

        /*
        * 得到有顺序的dataKeys
        * */
        List<String> dataKeys = fileInfo.getSortList();

        if (dataKeys == null || dataKeys.isEmpty()) {
            return false;
        }

        /*
        * 从沙箱异步读取文件
        * */
        getReadTask(path, readFileListener).execute(dataKeys.toArray(new String[dataKeys.size()]));

        return true;
    }

    /**
     * 从沙箱读取文件
     *
     * @param path 文件路径
     * @return
     */
    public File readFile(String path) {

        FileInfo fileInfo = getFileInfo(path);

        if (fileInfo == null) {
            return null;
        }

        if (TextUtils.isEmpty(fileInfo.getmPath())) {
            return null;
        }

        /*
        * 得到有顺序的dataKeys
        * */
        List<String> dataKeys = fileInfo.getSortList();

        if (dataKeys == null || dataKeys.isEmpty()) {
            return null;
        }

        /*
        * 从沙箱异步读取文件
        * */
        return getReadTask(path, dataKeys.toArray(new String[dataKeys.size()]));

    }

    /**
     * 公共的文件写入函数
     *
     * @param fileInfo          文件信息
     * @param writeFileListener 文件写入回调
     * @return
     */
    public boolean writeFile(final FileInfo fileInfo, final WriteFileListener writeFileListener) {

        if (fileInfo == null) {
            return false;
        }

        WriteCallback writeCallback = new WriteCallback() {
            @Override
            public void onSuccess(List<String> dataKeys) {

                fileInfo.setmDataKeys(dataKeys);

                /*
                * 写入文件描述，显示文件详情用
                * */
                boolean writeInfoResult = writeFileInfo(fileInfo);

                if (writeInfoResult) {

                    /*
                    * 写入文件目录，显示沙箱文件列表用
                    * */
                    boolean writeDirectoryResult = writeDirectory(fileInfo.getmPath());

                    //TODO 目前文件描述和文件目录的写入为串行任务，后面可以优化

                    /*
                    * 三种信息成功写入，视为整个文件的沙箱写入操作完成
                    * */
                    if (writeDirectoryResult) {

                        writeFileListener.onSuccess(fileInfo.getmPath(), fileInfo);

                        /*
                        * 清除缓存
                        * */
                        mBoxTransResult.remove(fileInfo.getmPath());
                    } else {
                        writeFileListener.onFail();
                    }
                } else {
                    writeFileListener.onFail();
                }
            }

            @Override
            public void onFail() {
                writeFileListener.onFail();
            }
        };

        return writeFileToBox(fileInfo.getmPath(), writeCallback);
    }

    /**
     * 将文件分包写入沙箱，写成功或失败通过参数中的callback回调反馈
     *
     * @param path     文件的绝对路径
     * @param callback 写文件的回调
     * @return
     */
    private boolean writeFileToBox(String path, WriteCallback callback) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        if (mBox == null) {
            return false;
        }

        if (mBoxTransResult == null) {

            mBoxTransResult = Collections.synchronizedMap(new HashMap<String, WriteResult>());
        }

        if (mBoxTransResult.containsKey(path)) {
            WriteResult writeResult = mBoxTransResult.get(path);

            /*
            * 传输表里有记录并且结果为true 说明当前正在处理或者已经成功放入过沙箱
            * */
            if (writeResult != null && writeResult.callback != null && writeResult.result) {

                /*
                * 替换老的回调
                * */
                if (callback != null) {
                    writeResult.callback = callback;
                }

                writeSuccess(path);
                return true;
            }
            /*
            * 传输表有记录但回调缺失或当前结果为失败的重新初始化并开始往沙箱写入
            * */
            else {
                mBoxTransResult.put(path, new WriteResult(callback, true));
            }
        } else {
            mBoxTransResult.put(path, new WriteResult(callback, true));
        }

        FileInputStream in = null;

        try {
            in = new FileInputStream(path);
            byte buffer[] = new byte[PART_SIZE];

            AtomicInteger num = new AtomicInteger();

            /*
            * 分包写入沙箱
            * */
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                byte writeBuffer[] = buffer.clone();
                /*
                * 出现错误马上中断
                * */
                if (!mBoxTransResult.get(path).result) {
                    return false;
                }

                String partKey = path + FileInfo.SEPARATOR + num.incrementAndGet();

                Log.i(TAG, "index = " + num.get() + " , partKey = " + partKey);

                if (len < PART_SIZE) {
                    writeBuffer = Arrays.copyOfRange(writeBuffer, 0, len);
                }

                writePart(path, num.get() + "", writeBuffer);
            }

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        } finally {

            try {
                in.close();
            } catch (Exception e) {

                return false;
            }

        }
        return true;
    }

    /**
     * 大文件中一个数据包的保存
     *
     * @param path  文件的绝对路径
     * @param index 数据包下标
     * @param data  文件数据包
     * @return 是否正常执行（不代表保存成功）
     * @throws Exception
     */
    private boolean writePart(String path, String index, byte[] data) throws Exception {

        if (TextUtils.isEmpty(path)) {
            return false;
        }

        if (data == null) {
            return false;
        }

        if (mBox == null) {
            return false;
        }

        mMarker.add(getWriteTask(path, index, data));

        return true;
    }

    /**
     * 获得并执行写入任务
     *
     * @param path  绝对路径
     * @param index 包下标
     * @param data  文件数据
     * @return 写入任务
     */
    private WriteTask getWriteTask(String path, String index, final byte[] data) {
        return (WriteTask) new WriteTask() {

            @Override
            Boolean doInBackground() {

                /*
                * 出现错误马上中断
                * */
                if (!isTransSuccess(mPath)) {
                    this.cancel(true);
                    return false;
                }

                try {

//                    byte[] s_img_b = "asdasdasdasd".getBytes();
                    mBox.writeData(FileInfo.encoderPath(mPartPath), data);
//                    mBox.writeData("SSstoragePPemula%2FedP0Oc.mOcoracleSxsimpleFFAFrecvFileA1480358747151Smp4_1", data);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                mMarker.mark(this);

                if (!aBoolean) {
                    onePartTransFail(mPath);
                } else {
                    addDataKey(mPath, mPartPath);
                }

                if (mMarker.isAllMark()) {
                    if (isTransSuccess(mPath)) {
                        writeSuccess(mPath);
                    } else {
                        onePartTransFail(mPath);
                    }
                }

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path, index);
    }

    /**
     * 返回沙箱读取文件的同步任务
     *
     * @param path
     * @param dataKeys
     * @return
     */
    private File getReadTask(final String path, String... dataKeys) {
        if (dataKeys == null || dataKeys.length == 0) {
            return null;
        }

        try {
            File outFile = new File(path);
//
//                    StringBuffer aa = new StringBuffer(path);
//                    aa.insert(path.lastIndexOf("."), "_pp");
//                    String outPath = aa.toString();
//                    File outFile = new File(outPath);

            if (!outFile.exists()) {
                outFile.createNewFile();
            } else {
                if (outFile.length() != 0) {
                    outFile.delete();
                    outFile.createNewFile();
                }
            }

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(outFile, true);
                for (String dataKey : dataKeys) {
                    if (TextUtils.isEmpty(dataKey)) {
                        return null;
                    }

                    byte[] fileDataPart = mBox.readData(FileInfo.encoderPath(dataKey));
                    fileOutputStream.write(fileDataPart);

                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {

                fileOutputStream.close();
            }

            return outFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回沙箱读取文件的任务
     *
     * @param path             文件路径
     * @param readFileListener 读取监听
     * @return
     */
    private AsyncTask<String, Void, File> getReadTask(final String path, final ReadFileListener readFileListener) {
        return new AsyncTask<String, Void, File>() {
            @Override
            protected File doInBackground(String... dataKeys) {
                if (dataKeys == null || dataKeys.length == 0) {
                    return null;
                }
                return getReadTask(path, dataKeys);
//                try {
//                    File outFile = new File(path);
////
////                    StringBuffer aa = new StringBuffer(path);
////                    aa.insert(path.lastIndexOf("."), "_pp");
////                    String outPath = aa.toString();
////                    File outFile = new File(outPath);
//
//                    if (!outFile.exists()) {
//                        outFile.createNewFile();
//                    } else {
//                        if (outFile.length() != 0) {
//                            outFile.delete();
//                            outFile.createNewFile();
//                        }
//                    }
//
//                    FileOutputStream fileOutputStream = null;
//                    try {
//                        fileOutputStream = new FileOutputStream(outFile, true);
//                        for (String dataKey : dataKeys) {
//                            if (TextUtils.isEmpty(dataKey)) {
//                                return null;
//                            }
//
//                            byte[] fileDataPart = mBox.readData(FileInfo.encoderPath(dataKey));
//                            fileOutputStream.write(fileDataPart);
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return null;
//                    } finally {
//
//                        fileOutputStream.close();
//                    }
//
//                    return outFile;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return null;
//                }
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);

                if (file == null || !file.exists() || file.length() == 0) {
                    readFileListener.onFail();
                    return;
                }

                readFileListener.onSuccess(file);
            }
        };
    }


    /**
     * 获得当前文件的传输状态
     *
     * @param path 文件路径
     * @return
     */
    private boolean isTransSuccess(String path) {

        if (TextUtils.isEmpty(path)) {
            return false;
        }

        if (mBoxTransResult == null || mBoxTransResult.isEmpty() || !mBoxTransResult.containsKey(path)) {
            return false;
        }

        WriteResult writeResult = mBoxTransResult.get(path);

        if (writeResult == null) {
            return false;
        }

        return writeResult.result;

    }

    /**
     * 添加数据键
     *
     * @param path
     * @param dataKey
     */
    private void addDataKey(String path, String dataKey) {
        WriteResult writeResult = getWriteResult(path);

        if (writeResult == null) {
            return;
        }

        writeResult.addDataKey(dataKey);

    }

    /**
     * 传输失败处理
     *
     * @param path 文件路径
     */
    private synchronized void onePartTransFail(String path) {

        WriteResult writeResult = getWriteResult(path);

        if (writeResult == null) {
            return;
        }

        writeResult.result = false;

        writeResult.callback.onFail();
    }

    /**
     * 传输成功
     *
     * @param path 文件路径
     */
    private synchronized void writeSuccess(String path) {

        WriteResult writeResult = getWriteResult(path);

        if (writeResult == null) {
            return;
        }

        writeResult.result = true;

        if (writeResult.callback != null) {
            writeResult.callback.onSuccess(getDataKeys(path));
        }
    }

    /**
     * 获得文件传输中所有的数据键
     *
     * @param path
     * @return
     */
    private List<String> getDataKeys(String path) {

        WriteResult writeResult = getWriteResult(path);

        if (writeResult == null) {
            return null;
        }

        return writeResult.getDataKeys();
    }

    /**
     * 获得传输结果
     * * @param path
     *
     * @return
     */
    private WriteResult getWriteResult(String path) {

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        if (mBoxTransResult == null || mBoxTransResult.isEmpty() || !mBoxTransResult.containsKey(path)) {
            return null;
        }

        return mBoxTransResult.get(path);

    }

    /**
     * 将沙箱文件写入简单的文件字典
     *
     * @param path
     * @return
     */
    private boolean writeDirectory(String path) {

        if (mBox == null) {
            return false;
        }

        try {
            String oDirectory = mBox.readString(DIRECTORY_KEY);

            if (TextUtils.isEmpty(oDirectory)) {
                oDirectory = path;
            } else {
                /*
                * 字典中已经存在该文件
                * */
                List<String> filePaths = Arrays.asList(oDirectory.split(DIRECTORY_SEPARATOR));
                if (filePaths != null && filePaths.contains(path)) {
                    return true;
                }

                oDirectory = String.format("%s%s%s", oDirectory, DIRECTORY_SEPARATOR, path);
            }
            mBox.writeData(DIRECTORY_KEY, oDirectory);

            if (oDirectory.equals(mBox.readString(DIRECTORY_KEY))) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 写入文件描述信息
     *
     * @param fileInfo
     * @return
     */
    private boolean writeFileInfo(FileInfo fileInfo) {
        if (mBox == null) {
            return false;
        }

        if (fileInfo == null) {
            return false;
        }

        String infoKey = fileInfo.getInfoPath();

        try {
            String fileInfoForString = new Gson().toJson(fileInfo);
            mBox.writeData(FileInfo.encoderPath(infoKey), fileInfoForString);
            String fileJson = mBox.readString(FileInfo.encoderPath(infoKey));
            return fileInfoForString.equals(fileJson);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获得沙箱文件名列表
     *
     * @return
     */
    private List<String> getFileKeys() {

        List<String> filePaths = new ArrayList<>();
        if (mBox == null) {
            return filePaths;
        }

        try {
            String directory = mBox.readString(DIRECTORY_KEY);
            if (TextUtils.isEmpty(directory)) {
                return filePaths;
            }
            String[] paths = directory.split(DIRECTORY_SEPARATOR);
            Collections.addAll(filePaths, paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePaths;
    }
}
