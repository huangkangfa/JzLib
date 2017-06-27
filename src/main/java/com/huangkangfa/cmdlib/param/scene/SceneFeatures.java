package com.huangkangfa.cmdlib.param.scene;

/**
 * 场景功能特性
 * Created by huangkangfa on 2017/6/19.
 */
public enum SceneFeatures {
    DEL("07"),                                           //场景功能特性为删除
    TRIGGER("09"),                                         //场景功能特性为触发
    SET("00");                                          //场景功能特性为设置

    private String val;

    SceneFeatures(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
