package com.networkengine.entity;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author panxiaoan
 * date 2017/4/6.
 */

public class ChangeDisturbStateParam {
    public static final int LIGHT_APP = 5; // 轻应用的消息免打扰
    public static final int PC_ONLINE = 4; // PC端在线时，手机端是否通知
    public static final int PERSON = 3;//单聊免打扰
    public static final int GROUP = 2;//群组免打扰
    public static final int ALL = 1;//全局免打扰

    @IntDef({PERSON, GROUP, ALL, LIGHT_APP})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface NotDisturbFlag {
    }

    private int nonDisturbType;  //免打扰类型1:全局， 2：群组3：个人
    private Boolean nonDisturbStatus;// "false"是免打扰,"true"是取消免打扰
    private String nonDisturbKey;//消息类型的唯一标识 如果nonDisturbType取值为2，本字段取免打扰的群组id，如果nonDisturbType取值为3，本字段取免打扰的用户id，如果nonDisturbType取值为5，本字段取轻应用的code


    public int getNonDisturbType() {
        return nonDisturbType;
    }

    public void setNonDisturbType(int nonDisturbType) {
        this.nonDisturbType = nonDisturbType;
    }


    public String getNonDisturbKey() {
        return nonDisturbKey;
    }

    public void setNonDisturbKey(String nonDisturbKey) {
        this.nonDisturbKey = nonDisturbKey;
    }

    public Boolean getNonDisturbStatus() {
        return nonDisturbStatus;
    }

    public void setNonDisturbStatus(Boolean nonDisturbStatus) {
        this.nonDisturbStatus = nonDisturbStatus;
    }

    /**
     * 获取免打扰状态标识
     *
     * @param isNotDisturb 是否免打扰
     * @return
     */
    public Boolean getSilentType(boolean isNotDisturb) {
        return isNotDisturb;
    }
}
