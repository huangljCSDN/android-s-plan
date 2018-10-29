package com.networkengine.event;

/**
 * Created by liuhao on 2018/4/10.
 * 设置当前的tab页面
 */

public class UpdateMainCurrentTab extends BaseEventBusAction {

    private int mTabIndex;

    public UpdateMainCurrentTab(int tabIndex) {
        this.mTabIndex = tabIndex;
    }

    public int getTabIndex() {
        return mTabIndex;
    }

    public void setTabIndex(int mTabIndex) {
        this.mTabIndex = mTabIndex;
    }
}
