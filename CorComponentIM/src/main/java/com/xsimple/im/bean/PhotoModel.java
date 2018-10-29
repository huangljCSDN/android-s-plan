package com.xsimple.im.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class PhotoModel implements Parcelable {

    private int id;
    private String originalPath;
    private long size;
    private boolean isChecked;
    private String name;

    public PhotoModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PhotoModel{" +
                "id=" + id +
                ", originalPath='" + originalPath + '\'' +
                ", size=" + size +
                ", isChecked=" + isChecked +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.originalPath);
        dest.writeLong(this.size);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
    }

    protected PhotoModel(Parcel in) {
        this.id = in.readInt();
        this.originalPath = in.readString();
        this.size = in.readLong();
        this.isChecked = in.readByte() != 0;
        this.name = in.readString();
    }

    public static final Parcelable.Creator<PhotoModel> CREATOR = new Parcelable.Creator<PhotoModel>() {
        @Override
        public PhotoModel createFromParcel(Parcel source) {
            return new PhotoModel(source);
        }

        @Override
        public PhotoModel[] newArray(int size) {
            return new PhotoModel[size];
        }
    };
}
