package com.xsimple.im.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by liuhao on 2018/5/4.
 */

public class IMSendFileEntity implements Parcelable {

    private String sha;

    private String localPath;

    private long fileLength;

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }



    @Override
    public boolean equals(Object obj) {

        if (obj instanceof IMSendFileEntity) {
            IMSendFileEntity entity = (IMSendFileEntity) obj;
            if (!TextUtils.isEmpty(sha) && sha.equals(entity.getSha())) {
                return true;
            }
            if (!TextUtils.isEmpty(localPath) && localPath.equals(entity.getLocalPath())) {
                return true;
            }
        }

        return super.equals(obj);
    }



    public IMSendFileEntity() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sha);
        dest.writeString(this.localPath);
        dest.writeLong(this.fileLength);
        dest.writeString(this.fileName);
    }

    protected IMSendFileEntity(Parcel in) {
        this.sha = in.readString();
        this.localPath = in.readString();
        this.fileLength = in.readLong();
        this.fileName = in.readString();
    }

    public static final Parcelable.Creator<IMSendFileEntity> CREATOR = new Parcelable.Creator<IMSendFileEntity>() {
        @Override
        public IMSendFileEntity createFromParcel(Parcel source) {
            return new IMSendFileEntity(source);
        }

        @Override
        public IMSendFileEntity[] newArray(int size) {
            return new IMSendFileEntity[size];
        }
    };
}
