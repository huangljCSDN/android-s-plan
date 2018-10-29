package com.networkengine.networkutil.process;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.networkengine.database.XDbManager;
import com.networkengine.database.table.FileRecord;
import com.networkengine.database.table.Member;
import com.networkengine.engine.LogicEngine;
import com.networkengine.entity.FileSubPackage;
import com.networkengine.networkutil.interfaces.SingNetFileTransferListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.networkengine.database.table.FileRecord.TRANSFER_TYPE_UPLOAD;

/**
 * Created by liuhao on 2017/6/9.
 * 单线程的下载，上传任务
 */

public abstract class SingNetWorkTask {

    public FileSubPackage mFileSubPackage;
    /**
     * 停止
     */
    protected boolean isStop;

    public SingNetFileTransferListener mSingNetFileTransferListener;

    private SingNetFileTransferListener mCallBackListener;

    private OnFileTransferEnd mOnFileTransferEnd;


    protected XDbManager mXDbManager;

    public abstract FileSubPackage setFileSubPackage();

    public abstract SingNetFileTransferListener setSingNetFileTransferListener();

    public abstract boolean doInBackground(PublishProgress publishProgress);

    public void setCallBackListener(SingNetFileTransferListener mCallBackListener) {
        this.mCallBackListener = mCallBackListener;
    }

