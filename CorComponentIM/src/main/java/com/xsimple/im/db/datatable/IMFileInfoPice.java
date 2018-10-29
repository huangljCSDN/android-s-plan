package com.xsimple.im.db.datatable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 线程信息
 * Created by liuhao on 2017/4/5.
 */
@Entity(nameInDb = "im_file_thread_record")
public class IMFileInfoPice {
    @Id(autoincrement = true)
    private Long pid;

    /**
     * 文件id
     */
    private Long fid;

    /**
     * 文件sha值
     */
    private String sha;
    /**
     * 开始时的进度
     */
    private long start;
    /**
     * 应该结束时的进度
     */
    private long end;
    /**
     * 分包的大小
     */
    private long size;

    /**
     * 当前子包的编号  断点续传
     * 从1开始
     */
    private int shaNum;
    /**
     * 子包的的数量  断点续传
     */
    private int shaCount;
    /**
     * 断点续传的md5  文件的以为标示
     */
    private String clientSid;

    /**
     * 零时文件的地址
     */
    private String tempFile;


    /**
     * 实际结束时的进度
     */
    private long finished;




    @Generated(hash = 1481857995)
    public IMFileInfoPice(Long pid, Long fid, String sha, long start, long end,
            long size, int shaNum, int shaCount, String clientSid, String tempFile,
            long finished) {
        this.pid = pid;
        this.fid = fid;
        this.sha = sha;
        this.start = start;
        this.end = end;
        this.size = size;
        this.shaNum = shaNum;
        this.shaCount = shaCount;
        this.clientSid = clientSid;
        this.tempFile = tempFile;
        this.finished = finished;
    }


    @Generated(hash = 2137730771)
    public IMFileInfoPice() {
    }




    public Long getPid() {
        return this.pid;
    }


    public void setPid(Long pid) {
        this.pid = pid;
    }


    public Long getFid() {
        return this.fid;
    }


    public void setFid(Long fid) {
        this.fid = fid;
    }


    public String getSha() {
        return this.sha;
    }


    public void setSha(String sha) {
        this.sha = sha;
    }


    public long getStart() {
        return this.start;
    }


    public void setStart(long start) {
        this.start = start;
    }


    public long getEnd() {
        return this.end;
    }


    public void setEnd(long end) {
        this.end = end;
    }


    public long getSize() {
        return this.size;
    }


    public void setSize(long size) {
        this.size = size;
    }


    public int getShaNum() {
        return this.shaNum;
    }


    public void setShaNum(int shaNum) {
        this.shaNum = shaNum;
    }


    public int getShaCount() {
        return this.shaCount;
    }


    public void setShaCount(int shaCount) {
        this.shaCount = shaCount;
    }


    public String getClientSid() {
        return this.clientSid;
    }


    public void setClientSid(String clientSid) {
        this.clientSid = clientSid;
    }


    public String getTempFile() {
        return this.tempFile;
    }


    public void setTempFile(String tempFile) {
        this.tempFile = tempFile;
    }


    public long getFinished() {
        return this.finished;
    }


    public void setFinished(long finished) {
        this.finished = finished;
    }







   

}
