package com.huangkangfa.cmdlib.frame;

import android.os.Parcel;
import android.os.Parcelable;

import com.huangkangfa.cmdlib.utils.DataTypeUtil;

/**
 * 注意  此对象数据都是接受过来的原始数据
 * Created by Administrator on 2016/11/25 0025.
 */
public class Frame_Distance extends BaseFrame implements Parcelable {
    private String dip;  //远程ip地址
    private String dp;   //远程端口号
    private String lp;   //本地端口号

    public Frame_Distance(String dip, String dp, String lp) {
        this.dip = dip;
        this.dp = dp;
        this.lp = lp;
    }

    public String getDip() {
        return DataTypeUtil.asciiToString(dip);
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    public String getDp() {  //转换成10进制输出
        return String.valueOf(DataTypeUtil.hexToDecimal(dp));
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getLp() { //转换成10进制输出
        return String.valueOf(DataTypeUtil.hexToDecimal(lp));
    }

    public void setLp(String lp) {
        this.lp = lp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dip);
        dest.writeString(this.dp);
        dest.writeString(this.lp);
    }

    protected Frame_Distance(Parcel in) {
        this.dip = in.readString();
        this.dp = in.readString();
        this.lp = in.readString();
    }

    public static final Parcelable.Creator<Frame_Distance> CREATOR = new Parcelable.Creator<Frame_Distance>() {
        @Override
        public Frame_Distance createFromParcel(Parcel source) {
            return new Frame_Distance(source);
        }

        @Override
        public Frame_Distance[] newArray(int size) {
            return new Frame_Distance[size];
        }
    };
}
