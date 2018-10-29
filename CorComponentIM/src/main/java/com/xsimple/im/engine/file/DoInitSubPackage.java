package com.xsimple.im.engine.file;

import android.os.Handler;

import java.util.Map;

/**
 * Created by liuhao on 2017/9/29.
 */

public class DoInitSubPackage {

    private long iMMessageId = -1;

    private long size;

    private String netPath;

    private String localPath;

    //文件标示
    private String sha;

    private Map<String, String> parameterMap;

    private String mCallbackKey;

    private Handler.Callback mCallback;

    public long getSize() {
        return size;
    }

    public String getNetPath() {
        return netPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public DoInitSubPackage setSize(long size) {
        this.size = size;
        return this;
    }

    public DoInitSubPackage setNetPath(String netPath) {
        this.netPath = netPath;
        return this;
    }

    public DoInitSubPackage setLocalPath(String localPath) {
        this.localPath = localPath;
        return this;
    }

    public DoInitSubPackage setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
        return this;
    }

    public String getSha() {
        return sha;
    }

    public DoInitSubPackage setSha(String sha) {
        this.sha = sha;
        return this;
    }

    public long getIMMessageId() {
        return iMMessageId;
    }

    public DoInitSubPackage setIMMessageId(long iMMessageId) {
        this.iMMessageId = iMMessageId;
        return this;
    }

    public Handler.Callback getCallback() {
        return mCallback;
    }

    public DoInitSubPackage setCallback(Handler.Callback mCallback) {
        this.mCallback = mCallback;
        return this;
    }

    public String getCallbackKey() {
        return mCallbackKey;
    }

    public DoInitSubPackage setCallbackKey(String mCallbackKey) {
        this.mCallbackKey = mCallbackKey;
        return this;
    }
}


