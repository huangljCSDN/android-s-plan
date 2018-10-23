package com.markLove.Xplan.bean;

public class BaseBean<T> {
    public int Status;
    public String Msg;
    public T Data;

    @Override
    public String toString() {
        return "BaseBean{" +
                "Status=" + Status +
                ", Msg='" + Msg + '\'' +
                ", Data=" + Data +
                '}';
    }
}
