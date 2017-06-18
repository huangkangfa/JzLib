package com.huangkangfa.cmdlib.frame;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangkangfa on 2017/6/16.
 */

public class Frame_GatewayWiFi extends BaseFrame implements Parcelable {
    private String ip;
    private String mac;
    private String zb_mac;
    private String ssid;

    public Frame_GatewayWiFi(String ip, String mac, String zb_mac, String ssid) {
        this.ip = ip;
        this.mac = mac;
        this.zb_mac = zb_mac;
        this.ssid = ssid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getZb_mac() {
        return zb_mac;
    }

    public void setZb_mac(String zb_mac) {
        this.zb_mac = zb_mac;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ip);
        dest.writeString(this.mac);
        dest.writeString(this.zb_mac);
        dest.writeString(this.ssid);
    }

    protected Frame_GatewayWiFi(Parcel in) {
        this.ip = in.readString();
        this.mac = in.readString();
        this.zb_mac = in.readString();
        this.ssid = in.readString();
    }

    public static final Parcelable.Creator<Frame_GatewayWiFi> CREATOR = new Parcelable.Creator<Frame_GatewayWiFi>() {
        @Override
        public Frame_GatewayWiFi createFromParcel(Parcel source) {
            return new Frame_GatewayWiFi(source);
        }

        @Override
        public Frame_GatewayWiFi[] newArray(int size) {
            return new Frame_GatewayWiFi[size];
        }
    };
}
