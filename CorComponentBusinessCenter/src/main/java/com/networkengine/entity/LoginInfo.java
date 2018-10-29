package com.networkengine.entity;

import com.networkengine.httpApi.MchlApiService;
import com.networkengine.database.table.Member;

public class LoginInfo {

    /**
     * MXM && MCHL 登陆信息
     */
    private Member user;

//    /**
//     * 升级下载 url
//     */
//    private String url;
//    /**
//     * 是否强制升级
//     */
//    private String vf;

    private MchlApiService mchlApiService;

//    private MxmApiService mxmApiService;

//    private String remark;
//
//    //职位
//    private String positionNmae;
//
//    //职位
//    private String job;
//
//    private String positionPath;
//
//    private String orgPath;

    //public String getPositionPath() {
      //  return positionPath;
   // }

   // public void setPositionPath(String positionPath) {
      //  this.positionPath = positionPath;
   // }

//    public String getOrgPath() {
//        return orgPath;
//    }
//
//    public void setOrgPath(String orgPath) {
//        this.orgPath = orgPath;
//    }

//    public String getJob() {
//        return job;
//    }
//
//    public void setJob(String job) {
//        this.job = job;
//    }

    public Member getUser() {
        return user;
    }

    public void setUser(Member user) {
        this.user = user;
    }


//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getVf() {
//        return vf;
//    }
//
//    public void setVf(String vf) {
//        this.vf = vf;
//    }

    public MchlApiService getMchlApiService() {
        return mchlApiService;
    }

    public void setMchlApiService(MchlApiService mchlApiService) {
        this.mchlApiService = mchlApiService;
    }

//    public MxmApiService getMxmApiService() {
//        return mxmApiService;
//    }
//
//    public void setMxmApiService(MxmApiService mxmApiService) {
//        this.mxmApiService = mxmApiService;
//    }

//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//    public String getPositionNmae() {
//        return positionNmae;
//    }
//
//    public void setPositionNmae(String positionNmae) {
//        this.positionNmae = positionNmae;
//    }

}
