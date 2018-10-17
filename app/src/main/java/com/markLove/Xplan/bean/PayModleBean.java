package com.markLove.Xplan.bean;

/**
 * Created by Administrator on 2017/11/28.
 */

public class PayModleBean {
    private String name;
    private int imgID;
    private int id;
    private boolean isSelect=false;

    public PayModleBean(int id, String name, int imgID) {
        this.name = name;
        this.imgID = imgID;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
    public PayModleBean setSelectd(boolean select) {
        isSelect = select;
        return this;
    }
}
