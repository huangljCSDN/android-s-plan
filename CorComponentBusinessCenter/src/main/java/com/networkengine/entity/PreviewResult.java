package com.networkengine.entity;

/**
 * Created by liuhao on 2017/3/2.
 * 文件预览实体类
 */

public class PreviewResult {

    /**
     * 被预览文件服务器返回的HTMl地址
     */
    private String htmlUrl;
    /**
     * 服务器是否存在该文件，开发者调试信息
     */
    private String description;
    /**
     * 服务器返回状态
     * success 成功
     * error 失败
     */
    private String status;

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
