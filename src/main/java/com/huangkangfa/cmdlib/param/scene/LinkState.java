package com.huangkangfa.cmdlib.param.scene;

/**
 * 联动开启或关闭的状态
 * Created by huangkangfa on 2017/6/16.
 */
public enum LinkState {
    OPEN("01"),                                             //设置的联动开启
    CLOSE("02");                                            //设置的联动关闭

    private String val;

    LinkState(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
