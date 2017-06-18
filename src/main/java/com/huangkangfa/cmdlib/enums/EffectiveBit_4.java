package com.huangkangfa.cmdlib.enums;

/**
 * 有效位
 * Created by huangkangfa on 2017/6/16.
 */
public enum EffectiveBit_4 {
    PARAM_AREA("00000043"), //区域的有效位

    /*OD4010    多路控制器参数*/
    PARAM_VARIED_A_MOMENT("00018000"), //A开关瞬间有效位
    PARAM_VARIED_A_DELAY("00038000"), //A开关延时有效位
    PARAM_VARIED_B_MOMENT("000c0000"), //B开关瞬间有效位
    PARAM_VARIED_B_DELAY("001c0000"), //B开关延时有效位
    PARAM_VARIED_C_MOMENT("00600000"), //C开关瞬间有效位
    PARAM_VARIED_C_DELAY("00e00000"), //C开关延时有效位

    /*OD4010    多彩灯参数*/
    PARAM_COLORLIGHT_NEEDTIME("04000000"), //时间有效位
    PARAM_COLORLIGHT_MONOTONE("1b000000"), //调光有效位
    PARAM_COLORLIGHT_COLORFUL("7b000000"), //调色有效位

    /*OD4010    色温灯参数*/
    PARAM_COLORTEMPERATURELIGHT_ALL("38000000"), //调节亮度跟色温的有效位

    /*OD4010    场景控制器参数*/
    PARAM_STATECONTROL_A("00000800"), //A路输入信号有效位
    PARAM_STATECONTROL_B("00001000"), //B路输入信号有效位
    PARAM_STATECONTROL_C("00002000"), //C路输入信号有效位

    /*OD4040    计量设备参数*/
    PARAM_METERINGEQUIPMENT_SWITCH("00060000"), //计量设备开关有效位

    /*OD6001    多彩灯机械开关参数*/
    PARAM_COLORLIGHTSET_A_MONOTONE("00000003"), //机械开关A设置调光有效位
    PARAM_COLORLIGHTSET_A_COLORFUL("0000000f"), //机械开关A设置调色有效位
    PARAM_COLORLIGHTSET_A_NULL("00000001"), //机械开关A设置暂无有效位
    PARAM_COLORLIGHTSET_B_MONOTONE("00000030"), //机械开关B设置调光有效位
    PARAM_COLORLIGHTSET_B_COLORFUL("000000f0"), //机械开关B设置调色有效位
    PARAM_COLORLIGHTSET_B_null("00000010"),//机械开关B设置暂无有效位
    PARAM_COLORLIGHTSET_C_null("00000100"),//机械开关C设置暂无有效位
    PARAM_COLORLIGHTSET_D_null("00001000"),//机械开关D设置暂无有效位

    /*OD5060    远程服务器配置相关*/
    PARAM_DISTANCEDATA_IP("00000040"),//远程地址有效位
    PARAM_DISTANCEDATA_PORT("00000008"),//远程端口有效位
    PARAM_DISTANCEDATA_LOCAL_PORT("00000004");//本地端口有效位

    private String val;

    EffectiveBit_4(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
