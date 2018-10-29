package com.xsimple.im.engine.file;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.networkengine.entity.FileSubPackage;
import com.networkengine.mqtt.SubjectDot;
import com.networkengine.networkutil.interfaces.SingNetFileTransferListener;
import com.networkengine.networkutil.process.NetFileTransferControl;
import com.networkengine.networkutil.process.SingDownNetWorkTask;
import com.networkengine.networkutil.process.SingUploadNetWorkTask;
import com.xsimple.im.db.DbManager;
import com.xsimple.im.db.datatable.IMFileInfo;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.engine.IMEngine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by liuhao on 2017/6/8.
 */

public class IMFileManager {

    private static IMFileManager mImFileManager;

    private NetFileTransferControl mNetFileTransferControl;

    private IMEngine mImEngine;

    private Context mContext;

    //private SandboxManager mSandboxManager;

    private DbManager mDbManager;

    // private ThreadPoolExecutor mSandThreadPool;
    /**
     * 消息处理中心
     */
    private SubjectDot<String, Handler.Callback, Message> mCallbackDot = new SubjectDot<String, Handler.Callback, Message>() {
        @Override
        public void execute(Handler.Callback callback, Message msgs) {
            callback.handleMessage(msgs);
        }
    };

    /**
     * 注册 如果，在下载的参数中，注册了监听，一定要在activity 中，解注册
     *
     * @param key
     * @param observer
     */
    public boolean registEventDot(String key, Handler.Callback observer) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        if (observer == null) {
            return false;
        }
        if (mCallbackDot.containsKey(key)) {
            return false;
        }
        mCallbackDot.attach(key, observer);
        return true;
    }

    /**
     * 解注册
     *
     * @param key
     */
    public void unregistEventDot(String key) {
        if (mCallbackDot.containsKey(key)) {
            mCallbackDot.dettach(key);
        }
    }


    private IMFileManager(Context context) {

        this.mContext = context.getApplicationContext();

        mImEngine = IMEngine.getInstance(context);

        mDbManager = DbManager.getInstance(mContext);

        mNetFileTransferControl = NetFileTransferControl.getFileTransferControl();

        // mSandboxManager = new SandboxManager(mContext, LogicEngine.getInstance().getEngineParameter().appKey);

        //    mSandThreadPool = getThreadPoolExecutor();

    }

    /**
     * 获取线程池
     *
     * @return
     */
    private ThreadPoolExecutor getThreadPoolExecutor() {

        int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
        int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        int KEEP_ALIVE_SECONDS = 30;
        BlockingQueue<Runnable> sPoolWorkQueue =
                new LinkedBlockingQueue<Runnable>(128);
        ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "NetFileTransferControl #" + mCount.getAndIncrement());
            }
        };

        ThreadPoolExecutor loadThreadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory);
        loadThreadPoolExecutor.allowCoreThreadTimeOut(true);

        return loadThreadPoolExecutor;
    }


    public static IMFileManager getImFileManager(Context context) {

        if (mImFileManager == null) {
            mImFileManager = new IMFileManager(context);
        }
        return mImFileManager;
    }

    public boolean isContainTask(String type) {

        return mNetFileTransferControl.isContainsTask(type);
    }


    /**
     * 单线程的单任务下载
     *
     * @param doInitSubPackage            请求参数的封装
     * @param singNetFileTransferListener 下载的回调
     *                                    回调可以通过注册观察者回调
     */
    public void singDownLoadImFile(DoInitSubPackage doInitSubPackage, SingNetFileTransferListener singNetFileTransferListener) {
        if (doInitSubPackage.getCallback() != null) {
            registEventDot(doInitSubPackage.getCallbackKey(), doInitSubPackage.getCallback());
        }
        mNetFileTransferControl.onSingExecute(new DownLoadTask(doInitSubPackage, singNetFileTransferListener));
    }

    /**
     * 单线程单任务上传
     *
     * @param doInitSubPackage            请求参数的封装
     * @param singNetFileTransferListener 下载的回调
     *                                    回调可以通过注册观察者回调
     */
    public void singUploadImFile(DoInitSubPackage doInitSubPackage, SingNetFileTransferListener singNetFileTransferListener) {
        if (doInitSubPackage.getCallback() != null) {
            registEventDot(doInitSubPackage.getCallbackKey(), doInitSubPackage.getCallback());
        }
        mNetFileTransferControl.onSingExecute(new UploadTask(doInitSubPackage, singNetFileTransferListener));

    }

    /**
     * 取消
     *
     * @param string
     */
    public void cancel(String string) {
        mNetFileTransferControl.cancel(string);
    }

    /**
     * 写入沙箱
     *
     * @param filePath
     */
    private void writeSandBox(final String filePath) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                File file = new File(filePath);
