package com.markLove.xplan.bean;

import java.util.List;

/**
 * Created by luoyunmin on 2017/8/7.
 */

public class IsLoveBean {

    /**
     * HaveData : true
     * dt : [{"isLoveers":"0"}]
     */

    private boolean HaveData;
    private List<DtBean> dt;

    public boolean isHaveData() {
        return HaveData;
    }

    public void setHaveData(boolean HaveData) {
        this.HaveData = HaveData;
    }

    public List<DtBean> getDt() {
        return dt;
    }

    public void setDt(List<DtBean> dt) {
        this.dt = dt;
    }

    public static class DtBean {
        /**
         * isLoveers : 0
         */

        private String isLoveers;

        public String getIsLoveers() {
            return isLoveers;
        }

        public void setIsLoveers(String isLoveers) {
            this.isLoveers = isLoveers;
        }
    }
}
