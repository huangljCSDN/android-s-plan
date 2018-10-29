package com.networkengine.engine;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.networkengine.ConfigUtil;
import com.networkengine.PubConstant;
import com.networkengine.controller.BusinessController;
import com.networkengine.controller.FileTransController;
import com.networkengine.controller.IMController;
import com.networkengine.controller.SystemController;
import com.networkengine.controller.callback.ErrorResult;
import com.networkengine.controller.callback.XCallback;
import com.networkengine.database.XDbManager;
import com.networkengine.database.table.Member;
import com.networkengine.entity.LoginInfo;
import com.networkengine.exception.NetworkException;
import com.networkengine.httpApi.MchlApiService;
import com.networkengine.mqtt.MqttService;

public class LogicEngine {
    /**
     * 是否已登录
     */
    private boolean logined;

//    /**
//     * mxm客户端
//     */
//    private MxmApiService mMxmClient;

    /**
     * mchl客户端
     */
    private MchlApiService mMchlClient;

    /**
     * mqtt服务
     */
    private MqttService mMqttThread;

//    /**
//     * Mxm登录信息
//     */
    private Member mUser;

    /**
     * 公共参数
     */
    private EngineParameter mParameter;

    /**
     * 逻辑引擎
     */
    private static LogicEngine mLogicEngine;

    /**
     * 业务控制器
     */
    private BusinessController mBusinessController;

//    private IMExtension mIMExtension;
//
//    public static MxmMessageShareVo mxmMessageShareVo;

//    private String updateUrl;
//
//    private String updateVf;
//
//    private String remark;
//
//    private String positionName;
//
//    private String job;
//
//    private String positionPath;
//
//    private String orgPath;

    private boolean hasNewVersion;

    public static final int WATERMARK_CHAT = 0x01;
    public static final int WATERMARK_ADDRESS_BOOK = 0x02;
    public static final int WATERMARK_EMPLOYEE_INFO = 0x04;
    private int watermarkStatus;

    private String watermarkContent;

    // PC端是否在线
    private boolean PCOnline;
    private boolean alertOnPCOnline;

    public static LogicEngine getInstance() {
        if (mLogicEngine == null) {
            synchronized (LogicEngine.class) {
                if (mLogicEngine == null) {
                    mLogicEngine = new LogicEngine();
                }
            }
        }
        return mLogicEngine;
    }

    /**
     * 是否已登录
     *
     * @return
     */
    public boolean isLogined() {
        return logined;
    }

//    /**
//     * 获取分享类型的对象
//     */
//    public MxmMessageShareVo getshareType() {
//        return mxmMessageShareVo = ((InitController) mLogicEngine.mBusinessController).getParam().getMessageShareVo();
//    }
//
//    /**
//     * 获取密码策略
//     *
//     * @return
//     */
//    public MxmPasswordLevelVo getPasswordLevel() {
//
//        return ((InitController) mLogicEngine.mBusinessController).getParam().getPasswordLevelVo();
//    }
//
//    /**
//     * 设置
//     *
//     * @param mxmInitResult
//     */
//    public void setMxmInitResult(MxmInitResult mxmInitResult) {
//        ((InitController) mLogicEngine.mBusinessController).setParam(mxmInitResult);
//    }

    /**
     * 设置登录状态
     *
     * @param logined
     */
    public void setLogined(boolean logined) {
        this.logined = logined;
    }

    public static String getMxmUrl() {

        String gwHost = ConfigUtil.getGwHost();
        //没有走接入平台
        if (TextUtils.isEmpty(gwHost)) {
            return String.format("%s://%s:%s%s", PubConstant.MXM_PROT, PubConstant.MXM_HOST, PubConstant.MXM_PORT, PubConstant.MXM_BASE_URL);
        }

        return gwHost + "/mxm/";

        // POST /mxm/://:/api/v1/process/findCount HTTP/1.1
    }

    /**
     * 获取 v4 mxm 的下载地址
     *
     * @return
     */
    public static String getAttachmentUrl() {

        return "api/v4/attachment/id/";
    }

