package com.networkengine.entity;

import android.text.TextUtils;

/**
 * Created by lvxy o
 */
public class CollectContent {

    /**
     * 文本内容
     */
    private String text;

    /**
     * 链接或网络图片路径
     */
    private String url;
    /**
     * 对应fileInfo的name，文件名称
     */
    private String fileName;
    /**
     * 对应fileInfo的size，文件大小
     */
    private String fileSize;
    /**
     * 对应fileInfo的time，视频或语音的长度
     */
    private String time;
    /**
     * 对应fileInfo的sha
     */
    private String sha;

    /**
     * 位置
     */
    private String latitude;
    private String longitude;
    private String address;
    private String addressName = "";
    /**
     * 标题
     */
    private String title;
    private String tiltle;
    /**
     * 链接的描述内容
     */
    private String describe;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getTitle() {

        return TextUtils.isEmpty(title) ? tiltle : title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.tiltle = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

}
