package com.huangkangfa.cmdlib.param.scene;

/**
 * 场景类型
 * Created by huangkangfa on 2017/6/19.
 */
public enum SceneType {
    SCENE("01"),                                             //要设置的场景类型为场景
    LINK("02");                                              //要设置的场景类型为联动

    private String val;

    SceneType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
