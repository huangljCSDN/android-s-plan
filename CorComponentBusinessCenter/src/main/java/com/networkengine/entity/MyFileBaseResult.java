package com.networkengine.entity;

import java.util.List;

/**
 * Created by liuhao on 2018/5/3.
 */

public class MyFileBaseResult<T> {

    private boolean status;
    private int count;
    private List<T> files;
    private List<T> list;

    public List<T> getList() {

        return (list != null && !list.isEmpty()) ? list : files;
    }


    public boolean isStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }

}
