package com.huangkangfa.cmdlib.index;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 可变索引
 * Created by huangkangfa on 2017/6/15.
 */
public class Index_Change extends BaseIndex implements Parcelable {
    private StringBuilder val=null;
    private String len=null;  //实际数据的长度
    private ChildIndex_Change mChildIndex_Change=null;  //子索引选择项
    public Index_Change(){
        val=new StringBuilder("ff");
    }

    public void setLen(String len) {
        this.len = len;
    }

    public ChildIndex_Change getChildIndex_Change() {
        return mChildIndex_Change;
    }

    public void setChildIndex_Change(ChildIndex_Change mChildIndex_Change) {
        this.mChildIndex_Change = mChildIndex_Change;
    }

    @Override
    public String getValue(){
        val.append(len).append(mChildIndex_Change.getValue());
        return val.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.val);
    }

    protected Index_Change(Parcel in) {
        this.val = (StringBuilder) in.readSerializable();
    }

    public static final Parcelable.Creator<Index_Change> CREATOR = new Parcelable.Creator<Index_Change>() {
        @Override
        public Index_Change createFromParcel(Parcel source) {
            return new Index_Change(source);
        }

        @Override
        public Index_Change[] newArray(int size) {
            return new Index_Change[size];
        }
    };
}
