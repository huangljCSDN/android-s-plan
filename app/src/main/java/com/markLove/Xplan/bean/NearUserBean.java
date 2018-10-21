package com.markLove.Xplan.bean;

import java.util.ArrayList;
import java.util.List;

public class NearUserBean {
    public List<NearUserBean.NearUserEntity> Data;
    public int Status;

    public List<NearUserBean.NearUserEntity> getData() {
        return Data;
    }

    public void setData(List<NearUserBean.NearUserEntity> data) {
        Data = data;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public class NearUserEntity{
        public String nickName;
        public int sex;
        public String lastOnline;
        public long userId;
        public String headUrl;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getLastOnline() {
            return lastOnline;
        }

        public void setLastOnline(String lastOnline) {
            this.lastOnline = lastOnline;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        @Override
        public String toString() {
            return "NearUserEntity{" +
                    "nickName='" + nickName + '\'' +
                    ", sex=" + sex +
                    ", lastOnline='" + lastOnline + '\'' +
                    ", userId=" + userId +
                    ", headUrl='" + headUrl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NearUserBean{" +
                "Data=" + Data +
                ", Status=" + Status +
                '}';
    }
}
