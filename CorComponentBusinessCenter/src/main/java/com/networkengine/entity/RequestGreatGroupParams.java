package com.networkengine.entity;

import java.util.List;

/**
 * Created by liuhao on 2018/5/29.
 */

public class RequestGreatGroupParams {


    private String name;
    private List<MembersParams> members;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MembersParams> getMembers() {
        return members;
    }

    public void setMembers(List<MembersParams> members) {
        this.members = members;
    }

    public static class MembersParams {

        private String userId;
        private String userName;

        public MembersParams(String userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
