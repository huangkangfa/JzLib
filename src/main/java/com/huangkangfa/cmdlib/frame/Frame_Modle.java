package com.huangkangfa.cmdlib.frame;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangkangfa on 2017/6/17.
 */

public class Frame_Modle extends BaseFrame implements Parcelable {
    protected String ucDeviceType; //设备类型
    protected String ucSensorType; //产品类别
    protected String ucWorkMode; //工作模式
    protected String ucWorkState; //测试字状态
    protected String ucOD_Upload_Interval_0; //定时上报时间间隔
    protected String ucOD_Upload_Interval_1; //定时上报偏移时间
    protected String ucOD_Area; //区域信息
    protected String ucOD_Func; //功能信息
    protected String uiBATVoltage; //设备电压值0V
    protected String ucRSSI; //信号强度
    protected String ucLQI; //信号连接质量
    protected String others; //其他特殊参数

    public Frame_Modle(String ucDeviceType, String ucSensorType, String ucWorkMode, String ucWorkState, String ucOD_Upload_Interval_0, String ucOD_Upload_Interval_1, String ucOD_Area, String ucOD_Func, String uiBATVoltage, String ucRSSI, String ucLQI, String others) {
        this.ucDeviceType = ucDeviceType;
        this.ucSensorType = ucSensorType;
        this.ucWorkMode = ucWorkMode;
        this.ucWorkState = ucWorkState;
        this.ucOD_Upload_Interval_0 = ucOD_Upload_Interval_0;
        this.ucOD_Upload_Interval_1 = ucOD_Upload_Interval_1;
        this.ucOD_Area = ucOD_Area;
        this.ucOD_Func = ucOD_Func;
        this.uiBATVoltage = uiBATVoltage;
        this.ucRSSI = ucRSSI;
        this.ucLQI = ucLQI;
        this.others = others;
    }

    public String getUcDeviceType() {
        return ucDeviceType;
    }

    public void setUcDeviceType(String ucDeviceType) {
        this.ucDeviceType = ucDeviceType;
    }

    public String getUcSensorType() {
        return ucSensorType;
    }

    public void setUcSensorType(String ucSensorType) {
        this.ucSensorType = ucSensorType;
    }

    public String getUcWorkMode() {
        return ucWorkMode;
    }

    public void setUcWorkMode(String ucWorkMode) {
        this.ucWorkMode = ucWorkMode;
    }

    public String getUcWorkState() {
        return ucWorkState;
    }

    public void setUcWorkState(String ucWorkState) {
        this.ucWorkState = ucWorkState;
    }

    public String getUcOD_Upload_Interval_0() {
        return ucOD_Upload_Interval_0;
    }

    public void setUcOD_Upload_Interval_0(String ucOD_Upload_Interval_0) {
        this.ucOD_Upload_Interval_0 = ucOD_Upload_Interval_0;
    }

    public String getUcOD_Upload_Interval_1() {
        return ucOD_Upload_Interval_1;
    }

    public void setUcOD_Upload_Interval_1(String ucOD_Upload_Interval_1) {
        this.ucOD_Upload_Interval_1 = ucOD_Upload_Interval_1;
    }

    public String getUcOD_Area() {
        return ucOD_Area;
    }

    public void setUcOD_Area(String ucOD_Area) {
        this.ucOD_Area = ucOD_Area;
    }

    public String getUcOD_Func() {
        return ucOD_Func;
    }

    public void setUcOD_Func(String ucOD_Func) {
        this.ucOD_Func = ucOD_Func;
    }

    public String getUiBATVoltage() {
        return uiBATVoltage;
    }

    public void setUiBATVoltage(String uiBATVoltage) {
        this.uiBATVoltage = uiBATVoltage;
    }

    public String getUcRSSI() {
        return ucRSSI;
    }

    public void setUcRSSI(String ucRSSI) {
        this.ucRSSI = ucRSSI;
    }

    public String getUcLQI() {
        return ucLQI;
    }

    public void setUcLQI(String ucLQI) {
        this.ucLQI = ucLQI;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ucDeviceType);
        dest.writeString(this.ucSensorType);
        dest.writeString(this.ucWorkMode);
        dest.writeString(this.ucWorkState);
        dest.writeString(this.ucOD_Upload_Interval_0);
        dest.writeString(this.ucOD_Upload_Interval_1);
        dest.writeString(this.ucOD_Area);
        dest.writeString(this.ucOD_Func);
        dest.writeString(this.uiBATVoltage);
        dest.writeString(this.ucRSSI);
        dest.writeString(this.ucLQI);
        dest.writeString(this.others);
    }

    protected Frame_Modle(Parcel in) {
        this.ucDeviceType = in.readString();
        this.ucSensorType = in.readString();
        this.ucWorkMode = in.readString();
        this.ucWorkState = in.readString();
        this.ucOD_Upload_Interval_0 = in.readString();
        this.ucOD_Upload_Interval_1 = in.readString();
        this.ucOD_Area = in.readString();
        this.ucOD_Func = in.readString();
        this.uiBATVoltage = in.readString();
        this.ucRSSI = in.readString();
        this.ucLQI = in.readString();
        this.others = in.readString();
    }

    public static final Parcelable.Creator<Frame_Modle> CREATOR = new Parcelable.Creator<Frame_Modle>() {
        @Override
        public Frame_Modle createFromParcel(Parcel source) {
            return new Frame_Modle(source);
        }

        @Override
        public Frame_Modle[] newArray(int size) {
            return new Frame_Modle[size];
        }
    };
}
