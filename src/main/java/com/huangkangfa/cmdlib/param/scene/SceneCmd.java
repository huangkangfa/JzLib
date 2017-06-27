package com.huangkangfa.cmdlib.param.scene;

/**
 * 场景子命令标识
 * Created by huangkangfa on 2017/6/19.
 */
public enum SceneCmd {
    SET("01");     //子命令标识，表示参数配置命令

    private String val;

    SceneCmd(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