//                String type = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
//                FileInfo fileInfo = new FileInfo(filePath, type, file.length(), String.valueOf(System.currentTimeMillis()));
//                boolean writeFile = mSandboxManager.writeFile(fileInfo, new WriteFileListener() {
//
//                    @Override
//                    public void onSuccess(String path, FileInfo fileInfo) {
//                        LogUtil.e("写入沙箱成功》》》 " + path);
//                    }
//
//                    @Override
//                    public void onFail() {
//                        LogUtil.e("写入沙箱失败》》》 " + filePath);
//                    }
//                });
//                LogUtil.e("写入沙箱失败同步结果》》 " + writeFile);
//            }
//        };
        //  mSandThreadPool.execute(runnable);
    }


    class DownLoadTask extends SingDownNetWorkTask {

        private DoInitSubPackage mDoInitSubPackage;

        private SingNetFileTransferListener singNetFileTransferListener;

        DownLoadTask(DoInitSubPackage singInitSubPackage, SingNetFileTransferListener singNetFileTransferListener) {

            this.mDoInitSubPackage = singInitSubPackage;
            this.singNetFileTransferListener = singNetFileTransferListener;
        }


        @Override
        public ResponseBody getNetResponseBody() {
            if (mFileSubPackage == null)
                return null;
            Map<String, String> parameter = mFileSubPackage.getParameter();
            if (parameter == null) {
                return null;
            }
            try {
                return mImEngine.getFileTransController().download(parameter);
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        public FileSubPackage setFileSubPackage() {
            String localPath = mDoInitSubPackage.getLocalPath();
            FileSubPackage subPackage = new FileSubPackage();
            subPackage.setStart(0);
            subPackage.setEnd(mDoInitSubPackage.getSize());
            subPackage.setTotal(mDoInitSubPackage.getSize());
            subPackage.setPos(0);
            subPackage.setSha(mDoInitSubPackage.getSha());
            subPackage.setLocalPath(localPath);
            subPackage.setNetPath(mDoInitSubPackage.getNetPath());
            String name = localPath.substring(localPath.lastIndexOf("/"), localPath.length());
            subPackage.setName(name);
            subPackage.setParameter(mDoInitSubPackage.getParameterMap());
            subPackage.setFunction(1);
            subPackage.setFileSubPackageId(mDoInitSubPackage.getIMMessageId());
            if (mDoInitSubPackage.getIMMessageId() != -1) {
                subPackage.setType(String.valueOf(mDoInitSubPackage.getIMMessageId()));
            }
            return subPackage;
        }

        @Override
        public SingNetFileTransferListener setSingNetFileTransferListener() {

            return new SingNetFileTransferListenerImpl(mDoInitSubPackage.getCallbackKey(), true, singNetFileTransferListener);
        }

    }

    class UploadTask extends SingUploadNetWorkTask {

        private DoInitSubPackage mDoInitSubPackage;

        SingNetFileTransferListener singNetFileTransferListener;

        UploadTask(DoInitSubPackage singInitSubPackage, SingNetFileTransferListener singNetFileTransferListener) {

            this.mDoInitSubPackage = singInitSubPackage;
            this.singNetFileTransferListener = singNetFileTransferListener;
        }

        @Override
        public RequestBody setRequestBody() {
            if (mFileSubPackage == null)
                return null;
            Map<String, String> parameter = mFileSubPackage.getParameter();
            if (parameter == null) {
                return null;
            }
            File file = new File(mFileSubPackage.getLocalPath());
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
            builder.addFormDataPart("senderId", parameter.get("senderId"));
            builder.addFormDataPart("receiverId", parameter.get("receiverId"));

            return builder.build();

        }

        @Override
        public String uploadFile(RequestBody requestBody) {

            if (requestBody == null) {
                return null;
            }
            try {
                return mImEngine.getFileTransController().uploadFile(requestBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public FileSubPackage setFileSubPackage() {
            String localPath = mDoInitSubPackage.getLocalPath();
            FileSubPackage subPackage = new FileSubPackage();
            subPackage.setStart(0);
            subPackage.setEnd(mDoInitSubPackage.getSize());
            subPackage.setTotal(mDoInitSubPackage.getSize());
            subPackage.setPos(0);
            subPackage.setLocalPath(localPath);
            subPackage.setNetPath(mDoInitSubPackage.getNetPath());
            String name = localPath.substring(localPath.lastIndexOf("/"), localPath.length());
            subPackage.setName(name);
            subPackage.setParameter(mDoInitSubPackage.getParameterMap());
            subPackage.setFunction(1);
            subPackage.setFileSubPackageId(mDoInitSubPackage.getIMMessageId());
            if (mDoInitSubPackage.getIMMessageId() != -1) {
                subPackage.setType(String.valueOf(mDoInitSubPackage.getIMMessageId()));
            }
            return subPackage;
        }

        @Override
        public SingNetFileTransferListener setSingNetFileTransferListener() {


            return new SingNetFileTransferListenerImpl(mDoInitSubPackage.getCallbackKey(), false, singNetFileTransferListener);
        }
    }

    class SingNetFileTransferListenerImpl implements SingNetFileTransferListener {

        SingNetFileTransferListener mSingNetFileTransferListener;

        IMMessage mImMessage;

        String key;

        boolean isDownOrUpload;

        long time = 0;

        SingNetFileTransferListenerImpl(String key, boolean isDownOrUpload, SingNetFileTransferListener singNetFileTransferListener) {
            this.mSingNetFileTransferListener = singNetFileTransferListener;
            this.key = key;
            this.isDownOrUpload = isDownOrUpload;
        }

        /**
         * 修改消息
         *
         * @param state
         * @param packages
         */
        private void updateIMMessage(String key, int state, FileSubPackage packages) {
            //非 IM 下载
            if (packages.getFileSubPackageId() == -1) {
                Message message = Message.obtain();
                message.what = state;
                message.obj = packages;
                mCallbackDot.notice(key, message);
                return;
            }
            if (mImMessage == null) {
                mImMessage = mDbManager.getIMMessages(packages.getFileSubPackageId());
            }
            if (mImMessage == null) {
                return;
            }
            IMFileInfo imFileInfo = mImMessage.getIMFileInfo();
            if (imFileInfo == null) {
                return;
            }
            imFileInfo.setStatus(state);
            //消息状态是成功的状态，不修改消息状态，只修改文件消息的状态
            if (mImMessage.getStatus() != IMMessage.STATUS_SUCCESS) {
                mImMessage.setStatus(state);
            }
            if (state == IMMessage.STATUS_SENDING) {
                imFileInfo.setPos(packages.getPos());
            } else if (state == IMMessage.STATUS_SUCCESS) {
                imFileInfo.setPos(imFileInfo.getSize());
                if (!isDownOrUpload) {
                    if (!TextUtils.isEmpty(packages.getSha())) {
                        imFileInfo.setSha(packages.getSha());
                        mImMessage.setStatus(IMMessage.STATUS_SENDING);
                    } else {
                        imFileInfo.setPos(0L);
                        imFileInfo.setStatus(IMMessage.STATUS_FAIL);
                        mImMessage.setStatus(IMMessage.STATUS_FAIL);
                    }
                } else {
                    imFileInfo.setPath(packages.getLocalPath());
                }
            } else if (state == IMMessage.STATUS_FAIL) {
                imFileInfo.setPos(0L);
                if (isDownOrUpload) {
                    imFileInfo.setPath("");
                    //失败次数加1
                    imFileInfo.setFailedCount(imFileInfo.getFailedCount() + 1);
                }
            }
            imFileInfo.update();
            mImMessage.update();
            Message message = Message.obtain();
            message.what = state;
            message.obj = mImMessage.getLocalId();
            mCallbackDot.notice(key, message);

        }

        private String getSha(String netResult) {
            if (TextUtils.isEmpty(netResult)) {
                return "";
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(netResult);
                if (jsonObject.optInt("errorCode", 0) == 1) {
                    return "";
                }
//                if (mContext.getString(R.string.im_login_again).equals(jsonObject.optString("msg", ""))) {
//                    return "";
//                }
                JSONArray array = jsonObject.optJSONArray("files");
                if (array == null || array.length() == 0) {
                    return "";
                }
                JSONObject files = array.getJSONObject(0);
                if (files == null) {
                    return "";
                }
                return files.optString("sha");

            } catch (JSONException e) {
                return "";
            }
        }

        @Override
        public void onFileTransferLoading(FileSubPackage packages) {
            if (System.currentTimeMillis() - time > 500) {
                time = System.currentTimeMillis();
                updateIMMessage(key, IMMessage.STATUS_SENDING, packages);
            }
            if (mSingNetFileTransferListener != null) {
                mSingNetFileTransferListener.onFileTransferLoading(packages);
            }

        }

        @Override
        public void onFileTransferSuccess(FileSubPackage packages) {
            //上传线程
            if (!isDownOrUpload) {
                packages.setSha(getSha(packages.getNetResult()));
            }
            updateIMMessage(key, IMMessage.STATUS_SUCCESS, packages);
            if (mSingNetFileTransferListener != null) {
                mSingNetFileTransferListener.onFileTransferSuccess(packages);
            }

        }

        @Override
        public void onFileTransferFailed(FileSubPackage packages) {
            updateIMMessage(key, IMMessage.STATUS_FAIL, packages);
            if (mSingNetFileTransferListener != null) {
                mSingNetFileTransferListener.onFileTransferFailed(packages);
            }

        }
    }

    /**
     * 单线程的单任务下载
     *
     * @param doInitSubPackage            请求参数的封装
     * @param singNetFileTransferListener 下载的回调
     *                                    回调可以通过注册观察者回调
     */
    public void singDownLoadFileByUrl(DoInitSubPackage doInitSubPackage, SingNetFileTransferListener singNetFileTransferListener) {

        mNetFileTransferControl.onSingExecute(new DownLoadByUrlTask(doInitSubPackage, singNetFileTransferListener));
    }


    class DownLoadByUrlTask extends SingDownNetWorkTask {

        private DoInitSubPackage mDoInitSubPackage;

        private SingNetFileTransferListener singNetFileTransferListener;

        DownLoadByUrlTask(DoInitSubPackage singInitSubPackage, SingNetFileTransferListener singNetFileTransferListener) {

            this.mDoInitSubPackage = singInitSubPackage;
            this.singNetFileTransferListener = singNetFileTransferListener;
        }


        @Override
        public ResponseBody getNetResponseBody() {
            if (mFileSubPackage == null)
                return null;
            String netPath = mFileSubPackage.getNetPath();
            if (TextUtils.isEmpty(netPath)) {
                return null;
            }
            try {
                return mImEngine.getFileTransController().downloads(netPath);
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        public FileSubPackage setFileSubPackage() {
            String localPath = mDoInitSubPackage.getLocalPath();
            FileSubPackage subPackage = new FileSubPackage();
            subPackage.setStart(0);
            subPackage.setTotal(mDoInitSubPackage.getSize());
            subPackage.setPos(0);
            subPackage.setLocalPath(localPath);
            subPackage.setNetPath(mDoInitSubPackage.getNetPath());
            String name = localPath.substring(localPath.lastIndexOf("/") + 1, localPath.length());
            subPackage.setName(name);
            return subPackage;
        }

        @Override
        public SingNetFileTransferListener setSingNetFileTransferListener() {

            return singNetFileTransferListener;
        }
    }


}
