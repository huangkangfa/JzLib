package com.huangkangfa.cmdlib.param.scene;

/**
 * 普通联动还是强制联动
 * Created by huangkangfa on 2017/6/16.
 */
public enum LinkType {
    FORCE("01"),                                             //设置强制联动
    ORDINARY("02");                                            //设置普通联动

    private String val;

    LinkType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
