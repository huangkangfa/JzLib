package com.huangkangfa.cmdlib.index;

import android.os.Parcel;
import android.os.Parcelable;

import com.huangkangfa.cmdlib.utils.DataTypeUtil;

/**
 * 不可变索引
 * Created by huangkangfa on 2017/6/15.
 */
public class Index_Constant extends BaseIndex implements Parcelable {
    private String val;
    public Index_Constant(int num){
        val= DataTypeUtil.decimalToHex(num);
    }
    public Index_Constant(String val){
        this.val= val;
    }
    public Index_Constant(String... val){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<val.length;i++)
            sb.append(val[i]);
        this.val= sb.toString();
    }

    @Override
    public String getValue(){
        return val;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.val);
    }

    protected Index_Constant(Parcel in) {
        this.val = in.readString();
    }

    public static final Parcelable.Creator<Index_Constant> CREATOR = new Parcelable.Creator<Index_Constant>() {
        @Override
        public Index_Constant createFromParcel(Parcel source) {
            return new Index_Constant(source);
        }

        @Override
        public Index_Constant[] newArray(int size) {
            return new Index_Constant[size];
        }
    };
}
