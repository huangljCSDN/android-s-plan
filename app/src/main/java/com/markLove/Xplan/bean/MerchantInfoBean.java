package com.markLove.Xplan.bean;

import java.util.List;

public class MerchantInfoBean {


    /**
     * qq : null
     * userInfo : [{"imGroupId":1,"merchantId":22551,"nickName":"岳岳","headImageUrl":null,"userId":369023,"createDT":1539694808000},{"imGroupId":1,"merchantId":22551,"nickName":"黄俊","headImageUrl":"","userId":369041,"createDT":1539694810000},{"imGroupId":1,"merchantId":22551,"nickName":"顶替","headImageUrl":"M00/09/C6/rBIAAlvFeVyAcbfEAACeszmt2WE948.jpg","userId":369037,"createDT":1539694813000}]
     * recipeList : [{"isRecommend":1,"createBy":"岳阳","merchantId":22551,"imageUrl":"M00/09/C7/rBIAAlvOlRmAZdDvAABiuzX1qOw049.jpg","orderBy":2,"id":80,"imageType":2,"createDT":1540265257000},{"isRecommend":1,"createBy":"岳阳","merchantId":22551,"imageUrl":"M00/09/C7/rBIAAlvOlRmAA8g4AACQVqlXuLU286.jpg","orderBy":3,"id":81,"imageType":2,"createDT":1540265257000},{"isRecommend":1,"createBy":"岳阳","merchantId":22551,"imageUrl":"M00/09/C7/rBIAAlvOlRmAdkCxAAChZIEk44o817.jpg","orderBy":4,"id":82,"imageType":2,"createDT":1540265257000},{"isRecommend":1,"createBy":"岳阳","merchantId":22551,"imageUrl":"M00/09/C7/rBIAAlvOlRmAVmLDAACeszmt2WE379.jpg","orderBy":5,"id":83,"imageType":2,"createDT":1540265257000},{"isRecommend":1,"createBy":"岳阳","merchantId":22551,"imageUrl":"M00/09/C7/rBIAAlvOlRiACq2uAAGLhK2Zc0s256.jpg","orderBy":6,"id":84,"imageType":2,"createDT":1540265257000},{"isRecommend":1,"createBy":"岳阳","merchantId":22551,"imageUrl":"M00/09/C7/rBIAAlvOlReAQklmAAPBl_CyAoY686.jpg","orderBy":7,"id":85,"imageType":2,"createDT":1540265257000},{"isRecommend":1,"createBy":"岳阳","merchantId":22551,"imageUrl":"M00/09/C7/rBIAAlvOlRmAQQSnAAB9HnF_2v0621.jpg","orderBy":8,"id":86,"imageType":2,"createDT":1540265257000},{"isRecommend":1,"createBy":"岳阳","merchantId":22551,"imageUrl":"M00/09/C7/rBIAAlvOlRiAeRc2AAA_5MbCg1c372.jpg","orderBy":10,"id":88,"imageType":2,"createDT":1540265257000},{"isRecommend":1,"createBy":"岳阳","merchantId":22551,"imageUrl":"M00/09/C7/rBIAAlvOlRiAGyt3AATD2LixZfg992.jpg","orderBy":11,"id":89,"imageType":2,"createDT":1540265257000}]
     * shopName : 深圳存爱盒子
     * shopAddress : 南山区西丽街道
     * title : 五月天要来深圳开演唱会了。
     * userId : 369023
     * logoUrl : M00/09/BD/rBIAAlukqmyAcCA_AAPBl_CyAoY404.jpg
     * imGroupId : 1
     * activityId : 7
     * groupName : 刘杰测试店铺聊天室
     * areaId : 440300
     * pointPraiseCount : 2
     * groupCount : 3
     * merchantId : 22551
     * phone : 13462857946
     * auditStatus : 2
     * tel : 020-23698742
     * abbreviationName : 盒子
     * activityIntroduction : <p>2222222</p>
     * email : null
     * introduction : 介绍一下呗
     */
    private String qq;
    private List<UserInfoEntity> userInfo;
    private List<RecipeListEntity> recipeList;
    private String shopName;
    private String shopAddress;
    private String title;
    private int userId;
    private String logoUrl;
    private int imGroupId;
    private int activityId;
    private String groupName;
    private int areaId;
    private int pointPraiseCount;
    private int groupCount;
    private int merchantId;
    private String phone;
    private int auditStatus;
    private String tel;
    private String abbreviationName;
    private String activityIntroduction;
    private String email;
    private String introduction;

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setUserInfo(List<UserInfoEntity> userInfo) {
        this.userInfo = userInfo;
    }

