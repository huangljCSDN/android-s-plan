package com.networkengine.database.entity;

/**
 * 作者：created by huanglingjun on 2018/10/31
 * 描述：
 */
public class FileInfo {
    public String name;
    public String sha;
    public String size;
    public String status;
    public String type;

    public FileInfo() {
    }

    public FileInfo(String name, String sha, String size, String status, String type) {
        this.name = name;
        this.sha = sha;
        this.size = size;
        this.status = status;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
