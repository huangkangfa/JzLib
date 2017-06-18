package com.huangkangfa.cmdlib.index;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 子索引对象基础类类
 * Created by huangkangfa on 2017/6/15.
 */
public class BaseIndex implements Parcelable {
    public String getValue(){
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public BaseIndex() {
    }

    protected BaseIndex(Parcel in) {
    }

    public static final Parcelable.Creator<BaseIndex> CREATOR = new Parcelable.Creator<BaseIndex>() {
        @Override
        public BaseIndex createFromParcel(Parcel source) {
            return new BaseIndex(source);
        }

        @Override
        public BaseIndex[] newArray(int size) {
            return new BaseIndex[size];
        }
    };
}
