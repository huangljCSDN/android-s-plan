package com.networkengine.controller;

import android.content.Context;

import com.networkengine.AsyncUtil.RestTask;
import com.networkengine.controller.callback.ErrorResult;
import com.networkengine.controller.callback.XCallback;
import com.networkengine.database.XDbManager;
import com.networkengine.engine.EngineParameter;
import com.networkengine.entity.LoginInfo;
import com.networkengine.httpApi.Api;
import com.networkengine.httpApi.MchlApiService;
import com.networkengine.mqtt.MqttService;

import retrofit2.Call;

/**
 * 业务层控制器
 */
public class BusinessController {

    protected Context mCt;

    protected MchlApiService mMchlApiService;

//    protected MxmApiService mMxmApiService;

    protected MqttService mMqttService;

    public EngineParameter mParameter;

    protected XDbManager mXDbManager;

    /**
     * 业务处理回调
     *
     * @param <T>      处理结果数据类型
     * @param <Result> 处理结果标示
     */
    public interface IController<T, Result> {
        void onSuccess(T t, Result code);

        void onFail(Result code);


    }

    /**
     * 公共业务回调
     * <T> 处理结果数据类型
     */
    public static abstract class BusinessHandler<T> implements IController<T, Integer>, RestTask.TaskListener<RestTask<T, Integer>, Integer> {

        public static final int SUCCESS = 0;

        @Override
        public void onComplete(RestTask<T, Integer> tRestTask, Integer code) {
            if (code != SUCCESS) {
                this.onFail(code);
                return;
            }

            this.onSuccess(tRestTask.getmTag(), code);
        }
    }

    public static abstract class BusinessControllerHandler<T> implements IController<T, ErrorResult>, RestTask.TaskListener<RestTask<T, ErrorResult>, ErrorResult> {


        @Override
        public void onComplete(RestTask<T, ErrorResult> tRestTask, ErrorResult errorResult) {
            if (errorResult.getCode() != ErrorResult.SERVER_SUCCESS_CODE) {
                this.onFail(errorResult);
                return;
            }

            this.onSuccess(tRestTask.getmTag(), errorResult);
        }
    }


    /**
     * protected构造
     *
     * @param businessController 构造依据
     */
    protected BusinessController(BusinessController businessController) {
        clone(businessController);
    }

    public BusinessController() {
    }

    /**
     * protected构造
     *
     * @param context   上下文
     * @param parameter 公共参数
     */
    protected BusinessController(Context context, EngineParameter parameter) {
        mCt = context;

        mMqttService = MqttService.getInstance(context);

        mMchlApiService = Api.mchlService(parameter, null);

//        mMxmApiService = Api.mxmService(context, parameter);

        mXDbManager = XDbManager.getInstance(context);

        mParameter = parameter;
    }

    /**
     * 克隆方法
     *
     * @param businessController 克隆依据
     */
    protected void clone(BusinessController businessController) {
        mCt = businessController.mCt;

        mMqttService = businessController.mMqttService;

        mMchlApiService = businessController.mMchlApiService;

//        mMxmApiService = businessController.mMxmApiService;

        mXDbManager = businessController.mXDbManager;

        mParameter = businessController.mParameter;
    }

//    /**
//     * 服务是否可用
//     *
//     * @return
//     */
//    public boolean isAvailable() {
//        return mMxmApiService != null;
//    }

    /**
     * 业务引擎初始化方法
     *
     * @param ct        上下文
     * @param parameter 环境参数
     * @return
     */
    public static BusinessController init(Context ct
            , EngineParameter parameter
            , XCallback<LoginInfo, ErrorResult> callback) {
        InitController initController = new InitController(ct, parameter);
        initController.initialize(callback);
        return initController;
    }

    /**
     * 获得通讯录控制器
     *
     * @return
     */
//    public ContactController getContactController() {
//        return new ContactController(this);
//    }

    /**
     * @return 获得系统控制器
     */
    public SystemController getSystemController() {
        return new SystemController(this);
    }

    /**
     * @return 获得IM控制器
     */
    public IMController getIMController() {
        return new IMController(this);
    }


    /**
     * @return 获得文件传输控制器
     */
    public FileTransController getFileTransController() {
        return new FileTransController();
    }

//    /**
//     * @return 获得工作空间控制器
//     */
//    public WorkspaceController getWorkspaceController() {
//        return new WorkspaceController(this);
//    }

    /**
     * 统一错误回调
     *
     * @param callback
     * @param call
     * @param t        异常
     */
    protected final void onFailCallback(XCallback callback, Call call, Throwable t) {
        if (!call.isCanceled()) {
            if (callback != null) {
                callback.onFail(ErrorResult.error(t));
            }
        }
    }

}