    public static String getMpmUrl() {
        return String.format("%s://%s:%s%s", PubConstant.MPM_PROT, PubConstant.MPM_HOST, PubConstant.MPM_PORT, PubConstant.MPM_BASE_URL);
    }

    public static String getMchlUrl() {
        String gwHost = ConfigUtil.getGwHost();
        if (TextUtils.isEmpty(gwHost)) {
            return String.format("%s://%s:%s%s", PubConstant.MCHL_PROT, PubConstant.MCHL_HOST, PubConstant.MCHL_PORT, PubConstant.MCHL_BASE_URL);
        }
        return gwHost + "/mchl/jsse/";

    }

    public static String getFileTransBaseUrl() {
        return String.format("%s://%s:%s%s", PubConstant.MCHL_PROT, PubConstant.MCHL_HOST, PubConstant.MCHL_PORT, PubConstant.MCHL_BASE_URL);
    }


    public static String getMqttUrl() {
        return String.format("%s://%s:%s", PubConstant.MQTT_PROT, PubConstant.MQTT_HOST, PubConstant.MQTT_PORT);
    }

    public static String getRazorUrl() {
        return String.format("%s://%s:%s%s", PubConstant.RAZOR_PROT, PubConstant.RAZOR_HOST, PubConstant.RAZOR_PORT, PubConstant.RAZOR_BASE_URL);
    }

    public static String getOAUrl() {
        return String.format("%s://%s:%s%s", PubConstant.OA_PROT, PubConstant.OA_HOST, PubConstant.OA_PORT, PubConstant.OA_BASE_URL);
    }

    public static abstract class Builder {

        private EngineParameter parameter;

        public Builder() {
        }

        public EngineParameter buildParameter() {
            parameter = new EngineParameter();
            parameter.imServiceBaseUrl = setImServiceBaseUrl();
            parameter.mxmServiceBaseUrl = setMxmServiceBaseUrl();
            parameter.fileTransBaseUrl = setFileTransBaseUrl();
            parameter.appKey = setAppKey();
            parameter.imei = setIMEI();
            parameter.os = setOS();
            parameter.device = setDevice();
            parameter.appVersion = setAppVersion();
            parameter.osVersion = setOsVersion();
            parameter.versionType = setVersionType();
            parameter.mqttServer = setMqttServer();
            parameter.uploadFileServerHost = setUploadFileServerHost();
            parameter.downloadFileServerHost = setDownloadFileServerHost();
            parameter.userName = setUserName();
            parameter.pwd = setPwd();
            parameter.userAgent = setUserAgent();
            return parameter;
        }

        public void build(Context context, XCallback<LogicEngine, ErrorResult> callback) {
            parameter = new EngineParameter();
            parameter.imServiceBaseUrl = setImServiceBaseUrl();
            parameter.mxmServiceBaseUrl = setMxmServiceBaseUrl();
            parameter.fileTransBaseUrl = setFileTransBaseUrl();
            parameter.appKey = setAppKey();
            parameter.imei = setIMEI();
            parameter.os = setOS();
            parameter.device = setDevice();
            parameter.appVersion = setAppVersion();
            parameter.osVersion = setOsVersion();
            parameter.versionType = setVersionType();
            parameter.mqttServer = setMqttServer();
            parameter.uploadFileServerHost = setUploadFileServerHost();
            parameter.downloadFileServerHost = setDownloadFileServerHost();
            parameter.userName = setUserName();
            parameter.pwd = setPwd();
            parameter.userAgent = setUserAgent();
            LogicEngine.init(context, parameter, callback);
        }


        public abstract String setIMEI();

        public abstract String setOS();

        public abstract String setDevice();

        public abstract String setAppVersion();

        public abstract String setOsVersion();

        public abstract String setVersionType();

        public abstract String setMqttServer();

        public abstract String setUploadFileServerHost();

        public abstract String setDownloadFileServerHost();

        public abstract String setAppKey();

        public abstract String setImServiceBaseUrl();

        public abstract String setMxmServiceBaseUrl();

        public abstract String setFileTransBaseUrl();

        public abstract String setUserName();

        public abstract String setPwd();

        public abstract String setUserAgent();
    }

