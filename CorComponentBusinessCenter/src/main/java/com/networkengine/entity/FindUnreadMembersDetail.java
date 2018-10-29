package com.networkengine.entity;

import java.util.List;

/**
 * author panxiaoan
 * date 2017/8/21.
 */

public class FindUnreadMembersDetail {
    private List<FindUnreadMemberEntity> unReadList;
    private List<FindUnreadMemberEntity> readList;

    public List<FindUnreadMemberEntity> getUnReadList() {
        return unReadList;
    }

    public void setUnReadList(List<FindUnreadMemberEntity> unReadList) {
        this.unReadList = unReadList;
    }

    public List<FindUnreadMemberEntity> getReadList() {
        return readList;
    }

    public void setReadList(List<FindUnreadMemberEntity> readList) {
        this.readList = readList;
    }
}
