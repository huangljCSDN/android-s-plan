package com.networkengine.controller.callback;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.networkengine.R;
import com.networkengine.entity.BaseResult;
import com.networkengine.entity.Result;
import com.networkengine.util.CoracleSdk;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Not Found 404
 */
public class ErrorResult {

    public static ErrorResult error(Result result) {
        if (result == null)
            throw new IllegalArgumentException("result == null");
        return error(result.getErrorCode(), result.getErrorMessage());
    }

    public static ErrorResult error(BaseResult result) {

        return error(result.getRes(), result.getMsg());
    }

    public static ErrorResult error(int errorCode) {
        return error(errorCode, null);
    }

    public static ErrorResult error(int errorCode, String errorMessage) {
        return new ErrorResult(errorCode, errorMessage);
    }

    public static ErrorResult error(String errorCode, String errorMessage) {
        if (!TextUtils.isEmpty(errorCode)
                && TextUtils.isDigitsOnly(errorCode)) {
            return new ErrorResult(Integer.parseInt(errorCode), errorMessage);
        } else {
            return new ErrorResult(ERROR_UNKNOWN, errorMessage);
        }
    }

    public static ErrorResult error(Throwable t) {
        if (t instanceof SocketTimeoutException) {
            return error(ERROR_TIMEOUT);
        } else if (t instanceof ConnectException) {
            return error(ERROR_CONNECT);
        } else if (t instanceof JsonParseException) {
            return error(ERROR_PARSE_DATA);
        } else if (t instanceof MalformedJsonException) {
            return error(ERROR_PARSE_DATA);
        } else if (t instanceof UnknownHostException) {
            return error(ERROR_UNKNOWN_HOST);
        } else {
            return error(ERROR_UNKNOWN);
        }
    }

    public static final int ERROR_UNKNOWN = -1;
    public static final int ERROR_DATA_EMPTY = 0x01;
    public static final int ERROR_REQUEST = 0x02;// 错误的请求
    public static final int ERROR_TIMEOUT = 0x100;// 请求连接超时
    public static final int ERROR_CONNECT = 0x101;// 网络连接失败
    public static final int ERROR_UNKNOWN_HOST = 0x102;// 当前网络不可用，请检查设置
    public static final int ERROR_PARSE_DATA = 0x201;// 数据解析错误

    /**
     * 服务器错误码成功 经过本地转化
     */
    public static final int SERVER_SUCCESS_CODE = 0x1000;
    /**
     * 服务器错误码失败 经过本地转化
     */
    public static final int SERVER_UNSUCCESS_CODE = 0x1001;

    private int code;
    private String message;

    private ErrorResult(int code, String message) {
        this.code = code;
        if (message == null || message.isEmpty()) {
            this.message = getErrorMessage(code);
        } else {
            this.message = message;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    private String getErrorMessage(int errorCode) {

        String errorMessage = "unknown error";

        if (errorCode == ERROR_DATA_EMPTY) {
            errorMessage = CoracleSdk.getCoracleSdk().getContext().getString(R.string.business_data_exception);// 待完善
        } else if (errorCode == ERROR_UNKNOWN) {
            errorMessage = CoracleSdk.getCoracleSdk().getContext().getString(R.string.business_unknown_error);
        } else if (errorCode == ERROR_TIMEOUT) {
            errorMessage = CoracleSdk.getCoracleSdk().getContext().getString(R.string.business_request_timed_out);
        } else if (errorCode == ERROR_CONNECT) {
            errorMessage = CoracleSdk.getCoracleSdk().getContext().getString(R.string.business_check_network);
        } else if (errorCode == ERROR_PARSE_DATA) {
            errorMessage = CoracleSdk.getCoracleSdk().getContext().getString(R.string.business_data_parsing_failed);
        } else if (errorCode == ERROR_UNKNOWN_HOST) {
            errorMessage = CoracleSdk.getCoracleSdk().getContext().getString(R.string.business_check_network);
        } else if (errorCode == ERROR_REQUEST) {
            errorMessage = CoracleSdk.getCoracleSdk().getContext().getString(R.string.business_request_error);
        } else if (errorCode == SERVER_UNSUCCESS_CODE) {
            errorMessage = CoracleSdk.getCoracleSdk().getContext().getString(R.string.business_success);
        }

        return errorMessage;
    }

}
