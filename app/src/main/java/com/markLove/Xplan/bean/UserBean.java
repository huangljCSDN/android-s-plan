package com.markLove.Xplan.bean;

/**
 * 作者：created by huanglingjun on 2018/10/18
 * 描述：
 */
public class UserBean {


    /**
     * userInfo : {"birthday":1538323200000,"passWord":"e10adc3949ba59abbe56e057f20f883e","isRecommend":1,"city":"深圳市","signature":"222","coverImageUrl":"M00/09/C4/rBIAAlvAWRiACTUnAAFqKMB-mxM446.jpg","headImageUrl":"M00/09/C5/rBIAAlvEUMeAT_7SAC1t5rmCGXM716.jpg","openId":"","latitude":22.582208,"focusCount":0,"likeCount":0,"isMerchant":0,"updateDT":null,"registerSource":3,"checkStatus":0,"payPassWord":"","province":"广东省","updateBy":"","xplayNum":381378,"longitude":113.955685,"rewardCount":0,"qu":"","nickName":"黄凌君","sex":1,"userId":369033,"realName":"","registerAreaId":440300,"isReadName":0,"phone":"13632760395","registerDT":1539595583000,"userType":0,"account":"","status":0}
     * token : r60K0FFZIzE_WvABS1PdCvHc7ERF35Yye_qwDlMFVu34x1u9ade1LjbCQ1HCg6cyemUKzSbzhFnF2WT8cUHXJKh_indIkmO47ytVk5kCRsU5IWhYo030ZT4mBQERBF9RYSf_YMYHa2PoJV-6S-SdXpW6R_4Uyzlft0jWehAKPH4
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
         * birthday : 1538323200000
         * passWord : e10adc3949ba59abbe56e057f20f883e
         * isRecommend : 1
         * city : 深圳市
         * signature : 222
         * coverImageUrl : M00/09/C4/rBIAAlvAWRiACTUnAAFqKMB-mxM446.jpg
         * headImageUrl : M00/09/C5/rBIAAlvEUMeAT_7SAC1t5rmCGXM716.jpg
         * openId :
         * latitude : 22.582208
         * focusCount : 0
         * likeCount : 0
         * isMerchant : 0
         * updateDT : null
         * registerSource : 3
         * checkStatus : 0
         * payPassWord :
         * province : 广东省
         * updateBy :
         * xplayNum : 381378
         * longitude : 113.955685
         * rewardCount : 0
         * qu :
         * nickName : 黄凌君
         * sex : 1
         * userId : 369033
         * realName :
         * registerAreaId : 440300
         * isReadName : 0
         * phone : 13632760395
         * registerDT : 1539595583000
         * userType : 0
         * account :
         * status : 0
         */
        private long birthday;
        private String passWord;
        private int isRecommend;
        private String city;
        private String signature;
        private String coverImageUrl;
        private String headImageUrl;
        private String openId;
        private double latitude;
        private int focusCount;
        private int likeCount;
        private int isMerchant;
        private String updateDT;
        private int registerSource;
        private int checkStatus;
        private String payPassWord;
        private String province;
        private String updateBy;
        private int xplayNum;
        private double longitude;
        private int rewardCount;
        private String qu;
        private String nickName;
        private int sex;
        private int userId;
        private String realName;
        private int registerAreaId;
        private int isReadName;
        private String phone;
        private long registerDT;
        private int userType;
        private String account;
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

        public void setCity(String city) {
            this.city = city;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public void setCoverImageUrl(String coverImageUrl) {
            this.coverImageUrl = coverImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
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

        public void setUpdateDT(String updateDT) {
            this.updateDT = updateDT;
        }

        public void setRegisterSource(int registerSource) {
            this.registerSource = registerSource;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public void setPayPassWord(String payPassWord) {
            this.payPassWord = payPassWord;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public void setXplayNum(int xplayNum) {
            this.xplayNum = xplayNum;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setRewardCount(int rewardCount) {
            this.rewardCount = rewardCount;
        }

        public void setQu(String qu) {
            this.qu = qu;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public void setRegisterAreaId(int registerAreaId) {
            this.registerAreaId = registerAreaId;
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

        public void setAccount(String account) {
            this.account = account;
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

        public String getCity() {
            return city;
        }

        public String getSignature() {
            return signature;
        }

        public String getCoverImageUrl() {
            return coverImageUrl;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public String getOpenId() {
            return openId;
        }

        public double getLatitude() {
            return latitude;
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

        public String getUpdateDT() {
            return updateDT;
        }

        public int getRegisterSource() {
            return registerSource;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public String getPayPassWord() {
            return payPassWord;
        }

        public String getProvince() {
            return province;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public int getXplayNum() {
            return xplayNum;
        }

        public double getLongitude() {
            return longitude;
        }

        public int getRewardCount() {
            return rewardCount;
        }

        public String getQu() {
            return qu;
        }

        public String getNickName() {
            return nickName;
        }

        public int getSex() {
            return sex;
        }

        public int getUserId() {
            return userId;
        }

        public String getRealName() {
            return realName;
        }

        public int getRegisterAreaId() {
            return registerAreaId;
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

        public String getAccount() {
            return account;
        }

        public int getStatus() {
            return status;
        }
    }
}