    public void setXDbManager(XDbManager mXDbManager) {
        this.mXDbManager = mXDbManager;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public OnFileTransferEnd getOnFileTransferEnd() {
        return mOnFileTransferEnd;
    }

    public void setOnFileTransferEnd(OnFileTransferEnd mOnFileTransferEnd) {
        this.mOnFileTransferEnd = mOnFileTransferEnd;
    }

    /**
     * 判断本地文件是否存在
     *
     * @return
     */
    private boolean chackLocalFileIsExistsWhenDown(FileRecord fileRecord, PublishProgress publishProgress) {

        if (fileRecord == null) {
            return false;
        }
        //强制下载
        if (mFileSubPackage.isCompulsive()) {
            String localPath = fileRecord.getLocalPath();
            //构建一条下载记录
            if (!TextUtils.isEmpty(localPath)) {
                File file = new File(localPath);
                if (file.exists()) {
                    file.delete();
                }
            }
            return false;
        }
        LogicEngine instance = LogicEngine.getInstance();
        String uid = "";
        if (instance != null) {
            Member user = instance.getUser();
            if (user != null) {
                uid = user.getId();
            }
        }

        String fileRecordUid = fileRecord.getUid();
        //文件用户为空
        if (TextUtils.isEmpty(fileRecordUid)) {
            return false;
        }
        //文件记录不是当前用户的
        if (!fileRecordUid.equals(uid)) {
            return false;
        }
        String localPath = fileRecord.getLocalPath();
        //构建一条下载记录
        if (TextUtils.isEmpty(localPath)) {
            return false;
        }
        File file = new File(localPath);
        if (!file.exists()) {
            return false;
        }
        if (file.length() == 0) {
            file.delete();
            return false;
        }
        if (!localPath.equals(mFileSubPackage.getLocalPath())) {
            boolean isSuccess = copyFile(localPath, mFileSubPackage.getLocalPath(), publishProgress);
            if (!isSuccess) {
                return false;
            }
        } else {
            publishProgress.PublishProgress(file.length());
        }
        mFileSubPackage.setPos(file.length());
        mFileSubPackage.setTotal(file.length());
        mFileSubPackage.setEnd(file.length());
        mFileSubPackage.setFileRecordId(fileRecord.getId());
        return true;
    }

    /**
     * 文件复制
     *
     * @param inputPath
     * @param outPath
     * @param publishProgress
     * @return
     */
    private boolean copyFile(String inputPath, String outPath, PublishProgress publishProgress) {

        FileInputStream ins = null;
        FileOutputStream out = null;
        try {
            ins = new FileInputStream(inputPath);
            out = new FileOutputStream(outPath);
            byte[] b = new byte[4096];
            int n = 0;
            long length = 0;
            while ((n = ins.read(b)) != -1) {
                length += n;
                out.write(b, 0, n);
                publishProgress.PublishProgress(length);
            }

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {

            try {
                if (ins != null) {
                    ins.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {

            }
        }
        return true;
    }


    /**
     * 执行在子线程
     * 判断下载文件信息是否存在
     * 不存在，则重新存储
     *
     * @return
     */
    boolean doInBackgroundBefore(PublishProgress publishProgress) {
        //文件id 记录存在的操作 TODO
//        long fileRecordId = mFileSubPackage.getFileRecordId();
//        FileRecord loadFileRecord = mXDbManager.loadFileRecord(fileRecordId);
//        if (loadFileRecord != null) {
//
//        }
        if (mFileSubPackage == null) {
            return false;
        }
        //下载线程
        FileRecord fileRecord = null;

        if (this instanceof SingDownNetWorkTask && !mFileSubPackage.isCompulsive()) {
            //首先判断sha文件是否存在
            String sha = mFileSubPackage.getSha();
            //判断type
            String type = mFileSubPackage.getType();

            if (!TextUtils.isEmpty(sha)) {
                //先找寻上传的记录
                fileRecord = mXDbManager.loadFileRecordBySha(sha, mFileSubPackage.getFunction(), FileRecord.TRANSFER_TYPE_UPLOAD);
                if (chackLocalFileIsExistsWhenDown(fileRecord, publishProgress)) {
                    Log.e("hh", "根据sha找到本地文件（上传记录） 》》" + mFileSubPackage.getLocalPath());
                    return true;
                }
                //根据请求参数去寻找
                fileRecord = mXDbManager.loadFileRecordBySha(sha, mFileSubPackage.getFunction(), mFileSubPackage.getParameter());
                if (chackLocalFileIsExistsWhenDown(fileRecord, publishProgress)) {
                    Log.e("hh", "根据sha找到本地文件 》》" + mFileSubPackage.getLocalPath());
                    return true;
                }
            } else if (!TextUtils.isEmpty(type)) {
                fileRecord = mXDbManager.loadFileRecordByType(type, mFileSubPackage.getFunction());
                if (chackLocalFileIsExistsWhenDown(fileRecord, publishProgress)) {
                    Log.e("hh", "根据type找到本地文件 》》 " + mFileSubPackage.getLocalPath());
                    return true;
                }
            }
        }
        //下载上传创建新的记录
        if (fileRecord == null) {
            fileRecord = new FileRecord();
        }
        if (this instanceof SingDownNetWorkTask) {
            fileRecord.setDownOrUpload(FileRecord.TRANSFER_TYPE_DOWNLOAD);
        } else if (this instanceof SingUploadNetWorkTask) {
            fileRecord.setDownOrUpload(TRANSFER_TYPE_UPLOAD);
        }
        LogicEngine instance = LogicEngine.getInstance();
        if (instance != null) {
            Member user = instance.getUser();
            if (user != null) {
                fileRecord.setUid(user.getId());
            }
        }
        fileRecord.setFunction(mFileSubPackage.getFunction());
        fileRecord.setFunction_type(mFileSubPackage.getFunction_type());
        fileRecord.setName(mFileSubPackage.getName());
        fileRecord.setLocalPath(mFileSubPackage.getLocalPath());
        fileRecord.setNetPath(mFileSubPackage.getNetPath());
        fileRecord.setPosSize(0L);
        fileRecord.setTotalSize(mFileSubPackage.getTotal());
        fileRecord.setIsSuccess(false);
        fileRecord.setMd5(mFileSubPackage.getClientSid());
        fileRecord.setStartTime(System.currentTimeMillis());
        fileRecord.setType(mFileSubPackage.getType());
        fileRecord.setSha(mFileSubPackage.getSha());
        fileRecord.setState(FileRecord.LOADING);
        fileRecord.setParameter(new Gson().toJson(mFileSubPackage.getParameter()));
        if (mXDbManager != null) {
            long id = mXDbManager.insertOrReplaceFileRecord(fileRecord);
            Log.e("hh", "存储文件记录　id >>> " + id);
            mFileSubPackage.setFileRecordId(id);
        }
        return false;
    }

    /**
     * 主线线程
     */
    void doInBackgroundLater(boolean isSuccess) {
        if (mXDbManager == null || mFileSubPackage == null) {
            return;
        }
        FileRecord fileRecord = mXDbManager.loadFileRecord(mFileSubPackage.getFileRecordId());
        if (fileRecord == null) {
            return;
        }
        String netResult = mFileSubPackage.getNetResult();
        if (!TextUtils.isEmpty(netResult)) {
            try {
                JSONObject jsonObject = new JSONObject(netResult);
                JSONArray files = jsonObject.optJSONArray("files");
                if (files != null && files.length() > 0) {
                    JSONObject jsonObject1 = files.getJSONObject(0);
                    String sha = jsonObject1.optString("sha", "");
                    fileRecord.setSha(sha);
                }
            } catch (JSONException e) {

            }
        }
        fileRecord.setIsSuccess(isSuccess);
        fileRecord.setEndTime(System.currentTimeMillis());
        if (isSuccess) {
            fileRecord.setPosSize(mFileSubPackage.getTotal());
            fileRecord.setState(FileRecord.LOADING_SUCCESS);
        } else {
            fileRecord.setState(FileRecord.LOADING_FAILED);
        }
        mXDbManager.insertOrReplaceFileRecord(fileRecord);
    }

    /**
     * @param aBoolean
     */

    public void onPostExecute(Boolean aBoolean) {
        if (mOnFileTransferEnd != null) {
            mOnFileTransferEnd.OnFileTransferEnd(mFileSubPackage, aBoolean);
        }
        if (mCallBackListener != null) {
            if (aBoolean) {
                mCallBackListener.onFileTransferSuccess(mFileSubPackage);
            } else {
                mCallBackListener.onFileTransferFailed(mFileSubPackage);
            }
        }
        if (mSingNetFileTransferListener != null) {

            if (aBoolean) {
                mSingNetFileTransferListener.onFileTransferSuccess(mFileSubPackage);
            } else {
                mSingNetFileTransferListener.onFileTransferFailed(mFileSubPackage);
            }
        }
    }

    /**
     * @param pos
     */
    public void onProgressUpdate(long pos) {

        if (mCallBackListener != null) {
            mCallBackListener.onFileTransferLoading(mFileSubPackage);
        }
        if (mSingNetFileTransferListener != null) {
            mSingNetFileTransferListener.onFileTransferLoading(mFileSubPackage);
        }
    }


    public interface PublishProgress {
        void PublishProgress(long size);
    }

    public interface OnFileTransferEnd {
        void OnFileTransferEnd(FileSubPackage fileSubPackage, boolean isSuccess);
    }


}
