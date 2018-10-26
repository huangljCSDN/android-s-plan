package com.markLove.Xplan.bean;

import java.util.List;

/**
 * 作者：created by huanglingjun on 2018/10/25
 * 描述：
 */
public class GroupDetailBean {


    /**
     * userList : [{"joinTime":1540373321000,"identity":2,"isDelete":0,"headImageUrl":"M00/09/C5/rBIAAlvEUMeAT_7SAC1t5rmCGXM716.jpg","nickName":"刘杰","groupId":1,"id":27,"userType":0,"updateDT":null,"userId":369032,"status":0},{"joinTime":1540438079000,"identity":2,"isDelete":0,"headImageUrl":"M00/09/C5/rBIAAlvEUMeAT_7SAC1t5rmCGXM716.jpg","nickName":"黄凌君","groupId":1,"id":43,"userType":0,"updateDT":null,"userId":369033,"status":0}]
     * totalCount : 2
     * group : {"address":"123","checkStatus":1,"createBy":369030,"createDT":1538120692000,"currentNum":3,"dissolveTime":null,"endTime":1538149492000,"id":1,"imGroupId":0,"maxNum":10,"paymentType":1,"reachType":1,"sex":1,"startTime":1538120692000,"status":1,"title":"123","typeId":1}
     */

    private int totalCount;
    private GroupBean group;
    private List<UserListBean> userList;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public GroupBean getGroup() {
        return group;
    }

    public void setGroup(GroupBean group) {
        this.group = group;
    }

    public List<UserListBean> getUserList() {
        return userList;
    }

    public void setUserList(List<UserListBean> userList) {
        this.userList = userList;
    }

    public static class GroupBean {
        /**
         * address : 123
         * checkStatus : 1         1、未审核 2、已审核 3、审核失败
         * createBy : 369030         创建人id
         * createDT : 1538120692000  创建时间
         * currentNum : 3
         * dissolveTime : null       解散时间
         * endTime : 1538149492000
         * id : 1
         * imGroupId : 0
         * maxNum : 10
         * paymentType : 1   1、AA 2、你买单 3、我买单
         * reachType : 1     1、各自抵达 2、需要接送 3、负责接送
         * sex : 1   //      0不限 1男 2 女
         * startTime : 1538120692000
         * status : 1        0、未开始 1、进行中 2、已结束 3、已解散
         * title : 123
         * typeId : 1
         */

        private String address;
        //1、未审核 2、已审核 3、审核失败
        private int checkStatus;
        //创建人id
        private int createBy;
        //创建时间
        private long createDT;
        private int currentNum;
        //解散时间
        private long dissolveTime;
        private long endTime;
        private int id;
        private int imGroupId;
        private int maxNum;
        //1、AA 2、你买单 3、我买单
        private int paymentType;
        //1、各自抵达 2、需要接送 3、负责接送
        private int reachType;
        //0不限 1男 2 女
        private int sex;
        private long startTime;
        //0、未开始 1、进行中 2、已结束 3、已解散
        private int status;
        private String title;
        // 0、已加入 1、已报名 2、报名成功 3、已退出 4、强制踢出
        private int userStatus;
        private int typeId;

        public int getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(int userStatus) {
            this.userStatus = userStatus;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public int getCreateBy() {
            return createBy;
        }

        public void setCreateBy(int createBy) {
            this.createBy = createBy;
        }

        public long getCreateDT() {
            return createDT;
        }

        public void setCreateDT(long createDT) {
            this.createDT = createDT;
        }

        public int getCurrentNum() {
            return currentNum;
        }

        public void setCurrentNum(int currentNum) {
            this.currentNum = currentNum;
        }

        public long getDissolveTime() {
            return dissolveTime;
        }

        public void setDissolveTime(long dissolveTime) {
            this.dissolveTime = dissolveTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImGroupId() {
            return imGroupId;
        }

        public void setImGroupId(int imGroupId) {
            this.imGroupId = imGroupId;
        }

        public int getMaxNum() {
            return maxNum;
        }

        public void setMaxNum(int maxNum) {
            this.maxNum = maxNum;
        }

        public int getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(int paymentType) {
            this.paymentType = paymentType;
        }

        public int getReachType() {
            return reachType;
        }

        public void setReachType(int reachType) {
            this.reachType = reachType;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        @Override
        public String toString() {
            return "GroupBean{" +
                    "address='" + address + '\'' +
                    ", checkStatus=" + checkStatus +
                    ", createBy=" + createBy +
                    ", createDT=" + createDT +
                    ", currentNum=" + currentNum +
                    ", dissolveTime=" + dissolveTime +
                    ", endTime=" + endTime +
                    ", id=" + id +
                    ", imGroupId=" + imGroupId +
                    ", maxNum=" + maxNum +
                    ", paymentType=" + paymentType +
                    ", reachType=" + reachType +
                    ", sex=" + sex +
                    ", startTime=" + startTime +
                    ", status=" + status +
                    ", title='" + title + '\'' +
                    ", typeId=" + typeId +
                    ", userStatus=" + userStatus +
                    '}';
        }
    }

    public static class UserListBean {
        /**
         * joinTime : 1540373321000
         * identity : 2
         * isDelete : 0
         * headImageUrl : M00/09/C5/rBIAAlvEUMeAT_7SAC1t5rmCGXM716.jpg
         * nickName : 刘杰
         * groupId : 1
         * id : 27
         * userType : 0
         * updateDT : null
         * userId : 369032
         * status : 0
         */

        private long joinTime;
        private int identity;
        private int isDelete;
        private String headImageUrl;
        private String nickName;
        private int groupId;
        private int id;
        private int userType;
        private Object updateDT;
        private int userId;
        private int status;

        public long getJoinTime() {
            return joinTime;
        }

        public void setJoinTime(long joinTime) {
            this.joinTime = joinTime;
        }

        public int getIdentity() {
            return identity;
        }

        public void setIdentity(int identity) {
            this.identity = identity;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public Object getUpdateDT() {
            return updateDT;
        }

        public void setUpdateDT(Object updateDT) {
            this.updateDT = updateDT;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "UserListBean{" +
                    "joinTime=" + joinTime +
                    ", identity=" + identity +
                    ", isDelete=" + isDelete +
                    ", headImageUrl='" + headImageUrl + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", groupId=" + groupId +
                    ", id=" + id +
                    ", userType=" + userType +
                    ", updateDT=" + updateDT +
                    ", userId=" + userId +
                    ", status=" + status +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GroupDetailBean{" +
                "totalCount=" + totalCount +
                ", group=" + group +
                ", userList=" + userList +
                '}';
    }
}
