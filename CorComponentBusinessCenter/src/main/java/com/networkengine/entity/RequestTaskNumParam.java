package com.networkengine.entity;

/**
 * Created by pengpeng on 16/12/8.
 */

public class RequestTaskNumParam {

    private String url;

    private String httpMethod;

    public String getUrl() {
        return url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
