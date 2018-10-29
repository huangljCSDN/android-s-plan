package com.networkengine.entity;

/**
 * author panxiaoan
 * date 2017/4/5.
 */

public class RequestDisturbStateParam {
    public static final int DISTURB_TYPE_OVERALL = 1;
    public static final int DISTURB_TYPE_GROUP = 2;
    public static final int DISTURB_TYPE_PERSONAL = 3;
    public static final int DISTURB_TYPE_LIGHT_APP = 5;
    private int nonDisturbType;
    private String nonDisturbKey;

    public int getNonDisturbType() {
        return nonDisturbType;
    }

    public void setNonDisturbType(int nonDisturbType) {
        this.nonDisturbType = nonDisturbType;
    }


    public String getNonDisturbKey() {
        return nonDisturbKey;
    }

    public void setNonDisturbKey(String nonDisturbKey) {
        this.nonDisturbKey = nonDisturbKey;
    }
}