    public void setRecipeList(List<RecipeListEntity> recipeList) {
        this.recipeList = recipeList;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setImGroupId(int imGroupId) {
        this.imGroupId = imGroupId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public void setPointPraiseCount(int pointPraiseCount) {
        this.pointPraiseCount = pointPraiseCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAbbreviationName(String abbreviationName) {
        this.abbreviationName = abbreviationName;
    }

    public void setActivityIntroduction(String activityIntroduction) {
        this.activityIntroduction = activityIntroduction;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getQq() {
        return qq;
    }

    public List<UserInfoEntity> getUserInfo() {
        return userInfo;
    }

    public List<RecipeListEntity> getRecipeList() {
        return recipeList;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getTitle() {
        return title;
    }

    public int getUserId() {
        return userId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public int getImGroupId() {
        return imGroupId;
    }

    public int getActivityId() {
        return activityId;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getAreaId() {
        return areaId;
    }

    public int getPointPraiseCount() {
        return pointPraiseCount;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public String getPhone() {
        return phone;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public String getTel() {
        return tel;
    }

    public String getAbbreviationName() {
        return abbreviationName;
    }

    public String getActivityIntroduction() {
        return activityIntroduction;
    }

    public String getEmail() {
        return email;
    }

    public String getIntroduction() {
        return introduction;
    }

    public class UserInfoEntity {
        /**
         * imGroupId : 1
         * merchantId : 22551
         * nickName : 岳岳
         * headImageUrl : null
         * userId : 369023
         * createDT : 1539694808000
         */
        private int imGroupId;
        private int merchantId;
        private String nickName;
        private String headImageUrl;
        private int userId;
        private long createDT;

        public void setImGroupId(int imGroupId) {
            this.imGroupId = imGroupId;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setCreateDT(long createDT) {
            this.createDT = createDT;
        }

        public int getImGroupId() {
            return imGroupId;
        }

        public int getMerchantId() {
            return merchantId;
        }

        public String getNickName() {
            return nickName;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public int getUserId() {
            return userId;
        }

        public long getCreateDT() {
            return createDT;
        }
    }

    public class RecipeListEntity {
        /**
         * isRecommend : 1
         * createBy : 岳阳
         * merchantId : 22551
         * imageUrl : M00/09/C7/rBIAAlvOlRmAZdDvAABiuzX1qOw049.jpg
         * orderBy : 2
         * id : 80
         * imageType : 2
         * createDT : 1540265257000
         */
        private int isRecommend;
        private String createBy;
        private int merchantId;
        private String imageUrl;
        private int orderBy;
        private int id;
        private int imageType;
        private long createDT;

        public void setIsRecommend(int isRecommend) {
            this.isRecommend = isRecommend;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setOrderBy(int orderBy) {
            this.orderBy = orderBy;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setImageType(int imageType) {
            this.imageType = imageType;
        }

        public void setCreateDT(long createDT) {
            this.createDT = createDT;
        }

        public int getIsRecommend() {
            return isRecommend;
        }

        public String getCreateBy() {
            return createBy;
        }

        public int getMerchantId() {
            return merchantId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getOrderBy() {
            return orderBy;
        }

        public int getId() {
            return id;
        }

        public int getImageType() {
            return imageType;
        }

        public long getCreateDT() {
            return createDT;
        }
    }
}
