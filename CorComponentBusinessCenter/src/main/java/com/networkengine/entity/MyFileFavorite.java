package com.networkengine.entity;

/**
 * Created by liuhao on 2018/5/3.
 */

public class MyFileFavorite {

    private Content content;
    private long createDatetime;
    private int favoritesId;
    private String favoritesType;
    private String source;
    private long updateDatetime;
    private String userId;
    private String userName;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public int getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(int favoritesId) {
        this.favoritesId = favoritesId;
    }

    public String getFavoritesType() {
        return favoritesType;
    }

    public void setFavoritesType(String favoritesType) {
        this.favoritesType = favoritesType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(long updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static class Content {

        private String addressName;
        private String fileName;
        private String fileSize;
        private String sha;

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
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

        public String getSha() {
            return sha;
        }

        public void setSha(String sha) {
            this.sha = sha;
        }
    }

}
