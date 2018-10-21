package com.markLove.Xplan.bean;

/**
 * 作者：created by huanglingjun on 2018/10/18
 * 描述：
 */
public class MerchantBean {

    public long id;
    public String shopName;
    public String shopCount;
    public String shopBgUrl;

    public MerchantBean(long id, String shopName, String shopCount, String shopBgUrl) {
        this.id = id;
        this.shopName = shopName;
        this.shopCount = shopCount;
        this.shopBgUrl = shopBgUrl;
    }
}
