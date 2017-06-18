package com.huangkangfa.cmdlib.utils;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
public class StringUtil {
    /**
     * 字符串处理->获取偶数位字符串
     **/
    public static String getIpNum(String s) {
        StringBuffer S = new StringBuffer("");
        StringBuffer S_ = new StringBuffer(s);
        for (int i = 0; i < s.length(); i++) {
            if ((i + 1) % 2 == 0) {
                S.append(S_.substring(i, i + 1));
            }
        }
        return S.toString();
    }

    /**
     * 字符串处理->结尾去00
     */
    public static String getDataEndWithoutZero(String s) {
        StringBuffer S = new StringBuffer(s);
        while (true) {
            if (S.length() <= 2 || !S.substring(S.length() - 2, S.length()).equals("00")) {
                return S.toString();
            }
            S.delete(S.length() - 2, S.length());
        }
    }
    //判断字符串是否为空
    public static boolean isEmpty(String x){
        return "".equals(x);
    }
}
