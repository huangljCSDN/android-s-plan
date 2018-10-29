package com.networkengine.database.table;

import android.support.annotation.IntDef;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuhao on 2018/3/15.
 * 文件传输记录表
 */
@Entity(nameInDb = "table_file_record")
public class FileRecord {

    public static final int TRANSFER_TYPE_DOWNLOAD = 0;// 下载
    public static final int TRANSFER_TYPE_UPLOAD = 1;// 上传

    public static final int LOADING_FAILED = -1;//上传下载失败
    public static final int LOAD_INITIALIZE = 0;//初始状态
    public static final int LOADING = 1;//上传下载中
    public static final int LOADING_PAUSE = 2;//暂停
    public static final int LOADING_SUCCESS = 3;//上传下载失败成功

    public static final int FUNCTION_DEFAULT = 0;//默认

    public static final int FUNCTION_IM = 1;//im或者收藏

    public static final int FUNCTION_WEB_APP = 2;//轻应用

    public static final int FUNCTION_ORIGINAL_POWER = 3;//原声能力

    public static final int FUNCTION_EMOGI = 4;//表情包

    public static final int FUNCTION_SKIN = 5;//皮肤工能


    @IntDef({TRANSFER_TYPE_DOWNLOAD, TRANSFER_TYPE_UPLOAD})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    public @interface TransferType {
    }

    @IntDef({LOADING_FAILED, LOAD_INITIALIZE, LOADING, LOADING_PAUSE, LOADING_SUCCESS})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    public @interface State {
    }

    @IntDef({FUNCTION_DEFAULT, FUNCTION_IM, FUNCTION_WEB_APP, FUNCTION_ORIGINAL_POWER, FUNCTION_EMOGI,FUNCTION_SKIN})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    public @interface FunctionFlag {

    }

    @Id(autoincrement = true)
    private Long id;

    /**
     * 上传还是下载
     */
    @TransferType
    private int downOrUpload;

    @State
    private int state;

    /**
     * 文件所属用户
     */
    private String uid;
    /**
     * 下载或者上传成功
     */
    private boolean isSuccess;
    /**
     * 文件名
     */
    private String name;

    /**
     * 文件本地地址
     */
    private String localPath;
    /**
     * 文件网络地址
     */
    private String netPath;
    /**
     * 文件标示符
     */
    private String sha;
    /**
     * 文件下载的参数，对于同一sha值文件的不同下载参数下的不同文件
     */
    private String parameter;
    /**
     * 文件标示2 默认存储为文件的网诺地址与本地地址的拼接
     */
    private String type;
    /**
     * 文件md5 值
     */
    private String md5;
    /**
     * 文件开始上传下载的时间
     */
    private Long startTime;

    /**
     * 文件结束上传下载的时间
     */
    private Long endTime;
    /**
     * 文件总大小
     */
    private Long totalSize;
    /**
     * 文件当前大小
     */
    private Long posSize;
    /**
     * 文件记录功能
     * im
     */
    @FunctionFlag
    private int function;
    /**
     * 功能标记
     */
    private String function_type;
    @Generated(hash = 1164440050)
    public FileRecord(Long id, int downOrUpload, int state, String uid, boolean isSuccess, String name, String localPath,
            String netPath, String sha, String parameter, String type, String md5, Long startTime, Long endTime,
            Long totalSize, Long posSize, int function, String function_type) {
        this.id = id;
        this.downOrUpload = downOrUpload;
        this.state = state;
        this.uid = uid;
        this.isSuccess = isSuccess;
        this.name = name;
        this.localPath = localPath;
        this.netPath = netPath;
        this.sha = sha;
        this.parameter = parameter;
        this.type = type;
        this.md5 = md5;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalSize = totalSize;
        this.posSize = posSize;
        this.function = function;
        this.function_type = function_type;
    }
    @Generated(hash = 2032696706)
    public FileRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getDownOrUpload() {
        return this.downOrUpload;
    }
    public void setDownOrUpload(int downOrUpload) {
        this.downOrUpload = downOrUpload;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public boolean getIsSuccess() {
        return this.isSuccess;
    }
    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocalPath() {
        return this.localPath;
    }
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    public String getNetPath() {
        return this.netPath;
    }
    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }
    public String getSha() {
        return this.sha;
    }
    public void setSha(String sha) {
        this.sha = sha;
    }
    public String getParameter() {
        return this.parameter;
    }
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMd5() {
        return this.md5;
    }
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    public Long getStartTime() {
        return this.startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    public Long getEndTime() {
        return this.endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
    public Long getTotalSize() {
        return this.totalSize;
    }
    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }
    public Long getPosSize() {
        return this.posSize;
    }
    public void setPosSize(Long posSize) {
        this.posSize = posSize;
    }
    public int getFunction() {
        return this.function;
    }
    public void setFunction(int function) {
        this.function = function;
    }
    public String getFunction_type() {
        return this.function_type;
    }
    public void setFunction_type(String function_type) {
        this.function_type = function_type;
    }


}
