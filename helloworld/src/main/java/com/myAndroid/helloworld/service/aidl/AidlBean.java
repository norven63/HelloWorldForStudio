package com.myAndroid.helloworld.service.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 该类需要拷贝放入到客户端项目里，相同的包目录结构中
 */
public class AidlBean implements Parcelable{
    private int intA;
    private int intB;
    private String stringA;
    private String stringB;
    private int intC;

    public AidlBean(int intA, int intB, String stringA, String stringB, int intC) {
        this.intA = intA;
        this.intB = intB;
        this.stringA = stringA;
        this.stringB = stringB;
        this.intC = intC;
    }

    protected AidlBean(Parcel in) {
        intA = in.readInt();
        intB = in.readInt();
        stringA = in.readString();
        stringB = in.readString();
        intC = in.readInt();
    }

    public int getIntA() {
        return intA;
    }

    public int getIntB() {
        return intB;
    }

    public String getStringA() {
        return stringA;
    }

    public String getStringB() {
        return stringB;
    }

    public int getIntC() {
        return intC;
    }

    public static final Creator<AidlBean> CREATOR = new Creator<AidlBean>() {
        @Override
        public AidlBean createFromParcel(Parcel in) {
            return new AidlBean(in);
        }

        @Override
        public AidlBean[] newArray(int size) {
            return new AidlBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(intA);
        dest.writeInt(intB);
        dest.writeString(stringA);
        dest.writeString(stringB);
        dest.writeInt(intC);
    }
}
