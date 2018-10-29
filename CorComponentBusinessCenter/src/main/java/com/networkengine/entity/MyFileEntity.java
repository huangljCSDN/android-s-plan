package com.networkengine.entity;

/**
 * Created by liuhao on 2018/5/3.
 */

public class MyFileEntity {


    /**
     * 下载的id
     */
    private String myDownloadId;
    /**
     * 上传的id
     */
    private String attachmentId;

    private String type;
    private int pageSize;
    private String filename;

    public String getMyDownloadId() {
        return myDownloadId;
    }

    public void setMyDownloadId(String myDownloadId) {
        this.myDownloadId = myDownloadId;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
