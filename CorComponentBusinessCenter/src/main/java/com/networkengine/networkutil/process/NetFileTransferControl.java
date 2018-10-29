package com.networkengine.networkutil.process;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.networkengine.database.XDbManager;
import com.networkengine.entity.FileSubPackage;
import com.networkengine.mqtt.SubjectDot;
import com.networkengine.networkutil.interfaces.SingNetFileTransferListener;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by liuhao on 2017/4/27.
 * <p>
 * 默认的上传方法是 FileTransApiService 中的downloadFiles 方法和uploadFiles 方法
 */

public class NetFileTransferControl implements SingNetFileTransferListener {

    public static final int STATE_LOADING = 0;

    public static final int STATE_SUCCESS = 1;

    public static final int STATE_FAILED = 2;

    private Map<String, SingNetWorkTask> mSingTaskMap = new HashMap<>();

    private ExecutorService mFileThreadPool;


    private static NetFileTransferControl mSingleton;

    private Context mContext;

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

    private SubjectDot<String, Handler.Callback, Message> mCallbackDot = new SubjectDot<String, Handler.Callback, Message>() {
        @Override
        public void execute(Handler.Callback callback, Message msgs) {
            callback.handleMessage(msgs);
        }
    };

    public SubjectDot<String, Handler.Callback, Message> getCallbackDot() {
        return mCallbackDot;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public static NetFileTransferControl getFileTransferControl() {
        if (mSingleton == null) {
            mSingleton = new NetFileTransferControl();
        }
        return mSingleton;
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

    /**
     * 上传下载任务唯一入口
     *
     * @param singNetWorkTask
     */
    public synchronized void onSingExecute(final SingNetWorkTask singNetWorkTask) {
        if (singNetWorkTask == null) {
            return;
        }
        if (mFileThreadPool == null) {
            mFileThreadPool = getThreadPoolExecutor();
        }
        singNetWorkTask.setXDbManager(XDbManager.getInstance(mContext));
        singNetWorkTask.setOnFileTransferEnd(new SingNetWorkTask.OnFileTransferEnd() {
            @Override
            public void OnFileTransferEnd(FileSubPackage fileSubPackage, boolean isSuccess) {

                onRemove(fileSubPackage.getType());
            }
        });
        singNetWorkTask.setCallBackListener(this);
        SingNetWorkAsyncTask singTsak = new SingNetWorkAsyncTask(singNetWorkTask);
        singTsak.executeOnExecutor(mFileThreadPool);

        if (null != singNetWorkTask.mFileSubPackage) {
            mSingTaskMap.put(singNetWorkTask.mFileSubPackage.getType(), singNetWorkTask);
        }
    }

    /**
     * 下载
     */
    public synchronized void onDownload(final String url, final String localPath, final SingNetFileTransferListener singNetFileTransferListener) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(localPath)) {
            return;
        }
        onSingExecute(new SingDownNetWorkTask() {

            @Override
            public FileSubPackage setFileSubPackage() {
                FileSubPackage fileSubPackage = new FileSubPackage();
                fileSubPackage.setLocalPath(localPath);
                fileSubPackage.setNetPath(url);
                return fileSubPackage;
            }

            @Override
            public SingNetFileTransferListener setSingNetFileTransferListener() {
                return singNetFileTransferListener;
            }
        });
    }

    /**
     * 上传
     */
    public synchronized void onUpload(final String url, final String localPath, final Map<String, String> part, final String type, final SingNetFileTransferListener singNetFileTransferListener) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(localPath)) {
            return;
        }
        final File file = new File(localPath);
        if (!file.exists()) {
            return;
        }
        onSingExecute(new SingUploadNetWorkTask() {

            @Override
            public RequestBody setRequestBody() {

                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                builder.addFormDataPart(TextUtils.isEmpty(type) ? "file" : type, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                if (part != null && !part.isEmpty()) {
                    Set<Map.Entry<String, String>> entries = part.entrySet();
                    Iterator<Map.Entry<String, String>> iterator = entries.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> next = iterator.next();
                        String key = next.getKey();
                        String value = next.getValue();
                        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                            builder.addFormDataPart(key, value);
                        }
                    }
                }
                return builder.build();
            }

            @Override
            public FileSubPackage setFileSubPackage() {
                FileSubPackage fileSubPackage = new FileSubPackage();
                fileSubPackage.setLocalPath(localPath);
                fileSubPackage.setNetPath(url);
                return fileSubPackage;
            }

            @Override
            public SingNetFileTransferListener setSingNetFileTransferListener() {
                return singNetFileTransferListener;
            }
        });

    }

    /**
     * 判断任务是否存在
     *
     * @param type
     * @return
     */
    public boolean isContainsTask(String type) {
        if (TextUtils.isEmpty(type)) {
            return false;
        }
        return mSingTaskMap.containsKey(type);
    }

    /**
     * 移除
     *
     * @param string
     */
    public void onRemove(String string) {
        if (TextUtils.isEmpty(string))
            return;
        if (mSingTaskMap.containsKey(string)) {
            mSingTaskMap.remove(string);
        }
    }

    /**
     * 停止上传下载
     *
     * @param string
     */
    public void cancel(String string) {
        if (TextUtils.isEmpty(string))
            return;
        if (mSingTaskMap.containsKey(string)) {
            SingNetWorkTask singNetWorkTask = mSingTaskMap.remove(string);
            singNetWorkTask.setStop(true);
        }
    }


    /**
     * 暂停
     *
     * @param string
     */
    public void onPause(String string) {


    }


    @Override
    public void onFileTransferLoading(FileSubPackage packages) {
        if (!TextUtils.isEmpty(packages.getCallBackType())) {
            Message message = Message.obtain();
            message.what = STATE_LOADING;
            message.obj = packages;
            mCallbackDot.notice(packages.getCallBackType(), message);
        }

    }

    @Override
    public void onFileTransferSuccess(FileSubPackage packages) {
        if (!TextUtils.isEmpty(packages.getCallBackType())) {
            Message message = Message.obtain();
            message.what = STATE_SUCCESS;
            message.obj = packages;
            mCallbackDot.notice(packages.getCallBackType(), message);
        }
    }

    @Override
    public void onFileTransferFailed(FileSubPackage packages) {
        if (!TextUtils.isEmpty(packages.getCallBackType())) {
            Message message = Message.obtain();
            message.what = STATE_FAILED;
            message.obj = packages;
            mCallbackDot.notice(packages.getCallBackType(), message);
        }
    }
}
