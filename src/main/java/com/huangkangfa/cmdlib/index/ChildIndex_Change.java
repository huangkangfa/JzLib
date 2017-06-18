package com.huangkangfa.cmdlib.index;

import android.os.Parcel;
import android.os.Parcelable;

import com.huangkangfa.cmdlib.utils.DataTypeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 子索引选择项
 * Created by huangkangfa on 2017/6/15.
 */
public class ChildIndex_Change implements Parcelable {
    private List<String> data;

    public ChildIndex_Change(){
        data=new ArrayList<String>();
    }

    public void add(String data){
        this.data.add(data);
    }

    public String getValue(){
        String val="00000000";
        for(int i=0;i<data.size();i++){
            val=getSumOfHex(val,data.get(i));
        }
        return val;
    }

    /**
     * 有效位相加方法
     * 4个字节的16进制字符串相加，获取4个字节的16进制结果
     * @param a 4字节参数
     * @param b 4字节参数
     * @return
     */
    public static String getSumOfHex(String a, String b) {
        int len = a.length();
        if (len != b.length()) {
            return null;
        }

        String ba = "";
        for (int i = 0; i < len; i++) {
            ba += DataTypeUtil.hexToBinary(a.substring(i, i + 1));
        }

        String bb = "";
        for (int i = 0; i < len; i++) {
            bb += DataTypeUtil.hexToBinary(b.substring(i, i + 1));
        }

        String index = "";
        for (int i = 0; i < len * 4; i++) {
            if ("1".equals(ba.substring(i, i + 1)) || "1".equals(bb.substring(i, i + 1))) {
                index += "1";
            } else {
                index += "0";
            }
        }

        index = DataTypeUtil.binaryToHex(index);
        if (index.length() == len) {
            return index;
        } else {
            int temp = len - index.length();
            for (int i = 0; i < temp; i++) {
                index = "0" + index;
            }
            return index;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.data);
    }

    protected ChildIndex_Change(Parcel in) {
        this.data = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ChildIndex_Change> CREATOR = new Parcelable.Creator<ChildIndex_Change>() {
        @Override
        public ChildIndex_Change createFromParcel(Parcel source) {
            return new ChildIndex_Change(source);
        }

        @Override
        public ChildIndex_Change[] newArray(int size) {
            return new ChildIndex_Change[size];
        }
    };
}
