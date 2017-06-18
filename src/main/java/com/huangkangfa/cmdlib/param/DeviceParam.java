package com.huangkangfa.cmdlib.param;

/**
 * Created by huangkangfa on 2017/6/16.
 */

public enum DeviceParam {
    /*OD4010通用参数*/
    PARAM_STATE_OPEN("01"),                         //4010设备  参数-开
    PARAM_STATE_CLOSE("02"),                        //4010设备  参数-关
    PARAM_STATE_PAUSE("04"),                        //4010设备  参数-暂停
    PARAM_WAY_STRAIGHT("01"),                       //4010设备  参数-立即开启
    PARAM_WAY_SLOWLY("02"),                         //4010设备  参数-渐渐开启
    PARAM_WAY_DELAY("03"),                          //4010设备  参数-延时开启
    PARAM_MODE_COLORFUL_JUMP("0a"),                 //七彩灯 模式-七彩跳变
    PARAM_MODE_COLORFUL_SLOWLY("09"),               //七彩灯 模式-七彩渐变
    PARAM_MODE_COLORFUL_BREATH("0b"),               //七彩灯 模式-呼吸灯

    /*OD4010多彩灯参数*/
    PARAM_COLORLIGHT_TYPE_SPECIAL("01"),            //类型-特殊
    PARAM_COLORLIGHT_TYPE_COLORFUL("03"),           //类型-调色
    PARAM_COLORLIGHT_TYPE_MONOTONE_WARM("02"),      //类型-暖光
    PARAM_COLORLIGHT_TYPE_MONOTONE_COLD("04"),      //类型-冷光

    /*OD4010场景控制器参数*/
    PARAM_STATECONTROL_PARAM_TYPE("8a"),            //设备类别代表场景控制器

    /*OD6001多彩灯机械开关参数*/
    PARAM_MECHANICAL_MODE_MONOTONE("01"),           //模式-调光模式  用于机械开关设置
    PARAM_MECHANICAL_MODE_COLORFUL("05");           //模式-调色模式  用于机械开关设置

    private String val;

    DeviceParam(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
