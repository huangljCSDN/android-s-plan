package com.networkengine.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.networkengine.AsyncUtil.RestTask;
import com.networkengine.PubConstant;
import com.networkengine.engine.LogicEngine;
import com.networkengine.entity.RequestLogoutParam;
import com.networkengine.util.CoracleSdk;
import com.networkengine.util.PhoneUtile;

import java.io.IOException;

/**
 * Created by pengpeng on 17/2/15.
 */

public class SystemController extends BusinessController {

    public static final int SYSTEM_CODE_FAILE = -1;

    public static final int SYSTEM_CODE_SUCCESS = 0;

    public static final int SYSTEM_CODE_LOGOUT_FAIL = 1;

    public static final int SYSTEM_CODE_CHECK_UPDATE_FAIL = 2;

    public static final int SYSTEM_CODE_CHECK_DEVICE_STATUS_FAIL = 3;

    public static final int SYSTEM_CODE_UPDATE_PWD_FAIL = 4;

    public static final int SYSTEM_CODE_LOAD_USER_INFO_FAIL = 5;

    public static final int SYSTEM_CODE_UPDATE_USER_INFO_FAIL = 6;

    public static final int SYSTEM_CODE_LOAD_SOFT_INFO_FAIL = 7;

    public static final int SYSTEM_CODE_LOGOUT_MXM_FAIL = 8;

    public static final int SYSTEM_CODE_HELP_FAIL = 9;

    public static final int SYSTEM_CODE_GET_BANNER_FAIL = 10;

//    protected SystemController(Context context, EngineParameter parameter, MqttService mqttService) {
//        super(context, parameter, mqttService);
//    }

