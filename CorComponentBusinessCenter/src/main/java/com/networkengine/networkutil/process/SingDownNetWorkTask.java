package com.networkengine.networkutil.process;


import android.util.Log;

import com.networkengine.engine.LogicEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * Created by liuhao on 2017/6/9.
 */

public abstract class SingDownNetWorkTask extends SingNetWorkTask {

    /**
     * 默认的方法，子类可以重写该方法
     *
     * @return
     */
    public ResponseBody getNetResponseBody() {
        if (mFileSubPackage == null) {
            return null;
        }
        try {
            return LogicEngine.getInstance().getFileTransController().downloads(mFileSubPackage.getNetPath());
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean doInBackground(PublishProgress publishProgress) {
        if (mFileSubPackage == null)
            return false;


        ResponseBody netResponseBody = getNetResponseBody();


        return ioWrite(netResponseBody, publishProgress);

    }

    private boolean ioWrite(ResponseBody netResponseBody, PublishProgress publishProgress) {
        if (netResponseBody == null)
            return false;
        OutputStream outputStream = null;
        InputStream inputStream = null;

        MediaType mediaType = netResponseBody.contentType();
        if (mediaType == null) {
            return false;
        }
        if ("application/json;charset=UTF-8".equals(mediaType.toString())) {
            return false;
        }
        if (("text/html;charset=UTF-8").equals(mediaType.toString())) {

            return false;
        }
        //设置写入位子
        File file = new File(mFileSubPackage.getLocalPath());
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
            inputStream = netResponseBody.byteStream();
            long length = netResponseBody.contentLength();
            mFileSubPackage.setEnd(length);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[2048];
            int len = -1;
            int pos = 0;
            //读取数据
            while ((len = inputStream.read(buffer)) != -1) {

                if (isStop) {
                    netResponseBody.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    file.delete();
                    return false;
                }

                //写入文件
                outputStream.write(buffer, 0, len);
                pos += len;
                mFileSubPackage.setPos(pos);
                publishProgress.PublishProgress(pos);

            }

            outputStream.flush();

        } catch (FileNotFoundException e) {
            file.delete();
            Log.e("hh", "文件下载异常FileNotFoundException >> " + e.getMessage());
            return false;
        } catch (IOException e) {
            file.delete();
            Log.e("hh", "文件下载异常IOException >> " + e.getMessage());
            return false;
        } catch (IllegalStateException e) {
            file.delete();
            Log.e("hh", "文件下载异常IllegalStateException >> " + e.getMessage());
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


}
