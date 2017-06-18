package com.huangkangfa.cmdlib.enums;

/**
 * 命令标识 枚举
 */
public enum CmdID {
    READ("01"),         /**命令标识·读**/
    WRITE("02"),        /**命令标识·写**/
    TRANSPARENT("07"),  /**命令标识·半透传**/
    RESET("22"),        /**命令标识·复位**/
    SCENE("50"),        /**命令标识·场景联动**/
    NET("60");          /**命令标识·网络**/

    private String val;

    CmdID(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
