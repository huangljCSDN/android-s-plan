package com.markLove.Xplan.bean;

import java.util.List;

/**
 * Created by luoyunmin on 2017/6/9.
 */

public class FriendsBean {


    /**
     * HaveData : true
     * dt : [{"UserID":"891973","NickName":"木头","HeadImageUrl":"M00/01/AB/rBIAAlk5-G2AblheAAAeUwXBSBw007.jpg"}]
     */

    private boolean HaveData;
    private List<LikeInfoBean> dt;

    public boolean isHaveData() {
        return HaveData;
    }

    public void setHaveData(boolean HaveData) {
        this.HaveData = HaveData;
    }

    public List<LikeInfoBean> getDt() {
        return dt;
    }

    public void setDt(List<LikeInfoBean> dt) {
        this.dt = dt;
    }

    public static class LikeInfoBean {
        /**
         * UserID : 891973
         * NickName : 木头
         * HeadImageUrl : M00/01/AB/rBIAAlk5-G2AblheAAAeUwXBSBw007.jpg
         */

        private String UserID;
        private String NickName;
        private String HeadImageUrl;

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String UserID) {
            this.UserID = UserID;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getHeadImageUrl() {
            return HeadImageUrl;
        }

        public void setHeadImageUrl(String HeadImageUrl) {
            this.HeadImageUrl = HeadImageUrl;
        }
    }
}
