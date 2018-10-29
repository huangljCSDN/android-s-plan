package com.xsimple.im.event;

import com.networkengine.event.BaseEventBusAction;

/**
 * Created by liuhao on 2018/5/4.
 */

public class UpdateMediaResourceEvent extends BaseEventBusAction {

    private String sha;

    private String path;

    private String tag;

    public UpdateMediaResourceEvent(String sha, String path, String tag) {
        this.sha = sha;
        this.path = path;
        this.tag = tag;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
