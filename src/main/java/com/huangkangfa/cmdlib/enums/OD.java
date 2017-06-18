package com.huangkangfa.cmdlib.enums;

/**
 * Created by huangkangfa on 2017/6/15.
 */

public enum OD {
    _null(""),
    _1001("03e9"),
    _1005("03ed"),
    _1007("03ef"),
    _4010("0faa"),
    _4020("0fb4"),
    _4030("0fbe"),
    _4040("0fc8"),
    _4070("0fe6"),
    _5010("1392"),
    _5020("139c"),
    _5040("13b0"),
    _5060("13c4"),
    _6001("1771"),
    _6003("1773"),
    _7001("1b59");

    private String val;

    OD(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
