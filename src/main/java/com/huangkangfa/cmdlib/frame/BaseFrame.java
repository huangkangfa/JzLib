package com.huangkangfa.cmdlib.frame;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangkangfa on 2017/6/16.
 */

public class BaseFrame implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public BaseFrame() {
    }

    protected BaseFrame(Parcel in) {
    }

    public static final Parcelable.Creator<BaseFrame> CREATOR = new Parcelable.Creator<BaseFrame>() {
        @Override
        public BaseFrame createFromParcel(Parcel source) {
            return new BaseFrame(source);
        }

        @Override
        public BaseFrame[] newArray(int size) {
            return new BaseFrame[size];
        }
    };
}
