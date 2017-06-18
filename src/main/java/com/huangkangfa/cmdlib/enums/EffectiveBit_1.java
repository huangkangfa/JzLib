package com.huangkangfa.cmdlib.enums;

/**
 * 有效位
 * Created by huangkangfa on 2017/6/16.
 */
public enum EffectiveBit_1 {
    /*OD1001    通用参数*/
    PARAM_WIRELESSCHANNEL("01"),    //无线通道单址读取有效位
    PARAM_PANID("06"),              //PAN ID单址读取有效位

    /*OD1007    通用参数*/
    PARAM_AREA("08"),               //区域单址读取有效位
    PARAM_BAUDRATE("0d"),           //设备波特率单址读取有效位
    PARAM_CHECKTYPE("0e"),          //设备检验方式单址读取有效位
    PARAM_EQUIPMENTCATEGORY("02"),  //设备类别单址读取有效位
    PARAM_PRODUCTTYPE("03"),        //产品类型单址读取有效位

    /*OD4040    计量设备参数*/
    PARAM_ELECTRICITY("10"),        //计量设备有功电量单址读取位

    /*OD5020    心跳相关*/
    PARAM_READNUM_SUM("04"),        //心跳计数单址有效位

    /*OD5060    远程服务器配置相关*/
    PARAM_READNUM_IP("07"),         //远程地址单址读取位
    PARAM_READNUM_PORT("04"),       //远程端口单址读取位
    PARAM_READNUM_LOCAL_PORT("03"); //本地端口单址读取位

    private String val;

    EffectiveBit_1(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
