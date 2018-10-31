package com.markLove.Xplan.bean;

/**
 * 作者：created by huanglingjun on 2018/10/31
 * 描述：
 */
public class ListBean<T> {
    public T list;

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListBean{" +
                "list=" + list +
                '}';
    }
}
