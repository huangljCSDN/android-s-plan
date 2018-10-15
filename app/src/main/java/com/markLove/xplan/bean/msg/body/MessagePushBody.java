package com.markLove.xplan.bean.msg.body;

/**
 * Created by Administrator on 2018/4/9.
 */

public class MessagePushBody extends MessageBody{
    private String msg;
    private String title;
    private String linkUrl;
    private int goWay;
    private String id;
    private String imagePath;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getGoWay() {
        return goWay;
    }

    public void setGoWay(int goWay) {
        this.goWay = goWay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
