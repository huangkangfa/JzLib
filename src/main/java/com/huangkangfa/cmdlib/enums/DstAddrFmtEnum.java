package com.huangkangfa.cmdlib.enums;

/**
 * 指令作用对象 枚举
 */
public enum DstAddrFmtEnum {
    GATEWAY("00"),  /**指令作用对象·网关**/
    NODE("01"),     /**指令作用对象·设备**/
    ALL("ff");      /**指令作用对象·广播**/

    private String val;

    DstAddrFmtEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
