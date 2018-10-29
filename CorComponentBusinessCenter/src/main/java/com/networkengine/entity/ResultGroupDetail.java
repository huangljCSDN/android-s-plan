package com.networkengine.entity;

/**
 * Description：群组&讨论组详情结果
 */
public class ResultGroupDetail {


    /**
     * id : 37
     * name : 修改讨论组V4
     * img :
     * type : 2
     * createTime : 1527478613099
     * updateTime : 1527478613099
     * affice :
     * publishDate :
     * importantFlag : 0
     */

    private int id;
    private String name;
    private String img;
    private int type;
    private long createTime;
    private long updateTime;
    private String affice;
    private String publishDate;
    private int importantFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getAffice() {
        return affice;
    }

    public void setAffice(String affice) {
        this.affice = affice;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getImportantFlag() {
        return importantFlag;
    }

    public void setImportantFlag(int importantFlag) {
        this.importantFlag = importantFlag;
    }
}
