package com.networkengine.entity;

import java.util.List;

/**
 * Created by liuhao on 2018/5/30.
 */

public class RequestGroupAddOrRemovePersonParams {
    /**
     * 群id
     */
    private String id;

    /**
     * 添加或者移除的成员
     */
    private List<MembersParams> members;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
