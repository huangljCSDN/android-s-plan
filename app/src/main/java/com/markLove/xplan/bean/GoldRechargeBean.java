package com.markLove.xplan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class GoldRechargeBean implements Serializable{

    /**
     * Status : 200
     * Data : [{"GoldSetID":1,"GoldAccount":60,"GoldMoney":6,"IsApple":0},{"GoldSetID":2,"GoldAccount":330,"GoldMoney":30,"IsApple":0},{"GoldSetID":3,"GoldAccount":816,"GoldMoney":68,"IsApple":1},{"GoldSetID":4,"GoldAccount":1664,"GoldMoney":128,"IsApple":1},{"GoldSetID":5,"GoldAccount":4592,"GoldMoney":328,"IsApple":1},{"GoldSetID":6,"GoldAccount":7800,"GoldMoney":520,"IsApple":1}]
     * Message : 成功
     */

    private int Status;
    private String Message;
    private List<DataBean> Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean implements Serializable{
        /**
         * GoldSetID : 1
         * GoldAccount : 60
         * GoldMoney : 6
         * IsApple : 0
         */

        private int GoldSetID;
        private int GoldAccount;
        private int GoldMoney;
        private int IsApple;

        public int getGoldSetID() {
            return GoldSetID;
        }

        public void setGoldSetID(int GoldSetID) {
            this.GoldSetID = GoldSetID;
        }

        public int getGoldAccount() {
            return GoldAccount;
        }

        public void setGoldAccount(int GoldAccount) {
            this.GoldAccount = GoldAccount;
        }

        public int getGoldMoney() {
            return GoldMoney;
        }

        public void setGoldMoney(int GoldMoney) {
            this.GoldMoney = GoldMoney;
        }

        public int getIsApple() {
            return IsApple;
        }

        public void setIsApple(int IsApple) {
            this.IsApple = IsApple;
        }
    }
}
