package com.huangkangfa.cmdlib.param;

import com.huangkangfa.cmdlib.param.scene.LinkState;
import com.huangkangfa.cmdlib.param.scene.LinkType;
import com.huangkangfa.cmdlib.param.scene.SceneCmd;
import com.huangkangfa.cmdlib.param.scene.SceneFeatures;
import com.huangkangfa.cmdlib.param.scene.SceneType;

/**
 * 场景配置参数
 * Created by huangkangfa on 2017/6/16.
 */

public class SceneParam {
    public static SceneCmd  SCENECMD;                                                //子命令标识，表示参数配置命令
    public static String DEFAULTNAME="0000000000000000000000000000000000000000";     //存至网关的默认场景名称
    public static SceneType SCENETYPE;                                               //要设置的场景类型
    public static SceneFeatures SCENEFEATURES;                                       //场景功能特性
    public static LinkState LINKSTATE;                                               //设置的联动状态
    public static LinkType LINKTYPE;                                                 //设置是否强制联动
}
