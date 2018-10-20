package com.markLove.Xplan.bean;

/**
 * 作者：created by huanglingjun on 2018/10/18
 * 描述：
 */
public class UserBean {

    private String nickName;
    public int age;
    public int sex;
    public long time;
    public String headUrl;

    public UserBean(String nickName, int age, int sex, long time, String headUrl) {
        this.nickName = nickName;
        this.age = age;
        this.sex = sex;
        this.time = time;
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "nickName='" + nickName + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", time=" + time +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
