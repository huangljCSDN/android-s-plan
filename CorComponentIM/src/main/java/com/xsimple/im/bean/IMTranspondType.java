package com.xsimple.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuhao on 2018/4/12.
 * <p>
 * 消息转发的类型
 */

public class IMTranspondType implements Parcelable {
    /**
     * 消息已经存在
     */
    public static final int IM_TRANSPOND_IMMESSAGE_EXIST = 0;
    /**
     * 消息不存在
     */
    public static final int IM_TRANSPOND_IMMESSAGE_UNEXIST = 1;

    /**
     * 消息不存在 文件sha值
     */
    public static final int IM_TRANSPOND_IMMESSAGE_UNEXIST_BY_SHA = 2;

    /**
     * 转发的消息的类型 同IMMessage 的消息类型
     * 具体参考同IMMessage 消息介绍
     */
    private String mIMMessageType;
    /**
     * 0  代表消息在消息库已经存在 IM_TRANSPOND_IMMESSAGE_EXIST
     * 1  代表消息在消息库不存在，需要构建消息 IM_TRANSPOND_IMMESSAGE_UNEXIST_IMG
     */
    private int mTranspondType;
    /**
     * 转发的已经存在的消息的id
     */
    private long mExistMsgId;
    /**
     * 转发的需要构建的消息的内容
     * 文本消息为 text文本
     * 文件消息为 文件路径
     * 地图消息为 经纬度
     */
    private String mContent;

    /**
     * 数据库已经存在的消息
     *
     * @param msgid
     */
    public IMTranspondType(String type, long msgid) {
        this.mIMMessageType = type;
        this.mExistMsgId = msgid;
        this.mTranspondType = IM_TRANSPOND_IMMESSAGE_EXIST;
    }

    /**
     * 数据库不存在的消息
     *
     * @param type
     * @param content
     */
    public IMTranspondType(String type, String content) {
        this.mIMMessageType = type;
        this.mContent = content;
        this.mTranspondType = IM_TRANSPOND_IMMESSAGE_UNEXIST;
    }

    /**
     * 数据库不存在的消息
     *
     * @param type
     * @param content
     */
    public IMTranspondType(String type, String content, int transpondType) {
        this.mIMMessageType = type;
        this.mContent = content;
        this.mTranspondType = transpondType;
    }


    public String getIMMessageType() {
        return mIMMessageType;
    }

    public int getTranspondType() {
        return mTranspondType;
    }

    public long getExistMsgId() {
        return mExistMsgId;
    }

    public String getContent() {
        return mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mIMMessageType);
        dest.writeInt(this.mTranspondType);
        dest.writeLong(this.mExistMsgId);
        dest.writeString(this.mContent);
    }

    protected IMTranspondType(Parcel in) {
        this.mIMMessageType = in.readString();
        this.mTranspondType = in.readInt();
        this.mExistMsgId = in.readLong();
        this.mContent = in.readString();
    }

    public static final Parcelable.Creator<IMTranspondType> CREATOR = new Parcelable.Creator<IMTranspondType>() {
        @Override
        public IMTranspondType createFromParcel(Parcel source) {
            return new IMTranspondType(source);
        }

        @Override
        public IMTranspondType[] newArray(int size) {
            return new IMTranspondType[size];
        }
    };
}
