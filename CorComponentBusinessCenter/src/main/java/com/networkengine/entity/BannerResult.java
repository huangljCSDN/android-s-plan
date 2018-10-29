package com.networkengine.entity;

import java.util.List;

/**
 * Created by pengpeng on 17/6/24.
 */

public class BannerResult {

    private List<BannerInfoEntity> list;

    public List<BannerInfoEntity> getList() {
        return list;
    }

    public void setList(List<BannerInfoEntity> list) {
        this.list = list;
    }

    public static class BannerInfoEntity{
        private String imgurl;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
