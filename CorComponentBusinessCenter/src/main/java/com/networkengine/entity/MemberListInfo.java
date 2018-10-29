package com.networkengine.entity;

import com.networkengine.database.table.Member;

import java.util.List;

public class MemberListInfo {

    private List<Member> allMember;

    private List<Member> updateMemberList;

    private List<String> deleteMemberList;

    public List<Member> getAllMember() {
        return allMember;
    }

    public void setAllMember(List<Member> allMember) {
        this.allMember = allMember;
    }

    public List<Member> getUpdateMemberList() {
        return updateMemberList;
    }

    public void setUpdateMemberList(List<Member> updateMemberList) {
        this.updateMemberList = updateMemberList;
    }

    public List<String> getDeleteMemberList() {
        return deleteMemberList;
    }

    public void setDeleteMemberList(List<String> deleteMemberList) {
        this.deleteMemberList = deleteMemberList;
    }
}
