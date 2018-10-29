package com.networkengine.entity;

/**
 * Created by liuhao on 2018/6/5.
 */

public class RequestFavouriteParams {
    /**
     * 收藏的id
     */
    private String favoriteId;

    /**
     * text 文本 1L
     * img 图片 2L
     * audio 语音 3L
     * video 视频 4L
     * location 位置 5L
     * file 文件
     * link 链接
     */

    private String favoritesType;

    private String favoritesUserId;
    /**
     * 收藏的分页
     */
    private int pageNo;
    /**
     *
     */
    private int pageSize;

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public void setFavoriteType(String favoriteType) {
        this.favoritesType = favoriteType;
    }

    public void setFavoritesUserId(String favoritesUserId) {
        this.favoritesUserId = favoritesUserId;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
