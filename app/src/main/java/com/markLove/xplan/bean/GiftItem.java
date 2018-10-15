package com.markLove.xplan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luoyunmin on 2017/8/4.
 */

public class GiftItem implements Serializable {


    /**
     * Status : 200
     * Data : [{"Id":4,"GiftName":"发发等等的","GiftPrice":11,"GiftPicture":"M00/01/B0/rBIAAlk-QqSAPxulAAAs_fbV09o802.jpg","CreateTime":"2017-06-12T15:17:16"},{"Id":3,"GiftName":"蛋糕dd","GiftPrice":50,"GiftPicture":"M00/01/90/rBIAAlkdTsyANJw7AADmYNtNUno925.jpg","CreateTime":"0001-01-01T00:00:00"},{"Id":5,"GiftName":"dsf ","GiftPrice":1111,"GiftPicture":"M00/01/B0/rBIAAlk-QL6AV810AABbZFrGwzE012.jpg","CreateTime":"2017-06-12T15:22:06"}]
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

    public static class DataBean implements Serializable {
        /**
         * Id : 4
         * GiftName : 发发等等的
         * GiftPrice : 11.0
         * GiftPicture : M00/01/B0/rBIAAlk-QqSAPxulAAAs_fbV09o802.jpg
         * CreateTime : 2017-06-12T15:17:16
         * GiftGold: 1
         */
        private boolean isSelect;
        private int Id;
        private String GiftName;
        private double GiftPrice;
        private String GiftPicture;
        private String CreateTime;
        private double GiftGold;

        public double getGiftGold() {
            return GiftGold;
        }

        public void setGiftGold(double giftGold) {
            GiftGold = giftGold;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getGiftName() {
            return GiftName;
        }

        public void setGiftName(String GiftName) {
            this.GiftName = GiftName;
        }

        public double getGiftPrice() {
            return GiftPrice;
        }

        public void setGiftPrice(double GiftPrice) {
            this.GiftPrice = GiftPrice;
        }

        public String getGiftPicture() {
            return GiftPicture;
        }

        public void setGiftPicture(String GiftPicture) {
            this.GiftPicture = GiftPicture;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
