package com.huangkangfa.cmdlib.param;

/**
 * Created by huangkangfa on 2017/6/16.
 */

public enum SceneParam {
    PARAM_FLAG_SET("01"),                                               //子命令标识，表示参数配置命令
    PARAM_DEFAULT_NAME("0000000000000000000000000000000000000000"),     //存至网关的默认场景名称
    PARAM_TYPE_SCENE("01"),                                             //要设置的场景类型为场景
    PARAM_TYPE_LINK("02"),                                              //要设置的场景类型为联动
    PARAM_FEATURES_DEL("07"),                                           //场景功能特性为删除
    PARAM_FEATURES_START("09"),                                         //场景功能特性为触发
    PARAM_FEATURES_SET("00"),                                           //场景功能特性为设置
    PARAM_LINK_OPEN("01"),                                              //设置的联动开启
    PARAM_LINK_CLOSE("02"),                                             //设置的联动关闭
    PARAM_FORCE_TRUE("01"),                                             //设置强制联动
    PARAM_FORCE_FLASE("02");                                            //设置普通联动

    private String val;

    SceneParam(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
