package com.markLove.Xplan.bean;

import java.util.List;

/**
 * Created by luoyunmin on 2017/8/22.
 */

public class ChatOpen {

    /**
     * HaveData : true
     * dt : [{"ChatOpen":"1"}]
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
         * ChatOpen : 1
         */

        private String ChatOpen;

        public String getChatOpen() {
            return ChatOpen;
        }

        public void setChatOpen(String ChatOpen) {
            this.ChatOpen = ChatOpen;
        }
    }
}
