package com.markLove.Xplan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */

public class UserInfoByUseridBean implements Serializable{

    /**
     * Status : 200
     * Data : {"User":{"Degree":"aa","Age":"117","Birthday":"1900-01-01T00:00:00","NickName":"眉骨印","HeadImageUrl":"M00/01/A6/rBIAAlk2eq6AQRieAABCLVaodjk817.jpg","CunAiNo":0,"City":"aa","Job":"aa","MyProfile":" 温柔要有，但不是妥协，我要在安静中，不慌不忙的坚强","Tags":"aa","Horoscope":"魔羯座","Photoes":[{"ID":1000,"AlarmID":0,"Url":"","ThumbUrl":"","Description":"aa","UserID":891931,"PhonesType":4,"CreateDT":"1900-01-01T00:00:00"}],"IsReadName":0,"RealName":"aa","IdentityCard":"aa","UserID":891931}}
     * Message : 成功
     */

    private int Status;
    private DataBean Data;
    private String Message;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public static class DataBean implements Serializable{
        /**
         * User : {"Degree":"aa","Age":"117","Birthday":"1900-01-01T00:00:00","NickName":"眉骨印","HeadImageUrl":"M00/01/A6/rBIAAlk2eq6AQRieAABCLVaodjk817.jpg","CunAiNo":0,"City":"aa","Job":"aa","MyProfile":" 温柔要有，但不是妥协，我要在安静中，不慌不忙的坚强","Tags":"aa","Horoscope":"魔羯座","Photoes":[{"ID":1000,"AlarmID":0,"Url":"","ThumbUrl":"","Description":"aa","UserID":891931,"PhonesType":4,"CreateDT":"1900-01-01T00:00:00"}],"IsReadName":0,"RealName":"aa","IdentityCard":"aa","UserID":891931}
         */

        private UserBean User;

        public UserBean getUser() {
            return User;
        }

        public void setUser(UserBean User) {
            this.User = User;
        }

        public static class UserBean implements Serializable{

            /**
             * Degree :
             * Age : 23
             * Birthday : 1994-05-26T00:00:00
             * NickName : 阳光下的星星
             * HeadImageUrl : M00/01/A9/rBIAAlk4q1CAK_LPAABgjomtrO4457.jpg
             * CunAiNo : 182474
             * City : 深圳市
             * Job : 小小测试员小灰灰的我灰的我白的黑的红
             * MyProfile :
             * Signature :
             * Tags : 测试结果表明,爱情里没有你,你们在一起了,你会选择哪一
             * Horoscope : 双子座
             * Photoes : [{"ID":1105,"AlarmID":0,"Url":"M00/01/A9/rBIAAlk4yiKAI3r3AABwxdK2tNk354.jpg","ThumbUrl":"","Description":"","UserID":891960,"PhonesType":4,"CreateDT":"1900-01-01T00:00:00"},{"ID":1106,"AlarmID":0,"Url":"M00/01/A9/rBIAAlk4yi6AfKS1AABNHXT1F2g737.jpg","ThumbUrl":"","Description":"","UserID":891960,"PhonesType":4,"CreateDT":"1900-01-01T00:00:00"},{"ID":1107,"AlarmID":0,"Url":"M00/01/A9/rBIAAlk4yjqAK3ljAABLAKSnMQQ318.jpg","ThumbUrl":"","Description":"","UserID":891960,"PhonesType":4,"CreateDT":"1900-01-01T00:00:00"}]
             * IsReadName : 0
             * RealName :
             * IdentityCard :
             * UserID : 891960
             */

            private String Degree;
            private String Age;
            private String Birthday;
            private String NickName;
            private String HeadImageUrl;
            private int CunAiNo;
            private String City;
            private String Job;
            private String MyProfile;
            private String Signature;
            private String Tags;
            private String Horoscope;
            private int IsReadName;
            private String RealName;
            private String IdentityCard;
            private int UserID;
            private List<PhotoesBean> Photoes;

            public String getDegree() {
                return Degree;
            }

            public void setDegree(String Degree) {
                this.Degree = Degree;
            }

            public String getAge() {
                return Age;
            }

            public void setAge(String Age) {
                this.Age = Age;
            }

            public String getBirthday() {
                return Birthday;
            }

            public void setBirthday(String Birthday) {
                this.Birthday = Birthday;
            }

            public String getNickName() {
                return NickName;
            }

            public void setNickName(String NickName) {
                this.NickName = NickName;
            }

            public String getHeadImageUrl() {
                return HeadImageUrl;
            }

            public void setHeadImageUrl(String HeadImageUrl) {
                this.HeadImageUrl = HeadImageUrl;
            }

            public int getCunAiNo() {
                return CunAiNo;
            }

            public void setCunAiNo(int CunAiNo) {
                this.CunAiNo = CunAiNo;
            }

            public String getCity() {
                return City;
            }

            public void setCity(String City) {
                this.City = City;
            }

            public String getJob() {
                return Job;
            }

            public void setJob(String Job) {
                this.Job = Job;
            }

            public String getMyProfile() {
                return MyProfile;
            }

            public void setMyProfile(String MyProfile) {
                this.MyProfile = MyProfile;
            }

            public String getSignature() {
                return Signature;
            }

            public void setSignature(String Signature) {
                this.Signature = Signature;
            }

            public String getTags() {
                return Tags;
            }

            public void setTags(String Tags) {
                this.Tags = Tags;
            }

            public String getHoroscope() {
                return Horoscope;
            }

            public void setHoroscope(String Horoscope) {
                this.Horoscope = Horoscope;
            }

            public int getIsReadName() {
                return IsReadName;
            }

            public void setIsReadName(int IsReadName) {
                this.IsReadName = IsReadName;
            }

            public String getRealName() {
                return RealName;
            }

            public void setRealName(String RealName) {
                this.RealName = RealName;
            }

            public String getIdentityCard() {
                return IdentityCard;
            }

            public void setIdentityCard(String IdentityCard) {
                this.IdentityCard = IdentityCard;
            }

            public int getUserID() {
                return UserID;
            }

            public void setUserID(int UserID) {
                this.UserID = UserID;
            }

            public List<PhotoesBean> getPhotoes() {
                return Photoes;
            }

            public void setPhotoes(List<PhotoesBean> Photoes) {
                this.Photoes = Photoes;
            }

            public static class PhotoesBean implements Serializable{
                /**
                 * ID : 1105
                 * AlarmID : 0
                 * Url : M00/01/A9/rBIAAlk4yiKAI3r3AABwxdK2tNk354.jpg
                 * ThumbUrl :
                 * Description :
                 * UserID : 891960
                 * PhonesType : 4
                 * CreateDT : 1900-01-01T00:00:00
                 */

                private int ID;
                private int AlarmID;
                private String Url;
                private String ThumbUrl;
                private String Description;
                private int UserID;
                private int PhonesType;
                private String CreateDT;

                public int getID() {
                    return ID;
                }

                public void setID(int ID) {
                    this.ID = ID;
                }

                public int getAlarmID() {
                    return AlarmID;
                }

                public void setAlarmID(int AlarmID) {
                    this.AlarmID = AlarmID;
                }

                public String getUrl() {
                    return Url;
                }

                public void setUrl(String Url) {
                    this.Url = Url;
                }

                public String getThumbUrl() {
                    return ThumbUrl;
                }

                public void setThumbUrl(String ThumbUrl) {
                    this.ThumbUrl = ThumbUrl;
                }

                public String getDescription() {
                    return Description;
                }

                public void setDescription(String Description) {
                    this.Description = Description;
                }

                public int getUserID() {
                    return UserID;
                }

                public void setUserID(int UserID) {
                    this.UserID = UserID;
                }

                public int getPhonesType() {
                    return PhonesType;
                }

                public void setPhonesType(int PhonesType) {
                    this.PhonesType = PhonesType;
                }

                public String getCreateDT() {
                    return CreateDT;
                }

                public void setCreateDT(String CreateDT) {
                    this.CreateDT = CreateDT;
                }
            }
        }
    }
}
