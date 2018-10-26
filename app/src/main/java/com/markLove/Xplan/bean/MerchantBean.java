package com.markLove.Xplan.bean;

/**
 * 作者：created by huanglingjun on 2018/10/18
 * 描述：
 */
public class MerchantBean {

    /**
     * groupCount : 3
     * distance : 4242.486976392779
     * merchantId : 22553
     * shopName : 测是不是有点多，看看一共有多少个日子呢
     */
    private int groupCount;
    private double distance;
    private int merchantId;
    private String shopName;
    private String logoUrl;

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public double getDistance() {
        return distance;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public String getShopName() {
        return shopName;
    }

    @Override
    public String toString() {
        return "MerchantBean{" +
                "groupCount=" + groupCount +
                ", distance=" + distance +
                ", merchantId=" + merchantId +
                ", shopName='" + shopName + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }
}