    protected SystemController(BusinessController businessController) {
        super(businessController);
    }


//    /**
//     * 退出登陆
//     *
//     * @param taskListener
//     */
    public void logout(RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
        Context context = CoracleSdk.getCoracleSdk().getContext();
        RequestLogoutParam requestLogoutParam = new RequestLogoutParam();
        requestLogoutParam.setPlat(PubConstant.config.PLATFORM);
        requestLogoutParam.setToken(PhoneUtile.getDeviceId(context));
//        MxmLoginoutEntity mxmLoginoutEntity = new MxmLoginoutEntity();
//        mxmLoginoutEntity.setDeviceType(PubConstant.config.PLATFORM.toLowerCase());
//        mxmLoginoutEntity.setAppKey(mParameter.appKey);
//        mxmLoginoutEntity.setDeviceToken(PhoneUtil.getDeviceId(context));

        RestTask<String, Integer> logoutTask = getLogoutTask(requestLogoutParam, taskListener);

        logoutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    private RestTask<String, Integer> getLogoutTask( final RequestLogoutParam logoutParam, RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
        return new RestTask<String, Integer>(taskListener) {
            @Override
            protected Integer doInBackground(Void... voids) {
                // 由于退出是一个不可逆的操作，需要登录三个平台，任何一个失败，之前登出的平台都没办法恢复，所以只能无视登出失败，全部认为成功。。。

//                // 退出mxm
//                try {
//                    mMxmApiService.logout(mxmLoginoutEntity).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                // 退出mchl
                try {
                    mMchlApiService.logout(logoutParam == null ? new RequestLogoutParam() : logoutParam).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 停止mqtt
                LogicEngine.getInstance().getMqttService().stopMqtt();

                return SYSTEM_CODE_SUCCESS;
            }
        };
    }
//
//
    /**
     * 登出操作
     *
     * @param dwline 是否被挤下线
     */
    public void logout(final boolean dwline) {

        logout(new RestTask.TaskListener<RestTask<String, Integer>, Integer>() {

            @Override
            public void onComplete(RestTask<String, Integer> task, Integer value) {
                final Context context = CoracleSdk.getCoracleSdk().getContext();
//                //退出，清除cookie
//                Api.getInMemoryCookieStore().removeAll();
//                LogicEngine.getInstance().setLogined(false);
//                //版本更新的置为false
//                LogicEngine.getInstance().setHasNewVersion(false);
//                CORAgent.onEvent(Constant.CorAgentEventKey.LOGOUT, "退出");
//
//                CorUri uri = new CorUri("CorComponentLogin", CorUri.Patten.ENUM.method, "logout",
//                        new CorUri.Param("dwline", dwline + ""));
//                CorRouter.getCorRouter().getmClient().invoke(context, uri,
//                        new RouterCallback() {
//                            @Override
//                            public void callback(RouterCallback.Result result) {
//                                //先跳转，再关闭,排除LoginActivity 解决白屏的问题
//                                finishActivitiesByApplication(context, "com.coracle.xsimple.login.activity.LoginActivity");
//                            }
//                        });
            }
        });

    }
//
//
//    /**
//     * 结束进程
//     */
//    public void exitSystem() {
//
//        logout(new RestTask.TaskListener<RestTask<String, Integer>, Integer>() {
//
//            @Override
//            public void onComplete(RestTask<String, Integer> task, Integer value) {
//                Context context = CoracleSdk.getCoracleSdk().getContext();
//                //退出，清除cookie
//                Api.getInMemoryCookieStore().removeAll();
//                LogicEngine.getInstance().setLogined(false);
//                CORAgent.onEvent(Constant.CorAgentEventKey.LOGOUT, "退出");
//                finishActivitiesByApplication(context);
//                android.os.Process.killProcess(android.os.Process.myPid());
//
//            }
//        });
//    }
//
//    public void finishActivitiesByApplication(Context context, String className) {
//        List<Activity> list = new ArrayList<>();
//        try {
//            Class<Application> applicationClass = Application.class;
//            Field mLoadedApkField = applicationClass.getDeclaredField("mLoadedApk");
//            mLoadedApkField.setAccessible(true);
//            Object mLoadedApk = mLoadedApkField.get(context.getApplicationContext());
//            Class<?> mLoadedApkClass = mLoadedApk.getClass();
//            Field mActivityThreadField = mLoadedApkClass.getDeclaredField("mActivityThread");
//            mActivityThreadField.setAccessible(true);
//            Object mActivityThread = mActivityThreadField.get(mLoadedApk);
//            Class<?> mActivityThreadClass = mActivityThread.getClass();
//            Field mActivitiesField = mActivityThreadClass.getDeclaredField("mActivities");
//            mActivitiesField.setAccessible(true);
//            Object mActivities = mActivitiesField.get(mActivityThread);
//            if (mActivities instanceof Map) {
//                @SuppressWarnings("unchecked")
//                Map<Object, Object> arrayMap = (Map<Object, Object>) mActivities;
//                for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
//                    Object value = entry.getValue();
//                    Class<?> activityClientRecordClass = value.getClass();
//                    Field activityField = activityClientRecordClass.getDeclaredField("activity");
//                    activityField.setAccessible(true);
//                    Object o = activityField.get(value);
//                    list.add((Activity) o);
//                }
//            }
//            for (int i = 0; i < list.size(); i++) {
//                Activity activity = list.get(i);
//                if (!TextUtils.isEmpty(className) && activity.getClass().getName().equals(className)) {
//                    continue;
//                }
//                activity.finish();
//            }
//
//        } catch (Exception e) {
//        }
//    }
//
//    /**
//     * finisn 所有activity
//     *
//     * @param context getApplicationContext()
//     */
//    public void finishActivitiesByApplication(Context context) {
//        finishActivitiesByApplication(context, "");
//
//    }
//
//    static PromptDialog dialog;
//
//    public void checkUpdate(final Context context, final boolean formSetting, final RouterCallback callback) {
//        checkUpdate(context, new XCallback<MxmCheckVersionResult, ErrorResult>() {
//            @Override
//            public void onSuccess(final MxmCheckVersionResult result) {
//                //置false
//                LogicEngine.getInstance().setHasNewVersion(false);
//                if (result == null || TextUtils.isEmpty(result.getDownloadUrl())) {
//                    if (formSetting) {
//                        new PromptDialog.Builder(context)
//                                .setMessage(context.getString(R.string.business_the_new_version))
//                                .setPositiveButton(context.getString(R.string.business_got_it), null)
//                                .show();
//                    }
//                } else {
//                    //设置新版本，用于UI红点的显示
//                    LogicEngine.getInstance().setHasNewVersion(true);
//                    if (callback != null) {
//                        callback.callback(new RouterCallback.Result(RouterCallback.Result.SUCCESS, "", ""));
//                    }
//                    final boolean isForcedUpgrade = "1".equals(result.getForcedUpdate());
//                    //final boolean isForcedUpgrade = true;
//                    if (!formSetting && !isForcedUpgrade
//                            && result.getDownloadUrl().equals(SharedPrefsHelper.get("ignoreVersion", ""))) {
//                        return;
//                    }
//                    if (UpdateManager.getInstance().isDownloading()) {
//                        if (formSetting) {
//                            Toast.makeText(context, context.getString(R.string.business_downloading), Toast.LENGTH_SHORT).show();
//                        }
//                        return;
//                    }
//                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_version, null);
//                    TextView tvVersion = (TextView) view.findViewById(R.id.tv_dialog_new_version_version);
//                    tvVersion.setText("(V" + result.getVersionNum() + ")");
//                    if (isForcedUpgrade) {
//                        view.findViewById(R.id.tv_dialog_new_version_force_upgrate).setVisibility(View.VISIBLE);
//                    } else {
//                        view.findViewById(R.id.tv_dialog_new_version_force_upgrate).setVisibility(View.GONE);
//                    }
//                    TextView tvRemark = (TextView) view.findViewById(R.id.tv_dialog_new_version_remark);
//                    tvRemark.setText(result.getRemark());
//                    if (null != dialog) {
//                        dialog.dismiss();
//                    }
//                    dialog = new PromptDialog.Builder(context).setMessage(view)
//                            .setPositiveButton(context.getString(R.string.business_update_now), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    String url = String.format("%s%s", LogicEngine.getMxmUrl(), result.getDownloadUrl());
//                                    String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
//                                    UpdateManager.getInstance().startNotiUpdateTask(context, url, fileName);
//                                    //强制跟新,退出activity后,会在MainActivity置标志位禁止activity启动
//                                    UpdateManager.getInstance().setForcedUpgrade(isForcedUpgrade);
//                                    if (isForcedUpgrade) {
//                                        finishActivitiesByApplication(context);
//                                    }
//                                }
//                            })
//                            .setNegativeButton(context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if (isForcedUpgrade) {
//                                        LogicEngine.getInstance().getSystemController().exitSystem();
//                                    } else {
//                                        SharedPrefsHelper.put("ignoreVersion", result.getDownloadUrl());
//                                    }
//                                }
//                            })
//                            .setCanceledOnTouchOutside(!isForcedUpgrade)
//                            .setCancelable(!isForcedUpgrade)
//                            .show();
//                }
//            }
//
//            @Override
//            public void onFail(ErrorResult error) {
//                if (formSetting) {
//                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    /**
//     * 版本更新
//     *
//     * @param context
//     * @param xCallback
//     */
//    private void checkUpdate(Context context, final XCallback<MxmCheckVersionResult, ErrorResult> xCallback) {
//
//        MxmCheckVersionEntity entity = new MxmCheckVersionEntity();
//        entity.setAppKey(mParameter.appKey);
//        entity.setDeviceType(PubConstant.config.PLATFORM.toLowerCase());
//        entity.setVersionNum(PhoneUtil.getAppVersionName(context));
//        RestTask<MxmCheckVersionResult, ErrorResult> checkUpdateTask = getCheckUpdateTask(entity, new BusinessControllerHandler<MxmCheckVersionResult>() {
//            @Override
//            public void onSuccess(MxmCheckVersionResult mxmCheckVersionResult, ErrorResult errorResult) {
//
//                xCallback.onSuccess(mxmCheckVersionResult);
//
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                xCallback.onFail(code);
//            }
//        });
//        checkUpdateTask.execute();
//    }
//
//    private RestTask<MxmCheckVersionResult, ErrorResult> getCheckUpdateTask(final MxmCheckVersionEntity entity, RestTask.TaskListener<RestTask<MxmCheckVersionResult, ErrorResult>, ErrorResult> taskListener) {
//
//        return new RestTask<MxmCheckVersionResult, ErrorResult>(taskListener) {
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<MxmCheckVersionResult> syncCoracleCallback = new SyncCoracleCallback();
//                MxmCheckVersionResult mxmCheckVersionResult = syncCoracleCallback.execute(mMxmApiService.checkUpdate(entity));
//                ErrorResult errorResult = syncCoracleCallback.getErrorResult();
//                if (mxmCheckVersionResult != null) {
//                    setmTag(mxmCheckVersionResult);
//                }
//                return errorResult;
//            }
//        };
//    }
//
//
//    /**
//     * 获取工作区轻应用
//     *
//     * @param xCallback
//     */
//    public void getWebApplication(final XCallback<String, ErrorResult> xCallback) {
//
//        MxmWorkSpaceEntity entity = new MxmWorkSpaceEntity();
//        entity.setAppKey(mParameter.appKey);
//        entity.setPlatForm(PubConstant.config.PLATFORM.toLowerCase());
//
//        RestTask<MxmWorkSpaceResult, ErrorResult> webApplicationTask = getWebApplicationTask(entity, new BusinessControllerHandler<MxmWorkSpaceResult>() {
//            @Override
//            public void onSuccess(MxmWorkSpaceResult mxmWorkSpaceResult, ErrorResult code) {
//                xCallback.onSuccess(new Gson().toJson(mxmWorkSpaceResult));
//
//            }
//
//            @Override
//            public void onFail(ErrorResult errorResult) {
//                xCallback.onFail(errorResult);
//
//            }
//        });
//        webApplicationTask.execute();
//    }
//
//
//    private RestTask<MxmWorkSpaceResult, ErrorResult> getWebApplicationTask(final MxmWorkSpaceEntity entity, RestTask.TaskListener<RestTask<MxmWorkSpaceResult, ErrorResult>, ErrorResult> taskListener) {
//
//        return new RestTask<MxmWorkSpaceResult, ErrorResult>(taskListener) {
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<MxmWorkSpaceResult> coracleCallback = new SyncCoracleCallback<>();
//
//                MxmWorkSpaceResult execute = coracleCallback.execute(mMxmApiService.getWebApplication(entity));
//                if (execute != null) {
//                    setmTag(execute);
//                }
//                return coracleCallback.getErrorResult();
//            }
//        };
//    }
//
//    /**
//     * 取消订阅
//     *
//     * @param entity
//     * @param xCallback
//     */
//    public void cancelSubscription(MxmCancelApplicationSubscriptionEntity entity, final XCallback<Object, ErrorResult> xCallback) {
//        getCancelSubscriptionTask(entity, new BusinessControllerHandler<Object>() {
//
//            @Override
//            public void onSuccess(Object o, ErrorResult code) {
//                xCallback.onSuccess(o);
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                xCallback.onFail(code);
//            }
//        }).execute();
//    }
//
//    private RestTask<Object, ErrorResult> getCancelSubscriptionTask(final MxmCancelApplicationSubscriptionEntity entity, RestTask.TaskListener<RestTask<Object, ErrorResult>, ErrorResult> taskListener) {
//
//        return new RestTask<Object, ErrorResult>(taskListener) {
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<Object> coracleCallback = new SyncCoracleCallback<>();
//                Object execute = coracleCallback.execute(mMxmApiService.cancelSubscription(entity));
//                if (execute != null) {
//                    setmTag(execute);
//                }
//
//                return coracleCallback.getErrorResult();
//            }
//        };
//    }
//
//    public void checkDeviceStatus(RequestCheckDeviceStatusParam requestCheckDeviceStatusParam, final IController<DeciceStatusResult, Integer> iCheckDeviceStatusCallback) {
//        RestTask<DeciceStatusResult, Integer> checkDeviceStatusTask = getCheckDeviceStatusTask(requestCheckDeviceStatusParam, new RestTask.TaskListener<RestTask<DeciceStatusResult, Integer>, Integer>() {
//            @Override
//            public void onComplete(RestTask<DeciceStatusResult, Integer> stringRestTask, Integer value) {
//                if (value != SYSTEM_CODE_SUCCESS) {
//                    iCheckDeviceStatusCallback.onFail(value);
//                    return;
//                }
//
//                iCheckDeviceStatusCallback.onSuccess(stringRestTask.getmTag(), value);
//            }
//        });
//
//        checkDeviceStatusTask.execute();
//    }
//
//    /**
//     * 修改密码
//     *
//     * @param updatePwdEntity
//     * @param xCallback
//     */
//    public void updatePwd(MxmUpdatePwdEntity updatePwdEntity, final XCallback<String, ErrorResult> xCallback) {
//        getUpdatePwdTask(updatePwdEntity, new BusinessControllerHandler<Object>() {
//
//            @Override
//            public void onSuccess(Object o, ErrorResult code) {
//                xCallback.onSuccess(code.getMessage());
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                xCallback.onFail(code);
//            }
//        }).execute();
//
//    }
//
//    /**
//     * @param uid
//     * @param xCallback
//     */
//    public void loadEmpInfo(String uid, final XCallback<Member, ErrorResult> xCallback) {
//        MxmEmpDetailsEntity mxmEmpDetailsEntity = new MxmEmpDetailsEntity();
//        mxmEmpDetailsEntity.setEmpId(uid);
//
//        RestTask<Member, ErrorResult> loadEmpInfoTask = getLoadEmpInfoTask(mxmEmpDetailsEntity, new BusinessControllerHandler<Member>() {
//            @Override
//            public void onSuccess(Member member, ErrorResult code) {
//                if (xCallback != null) {
//                    xCallback.onSuccess(member);
//                }
//            }
//
//            @Override
//            public void onFail(ErrorResult errorResult) {
//                if (xCallback != null) {
//                    xCallback.onFail(errorResult);
//                }
//            }
//        });
//        loadEmpInfoTask.execute();
//
//    }
//
//    private RestTask<Member, ErrorResult> getLoadEmpInfoTask(final MxmEmpDetailsEntity entity, RestTask.TaskListener<RestTask<Member, ErrorResult>, ErrorResult> taskListener) {
//
//        return new RestTask<Member, ErrorResult>(taskListener) {
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<Member> coracleCallback = new SyncCoracleCallback<>();
//
//                Member execute = coracleCallback.execute(mMxmApiService.getEmpDetails(entity));
//                if (execute != null) {
//                    setmTag(execute);
//                }
//                return coracleCallback.getErrorResult();
//            }
//        };
//    }
//
//
//    /**
//     * 设置或取消
//     *
//     * @param tag
//     * @param uid
//     * @param xCallback
//     */
//    public void setfrequentlyContact(@MxmYNState String tag, String uid, final XCallback<String, ErrorResult> xCallback) {
//        MxmSetfrequentlyContactEntity mxmSetfrequentlyContactEntity = new MxmSetfrequentlyContactEntity();
//        mxmSetfrequentlyContactEntity.setEmpId(uid);
//        mxmSetfrequentlyContactEntity.setStatus(tag);
//        RestTask<Object, ErrorResult> restTask = getsetfrequentlyContactTask(mxmSetfrequentlyContactEntity, new BusinessControllerHandler<Object>() {
//            @Override
//            public void onSuccess(Object o, ErrorResult code) {
//                xCallback.onSuccess("");
//            }
//
//            @Override
//            public void onFail(ErrorResult errorResult) {
//                xCallback.onFail(errorResult);
//            }
//        });
//        restTask.execute();
//
//
//    }
//
//    private RestTask<Object, ErrorResult> getsetfrequentlyContactTask(final MxmSetfrequentlyContactEntity entity, RestTask.TaskListener<RestTask<Object, ErrorResult>, ErrorResult> taskListener) {
//
//        return new RestTask<Object, ErrorResult>(taskListener) {
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<Object> coracleCallback = new SyncCoracleCallback<>();
//
//                Object execute = coracleCallback.execute(mMxmApiService.setfrequentlyContact(entity));
//                ErrorResult errorResult = coracleCallback.getErrorResult();
//                if (ErrorResult.SERVER_SUCCESS_CODE == errorResult.getCode()) {
//                    setmTag(execute);
//                }
//                return errorResult;
//            }
//        };
//    }
//
//    /**
//     * 更新用户信息
//     *
//     * @param user
//     * @param callback
//     */
//    public void updateUser(Member user, final XCallback<Object, ErrorResult> callback) {
//        getUpdateUserInfoTask(user, new BusinessControllerHandler<Object>() {
//
//            @Override
//            public void onSuccess(Object o, ErrorResult code) {
//                callback.onSuccess(o);
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                callback.onFail(code);
//            }
//        }).execute();
//    }
//
//    public void loadSoftInfo(String key, BusinessHandler<AppInfoEntity> loadUserHandler) {
//        getLoadSoftInfoTask(key, loadUserHandler).execute();
//    }
//
////    public void help(BusinessHandler<HelpEntity> logoutHandler) {
////        getHelpTask(logoutHandler).execute();
////    }
//
////    //添加收藏
////    public void addToFavourites(CollectStatus collectStatus, Callback<CollectResult> callback) {
////        mMchlApiService.addFavorites(collectStatus).enqueue(callback);
////    }
////
////    //获取收藏
////    public void getForFavourites(GetCollectionEntity getCollectStatus, Callback<GetCollectionResult> callback) {
////        mMchlApiService.getFavourites(getCollectStatus).enqueue(callback);
////    }
////
////    //删除收藏
////    public void deleteCollection(DeleteCollectionEntity deleteCollectStatus, Callback<DeleteCollectResult> callback) {
////        mMchlApiService.deletFavourites(deleteCollectStatus).enqueue(callback);
////    }
//
//    /**
//     * 获取自定义表情
//     *
//     * @param startIndex
//     * @param pageSize
//     * @param callback
//     */
//    public void findEmoticonList(int startIndex, int pageSize, final XCallback<MxmFindEmoticonResult, ErrorResult> callback) {
//
//        MxmPagingEntity entity = new MxmPagingEntity();
//        entity.setLimit(pageSize);
//        entity.setPage(startIndex);
//        getFindEmoticonListTask(entity, new BusinessControllerHandler<MxmFindEmoticonResult>() {
//            @Override
//            public void onSuccess(MxmFindEmoticonResult mxmFindEmoticonResults, ErrorResult code) {
//                callback.onSuccess(mxmFindEmoticonResults);
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                callback.onFail(code);
//            }
//        }).execute();
//
//    }
//
//    public void Json2xml(String json, BusinessHandler<String> logoutHandler) {
//        json2xml(json, logoutHandler).execute();
//    }
//
//    public void Xml2json(String xml, BusinessHandler<String> logoutHandler) {
//        xml2json(xml, logoutHandler).execute();
//    }
//
//    public void getDVC(int width, int height, int[] code, BusinessHandler<String> logoutHandler) {
//        getDVCTask(width, height, code, logoutHandler).execute();
//    }
//
//    public void checkDVC(int[] code, BusinessHandler<String> logoutHandler) {
//        checkDVCTask(code, logoutHandler).execute();
//    }
//
//    public void getBenner1(BusinessHandler<BannerResult> bennerHandler) {
//        getBannerTask1(bennerHandler).execute();
//    }
//
//    public void getBenner(BusinessHandler<String> bennerHandler) {
//        getBannerTask(bennerHandler).execute();
//    }
//
//    public void feedback(String phone, String content, BusinessHandler<String> bennerHandler) {
//        getFeedbackTask(phone, content, bennerHandler).execute();
//    }
//
//
//    /**
//     * 获取意见反馈列表
//     *
//     * @param page
//     * @param size
//     * @param callback
//     */
//    public void getFeedBackList(int page, int size, final XCallback<List<MxmFeedBackListResult>, ErrorResult> callback) {
//        MxmPagingEntity entity = new MxmPagingEntity();
//        entity.setLimit(size);
//        entity.setPage(page);
//
//        getFeekbackListTask(entity, new BusinessControllerHandler<List<MxmFeedBackListResult>>() {
//            @Override
//            public void onSuccess(List<MxmFeedBackListResult> mxmFeedBackListResult, ErrorResult code) {
//                callback.onSuccess(mxmFeedBackListResult);
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                callback.onFail(code);
//            }
//        }).execute();
//    }
//
//    /**
//     * 意见反馈详情
//     *
//     * @param id
//     * @param xCallback
//     */
//    public void getDetail(String id, final XCallback<MxmFeedBackDetailResult, ErrorResult> xCallback) {
//
//        MxmFeedBackDetailEntity entity = new MxmFeedBackDetailEntity(id);
//
//        getDetailTask(entity, new BusinessControllerHandler<MxmFeedBackDetailResult>() {
//            @Override
//            public void onSuccess(MxmFeedBackDetailResult mxmFeedBackDetailResult, ErrorResult code) {
//                xCallback.onSuccess(mxmFeedBackDetailResult);
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                xCallback.onFail(code);
//            }
//        }).execute();
//    }
//
//
//    //提交意见反馈
//    public void saveBack(MxmSaveFeedbackEntity entity, final XCallback<Object, ErrorResult> callback) {
//        saveBackTask(entity, new BusinessControllerHandler<Object>() {
//
//            @Override
//            public void onSuccess(Object o, ErrorResult code) {
//                callback.onSuccess(o);
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                callback.onFail(code);
//            }
//        }).execute();
//    }
//
//    /**
//     * 获取分享的二维码
//     *
//     * @param xCallback
//     */
//    public void getShareCode(final XCallback<MxmShareCodeResult, ErrorResult> xCallback) {
//        MxmShareCodeEntity entity = new MxmShareCodeEntity(mParameter.appKey);
//        getShareTask(entity, new BusinessControllerHandler<MxmShareCodeResult>() {
//            @Override
//            public void onSuccess(MxmShareCodeResult mxmShareCodeResult, ErrorResult code) {
//                xCallback.onSuccess(mxmShareCodeResult);
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                xCallback.onFail(code);
//            }
//        }).execute();
//    }
//
//    /**
//     * 获取系统配置
//     *
//     * @param xCallback
//     */
//    public void appSetting(final XCallback<MxmInitResult, ErrorResult> xCallback) {
//        appSetting(new BusinessControllerHandler<MxmInitResult>() {
//            @Override
//            public void onSuccess(MxmInitResult mxmShareCodeResult, ErrorResult code) {
//                xCallback.onSuccess(mxmShareCodeResult);
//            }
//
//            @Override
//            public void onFail(ErrorResult code) {
//                xCallback.onFail(code);
//            }
//        }).execute();
//    }
//
//    //验证用户是否绑定手机
//    public void userCode(String userCode, BusinessHandler<String> bennerHandler) {
//        userCodeTask(userCode, bennerHandler).execute();
//    }
//
//    //发送短信验证码
//    public void mongateSend(String phone, String type, BusinessHandler<String> bennerHandler) {
//        mongateSendTask(phone, type, bennerHandler).execute();
//    }
//
//    //短信验证码验证
//    public void register(String phone, String code, BusinessHandler<String> bennerHandler) {
//        registerTask(phone, code, bennerHandler).execute();
//    }
//
//    //重设密码
//    public void forgetPwd(String userCode, String code, String pwd, BusinessHandler<String> bennerHandler) {
//        forgetPwdTask(userCode, code, pwd, bennerHandler).execute();
//    }
//
//    private RestTask<DeciceStatusResult, Integer> getCheckDeviceStatusTask(final RequestCheckDeviceStatusParam requestCheckDeviceStatusParam, RestTask.TaskListener<RestTask<DeciceStatusResult, Integer>, Integer> taskListener) {
//        return new RestTask<DeciceStatusResult, Integer>(taskListener) {
//            @Override
//            protected Integer doInBackground(Void... params) {
//                Response<DeciceStatusResult> checkDeviceStatusResponse = null;
//
//                try {
//                    checkDeviceStatusResponse = mMxmApiService.checkDeviceStatus(requestCheckDeviceStatusParam.getKey(), requestCheckDeviceStatusParam.getToken()).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_CHECK_DEVICE_STATUS_FAIL;
//                }
//
//                if (!checkDeviceStatusResponse.isSuccessful()) {
//                    return SYSTEM_CODE_CHECK_DEVICE_STATUS_FAIL;
//                }
//
//                DeciceStatusResult netDepartmentResult = checkDeviceStatusResponse.body();
//
//                setmTag(netDepartmentResult);
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//

//
//
//    private RestTask<Object, ErrorResult> getUpdatePwdTask(final MxmUpdatePwdEntity updatePwdEntity, final RestTask.TaskListener<RestTask<Object, ErrorResult>, ErrorResult> taskListener) {
//        return new RestTask<Object, ErrorResult>(taskListener) {
//
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<Object> coracleCallback = new SyncCoracleCallback<>();
//                Object execute = coracleCallback.execute(mMxmApiService.updatePwd(updatePwdEntity));
//                ErrorResult errorResult = coracleCallback.getErrorResult();
//                setmTag(execute);
//
//                return errorResult;
//            }
//        };
//    }
//
//
//    private RestTask<Object, ErrorResult> getUpdateUserInfoTask(final Member user, final RestTask.TaskListener<RestTask<Object, ErrorResult>, ErrorResult> taskListener) {
//        return new RestTask<Object, ErrorResult>(taskListener) {
//
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                MxmUpdateEmpInfoEntity mxmUpdateEmpInfoEntity = new MxmUpdateEmpInfoEntity();
//                mxmUpdateEmpInfoEntity.setImageAddress(user.getImageAddress());
//                mxmUpdateEmpInfoEntity.setEmail(user.getEmail());
//                mxmUpdateEmpInfoEntity.setId(user.getId());
//                mxmUpdateEmpInfoEntity.setPhone(user.getPhone());
//                mxmUpdateEmpInfoEntity.setTelephone(user.getTelephone());
//                mxmUpdateEmpInfoEntity.setUserConfigInfoVoList(user.getNetMemberExtendField());
//
//                SyncCoracleCallback<Object> syncCoracleCallback = new SyncCoracleCallback<>();
//                Object execute = syncCoracleCallback.execute(mMxmApiService.updateEmpInfo(mxmUpdateEmpInfoEntity));
//                ErrorResult errorResult = syncCoracleCallback.getErrorResult();
//                if (errorResult.getCode() == ErrorResult.SERVER_SUCCESS_CODE) {
//                    setmTag(execute);
//                }
//                return errorResult;
//            }
//        };
//    }
//
//    private RestTask<AppInfoEntity, Integer> getLoadSoftInfoTask(final String key, final RestTask.TaskListener<RestTask<AppInfoEntity, Integer>, Integer> taskListener) {
//        return new RestTask<AppInfoEntity, Integer>(taskListener) {
//
//            @Override
//            protected Integer doInBackground(Void... params) {
//
//                Response<AppInfoEntity> loadSoftResponse = null;
//                try {
//                    loadSoftResponse = mMxmApiService.getAppInfo(key).execute();
//                    if (loadSoftResponse == null) {
//                        return SYSTEM_CODE_LOAD_SOFT_INFO_FAIL;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_LOAD_SOFT_INFO_FAIL;
//                }
//                if (!loadSoftResponse.isSuccessful()) {
//                    return SYSTEM_CODE_LOAD_SOFT_INFO_FAIL;
//                }
//
//                AppInfoEntity loadSoftResult = loadSoftResponse.body();
//
//                setmTag(loadSoftResult);
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
////    private RestTask<HelpEntity, Integer> getHelpTask(final RestTask.TaskListener<RestTask<HelpEntity, Integer>, Integer> taskListener) {
////        return new RestTask<HelpEntity, Integer>(taskListener) {
////
////            @Override
////            protected Integer doInBackground(Void... params) {
////
////                Response<HelpEntity> helpResponse = null;
////                try {
////                    helpResponse = mMxmApiService.help().execute();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                    return SYSTEM_CODE_HELP_FAIL;
////                }
////                if (!helpResponse.isSuccessful()) {
////                    return SYSTEM_CODE_HELP_FAIL;
////                }
////
////                HelpEntity helpResult = helpResponse.body();
////
////                setmTag(helpResult);
////
////                return SYSTEM_CODE_SUCCESS;
////            }
////        };
////    }
//
//    //获取意见反馈列表
//    private RestTask<List<MxmFeedBackListResult>, ErrorResult> getFeekbackListTask(final MxmPagingEntity entity, final RestTask.TaskListener<RestTask<List<MxmFeedBackListResult>, ErrorResult>, ErrorResult> taskListener) {
//        return new RestTask<List<MxmFeedBackListResult>, ErrorResult>(taskListener) {
//
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<List<MxmFeedBackListResult>> coracleCallback = new SyncCoracleCallback<>();
//                List<MxmFeedBackListResult> execute = coracleCallback.execute(mMxmApiService.getFeedBackList(entity));
//                ErrorResult errorResult = coracleCallback.getErrorResult();
//                if (execute != null) {
//                    setmTag(execute);
//                }
//                return errorResult;
//            }
//        };
//    }
//
//
//    private RestTask<MxmFeedBackDetailResult, ErrorResult> getDetailTask(final MxmFeedBackDetailEntity entity, final RestTask.TaskListener<RestTask<MxmFeedBackDetailResult, ErrorResult>, ErrorResult> taskListener) {
//        return new RestTask<MxmFeedBackDetailResult, ErrorResult>(taskListener) {
//
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<MxmFeedBackDetailResult> coracleCallback = new SyncCoracleCallback<>();
//                MxmFeedBackDetailResult mxmFeedBackDetailResult = coracleCallback.execute(mMxmApiService.getFeedBackDetail(entity));
//                if (mxmFeedBackDetailResult != null) {
//                    setmTag(mxmFeedBackDetailResult);
//                }
//                return coracleCallback.getErrorResult();
//            }
//        };
//    }
//
//    private RestTask<Object, ErrorResult> saveBackTask(final MxmSaveFeedbackEntity entity, final RestTask.TaskListener<RestTask<Object, ErrorResult>, ErrorResult> taskListener) {
//        return new RestTask<Object, ErrorResult>(taskListener) {
//
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<Object> coracleCallback = new SyncCoracleCallback<>();
//                Object execute = coracleCallback.execute(mMxmApiService.saveFeedBack(entity));
//                ErrorResult errorResult = coracleCallback.getErrorResult();
//                if (errorResult.getCode() == ErrorResult.SERVER_SUCCESS_CODE) {
//                    setmTag(execute);
//                }
//                return errorResult;
//            }
//        };
//    }
//
//    private RestTask<MxmInitResult, ErrorResult> appSetting(final RestTask.TaskListener<RestTask<MxmInitResult, ErrorResult>, ErrorResult> taskListener) {
//        return new RestTask<MxmInitResult, ErrorResult>(taskListener) {
//
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<MxmInitResult> coracleCallback = new SyncCoracleCallback<>();
//                MxmInitResult mxmInitResult = coracleCallback.execute(mMxmApiService.appSetting());
//                ErrorResult errorResult = coracleCallback.getErrorResult();
//                if (errorResult.getCode() == ErrorResult.SERVER_SUCCESS_CODE) {
//                    setmTag(mxmInitResult);
//                }
//                return errorResult;
//            }
//        };
//    }
//
//    //获取分享二维码
//    private RestTask<MxmShareCodeResult, ErrorResult> getShareTask(final MxmShareCodeEntity mxmShareCodeEntity, final RestTask.TaskListener<RestTask<MxmShareCodeResult, ErrorResult>, ErrorResult> taskListener) {
//        return new RestTask<MxmShareCodeResult, ErrorResult>(taskListener) {
//
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<MxmShareCodeResult> coracleCallback = new SyncCoracleCallback<>();
//                MxmShareCodeResult execute = coracleCallback.execute(mMxmApiService.getShareCode(mxmShareCodeEntity));
//                ErrorResult errorResult = coracleCallback.getErrorResult();
//                if (execute != null) {
//                    setmTag(execute);
//
//                }
//                return errorResult;
//            }
//        };
//    }
//
//    //验证用户是否存在
//    private RestTask<String, Integer> userCodeTask(final String userCode, final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//
//            @Override
//            protected Integer doInBackground(Void... params) {
//
//                Response<String> ListResponse = null;
//                try {
//                    ListResponse = mMxmApiService.userCode(userCode).execute();
//                    if (ListResponse == null) {
//                        return SYSTEM_CODE_HELP_FAIL;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_HELP_FAIL;
//                }
//                if (!ListResponse.isSuccessful()) {
//                    return SYSTEM_CODE_HELP_FAIL;
//                }
//
//                String listResult = ListResponse.body();
//
//                setmTag(listResult);
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    //获取验证码
//    private RestTask<String, Integer> mongateSendTask(final String phone, final String type, final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//
//            @Override
//            protected Integer doInBackground(Void... params) {
//
//                Response<String> ListResponse = null;
//                try {
//                    ListResponse = mMxmApiService.mongateSend(phone, type).execute();
//                    if (ListResponse == null) {
//                        return SYSTEM_CODE_HELP_FAIL;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_HELP_FAIL;
//                }
//                if (!ListResponse.isSuccessful()) {
//                    return SYSTEM_CODE_HELP_FAIL;
//                }
//
//                String listResult = ListResponse.body();
//
//                setmTag(listResult);
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    //校验验证码
//    private RestTask<String, Integer> registerTask(final String phone, final String code, final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//
//            @Override
//            protected Integer doInBackground(Void... params) {
//
//                Response<String> ListResponse = null;
//                try {
//                    ListResponse = mMxmApiService.register(phone, code).execute();
//                    if (ListResponse == null) {
//                        return SYSTEM_CODE_HELP_FAIL;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_HELP_FAIL;
//                }
//                if (!ListResponse.isSuccessful()) {
//                    return SYSTEM_CODE_HELP_FAIL;
//                }
//
//                String listResult = ListResponse.body();
//
//                setmTag(listResult);
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    //重新设置密码
//    private RestTask<String, Integer> forgetPwdTask(final String userCode, final String code, final String pwd, final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//
//            @Override
//            protected Integer doInBackground(Void... params) {
//
//                Response<String> ListResponse = null;
//                try {
//                    ListResponse = mMxmApiService.forgetPwd(userCode, code, pwd).execute();
//                    if (ListResponse == null) {
//                        return SYSTEM_CODE_HELP_FAIL;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_HELP_FAIL;
//                }
//                if (!ListResponse.isSuccessful()) {
//                    return SYSTEM_CODE_HELP_FAIL;
//                }
//
//                String listResult = ListResponse.body();
//
//                setmTag(listResult);
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    private RestTask<MxmFindEmoticonResult, ErrorResult> getFindEmoticonListTask(final MxmPagingEntity entity, final RestTask.TaskListener<RestTask<MxmFindEmoticonResult, ErrorResult>, ErrorResult> taskListener) {
//        return new RestTask<MxmFindEmoticonResult, ErrorResult>(taskListener) {
//
//            @Override
//            protected ErrorResult doInBackground(Void... params) {
//
//                SyncCoracleCallback<MxmFindEmoticonResult> coracleCallback = new SyncCoracleCallback<>();
//                MxmFindEmoticonResult findEmoticonResult = coracleCallback.execute(mMxmApiService.findEmoticonList(entity));
//                ErrorResult errorResult = coracleCallback.getErrorResult();
//                if (findEmoticonResult != null) {
//                    setmTag(findEmoticonResult);
//                }
//
//                return errorResult;
//            }
//        };
//    }
//
//    private RestTask<String, Integer> json2xml(final String json, final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//
//            @Override
//            protected Integer doInBackground(Void... params) {
//                Response<String> json2xmlResponse = null;
//                try {
//                    json2xmlResponse = mMxmApiService.json2xml(json).execute();
//                    if (json2xmlResponse == null) {
//                        return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//                if (!json2xmlResponse.isSuccessful()) {
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//
//                String json2xmlResult = json2xmlResponse.body();
//
//                setmTag(json2xmlResult);
//
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    private RestTask<String, Integer> xml2json(final String xml, final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//
//            @Override
//            protected Integer doInBackground(Void... params) {
//                Response<String> xml2jsonResponse = null;
//                try {
//                    xml2jsonResponse = mMxmApiService.xml2json(xml).execute();
//                    if (xml2jsonResponse == null) {
//                        return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//                if (!xml2jsonResponse.isSuccessful()) {
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//
//                String xml2jsonResult = xml2jsonResponse.body();
//
//                setmTag(xml2jsonResult);
//
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    private RestTask<String, Integer> getDVCTask(final int width, final int height, final int[] code, final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//
//            @Override
//            protected Integer doInBackground(Void... params) {
//                Response<String> getDVCResponse = null;
//                try {
//                    getDVCResponse = mMxmApiService.getDVC(width, height, code).execute();
//                    if (getDVCResponse == null) {
//                        return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//                if (!getDVCResponse.isSuccessful()) {
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//
//                String getDVCResult = getDVCResponse.body();
//
//                setmTag(getDVCResult);
//
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    private RestTask<String, Integer> checkDVCTask(final int[] code, final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//
//            @Override
//            protected Integer doInBackground(Void... params) {
//                Response<String> checkDVCResponse = null;
//                try {
//                    checkDVCResponse = mMxmApiService.checkDVC(code).execute();
//                    if (checkDVCResponse == null) {
//                        return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//                if (!checkDVCResponse.isSuccessful()) {
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//
//                String checkDVCResult = checkDVCResponse.body();
//
//                setmTag(checkDVCResult);
//
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//
//    private RestTask<BannerResult, Integer> getBannerTask1(final RestTask.TaskListener<RestTask<BannerResult, Integer>, Integer> taskListener) {
//        return new RestTask<BannerResult, Integer>(taskListener) {
//            @Override
//            protected Integer doInBackground(Void... params) {
//                Response<BannerResult> bannerResponse = null;
//
//                try {
//                    bannerResponse = mMxmApiService.getBanner1().execute();
//                    if (bannerResponse == null) {
//                        return SYSTEM_CODE_GET_BANNER_FAIL;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_GET_BANNER_FAIL;
//                }
//
//                if (!bannerResponse.isSuccessful()) {
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//
//                BannerResult banner = bannerResponse.body();
//
//                setmTag(banner);
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    private RestTask<String, Integer> getBannerTask(final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//            @Override
//            protected Integer doInBackground(Void... params) {
//                Response<String> bannerResponse = null;
//
//                try {
//                    bannerResponse = mMxmApiService.getBanner().execute();
//                    if (bannerResponse == null) {
//                        return SYSTEM_CODE_GET_BANNER_FAIL;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_GET_BANNER_FAIL;
//                }
//
//                if (!bannerResponse.isSuccessful()) {
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//
//                String banner = bannerResponse.body();
//
//                setmTag(banner);
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    private RestTask<String, Integer> getFeedbackTask(final String phone, final String content, final RestTask.TaskListener<RestTask<String, Integer>, Integer> taskListener) {
//        return new RestTask<String, Integer>(taskListener) {
//            @Override
//            protected Integer doInBackground(Void... params) {
//                Response<String> feedbackResponse = null;
//
//                try {
//                    feedbackResponse = mMxmApiService.feedback(mParameter.appKey
//                            , PubConstant.config.PLATFORM
//                            , mParameter.userName, phone, content
//                            , mCt.getPackageName())
//                            .execute();
//                    if (feedbackResponse == null) {
//                        return SYSTEM_CODE_GET_BANNER_FAIL;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return SYSTEM_CODE_GET_BANNER_FAIL;
//                }
//
//                if (!feedbackResponse.isSuccessful()) {
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//
//                if (feedbackResponse.body() == null) {
//                    return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                }
//
//                try {
//                    JSONObject json = new JSONObject(feedbackResponse.body());
//                    if (json.optInt("code", 0) != 200) {
//                        return SYSTEM_CODE_LOGOUT_MXM_FAIL;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                String banner = feedbackResponse.body();
//
//                setmTag(banner);
//
//                return SYSTEM_CODE_SUCCESS;
//            }
//        };
//    }
//
//    /**
//     * @param map
//     */
//    public void verifySign(Map<String, String> map) {
//
//        mMxmApiService.verifySign("http://192.168.2.53:8080/ss/sample/verify_sign", map).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//                if (!response.isSuccessful()) {
//                    return;
//                }
//                String body = response.body();
//                if (body == null) {
//                    return;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//            }
//        });
//    }
//
//    public void fetchData(Map<String, String> map) {
//        mMxmApiService.fetchData("http://192.168.2.53:8080/ss/sample/fetch_data", map).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (!response.isSuccessful()) {
//                    return;
//                }
//                String body = response.body();
//                if (body == null) {
//                    return;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//            }
//        });
//    }
//
//
//    public void getSign(final CallBack<String, String> callBack) {
//        mMxmApiService.getSign("http://192.168.2.53:8080/ss/sample/get_sign").enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (!response.isSuccessful()) {
//                    callBack.onFail("false");
//                    return;
//                }
//                String body = response.body();
//                if (body == null) {
//                    callBack.onFail("body is null");
//                    return;
//                }
//                callBack.onSuccess(body);
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                callBack.onFail(t.getMessage());
//            }
//        });
//    }
//
//    public void getTodoList(String id, String type, Callback<GetTodoListResult> callback) {
//        mMxmApiService.getTodoList(id, type).enqueue(callback);
//    }
//
//
//    /**
//     * 获取皮肤列表接口
//     *
//     * @param callback
//     */
//    public void getSkinList(final XCallback<List<MxmSkinDetailResult>, ErrorResult> callback) {
//        mMxmApiService.getSkinList().enqueue(new CoracleCallback<List<MxmSkinDetailResult>>() {
//            @Override
//            public void onSuccess(List<MxmSkinDetailResult> mxmSkinDetailResult) {
//                if (mxmSkinDetailResult == null || mxmSkinDetailResult.isEmpty()) {
//                    callback.onFail(ErrorResult.error(ErrorResult.ERROR_DATA_EMPTY));
//                    return;
//                }
//
//                callback.onSuccess(mxmSkinDetailResult);
//            }
//
//            @Override
//            public void onFailed(ErrorResult errorResult) {
//                callback.onFail(errorResult);
//            }
//        });
//    }
//
//    /**
//     * 获取节日皮肤列表
//     *
//     * @param callback
//     */
//    public void getHolidaySkinList(final XCallback<List<MxmSkinDetailResult>, ErrorResult> callback) {
//        mMxmApiService.getHolidaySkinList().enqueue(new CoracleCallback<List<MxmSkinDetailResult>>() {
//            @Override
//            public void onSuccess(List<MxmSkinDetailResult> mxmSkinDetailResult) {
//                if (mxmSkinDetailResult == null || mxmSkinDetailResult.isEmpty()) {
//                    callback.onFail(ErrorResult.error(ErrorResult.ERROR_DATA_EMPTY));
//                    return;
//                }
//
//                callback.onSuccess(mxmSkinDetailResult);
//            }
//
//            @Override
//            public void onFailed(ErrorResult errorResult) {
//                callback.onFail(errorResult);
//            }
//        });
//
//    }
//
//    /**
//     * mxm上传
//     *
//     * @param url
//     * @param body
//     * @return
//     * @throws IOException
//     */
//    public String uploadFilesMxm(String url, RequestBody body) throws IOException {
//        //一定不能用 MxmApiService,会加密
//        Call<String> call = Api.mxmFileService().uploadFiles(url, body);
//        Response<String> execute = call.execute();
//        if (execute == null) {
//            return null;
//        }
//        return execute.body();
//    }
//
//
//    /**
//     * 下载
//     *
//     * @param url
//     * @return
//     * @throws IOException
//     */
//    public ResponseBody downloads(String url) throws IOException {
//        if (TextUtils.isEmpty(url)) {
//            return null;
//        }
//        //不要用 MxmApiService 会导致加密
//        // mMxmApiService
//
//        Call<ResponseBody> bodyCall = Api.mxmFileService().doDownLoadGet(url);
//
//        Response<ResponseBody> execute = bodyCall.execute();
//        if (execute == null) {
//            return null;
//        }
//        ResponseBody body = execute.body();
//        if (body == null)
//            return null;
//        return body;
//    }
}
