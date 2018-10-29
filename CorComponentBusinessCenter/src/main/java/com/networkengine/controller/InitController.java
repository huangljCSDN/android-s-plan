package com.networkengine.controller;

import android.content.Context;

import com.networkengine.PubConstant;
import com.networkengine.controller.callback.ErrorResult;
import com.networkengine.controller.callback.XCallback;
import com.networkengine.engine.EngineParameter;
import com.networkengine.entity.LoginInfo;
import com.networkengine.entity.LoginResult;
import com.networkengine.entity.RequestLoginParam;
import com.networkengine.httpApi.Api;
import com.networkengine.util.LogUtil;
import com.networkengine.util.PhoneUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.coracle.coragent.CORAgent;
//import com.networkengine.util.PhoneUtil;
//import com.networkengine.util.SharedPrefsHelper;

public class InitController extends BusinessController {


    protected InitController(Context ct, EngineParameter parameter) {
        super(ct, parameter);
    }

    public void initialize(final XCallback<LoginInfo, ErrorResult> callback) {

//        final MxmInitEntity mxmInitEntity = new MxmInitEntity();
//        mxmInitEntity.setAppKey(mParameter.appKey);
//        mxmInitEntity.setDeviceModel(Build.BRAND + " " + Build.MODEL);
//        mxmInitEntity.setDeviceName(Build.MODEL);
//        mxmInitEntity.setDeviceToken(PhoneUtil.getDeviceId(mCt));
//        mxmInitEntity.setDeviceType(PubConstant.config.PLATFORM.toLowerCase());
//        mxmInitEntity.setSystemVersion(Build.VERSION.RELEASE);
//        mxmInitEntity.setIsRoot(Check.checkIsRoot() ? "Y" : "N");
//        mxmInitEntity.setAppVersion(PhoneUtil.getAppVersionName(mCt));
//        mMxmApiService.mxmInit(mxmInitEntity).enqueue(new CoracleCallback<MxmInitResult>() {
//
//            @Override
//            public void onSuccess(MxmInitResult mxmInitResult) {
//                param = mxmInitResult;
//                loginMXM(callback);
//            }
//
//            @Override
//            public void onFailed(ErrorResult errorResult) {
//                callback.onFail(errorResult);
//            }
//        });
        loginMCHL(new LoginInfo(),callback);

    }


//    /**
//     * 登录 mxm 平台
//     */
//
//    private void loginMXM(final XCallback<LoginInfo, ErrorResult> callback) {
//
//        MxmLoginEntity entity = new MxmLoginEntity();
//        entity.setUserName(mParameter.userName);
//        entity.setPassWord(mParameter.pwd);
//        entity.setDeviceToken(PhoneUtil.getDeviceId(mCt));
//        entity.setAppKey(mParameter.appKey);
//
//
//        mMxmApiService.mxmLogin(entity).enqueue(new CoracleCallback<Member>() {
//            @Override
//            public void onSuccess(Member mxmMember) {
//                if (mxmMember == null) {
//                    callback.onFail(ErrorResult.error(ErrorResult.ERROR_DATA_EMPTY));
//                    return;
//                }
//                //mxm 4.0服务器去掉idzi
//                mxmMember.setId(mxmMember.getUserId());
//                CORAgent.addHttpCookies(Api.getInMemoryCookieStore().getCookies());
//                mParameter.gwAgent = mxmMember.getJwt();
//                LoginInfo loginInfo = new LoginInfo();
//                loginInfo.setUser(mxmMember);
//
//                bindDevice(loginInfo, callback);
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
//     * 绑定设备
//     *
//     * @param loginInfo
//     * @param callback
//     */
//    private void bindDevice(final LoginInfo loginInfo, final XCallback<LoginInfo, ErrorResult> callback) {
//        final MxmBindDeviceEntity mxmBindDeviceEntity = new MxmBindDeviceEntity();
//        mxmBindDeviceEntity.setAppKey(mParameter.appKey);
//        mxmBindDeviceEntity.setDeviceToken(PhoneUtil.getDeviceId(mCt));
//        mxmBindDeviceEntity.setDeviceType(PubConstant.config.PLATFORM.toLowerCase());
//        mMxmApiService.bindDevice(mxmBindDeviceEntity).enqueue(new CoracleCallback<MxmBindDeviceResult>() {
//            @Override
//            public void onSuccess(MxmBindDeviceResult mxmBindDeviceResult) {
//                loginMCHL(loginInfo, callback);
//            }
//
//            @Override
//            public void onFailed(ErrorResult errorResult) {
//                callback.onFail(errorResult);
//            }
//        });
//    }


