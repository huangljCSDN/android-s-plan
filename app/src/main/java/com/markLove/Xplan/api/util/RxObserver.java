package com.markLove.Xplan.api.util;

import com.markLove.Xplan.base.App;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.config.Constants;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by huanglingjun on 2018/5/16.
 */

public class RxObserver<T> implements Observer<T> {

    public RxObserver(RequestCallBack requestCallBack){
        this.requestCallBack = requestCallBack;
    }


    @Override
    public void onError(@NonNull Throwable e) {
        if (requestCallBack == null) return;
        if (e instanceof SocketTimeoutException) {
            requestCallBack.onFail("服务器连接超时");
        } else if (e instanceof ConnectException) {
            requestCallBack.onFail("服务器链接失败");
        } else if (e instanceof TimeoutException) {
            requestCallBack.onFail("网络超时");
        } else if (e instanceof UnknownHostException) {
            requestCallBack.onFail("未知主机错误");
        } else {
            requestCallBack.onFail("请求网络失败");
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if(requestCallBack == null) return;
        if(t instanceof BaseBean){
            BaseBean baseBean = (BaseBean)t;
            if (baseBean.Status == Constants.TOKEN_EXPIRED_CODE){
                if (App.getInstance().isLogin()){
                    App.getInstance().onTokenExpires();
                }
            } else {
                requestCallBack.onSuccess(t);
            }
        } else {
            requestCallBack.onSuccess(t);
        }
    }

    private void checkIsSuccess(T o) {
//        if (o instanceof BaseResponse) {
//            BaseResponse response = (BaseResponse) o;
//            String errorMessage = "未知错误";
//            switch (response.getCode()) {
//                case HttpInterfaceCode.NET_REUQEST_OK:
//                    success(o);
//                    return;
//                case HttpInterfaceCode.NET_REQUEST_ERROR:
//                    errorMessage = "请求错误";
//                    break;
//                case HttpInterfaceCode.NET_REQUEST_REJECT:
//                    errorMessage = "请求被拒绝";
//                    break;
//                case HttpInterfaceCode.NET_REQUEST_UNFOUND:
//                    errorMessage = "请求未响应";
//                    break;
//                case HttpInterfaceCode.NET_SERVICE_ERROR:
//                    errorMessage = "服务器错误";
//                    break;
//                case HttpInterfaceCode.NET_NUKNOWN_ERROR:
//                    errorMessage = "未知错误";
//                    break;
//            }
//        }
    }

    private RequestCallBack requestCallBack;
}
