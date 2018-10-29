package com.networkengine.entity;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuhao on 2017/4/27.
 */

public class FileSubPackage {

    /**
     * 回调的type
     */
    private String callBackType;

    /**
     * 文件数据库记录id
     */
    private long fileRecordId;
    /**
     * 标示的id
     */
    private long fileSubPackageId = -1;

    /**
     * 强制下载
     * <p>
     * 默认为 false,不强制，会去查找数据库文件记录，找到则不会执行下载任务
     * 设置为ture ,则会执行强制下载，但是，数据库中的下载记录，还是只有一条
     */
    private boolean isCompulsive;
    /**
     * 文件所在功能
     */
    private int function;
    /**
     * 文件功能标示
     */
    private String function_type;

    /**
     * 文件的标示如果没有设置，以文件的网络地址与本地地址的拼接值为准
     */
    private String type;
    /**
     * sha 值
     */
    private String sha;
    /**
     * 分包id
     */
    private long id;
    /**
     * 网络地址
     */
    private String netPath;
    /**
     * 网络参数
     */
    private Map<String, String> parameter;
    /**
     * 服务器返回的json
     */
    private String netResult;

    /**
     * 本地地址
     */
    private String localPath;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件总大小
     */
    private long total;

    /**
     * 分包的大小
     */
    private long size;

    /**
     * 开始位置
     */
    private long start;
    /**
     * 结束位置
     */
    private long end;
    /**
     * 已下载或者上传的大小
     */
    private long pos;

    /**
     * 客户端生成的MD5 标示
     */
    private String clientSid;
    /**
     * 分包的当前编号
     */
    private int shaNum;
    /**
     * 分包数量
     */
    private int shaCount;
    /**
     * 分包的零时文件的地址
     */
    private String tempFile;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParameter() {
        if (parameter == null) {
            return new HashMap<>();
        }
        return parameter;
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    public String getNetResult() {
        return netResult;
    }

    public void setNetResult(String netResult) {
        this.netResult = netResult;
    }

    public String getClientSid() {
        return clientSid;
    }

    public void setClientSid(String clientSid) {
        this.clientSid = clientSid;
    }

    public int getShaNum() {
        return shaNum;
    }

    public void setShaNum(int shaNum) {
        this.shaNum = shaNum;
    }

    public int getShaCount() {
        return shaCount;
    }

    public void setShaCount(int shaCount) {
        this.shaCount = shaCount;
    }

    public String getTempFile() {
        return tempFile;
    }

    public void setTempFile(String tempFile) {
        this.tempFile = tempFile;
    }

    public String getType() {
        if (TextUtils.isEmpty(type)) {
            return String.format("%s%s", netPath, localPath);
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public long getFileRecordId() {
        return fileRecordId;
    }

    public void setFileRecordId(long fileRecordId) {
        this.fileRecordId = fileRecordId;
    }

    public boolean isCompulsive() {
        return isCompulsive;
    }

    public void setCompulsive(boolean compulsive) {
        isCompulsive = compulsive;
    }

    public int getFunction() {
        return function;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public String getFunction_type() {
        return function_type;
    }

    public void setFunction_type(String function_type) {
        this.function_type = function_type;
    }

    public long getFileSubPackageId() {
        return fileSubPackageId;
    }

    public void setFileSubPackageId(long fileSubPackageId) {
        this.fileSubPackageId = fileSubPackageId;
    }

    public String getCallBackType() {
        return callBackType;
    }

    public void setCallBackType(String callBackType) {
        this.callBackType = callBackType;
    }

    @Override
    public String toString() {
        return "FileSubPackage{" +
                "fileRecordId=" + fileRecordId +
                ", type='" + type + '\'' +
                ", sha='" + sha + '\'' +
                ", id=" + id +
                ", netPath='" + netPath + '\'' +
                ", parameter=" + parameter +
                ", netResult='" + netResult + '\'' +
                ", localPath='" + localPath + '\'' +
                ", name='" + name + '\'' +
                ", total=" + total +
                ", size=" + size +
                ", start=" + start +
                ", end=" + end +
                ", pos=" + pos +
                ", clientSid='" + clientSid + '\'' +
                ", shaNum=" + shaNum +
                ", shaCount=" + shaCount +
                ", tempFile='" + tempFile + '\'' +
                '}';
    }

}
