package com.networkengine.database.table;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuhao on 2018/6/12.
 */
public class MemberExtendField implements Parcelable {


    private String fieldName;

    private String fieldType;

    private String isEdit;

    private String name;

    private String value;

    private int valueLength;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getIsEdit() {
        return this.isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getValueLength() {
        return valueLength;
    }

    public void setValueLength(int valueLength) {
        this.valueLength = valueLength;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fieldName);
        dest.writeString(this.fieldType);
        dest.writeString(this.isEdit);
        dest.writeString(this.name);
        dest.writeString(this.value);
        dest.writeInt(this.valueLength);
    }

    public MemberExtendField() {
    }

    protected MemberExtendField(Parcel in) {
        this.fieldName = in.readString();
        this.fieldType = in.readString();
        this.isEdit = in.readString();
        this.name = in.readString();
        this.value = in.readString();
        this.valueLength = in.readInt();
    }

    public static final Parcelable.Creator<MemberExtendField> CREATOR = new Parcelable.Creator<MemberExtendField>() {
        @Override
        public MemberExtendField createFromParcel(Parcel source) {
            return new MemberExtendField(source);
        }

        @Override
        public MemberExtendField[] newArray(int size) {
            return new MemberExtendField[size];
        }
    };
}

