package com.networkengine.entity;

/**
 * Created by pengpeng on 16/12/12.
 */

public class HelpResult {

    private HelpResultEntity detail;

    public HelpResultEntity getDetail() {
        return detail;
    }

    public void setDetail(HelpResultEntity detail) {
        this.detail = detail;
    }
    public class HelpResultEntity {
        private String email;

        private String telNum;

        public String getTelNum() {
            return telNum;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setTelNum(String telNum) {
            this.telNum = telNum;
        }
    }
}
