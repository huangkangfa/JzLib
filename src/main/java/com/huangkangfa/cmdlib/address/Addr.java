package com.huangkangfa.cmdlib.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.huangkangfa.cmdlib.exception.CmdException;

/**
 * 目标地址类
 */
public class Addr implements Parcelable {
    private String SrcAddr=null;//网关ZigBee的Mac地址
    private String DstAddr=null;//设备ZigBee的Mac地址

    public Addr(String SrcAddr){
        this.SrcAddr=SrcAddr;
    }
    public Addr(String SrcAddr,String DstAddr){
        this.SrcAddr=SrcAddr;
        this.DstAddr=DstAddr;
    }

    public String getValue(){
        StringBuilder sb=new StringBuilder();
        if(SrcAddr==null){
            throw new CmdException(CmdException.AddrisNull);
        }
        sb.append(SrcAddr);
        if(DstAddr!=null){
            sb.append(DstAddr);
        }
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SrcAddr);
        dest.writeString(this.DstAddr);
    }

    public Addr() {
    }

    protected Addr(Parcel in) {
        this.SrcAddr = in.readString();
        this.DstAddr = in.readString();
    }

    public static final Parcelable.Creator<Addr> CREATOR = new Parcelable.Creator<Addr>() {
        @Override
        public Addr createFromParcel(Parcel source) {
            return new Addr(source);
        }

        @Override
        public Addr[] newArray(int size) {
            return new Addr[size];
        }
    };
}
