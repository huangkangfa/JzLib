package com.huangkangfa.cmdlib.exception;

/**
 * 组帧错误异常类
 */
public class CmdException extends RuntimeException{
    public static final String CmdIDisNull="数据帧错误。命令标识不能为空";
    public static final String DstAddrFmtEnumisNull="数据帧错误。指令作用对象不能为空";
    public static final String AddrisNull="数据帧错误。指令目标地址不能为空";
    public static final String ODisNull="数据帧错误。OD类型不能为空";
    public static final String IndexisNull="数据帧错误。子索引不能为空";
    public static final String DataisNull="数据帧错误。指令数据不能为空";

    public CmdException(String msg){
        super(msg);
    }

}
