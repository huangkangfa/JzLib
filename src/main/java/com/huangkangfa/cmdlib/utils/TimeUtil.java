package com.huangkangfa.cmdlib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/3 0003.
 */
public class TimeUtil {
    public static int getIntFromWeekName(String name){
        switch (name){
            case "周一":
                return 2;
            case "周二":
                return 3;
            case "周三":
                return 4;
            case "周四":
                return 5;
            case "周五":
                return 6;
            case "周六":
                return 7;
            case "周日":
                return 1;
        }
        return -1;
    }
    /**
     * 获取当前时间(yyyy-MM-dd-E-HH-mm-ss-SS)
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-E-HH-mm-ss-SS");
        String time = df.format(new Date());// new Date()为获取当前系统时间
        return time;
    }
}