    private LogicEngine() {
    }

    private static void init(final Context context
            , final EngineParameter parameter
            , final XCallback<LogicEngine, ErrorResult> callback) {
        if (parameter == null) {
            throw new NetworkException("parameter == null");
        }

        if (TextUtils.isEmpty(parameter.userName)) {
            throw new NetworkException("uid == null");
        }

        if (TextUtils.isEmpty(parameter.appKey)) {
            throw new NetworkException("appKey == null");
        }

        mLogicEngine = getInstance();

        mLogicEngine.mParameter = parameter;

        mLogicEngine.mBusinessController = BusinessController
                .init(context, parameter, new XCallback<LoginInfo, ErrorResult>() {
                    @Override
                    public void onSuccess(LoginInfo result) {
                        Member user = result.getUser();

                        if (user != null) {
                            XDbManager.getInstance(context).insertMember(user);
                        }

                        //    mLogicEngine.setUpdateUrl(result.getUrl());

                        //    mLogicEngine.setUpdateVf(result.getVf());

                        mLogicEngine.setUser(user);

//                        mLogicEngine.setMxmClient(result.getMxmApiService());

                        mLogicEngine.setMchlClient(result.getMchlApiService());

                        // mLogicEngine.setRemark(result.getRemark());

                        // mLogicEngine.setPositionName(result.getPositionNmae());

                        //  mLogicEngine.setJob(result.getJob());

                        //  mLogicEngine.setOrgPath(result.getOrgPath());

                        //  mLogicEngine.setPositionPath(result.getPositionPath());

//                        setWatermark(context, ((InitController) mLogicEngine.mBusinessController).getParam().getSecurityWatermarkVo(), user);

                        callback.onSuccess(mLogicEngine);
                    }

                    @Override
                    public void onFail(ErrorResult error) {
                        if (callback != null) {
                            callback.onFail(error);
                        }
                    }
                });

        if (mLogicEngine.mBusinessController == null) {
            throw new NetworkException("mIMClient is null init faill");
        }
    }

//    /**
//     * 设置水印规则
//     *
//     * @param param 参数
//     * @param user  用户
//     */
//    private static void setWatermark(Context context, MxmSecurityWatermarkVo param, Member user) {
//        int status = 0;
//        if (param.isChatWindowWatermark()) {
//            status |= WATERMARK_CHAT;
//        }
//        if (param.isAddressBookWatermark()) {
//            status |= WATERMARK_ADDRESS_BOOK;
//        }
//        if (param.isPersonInfoWatermark()) {
//            status |= WATERMARK_EMPLOYEE_INFO;
//        }
//        mLogicEngine.setWatermarkStatus(status);
//
//        String loginName = user.getLoginName();
//        if (loginName.contains("@")) {
//            loginName = loginName.substring(0, loginName.lastIndexOf("@"));
//        }
//        int maxLength = param.getWatermarkRule();
//        if (loginName.length() > maxLength) {
//            loginName = loginName.substring(loginName.length() - maxLength, loginName.length());
//        }
//        String appName = "";
//        try {
//            PackageManager manager = context.getPackageManager();
//            ApplicationInfo info = manager.getApplicationInfo(context.getPackageName(), 0);
//            if (info != null) {
//                appName = (String) manager.getApplicationLabel(info);
//            }
//        } catch (Exception e) {
//        }
//        mLogicEngine.setWatermarkContent(appName +
//                "   " + user.getUserName() +
//                "   " + loginName + "                    .");
//    }

//    public ContactController getContactController() {
//        return mLogicEngine.mBusinessController.getContactController();
//    }

    public SystemController getSystemController() {
        return mLogicEngine.mBusinessController.getSystemController();
    }

    public IMController getIMController() {
        return mLogicEngine.mBusinessController.getIMController();
    }

    public FileTransController getFileTransController() {
        return mLogicEngine.mBusinessController.getFileTransController();
    }

//    public WorkspaceController getWorkspaceController() {
//        return mLogicEngine.mBusinessController.getWorkspaceController();
//    }

    public void registMqttObserver(String key, Handler.Callback observer) {
        mMqttThread.registMqttObserver(key, observer);
    }

