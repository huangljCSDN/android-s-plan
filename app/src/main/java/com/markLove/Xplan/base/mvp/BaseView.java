package com.markLove.Xplan.base.mvp;

/**
 * Created by huanglingjun on 2018/5/11.
 */

public interface BaseView {
    void showLoading();
    void hideLoading();
    void showTokenExpiredDialog();
    void showError(String error);
}
