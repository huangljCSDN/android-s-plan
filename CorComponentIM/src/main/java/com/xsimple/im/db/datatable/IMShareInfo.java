package com.xsimple.im.db.datatable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lXy_pro on 17/6/12.
 */

@Entity(nameInDb = "im_msg_shareinfo_new")
public class IMShareInfo{


@Id(autoincrement = true)
private Long sId;

/**
 * 链接缩略图
 */
private String imgUrl;
/**
 * 链接标题
 */
private String title;
/**
 * 链接描述
 */
private String content;
/**
 * 链接网址
 */
private String url;
@Generated(hash = 1520256771)
public IMShareInfo(Long sId, String imgUrl, String title, String content,
        String url) {
    this.sId = sId;
    this.imgUrl = imgUrl;
    this.title = title;
    this.content = content;
    this.url = url;
}
@Generated(hash = 1453435174)
public IMShareInfo() {
}
public String getImgUrl() {
    return this.imgUrl;
}
public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
}
public String getTitle() {
    return this.title;
}
public void setTitle(String title) {
    this.title = title;
}
public String getContent() {
    return this.content;
}
public void setContent(String content) {
    this.content = content;
}
public String getUrl() {
    return this.url;
}
public void setUrl(String url) {
    this.url = url;
}
public Long getSId() {
    return this.sId;
}
public void setSId(Long sId) {
    this.sId = sId;
}
        }
