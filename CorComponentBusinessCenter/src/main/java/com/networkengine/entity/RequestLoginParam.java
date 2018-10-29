package com.networkengine.entity;

import com.networkengine.engine.EngineParameter;

public class RequestLoginParam {
    /**
     * loginName            //登录名 必填
     * password                     //密码 必填
     * osType              //设备类型，移动端取值 IOS |Android ，PC端取值 PC必填。后台通过此值决定推送消息，推送方式为mqtt或 apns
     * apnsToken            //IOS设备的token值，当osType=IOS 时需要。如果不存在，IOS的推送消息不会通知客户端
     * osVersion                    //系统版本号，非必填，通道层目前没使用到。
     * deviceType           //设备类型 移动端取值phone | pad，PC端取值 pc  必填
     * mac                      //设备mac地址，非必填，通道层目前没使用到。
     * imei                      //设备 IMEI号 必填，在通道层作为设备的唯一标示
     * appKey          //应用的唯一标识
     * appVersion       //app版本
     * versionType      // 版本的类型
     * bizInput                //业务定义参数输入，非必填
     */
    // TODO 待整理
    private String loginName;
    private String password;
    private String osType;
    private String osVersion;
    private String imei;
    private String appKey;
    private String appVersion;
    //版本类型，暂时写死
    private String versionType = "ENABLE";
    private String deviceType;
    private String macAddress;
    private String bizInput;

    public RequestLoginParam() {
    }

    // TODO 待整理
    public RequestLoginParam(EngineParameter parameter) {
        loginName = parameter.userName;
        password = parameter.pwd;
        osType = parameter.os;
        osVersion = parameter.osVersion;
        imei = parameter.imei;
        appKey = parameter.appKey;
        appVersion = parameter.appVersion;
        versionType = parameter.versionType;
        deviceType = parameter.device;
        bizInput = "";
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getBizInput() {
        return bizInput;
    }

    public void setBizInput(String bizInput) {
        this.bizInput = bizInput;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
