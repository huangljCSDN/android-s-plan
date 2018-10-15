package com.markLove.xplan.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class MessageBean {

    /**
     * HaveData : true
     * dt : [{"Id":"3695","UserID":"891656","MsgType":"5","IsRead":"0","MsgTitle":"注册邀请匹配提醒：邀请你和TA匹配情侣","MsgContent":"你的小男友邀请你一起玩爱情存单啦！有惊喜哦！你愿意吗？","MsgLogoType":"19","IsDelete":"0","CreateDT":"2017/3/28 14:45:55","OrderNo":"","OtherUserID":"891430","LinkUrl":""},{"Id":"3881","UserID":"891656","MsgType":"5","IsRead":"0","MsgTitle":"聊天提醒：你有新的匹配对象可以聊天啦！","MsgContent":"于2017-05-25 19:09:54,男子也喜欢了你，成为你的新的匹配，你们可以愉快的聊天啦！","MsgLogoType":"17","IsDelete":"0","CreateDT":"2017/5/25 19:09:54","OrderNo":"","OtherUserID":"890586","LinkUrl":""}]
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
         * Id : 3695
         * UserID : 891656
         * MsgType : 5
         * IsRead : 0
         * MsgTitle : 注册邀请匹配提醒：邀请你和TA匹配情侣
         * MsgContent : 你的小男友邀请你一起玩爱情存单啦！有惊喜哦！你愿意吗？
         * MsgLogoType : 19
         * IsDelete : 0
         * CreateDT : 2017/3/28 14:45:55
         * OrderNo :
         * OtherUserID : 891430
         * LinkUrl :
         */

        private String Id;
        private String UserID;
        private String MsgType;
        private String IsRead;
        private String MsgTitle;
        private String MsgContent;
        private String MsgLogoType;
        private String IsDelete;
        private String CreateDT;
        private String OrderNo;
        private String OtherUserID;
        private String LinkUrl;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String UserID) {
            this.UserID = UserID;
        }

        public String getMsgType() {
            return MsgType;
        }

        public void setMsgType(String MsgType) {
            this.MsgType = MsgType;
        }

        public String getIsRead() {
            return IsRead;
        }

        public void setIsRead(String IsRead) {
            this.IsRead = IsRead;
        }

        public String getMsgTitle() {
            return MsgTitle;
        }

        public void setMsgTitle(String MsgTitle) {
            this.MsgTitle = MsgTitle;
        }

        public String getMsgContent() {
            return MsgContent;
        }

        public void setMsgContent(String MsgContent) {
            this.MsgContent = MsgContent;
        }

        public String getMsgLogoType() {
            return MsgLogoType;
        }

        public void setMsgLogoType(String MsgLogoType) {
            this.MsgLogoType = MsgLogoType;
        }

        public String getIsDelete() {
            return IsDelete;
        }

        public void setIsDelete(String IsDelete) {
            this.IsDelete = IsDelete;
        }

        public String getCreateDT() {
            return CreateDT;
        }

        public void setCreateDT(String CreateDT) {
            this.CreateDT = CreateDT;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getOtherUserID() {
            return OtherUserID;
        }

        public void setOtherUserID(String OtherUserID) {
            this.OtherUserID = OtherUserID;
        }

        public String getLinkUrl() {
            return LinkUrl;
        }

        public void setLinkUrl(String LinkUrl) {
            this.LinkUrl = LinkUrl;
        }
    }
}
