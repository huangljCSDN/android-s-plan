package com.markLove.Xplan.bean;

/**
 * 作者：created by huanglingjun on 2018/10/18
 * 描述：
 */
public class UserBean {


    /**
     * userInfo : {"birthday":1265012283000,"passWord":"E10ADC3949BA59ABBE56E057F20F883E","isRecommend":1,"rewardCount":0,"nickName":"岳岳","latitude":22.704737,"sex":0,"focusCount":0,"likeCount":0,"isMerchant":0,"userId":369023,"registerSource":2,"registerAreaId":0,"checkStatus":0,"isReadName":0,"phone":"13692105769","registerDT":1537172293000,"userType":0,"xplayNum":381368,"longitude":114.040505,"status":0}
     * token : qkWNOnin2j0nWVt5d/Pga5uKTuuHMtQEw/Rb+QBNi/WZlSeUKJf5dRdllFrZJc1LicyaPwyIEvGHIn7/WHXLROSgHjZ/gWNVP80V8rjMkmc8NLO6A42xK6EdjicV9q73AE5zRt53rFcdcJcYN+kyy51clWm8PZ69r8Zz4W0x85c=
     */
    private UserInfoEntity userInfo;
    private String token;

    public void setUserInfo(UserInfoEntity userInfo) {
        this.userInfo = userInfo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoEntity getUserInfo() {
        return userInfo;
    }

    public String getToken() {
        return token;
    }

    public class UserInfoEntity {
        /**
         * birthday : 1265012283000
         * passWord : E10ADC3949BA59ABBE56E057F20F883E
         * isRecommend : 1
         * rewardCount : 0
         * nickName : 岳岳
         * latitude : 22.704737
         * sex : 0
         * focusCount : 0
         * likeCount : 0
         * isMerchant : 0
         * userId : 369023
         * registerSource : 2
         * registerAreaId : 0
         * checkStatus : 0
         * isReadName : 0
         * phone : 13692105769
         * registerDT : 1537172293000
         * userType : 0
         * xplayNum : 381368
         * longitude : 114.040505
         * status : 0
         */
        private long birthday;
        private String passWord;
        private int isRecommend;
        private int rewardCount;
        private String nickName;
        private double latitude;
        private int sex;
        private int focusCount;
        private int likeCount;
        private int isMerchant;
        private int userId;
        private int registerSource;
        private int registerAreaId;
        private int checkStatus;
        private int isReadName;
        private String phone;
        private long registerDT;
        private int userType;
        private int xplayNum;
        private double longitude;
        private int status;

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public void setIsRecommend(int isRecommend) {
            this.isRecommend = isRecommend;
        }

        public void setRewardCount(int rewardCount) {
            this.rewardCount = rewardCount;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public void setFocusCount(int focusCount) {
            this.focusCount = focusCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public void setIsMerchant(int isMerchant) {
            this.isMerchant = isMerchant;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setRegisterSource(int registerSource) {
            this.registerSource = registerSource;
        }

        public void setRegisterAreaId(int registerAreaId) {
            this.registerAreaId = registerAreaId;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public void setIsReadName(int isReadName) {
            this.isReadName = isReadName;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setRegisterDT(long registerDT) {
            this.registerDT = registerDT;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public void setXplayNum(int xplayNum) {
            this.xplayNum = xplayNum;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getBirthday() {
            return birthday;
        }

        public String getPassWord() {
            return passWord;
        }

        public int getIsRecommend() {
            return isRecommend;
        }

        public int getRewardCount() {
            return rewardCount;
        }

        public String getNickName() {
            return nickName;
        }

        public double getLatitude() {
            return latitude;
        }

        public int getSex() {
            return sex;
        }

        public int getFocusCount() {
            return focusCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public int getIsMerchant() {
            return isMerchant;
        }

        public int getUserId() {
            return userId;
        }

        public int getRegisterSource() {
            return registerSource;
        }

        public int getRegisterAreaId() {
            return registerAreaId;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public int getIsReadName() {
            return isReadName;
        }

        public String getPhone() {
            return phone;
        }

        public long getRegisterDT() {
            return registerDT;
        }

        public int getUserType() {
            return userType;
        }

        public int getXplayNum() {
            return xplayNum;
        }

        public double getLongitude() {
            return longitude;
        }

        public int getStatus() {
            return status;
        }
    }
}