    /**
     * 登录 mchl 平台
     */
    private void loginMCHL(final LoginInfo loginInfo
            , final XCallback<LoginInfo, ErrorResult> callback) {
        RequestLoginParam params = new RequestLoginParam();

        params.setLoginName(mParameter.userName);
        params.setPassword(mParameter.pwd);
        params.setAppKey(mParameter.appKey);
        params.setAppVersion(PhoneUtil.getAppVersionName(mCt));
        params.setImei(PhoneUtil.getDeviceId(mCt));
        params.setMacAddress(PhoneUtil.getMACAddress(mCt));
        params.setDeviceType(PubConstant.config.DEVICE);
        params.setOsType(PubConstant.config.PLATFORM);
        params.setOsVersion(PhoneUtil.getSDKVersionNumber()+"");

        mMchlApiService.login(params).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                LogUtil.i("LoginResult="+response);
                if (callback == null) {
                    return;
                }
                if (!response.isSuccessful()) {
                    callback.onFail(ErrorResult.error(response.code(), response.message()));

                    return;
                }
                LoginResult result = response.body();
                if (result.getCode() == null || !result.getCode().equals("0")) {
                    callback.onFail(ErrorResult.error(result.getCode(), result.getMsg()));
                    return;
                }

                // 合并 MXM && MCHL 接口数据
                loginInfo.getUser().setUserToken(result.getData().getAuthToken());

                // 添加 Token 到请求头
                mMchlApiService = Api.mchlService(mParameter, loginInfo.getUser());

                //loginInfo.setUrl(registerResult.getUrl());
                //   loginInfo.setVf(registerResult.getVf());
                loginInfo.setMchlApiService(mMchlApiService);
//                loginInfo.setMxmApiService(mMxmApiService);
                // loginInfo.setRemark(registerResult.getRemark());
                // loginInfo.setPositionNmae(member.getPositionName());
                //  loginInfo.setJob(member.getJob());
                //  loginInfo.setPositionPath(member.getPositionPath());
                //  loginInfo.setOrgPath(member.getOrgPath());

                callback.onSuccess(loginInfo);

                upateEmpHeads();
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                onFailCallback(callback, call, t);
            }
        });
    }


    /**
     * 获取更新用户头像的接口
     */
    private void upateEmpHeads() {

//        long lastTime = SharedPrefsHelper.get("lastTime", 0L);
//
//        mMxmApiService.upateEmpHeads(new MxmUpateEmpHeadsEntity(lastTime)).enqueue(new CoracleCallback<MxmUpateEmpHeadsResult>() {
//            @Override
//            public void onSuccess(MxmUpateEmpHeadsResult mxmUpateEmpHeadsResult) {
//                if (mxmUpateEmpHeadsResult == null) {
//                    return;
//                }
//                SharedPrefsHelper.put("lastTime", mxmUpateEmpHeadsResult.getLastTime());
//                List<String> updateList = mxmUpateEmpHeadsResult.getUpdateList();
//                if (updateList != null) {
//                    for (String ids : updateList) {
//                        if (TextUtils.isEmpty(ids)) {
//                            continue;
//                        }
//                        String type = String.format("%s%s", ids, PubConstant.glide_sign.USER_IOCN);
//                        SharedPrefsHelper.put(type, String.valueOf(System.currentTimeMillis()));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailed(ErrorResult errorResult) {
//            }
//        });
    }


}
