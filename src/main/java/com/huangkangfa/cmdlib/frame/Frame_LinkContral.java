package com.huangkangfa.cmdlib.frame;

/**
 * Created by huangkangfa on 2017/6/19.
 */

public class Frame_LinkContral {
    private String index;  //子索引
    private String clientMac; //主mac地址
    private String clientNum; //主路
    private String serverMac; ///副mac地址
    private String serverNum; //副路
    private String others;  //其他

    public Frame_LinkContral() {
        this.others = "";
    }

    public Frame_LinkContral(String index, String clientMac, String clientNum, String serverMac, String serverNum, String others) {
        this.index = index;
        this.clientMac = clientMac;
        this.clientNum = clientNum;
        this.serverMac = serverMac;
        this.serverNum = serverNum;
        this.others = others;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getClientMac() {
        return clientMac;
    }

    public void setClientMac(String clientMac) {
        this.clientMac = clientMac;
    }

    public String getClientNum() {
        return clientNum;
    }

    public void setClientNum(String clientNum) {
        this.clientNum = clientNum;
    }

    public String getServerMac() {
        return serverMac;
    }

    public void setServerMac(String serverMac) {
        this.serverMac = serverMac;
    }

    public String getServerNum() {
        return serverNum;
    }

    public void setServerNum(String serverNum) {
        this.serverNum = serverNum;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
}
