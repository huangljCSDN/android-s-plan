package com.markLove.Xplan.bean.msg.body;

/**
 * Created by luoyunmin on 2017/7/5.
 */

public class ResultMessageBody extends MessageBody {
    String id;
    String errorcode;

    public ResultMessageBody(String id, String errorcode) {
        this.id = id;
        this.errorcode = errorcode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getErrorCode() {
        return errorcode;
    }

    public void setErrorCode(String errorCode) {
        this.errorcode = errorCode;
    }

}
