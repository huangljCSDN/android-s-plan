package com.networkengine.entity;

import java.util.List;

/**
 * Created by liuhao on 2018/4/20.
 */

public class GroupFile {

    private List<FileListBean> fileList;

    public List<FileListBean> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileListBean> fileList) {
        this.fileList = fileList;
    }

    public static class FileListBean {
        /**
         * extension : JPG
         * fileName : IMG_3004.JPG
         * filePath : 59f0322c21
         * fileSize : 60654
         * id : 1
         * sender : 蔡雪
         * senderId : 蔡雪
         * senderTime : 1524039673835
         * validityTime : 2078457856
         */

        private String extension;
        private String fileName;
        private String filePath;
        private long fileSize;
        private int id;
        private String sender;
        private String senderId;
        private long senderTime;
        private long validityTime;

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public long getSenderTime() {
            return senderTime;
        }

        public void setSenderTime(long senderTime) {
            this.senderTime = senderTime;
        }

        public long getValidityTime() {
            return validityTime;
        }

        public void setValidityTime(long validityTime) {
            this.validityTime = validityTime;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }
    }
}
