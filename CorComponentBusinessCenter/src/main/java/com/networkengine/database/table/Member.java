package com.networkengine.database.table;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * 组织成员
 */
@Entity
public class Member implements Comparable<Member>, Cloneable {


    @Id
    private String id;
    /**
     * 登陆名
     */
    private String loginName;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 系统
     */
    private String userType;
    /**
     * 系统
     */
    private String userSystem;
    /**
     * 用户头像
     */
    private String imageAddress;

    /**
     * 公司名
     */
    private String companyName;
    /**
     * 邮件
     */
    private String email;
    /**
     * 地址
     */
    private String address;

    // private String job;
    /**
     * 手机
     */
    private String phone;
    /**
     * 座机
     */
    private String telephone;
    /**
     * 组织名
     */
    private String orgName;
    /**
     * 性别
     */
    private String sex;

    private String updateTime;
//    /**
//     * 删除掉
//     */
//    private String spelling;
    /**
     * 职位
     */
    @Property(nameInDb = "POSITION")
    private String positionName;


    /**
     * 个性签名
     */
    private String signature;

    /**
     * 拼音名
     */
    private String pinyinName;
    /**
     * 职位id
     */
    private String positionPath;

    /**
     * 组织id
     */
    private String orgPath;
    /**
     * 是否显示手机号
     */
    private String hidePhone;
    /**
     * 是否收藏
     */
    private String collectionFlag;
    /**
     * 姓名拼音的首字母 替代了spelling
     */
    private String initial;
    /**
     * 通讯录返回的排序条件
     */
    private String sortNo;
    /**
     * 生日
     */
    private String birthdate;

    /**
     * 上级
     */
    private String leaderName;

    /**
     * 用户扩展字段
     * 不存库
     */
    @Transient
    private List<MemberExtendField> userConfigInfoVoList;

    @Keep
    public List<MemberExtendField> getNetMemberExtendField() {
        return userConfigInfoVoList;
    }

    @Keep
    public void setNetMemberExtendField(List<MemberExtendField> userConfigInfoVoList) {
        this.userConfigInfoVoList = userConfigInfoVoList;
    }

    /**
     *
     */
    @Transient
    private String hobby;
    /**
     * 是否在职离职
     */
    @Transient
    private String job;

    /**
     * mchl 用户 token
     */
    @Transient
    private String userToken;
    /**
     * jwt 字符串
     */
    @Transient
    private String jwtCode;
    /**
     * 通讯录返回 无用字段
     */
    @Transient
    private int deleteFlag;
    /**
     * 创建方式
     */
    @Transient
    private String createType;


    public String getJwt() {
        return jwtCode;
    }

    public void setJwt(String jwt) {
        this.jwtCode = jwt;
    }

@Generated(hash = 8334915)
    public Member(String id, String loginName, String userId, String userName, String userType, String userSystem,
                  String imageAddress, String companyName, String email, String address, String phone, String telephone,
                  String orgName, String sex, String updateTime, String positionName, String signature,
                  String pinyinName, String positionPath, String orgPath, String hidePhone, String collectionFlag,
                  String initial, String sortNo, String birthdate, String leaderName) {
        this.id = id;
        this.loginName = loginName;
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.userSystem = userSystem;
        this.imageAddress = imageAddress;
        this.companyName = companyName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.telephone = telephone;
        this.orgName = orgName;
        this.sex = sex;
        this.updateTime = updateTime;
        this.positionName = positionName;
        this.signature = signature;
        this.pinyinName = pinyinName;
        this.positionPath = positionPath;
        this.orgPath = orgPath;
        this.hidePhone = hidePhone;
        this.collectionFlag = collectionFlag;
        this.initial = initial;
        this.sortNo = sortNo;
        this.birthdate = birthdate;
        this.leaderName = leaderName;
    }

    @Generated(hash = 367284327)
    public Member() {
    }


    public String getId() {
        return null == id ? userId : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageAddress() {
        return this.imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return null == userId ? id : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserSystem() {
        return userSystem;
    }

    public void setUserSystem(String userSystem) {
        this.userSystem = userSystem;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }


    @Override
    public int compareTo(@NonNull Member o) {
        return this.initial.compareTo(o.initial) > 0 ? 1 : -1;
    }

    @Override
    public Object clone() {
        Member member = null;
        try {
            member = (Member) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return member;
    }

    public String getInitial() {
        return this.initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getPositionName() {
        return this.positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPinyinName() {
        return this.pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public String getPositionPath() {
        return this.positionPath;
    }

    public void setPositionPath(String positionPath) {
        this.positionPath = positionPath;
    }

    public String getOrgPath() {
        return this.orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    public String getHidePhone() {
        return this.hidePhone;
    }

    public void setHidePhone(String hidePhone) {
        this.hidePhone = hidePhone;
    }

    public String getCollectionFlag() {
        return this.collectionFlag;
    }

    public void setCollectionFlag(String collectionFlag) {
        this.collectionFlag = collectionFlag;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
//    @Generated(hash = 540239802)
//    public List<Organization> getOrganizations() {
//        if (organizations == null) {
//            final DaoSession daoSession = this.daoSession;
//            if (daoSession == null) {
//                throw new DaoException("Entity is detached from DAO context");
//            }
//            OrganizationDao targetDao = daoSession.getOrganizationDao();
//            List<Organization> organizationsNew = targetDao
//                    ._queryMember_Organizations(id);
//            synchronized (this) {
//                if (organizations == null) {
//                    organizations = organizationsNew;
//                }
//            }
//        }
//        return organizations;
//    }

    public String getSortNo() {
        return this.sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getLeaderName() {
        return this.leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }


    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userType='" + userType + '\'' +
                ", userSystem='" + userSystem + '\'' +
                ", imageAddress='" + imageAddress + '\'' +
                ", companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", telephone='" + telephone + '\'' +
                ", orgName='" + orgName + '\'' +
                ", sex='" + sex + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", positionName='" + positionName + '\'' +
                '}';
    }
}