    public void unregistMqttObserver(String key) {
        mMqttThread.unregistMqttObserver(key);
    }

    public MchlApiService getMchlClient() {
        return mMchlClient;
    }

//    public MxmApiService getMxmClient() {
//        return mMxmClient;
//    }

//    public MxmApiService getNoLoginMxmClient(Context context) {
//
//        EngineParameter engineParameter = new NologinBuilder(context).buildParameter();
//
//        return Api.mxmService(context, engineParameter);
//    }

    public Member getUser() {
        return (Member) mUser.clone();
    }

    public void setUser(Member mUser) {
        this.mUser = mUser;
    }

//    public Member updateUser() {
//        Member oldUser = getUser();
//        Member newUser = getContactController()
//                .getMember(oldUser.getId());
//        if (newUser != null) {
//            newUser.setUserToken(oldUser.getUserToken());
//            return newUser;
//        } else {
//            return oldUser;
//        }
//    }

    //获取头像地址的前缀，拼接规则是前缀+用户id.jpg
    public static String getImgPrefix() {
        //   return getMxmUrl() + "uf/employee/photo/";
        return getMxmUrl() + "api/v4/emp/download_emp_img/";
    }

    //获取文件下载的地址

    public static String getMchlDownLoadPath(String sha) {

        return getMchlUrl() + "file/download?sha=" + sha;
    }

    public EngineParameter getEngineParameter() {
        return mParameter;
    }

    public void setMqttThread(MqttService mMqttThread) {
        this.mMqttThread = mMqttThread;
    }

    public void setMchlClient(MchlApiService mMchlClient) {
        this.mMchlClient = mMchlClient;
    }

//    public void setMxmClient(MxmApiService mMxmClient) {
//        this.mMxmClient = mMxmClient;
//    }

    public MqttService getMqttService() {
        return mMqttThread;
    }

    public boolean hasNewVersion() {
        return hasNewVersion;
    }

    public void setHasNewVersion(boolean hasNewVersion) {
        this.hasNewVersion = hasNewVersion;
    }
//    public void setUpdateUrl(String updateUrl) {
//        this.updateUrl = updateUrl;
//    }
//
//    public void setUpdateVf(String updateVf) {
//        this.updateVf = updateVf;
//    }
//
//    public String getUpdateUrl() {
//        return updateUrl;
//    }
//
//    public boolean isForcedUpgrade() {
//        return "1".equals(updateVf);
//    }

//    public IMExtension getmIMExtension() {
//        return mIMExtension;
//    }
//
//    public void setmIMExtension(IMExtension mIMExtension) {
//        this.mIMExtension = mIMExtension;
//    }

//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }

//    public String getPositionName() {
//        return TextUtils.isEmpty(positionName) ? "" : positionName;
//    }
//
//    public void setPositionName(String positionName) {
//        this.positionName = positionName;
//    }
//
//    public String getJob() {
//        return TextUtils.isEmpty(job) ? "" : job;
//    }
//
//    public void setJob(String job) {
//        this.job = job;
//    }
//
//    public String getPositionPath() {
//        return positionPath;
//    }
//
//    public void setPositionPath(String positionPath) {
//        this.positionPath = positionPath;
//    }
//
//    public String getOrgPath() {
//        return orgPath;
//    }
//
//    public void setOrgPath(String orgPath) {
//        this.orgPath = orgPath;
//    }

    public boolean isPCOnline() {
        return PCOnline;
    }

    public void setPCOnline(boolean online) {
        this.PCOnline = online;
    }

    public boolean isAlertOnPCOnline() {
        return alertOnPCOnline;
    }

    public void setAlertOnPCOnline(boolean alert) {
        this.alertOnPCOnline = alert;
    }

    public int getWatermarkStatus() {
        return watermarkStatus;
    }

    public void setWatermarkStatus(int watermarkStatus) {
        this.watermarkStatus = watermarkStatus;
    }

    public String getWatermarkContent() {
        return watermarkContent;
    }

    public void setWatermarkContent(String content) {
        this.watermarkContent = content;
    }


}

