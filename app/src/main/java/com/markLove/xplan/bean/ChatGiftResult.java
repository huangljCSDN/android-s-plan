package com.markLove.xplan.bean;

/**
 * Created by luoyunmin on 2017/8/23.
 */

public class ChatGiftResult {

    /**
     * Status : 200
     * Data : 1
     * Message : 成功
     */

    private int Status;
    private int Data;
    private String Message;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getData() {
        return Data;
    }

    public void setData(int Data) {
        this.Data = Data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
