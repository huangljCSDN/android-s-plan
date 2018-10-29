package com.networkengine.exception;

/**
 * Created by pengpeng on 16/12/12.
 */

public class NetworkException extends RuntimeException {

    //TODO 异常还未细分 ErrorCode ＝ 业务码｜网络API类型码（[MCHL、MXM、MQTT]）｜错误类型码 ？

    public static final int ECODE_DE_ERROR = 0;
    public static final int ECODE_INIT_GET_SERVER_INFO_ERROR = 0x1;
    public static final int ECODE_INIT_GET_COOKIE_ERROR = 0x2;
    public static final int ECODE_INIT_GET_USER_INFO_ERROR = 0x3;
    public static final int ECODE_INIT_LOGIN_MCHL_ERROR = 0x4;
    public static final int ECODE_INIT_LOAD_DIS_GROUP_ERROR = 0x5;
    public static final int ECODE_INIT_LOAD_GROUP_ERROR = 0x6;
    public static final int ECODE_INIT_ERROR = 0x7;

    /**
     * 错误码
     */
    private int mCode = ECODE_DE_ERROR;

    public NetworkException(String detailMessage) {
        super(detailMessage);
    }

    public NetworkException(int code, String detailMessage) {
        super(detailMessage);
        mCode = code;
    }
}
