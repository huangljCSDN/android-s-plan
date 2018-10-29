package com.networkengine.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pengpeng on 17/3/22.
 */

public class BaseGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    public static class Mem implements Serializable, Comparable<Mem> {

        private String ln;

        private String un;

        private String id;

        private String name;

        private int userStatus;

        private long joinTime;

        private int job;

        private int leaveTime;

        private String cu;

        private int groupId;

        private String imageAddress;
        public String pinyin; // 姓名对应的拼音
        public String firstLetter; // 拼音的首字母

        public Mem() {
        }

        public Mem(String ln, String un) {
            this.ln = ln;
            this.un = un;

        }

        public String getPinyin() {
            return pinyin;
        }

        public String getFirstLetter() {
//            pinyin = PingYinUtil.getPingYin(un); // 根据姓名获取拼音
//            if (TextUtils.isEmpty(pinyin)) {
//                return "#";
//            }
//            firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
//            if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
//                firstLetter = "#";
//            }

            return firstLetter;
        }


        /**
         * @return 是否为群主
         */
        public boolean isGroupOwner() {
            return job == 1;
        }

        public String getLn() {
            return ln;
        }

        public void setLn(String ln) {
            this.ln = ln;
        }

        public String getUn() {
            return un;
        }

        public void setUn(String un) {
            this.un = un;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(int userStatus) {
            this.userStatus = userStatus;
        }

        public long getJoinTime() {
            return joinTime;
        }

        public void setJoinTime(long joinTime) {
            this.joinTime = joinTime;
        }

        public int getJob() {
            return job;
        }

        public void setJob(int job) {
            this.job = job;
        }

        public int getLeaveTime() {
            return leaveTime;
        }

        public void setLeaveTime(int leaveTime) {
            this.leaveTime = leaveTime;
        }

        public String getCu() {
            return cu;
        }

        public void setCu(String cu) {
            this.cu = cu;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public String getImageAddress() {
            return imageAddress;
        }

        public void setImageAddress(String imageAddress) {
            this.imageAddress = imageAddress;
        }

        @Override
        public int compareTo(@NonNull Mem o) {
            if (firstLetter.equals("#") && !o.getFirstLetter().equals("#")) {
                return 1;
            } else if (!firstLetter.equals("#") && o.getFirstLetter().equals("#")) {
                return -1;
            } else {
                return pinyin.compareToIgnoreCase(o.getPinyin());
            }
        }

    }

    private String name;
    private String cu;
    private String cuId;
    private String id;
    private String tip;
    private String status;
    private String remark;
    public String creater;
    /**
     * 重要群组
     * 0 不重要
     * 1 重要
     */
    private int importantFlag;

    private int important_flag;
    /**
     * 群公告发布时间
     */
    private String publis_date;

    private List<GroupEntity.Mem> mems;

    //增加
    private String update_time;

    private String create_time;


    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCu() {
        return cu;
    }

    public void setCu(String cu) {
        this.cu = cu;
    }

    public String getCuId() {
        return cuId;
    }

    public void setCuId(String cuId) {
        this.cuId = cuId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<GroupEntity.Mem> getMems() {
        return mems;
    }

    public void setMems(List<GroupEntity.Mem> mems) {
        this.mems = mems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getImportantFlag() {
        if (importantFlag == 1 || important_flag == 1) {
            return 1;
        }
        return 0;
    }

    public void setImportantFlag(int importantFlag) {
        this.importantFlag = importantFlag;
        this.important_flag = importantFlag;
    }

    public String getPublis_date() {
        return publis_date;
    }

    public void setPublis_date(String publis_date) {
        this.publis_date = publis_date;
    }
}
