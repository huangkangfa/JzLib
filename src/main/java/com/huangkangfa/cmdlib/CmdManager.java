package com.huangkangfa.cmdlib;

import android.util.Log;

import com.huangkangfa.cmdlib.address.Addr;
import com.huangkangfa.cmdlib.enums.CmdID;
import com.huangkangfa.cmdlib.enums.DstAddrFmtEnum;
import com.huangkangfa.cmdlib.enums.EffectiveBit_1;
import com.huangkangfa.cmdlib.enums.EffectiveBit_4;
import com.huangkangfa.cmdlib.enums.OD;
import com.huangkangfa.cmdlib.frame.Frame_Distance;
import com.huangkangfa.cmdlib.frame.Frame_GatewayLogin;
import com.huangkangfa.cmdlib.frame.Frame_GatewayWiFi;
import com.huangkangfa.cmdlib.frame.Frame_LinkContral;
import com.huangkangfa.cmdlib.frame.Frame_Modle;
import com.huangkangfa.cmdlib.index.ChildIndex_Change;
import com.huangkangfa.cmdlib.index.Index_Change;
import com.huangkangfa.cmdlib.index.Index_Constant;
import com.huangkangfa.cmdlib.param.DeviceParam;
import com.huangkangfa.cmdlib.param.SceneParam;
import com.huangkangfa.cmdlib.utils.DataTypeUtil;
import com.huangkangfa.cmdlib.utils.StringUtil;
import com.huangkangfa.cmdlib.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 指令管理类
 * Created by huangkangfa on 2017/6/16.
 */
public class CmdManager {
    /*********************************** 网关登入与时间修正 ***************************************/
    //获取wifi模块下的zb的mac信息--登入帧
    public static String getCmdLoginFrame() {
        CmdObj cmd = new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr("0000000000000000"));
        cmd.setOD(OD._5010);
        cmd.setBaseindex(new Index_Constant("00"));
        cmd.setData("");
        return cmd.getValue();
    }

    //解析登入帧获取zigbee的mac地址
    public static Frame_GatewayLogin getZbMacFromLoginFrame(String data) {
        Frame_GatewayLogin frame = null;
        try {
            String index = data.substring(30, 38);
            String system_state = data.substring(38, 40);
            String pattern = data.substring(40, 42);
            String user_pwd = data.substring(42, 54);
            String universal_pwd = data.substring(54, 66);
            String wifi_mac = data.substring(66, 82);
            String zb_mac = data.substring(82, 98);
            String product_id = data.substring(98, 110);
            String product_code = data.substring(110, 122);
            String software_version = data.substring(122, 130);
            String hardware_version = data.substring(130, 138);
            frame = new Frame_GatewayLogin(index, system_state, pattern, user_pwd, universal_pwd, wifi_mac, zb_mac, product_id, product_code, software_version, hardware_version);
        } catch (Exception e) {
            Log.e("登入帧解析错误", e.getMessage());
        }
        return frame;
    }

    //单址读取网关的zigbee的mac地址
    public static String getCmdLoginFrame_OnlyZbMac() {
        CmdObj cmd = new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr("0000000000000000"));
        cmd.setOD(OD._5010);
        cmd.setBaseindex(new Index_Constant("07"));
        cmd.setData("");
        return cmd.getValue();
    }

    //解析单址读取网关的zigbee的MAC地址的指令，获取mac
    public static String getZbMacFromFrame(String data) {
        if (data.length() == 50 && data.substring(26, 28).equals("07") && data.substring(28, 30).equals("08")) {
            return data.substring(30, 46);
        }
        return "0000000000000000";
    }

    //获取该网关下的节点设备信息--刷新指令
    public static String getCmdAllNode(String mac) {
        CmdObj cmd = new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));
        cmd.setOD(OD._1005);
        cmd.setBaseindex(new Index_Constant("0f"));
        cmd.setData("");
        return cmd.getValue();
    }

    //时间校正指令
    public static String getCmdCorrectTime(String mac) {
        String[] ts = TimeUtil.getNowTime().split("-");
        String time = DataTypeUtil.setBytesLen(DataTypeUtil.decimalToHex(Integer.valueOf(ts[0].substring(ts[0].length() - 2, ts[0].length()))), 1, false)
                + DataTypeUtil.setBytesLen(DataTypeUtil.decimalToHex(Integer.valueOf(ts[1])), 1, false)
                + DataTypeUtil.setBytesLen(DataTypeUtil.decimalToHex(Integer.valueOf(ts[2])), 1, false)
                + DataTypeUtil.setBytesLen(DataTypeUtil.decimalToHex(TimeUtil.getIntFromWeekName(ts[3])), 1, false)
                + DataTypeUtil.setBytesLen(DataTypeUtil.decimalToHex(Integer.valueOf(ts[4])), 1, false)
                + DataTypeUtil.setBytesLen(DataTypeUtil.decimalToHex(Integer.valueOf(ts[5])), 1, false)
                + DataTypeUtil.setBytesLen(DataTypeUtil.decimalToHex(Integer.valueOf(ts[6])), 1, false) + "00";
        CmdObj cmd = new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));
        cmd.setOD(OD._5040);
        cmd.setBaseindex(new Index_Constant("02", DataTypeUtil.decimalToHex(time.length() / 2)));
        cmd.setData(time);
        return cmd.getValue();
    }


    /*************************************** 多路面板 *******************************************/
    /**
     * 多路控制指令
     *
     * @param gateway_mac 网关下zigbee的mac
     * @param node_mac    节点mac
     * @param type_area   设备类别+产品类型   区域控制时用到
     * @param area        区域信息    没有区域参数时直接传空字符串
     * @param A           A路开关
     * @param A_delay     A路时间参数
     * @param B           B路开关
     * @param B_delay     B路时间参数
     * @param C           C路开关
     * @param C_delay     C路时间参数
     * @return
     */
    public static String getCmd_Varied(String gateway_mac, String node_mac, String type_area, String area, String A, String A_delay, String B, String B_delay, String C, String C_delay) {
        CmdObj cmd = new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(area == null ? DstAddrFmtEnum.NODE : DstAddrFmtEnum.ALL);
        cmd.setAddr(area == null ? new Addr(gateway_mac, node_mac) : new Addr(gateway_mac));
        cmd.setOD(OD._4010);

        StringBuilder sb = new StringBuilder();  //实际指令
        Index_Change index = new Index_Change(); //可变索引
        ChildIndex_Change cc_index = new ChildIndex_Change(); //子索引选择项

        if (area != null) {
            sb.append(type_area).append(area);
            cc_index.add(EffectiveBit_4.PARAM_AREA.getVal());
        }
        if (A != null) {
            cc_index.add(EffectiveBit_4.PARAM_VARIED_A_MOMENT.getVal());
            if (A_delay != null) {
                cc_index.add(EffectiveBit_4.PARAM_VARIED_A_DELAY.getVal());
                sb.append(DeviceParam.PARAM_WAY_DELAY + A + DataTypeUtil.setBytesLen(A_delay, 2, false));
            } else {
                sb.append(DeviceParam.PARAM_WAY_STRAIGHT + A);
            }
        }
        if (B != null) {
            cc_index.add(EffectiveBit_4.PARAM_VARIED_B_MOMENT.getVal());
            if (B_delay != null) {
                cc_index.add(EffectiveBit_4.PARAM_VARIED_B_DELAY.getVal());
                sb.append(DeviceParam.PARAM_WAY_DELAY + B + DataTypeUtil.setBytesLen(B_delay, 2, false));
            } else {
                sb.append(DeviceParam.PARAM_WAY_STRAIGHT + B);
            }
        }
        if (C != null) {
            cc_index.add(EffectiveBit_4.PARAM_VARIED_C_MOMENT.getVal());
            if (C_delay != null) {
                cc_index.add(EffectiveBit_4.PARAM_VARIED_C_DELAY.getVal());
                sb.append(DeviceParam.PARAM_WAY_DELAY + C + DataTypeUtil.setBytesLen(C_delay, 2, false));
            } else {
                sb.append(DeviceParam.PARAM_WAY_STRAIGHT + C);
            }
        }

        cmd.setData(sb.toString());
        index.setChildIndex_Change(cc_index);
        index.setLen(DataTypeUtil.decimalToHex(4 + sb.toString().length() / 2));
        cmd.setBaseindex(index);

        return cmd.getValue();
    }

    //多路面板联控指令 写入
    public static String getCmd_Write_LinkControl(String gateway_mac, String mac, int num, int startWay, int endWay, String address_mac, String functionId) {
        CmdObj cmd = new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac, mac));
        cmd.setOD(OD._6003);

        StringBuilder sb = new StringBuilder();
        sb.append(DataTypeUtil.decimalToHex(startWay)).append(DataTypeUtil.decimalToHex(endWay)).append(address_mac).append(functionId == null ? "0100" : functionId);

        cmd.setBaseindex(new Index_Constant(DataTypeUtil.decimalToHex(num), DataTypeUtil.decimalToHex(sb.toString().length() / 2)));
        cmd.setData(sb.toString());

        return cmd.getValue();
    }
    //多路面板联控指令  读取
    public static String getCmd_Read_LinkControl(String gateway_mac,String mac,int num){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._6003);
        cmd.setBaseindex(new Index_Constant(DataTypeUtil.decimalToHex(num)));
        cmd.setData("");
        return cmd.getValue();
    }
    //多路面板解除绑定关系
    public static String getCmd_Unbing_LinkContral(String gateway_mac,String mac,int num){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._6003);
        cmd.setBaseindex(new Index_Constant(DataTypeUtil.decimalToHex(num)));
        cmd.setData("0cffff0000000000000000ffff");
        return cmd.getValue();
    }
    //多路面板联控指令反馈解析
    public static Frame_LinkContral getCmd_LinkContralFromString(String data){
        Frame_LinkContral m=new Frame_LinkContral();
        m.setClientMac(data.substring(6,22));
        m.setIndex(data.substring(26,28));
        m.setClientNum(data.substring(30,32));
        m.setServerNum(data.substring(32,34));
        m.setServerMac(data.substring(34,50));
        m.setOthers(data.substring(50,54));
        return m;
    }

    /*************************************** 色温灯(测试) *******************************************/
    public static String getCmd_ColorTemperature(String gateway_mac, String node_mac, boolean openFlag, String ld, String sw) {
        CmdObj cmd = new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac, node_mac));
        cmd.setOD(OD._4010);

        StringBuilder sb = new StringBuilder();
        if (openFlag) {
            sb.append(DeviceParam.PARAM_STATE_OPEN).append(ld).append(sw);
        } else {
            sb.append(DeviceParam.PARAM_STATE_CLOSE).append("0000");
        }
        cmd.setData(sb.toString());

        Index_Change index = new Index_Change();
        ChildIndex_Change cc_index = new ChildIndex_Change();
        cc_index.add(EffectiveBit_4.PARAM_COLORTEMPERATURELIGHT_ALL.getVal());
        index.setChildIndex_Change(cc_index);
        index.setLen(DataTypeUtil.decimalToHex(4 + sb.toString().length() / 2));
        cmd.setBaseindex(index);

        return cmd.getValue();
    }

    /*************************************** 多彩灯 *******************************************/
    /**
     * 获取多彩灯控制指令
     * 控制的模式有直接、渐渐、延时、七彩跳变、七彩渐变（不带时间参数）、呼吸灯（不带时间参数）
     * 控制的类型有调暖光、调冷光、调色、特殊（针对七彩跳变、七彩渐变、呼吸灯）
     *
     * @param gateway_mac 网关下zigbee的mac
     * @param node_mac    节点mac
     * @param type_area   设备类别+产品类型   区域控制时用到
     * @param area        区域信息    没有区域参数时直接传空字符串
     * @param startType   开启的模式
     * @param colorType   开启的类型
     * @param time        延时效果的时间参数
     * @param state       开关状态
     * @param r_or_w      调光的w    或   调色的r
     * @param g           调色的g
     * @param b           调色的b
     * @return
     */
    public static String getCmd_ColorLight(String gateway_mac, String node_mac, String type_area, String area, String startType, String colorType, String time, boolean state, String r_or_w, String g, String b) {
        CmdObj cmd = new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(area == null ? DstAddrFmtEnum.NODE : DstAddrFmtEnum.ALL);
        cmd.setAddr(area == null ? new Addr(gateway_mac, node_mac) : new Addr(gateway_mac));
        cmd.setOD(OD._4010);

        boolean needTime = (DeviceParam.PARAM_WAY_SLOWLY.equals(startType) || DeviceParam.PARAM_WAY_DELAY.equals(startType) || DeviceParam.PARAM_MODE_COLORFUL_JUMP.equals(startType));
        StringBuilder sb = new StringBuilder();
        Index_Change index = new Index_Change();
        ChildIndex_Change cc_index = new ChildIndex_Change();

        if (area != null) {
            sb.append(type_area).append(area);
            cc_index.add(EffectiveBit_4.PARAM_AREA.getVal());
        }
        if (needTime) {
            if (DeviceParam.PARAM_WAY_STRAIGHT.getVal().equals(startType)) {
                time = "0000";
            }
            cc_index.add(EffectiveBit_4.PARAM_COLORLIGHT_NEEDTIME.getVal());
        } else {
            time = "";
        }
        if (DeviceParam.PARAM_COLORLIGHT_TYPE_MONOTONE_COLD.getVal().equals(colorType) || DeviceParam.PARAM_COLORLIGHT_TYPE_MONOTONE_WARM.getVal().equals(colorType)) {
            cc_index.add(EffectiveBit_4.PARAM_COLORLIGHT_MONOTONE.getVal());
        } else {
            cc_index.add(EffectiveBit_4.PARAM_COLORLIGHT_COLORFUL.getVal());
        }
        if (DeviceParam.PARAM_MODE_COLORFUL_JUMP.getVal().equals(startType) || DeviceParam.PARAM_MODE_COLORFUL_SLOWLY.getVal().equals(startType)) {
            colorType = DeviceParam.PARAM_COLORLIGHT_TYPE_SPECIAL.getVal();
        }
        sb.append(startType).append(colorType).append(DataTypeUtil.setBytesLen(time, 2, false)).append(state ? DeviceParam.PARAM_STATE_OPEN : DeviceParam.PARAM_STATE_CLOSE);
        if (DeviceParam.PARAM_COLORLIGHT_TYPE_MONOTONE_WARM.getVal().equals(colorType) || DeviceParam.PARAM_COLORLIGHT_TYPE_MONOTONE_COLD.getVal().equals(colorType)) {
            sb.append(r_or_w);
        } else if (DeviceParam.PARAM_COLORLIGHT_TYPE_COLORFUL.getVal().equals(colorType)) {
            sb.append(r_or_w + g + b);
        } else if (DeviceParam.PARAM_COLORLIGHT_TYPE_SPECIAL.getVal().equals(colorType)) {
            sb.append("000000");
        }

        cmd.setData(sb.toString());
        index.setChildIndex_Change(cc_index);
        index.setLen(DataTypeUtil.decimalToHex(4 + sb.toString().length() / 2));
        cmd.setBaseindex(index);

        return cmd.getValue();
    }

    /*************************************** 机械开关 *******************************************/
    /**
     * 多彩灯机械开关
     * 设置的模式有七彩跳变、七彩渐变、呼吸灯、调光、调色
     * @param gateway_mac  网关zigbee的mac
     * @param node_mac      节点mac
     * @param lightModeA    A路模式选择
     * @param r_w_A
     * @param gA
     * @param bA
     * @param lightModeB    B路模式选择
     * @param r_w_B
     * @param gB
     * @param bB
     * @return
     */
    public static String getCmd_ColorLight_Set(String gateway_mac, String node_mac,String type_area,String area, String lightModeA, String r_w_A, String gA, String bA, String lightModeB, String r_w_B, String gB, String bB) {
        if (lightModeA == null && lightModeB == null) return null;
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(area==null?DstAddrFmtEnum.NODE:DstAddrFmtEnum.ALL);
        cmd.setAddr(area==null?new Addr(gateway_mac,node_mac):new Addr(gateway_mac));
        cmd.setOD(OD._6001);

        Index_Change index =new Index_Change();
        ChildIndex_Change cc_index=new ChildIndex_Change();
        StringBuilder sb=new StringBuilder();
        if(area!=null){
            sb.append(type_area).append(area);
            cc_index.add(EffectiveBit_4.PARAM_AREA.getVal());
        }
        if(lightModeA!=null){
            if(DeviceParam.PARAM_MECHANICAL_MODE_MONOTONE.getVal().equals(lightModeA)){
                sb.append(lightModeA).append(r_w_A);
            }else if(DeviceParam.PARAM_MECHANICAL_MODE_COLORFUL.getVal().equals(lightModeA)){
                sb.append(lightModeA).append(r_w_A+gA+bA);
            }else if(DeviceParam.PARAM_MODE_COLORFUL_BREATH.getVal().equals(lightModeA)){
                sb.append(lightModeA+r_w_A+gA+bA);
            }else{
                sb.append(lightModeA+"000000");
            }
            cc_index.add(DeviceParam.PARAM_MECHANICAL_MODE_MONOTONE.getVal().equals(lightModeA)?EffectiveBit_4.PARAM_COLORLIGHTSET_A_MONOTONE.getVal():EffectiveBit_4.PARAM_COLORLIGHTSET_A_COLORFUL.getVal());
        }else{
            sb.append("00");
            cc_index.add(EffectiveBit_4.PARAM_COLORLIGHTSET_A_NULL.getVal());
        }
        if(lightModeB!=null){
            if(DeviceParam.PARAM_MECHANICAL_MODE_MONOTONE.getVal().equals(lightModeB)){
                sb.append(lightModeB).append(r_w_B);
            }else if(DeviceParam.PARAM_MECHANICAL_MODE_COLORFUL.getVal().equals(lightModeB)){
                sb.append(lightModeB).append(r_w_B+gB+bB);
            }else if(DeviceParam.PARAM_MODE_COLORFUL_BREATH.getVal().equals(lightModeB)){
                sb.append(lightModeB+r_w_B+gB+bB);
            }else{
                sb.append(lightModeB+"000000");
            }
            cc_index.add(DeviceParam.PARAM_MECHANICAL_MODE_MONOTONE.getVal().equals(lightModeB)?EffectiveBit_4.PARAM_COLORLIGHTSET_B_MONOTONE.getVal():EffectiveBit_4.PARAM_COLORLIGHTSET_B_COLORFUL.getVal());
        }else{
            sb.append("00");
            cc_index.add(EffectiveBit_4.PARAM_COLORLIGHTSET_B_null.getVal());
        }
        sb.append("0000");
        cc_index.add(EffectiveBit_4.PARAM_COLORLIGHTSET_C_null.getVal());
        cc_index.add(EffectiveBit_4.PARAM_COLORLIGHTSET_D_null.getVal());

        index.setChildIndex_Change(cc_index);
        index.setLen(DataTypeUtil.decimalToHex(4+sb.toString().length()/2));
        cmd.setData(sb.toString());
        cmd.setBaseindex(index);

        return cmd.getValue();
    }

    /*************************************** 场景控制器 *******************************************/
    /**
     * 获取场景控制器场景设置指令
     *
     * @param mac1 网关mac
     * @param mac2 设备mac
     * @param A    第一个按钮对应的场景编号
     * @param B    第二个按钮对应的场景编号
     * @param C    第三个按钮对应的场景编号
     * @return
     */
    public static String getStateControlCommand(String mac1, String mac2,String type_area,String area, String A, String B, String C) {
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(area==null?DstAddrFmtEnum.NODE:DstAddrFmtEnum.ALL);
        cmd.setAddr(area==null?new Addr(mac1,mac2):new Addr(mac1));
        cmd.setOD(OD._4010);

        StringBuilder sb=new StringBuilder();
        Index_Change index=new Index_Change();
        ChildIndex_Change cc_index=new ChildIndex_Change();

        if(area!=null){
            sb.append(type_area).append(area);
            cc_index.add(EffectiveBit_4.PARAM_AREA.getVal());
        }
        sb.append(DeviceParam.PARAM_STATECONTROL_PARAM_TYPE.getVal());
        if(A!=null){
            sb.append(A);
            cc_index.add(EffectiveBit_4.PARAM_STATECONTROL_A.getVal());
        }
        if(B!=null){
            sb.append(B);
            cc_index.add(EffectiveBit_4.PARAM_STATECONTROL_B.getVal());
        }
        if(C!=null){
            sb.append(C);
            cc_index.add(EffectiveBit_4.PARAM_STATECONTROL_C.getVal());
        }

        index.setChildIndex_Change(cc_index);
        index.setLen(DataTypeUtil.decimalToHex(4+sb.toString().length()/2));
        cmd.setBaseindex(index);
        cmd.setData(sb.toString());

        return cmd.getValue();
    }

    /*************************************** 测试设备 *******************************************/
    public static String getTestDeviceCommand(String mac1,String mac2,String num,String txt){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(mac1,mac2));
        cmd.setOD(OD._7001);

        StringBuilder sb=new StringBuilder();
        Index_Constant index=new Index_Constant(num);
        sb.append("12").append(num).append(DataTypeUtil.decimalToHex(txt.length()/4)).append(DataTypeUtil.setBytesLen(txt,16,true));

        cmd.setBaseindex(index);
        cmd.setData(sb.toString());

        return cmd.getValue();
    }

    /*************************************** 数据采集设备 *******************************************/
    //读取数据采集器的相关数据
    public static String getCmd_CollectionEquipment_Refresh(String gateway_mac, String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._4020);
        cmd.setBaseindex(new Index_Constant("00"));
        cmd.setData("");
        return cmd.getValue();
    }

    /*************************************** 计量设备 *******************************************/
    /**
     * 计量设备开关控制
     * @param gateway_mac  网关下zigbee的mac
     * @param mac     节点mac
     * @param type_area 设备类型+产品类别
     * @param area      区域
     * @param state   开关状态
     * @return
     */
    public static String getCmd_MeteringEquipment(String gateway_mac, String mac,String type_area, String area, boolean state,String time) {
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(area==null?DstAddrFmtEnum.NODE:DstAddrFmtEnum.ALL);
        cmd.setAddr(area==null?new Addr(gateway_mac,mac):new Addr(gateway_mac));
        cmd.setOD(OD._4040);

        Index_Change index=new Index_Change();
        ChildIndex_Change cc_index=new ChildIndex_Change();
        StringBuilder sb=new StringBuilder();
        cc_index.add(EffectiveBit_4.PARAM_METERINGEQUIPMENT_SWITCH.getVal());
        if(area!=null){
            sb.append(type_area).append(area).append(state?DeviceParam.PARAM_STATE_OPEN:DeviceParam.PARAM_STATE_CLOSE);
            cc_index.add(EffectiveBit_4.PARAM_AREA.getVal());
        }else{
            sb.append(state?DeviceParam.PARAM_STATE_OPEN:DeviceParam.PARAM_STATE_CLOSE);
        }
        sb.append(time);

        index.setChildIndex_Change(cc_index);
        index.setLen(DataTypeUtil.decimalToHex(4+sb.toString().length()/2));
        cmd.setBaseindex(index);
        cmd.setData(sb.toString());
        return cmd.getValue();
    }
    //计量设备电量清零
    public static String getCmd_MeteringEquipmentSetZero(String gateway_mac, String mac) {
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._4040);

        StringBuilder sb=new StringBuilder("00000000");
        Index_Constant index =new Index_Constant(EffectiveBit_1.PARAM_ELECTRICITY.getVal(),DataTypeUtil.decimalToHex(sb.toString().length()/2));
        cmd.setBaseindex(index);
        cmd.setData(sb.toString());

        return cmd.getValue();
    }

    /*************************************** 透传外壳 *******************************************/
    //获取透传指令
    public static String getCmd_TransparentTransmission(String mac1, String mac2, String data) {
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.TRANSPARENT);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(mac1,mac2));
        cmd.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(data.length()/2)));
        cmd.setData(data);
        return  cmd.getValue();
    }

    /************************************ 网关或设备配置 *****************************************/
    /**
     * 复位指令
     * @param gateway_mac  网关mac
     * @param mac   节点mac   当节点mac为""时，代表网关复位指令
     */
    public static String getCmd_Reset(String gateway_mac,String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.RESET);
        cmd.setDstAddrFmtEnum(mac==null?DstAddrFmtEnum.GATEWAY:DstAddrFmtEnum.NODE);
        cmd.setAddr(mac==null?new Addr(gateway_mac,gateway_mac):new Addr(gateway_mac,mac));
        cmd.setBaseindex(new Index_Constant(""));
        cmd.setData("");
        return cmd.getValue();
    }
    /**
     * 修改网关心跳值
     * @param gateway_mac  网关
     * @param num   16进制具体值
     * @return
     */
    public static String getCmd_HeartBeatSetZero(String gateway_mac,String num){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setOD(OD._5020);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_READNUM_SUM.getVal(),DataTypeUtil.decimalToHex(num.length()/2)));
        cmd.setData(num);
        return cmd.getValue();
    }
    //是否是指定网关的心跳上报帧
    public static boolean getBooleanFromHeartBeatString(String data,String mac){
        if(data.length()==106&&CmdID.READ.getVal().equals(data.substring(4,6))&&data.substring(6,22).equals(mac)&&data.substring(22,26).equals(OD._5020)&&DataTypeUtil.getCheckBoolean(data)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 入网时间窗
     * @param gateway_mac  网关
     * @param time  配置参数范围：0x00-0xFF，0x00 禁止入网，0xFF 永久允许    单位秒
     * @return
     */
    public static String getCmd_OpenOrCloseAddModles(String gateway_mac,String time,boolean state){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.NET);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(gateway_mac));

        StringBuilder sb=new StringBuilder();
        sb.append("01").append(state?DeviceParam.PARAM_STATE_OPEN:DeviceParam.PARAM_STATE_CLOSE).append("01").append(time).append("0000");
        cmd.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(sb.toString().length()/2)));
        cmd.setData(sb.toString());
        return cmd.getValue();
    }
    //设置设备区域
    public static String getSetAreaCommand(String gateway_mac, String mac, String area) {
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._1007);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_AREA.getVal(),"01"));
        cmd.setData(area);
        return cmd.getValue();
    }
    //单址读取无线通道
    public static String getCmd_ReadWirelessChannel(String gateway_mac,String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(mac==null?DstAddrFmtEnum.GATEWAY:DstAddrFmtEnum.NODE);
        cmd.setAddr(mac==null?new Addr(gateway_mac):new Addr(gateway_mac,mac));
        cmd.setOD(OD._1001);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_WIRELESSCHANNEL.getVal()));
        cmd.setData("");

        return cmd.getValue();
    }
    //单址写入无线通道
    public static String getCmd_WriteWirelessChannel(String gateway_mac,String mac,String data){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(mac==null?DstAddrFmtEnum.GATEWAY:DstAddrFmtEnum.NODE);
        cmd.setAddr(mac==null?new Addr(gateway_mac):new Addr(gateway_mac,mac));
        cmd.setOD(OD._1001);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_WIRELESSCHANNEL.getVal(),DataTypeUtil.decimalToHex(data.length()/2)));
        cmd.setData(data);

        return cmd.getValue();
    }
    //单址读取无线通道反馈解析，获取无线通道字段   返回null代表解析失败
    public static String getWirelessChannelFromString(String data,String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&CmdID.READ.equals(data.substring(4,6))&&EffectiveBit_1.PARAM_WIRELESSCHANNEL.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    //单址读取PAN ID
    public static String getCmd_ReadPanId(String gateway_mac,String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(mac==null?DstAddrFmtEnum.GATEWAY:DstAddrFmtEnum.NODE);
        cmd.setAddr(mac==null?new Addr(gateway_mac):new Addr(gateway_mac,mac));
        cmd.setOD(OD._1001);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_PANID.getVal()));
        cmd.setData("");
        return cmd.getValue();
    }
    //单址写入PAN ID
    public static String getCmd_WritePanId(String gateway_mac,String mac,String data){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(mac==null?DstAddrFmtEnum.GATEWAY:DstAddrFmtEnum.NODE);
        cmd.setAddr(mac==null?new Addr(gateway_mac):new Addr(gateway_mac,mac));
        cmd.setOD(OD._1001);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_PANID.getVal(),DataTypeUtil.decimalToHex(data.length()/2)));
        cmd.setData(data);
        return cmd.getValue();
    }
    //单址读取无线通道反馈解析，获取无线通道字段   返回null代表解析失败
    public static String getPanIdFromString(String data,String mac){
        if(data.length()==38){
            if(data.substring(6,22).equals(mac)&&CmdID.READ.equals(data.substring(4,6))&&EffectiveBit_1.PARAM_PANID.equals(data.substring(26,28))){
                return data.substring(30,34);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    //单址读取设备波特率
    public static String getCmd_ReadBaudRate(String gateway_mac,String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._1007);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_BAUDRATE.getVal()));
        return cmd.getValue();
    }
    //单址写入设备波特率
    public static String getCmd_WriteBaudRate(String gateway_mac,String mac,String data){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._1007);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_BAUDRATE.getVal(),DataTypeUtil.decimalToHex(data.length()/2)));
        cmd.setData(data);
        return cmd.getValue();
    }
    //单址读取设备波特率反馈解析，获取设备波特率字段   返回null代表解析失败
    public static String getBaudRateFromString(String data,String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&CmdID.READ.equals(data.substring(4,6))&&EffectiveBit_1.PARAM_BAUDRATE.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    //单址读取设备检验方式
    public static String getCmd_ReadCheckType(String gateway_mac,String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._1007);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_CHECKTYPE.getVal()));
        cmd.setData("");
        return cmd.getValue();
    }
    //单址写入设备检验方式
    public static String getCmd_WriteCheckType(String gateway_mac,String mac,String data){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._1007);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_CHECKTYPE.getVal(),DataTypeUtil.decimalToHex(data.length()/2)));
        cmd.setData(data);
        return  cmd.getValue();
    }
    //单址读取设备检验方式反馈解析，获取设备检验方式字段   返回null代表解析失败
    public static String getCheckTypeFromString(String data,String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&CmdID.READ.equals(data.substring(4,6))&&EffectiveBit_1.PARAM_CHECKTYPE.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    //单址读取设备类别
    public static String getCmd_ReadEquipmentCategory(String gateway_mac,String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._1007);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_EQUIPMENTCATEGORY.getVal()));
        return cmd.getValue();
    }
    //单址写入设备类别
    public static String getCmd_WriteEquipmentCategory(String gateway_mac,String mac,String data){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._1007);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_EQUIPMENTCATEGORY.getVal(),DataTypeUtil.decimalToHex(data.length()/2)));
        cmd.setData(data);
        return cmd.getValue();
    }
    //单址读取设备类别反馈解析，获取设备类别字段   返回null代表解析失败
    public static String getEquipmentCategoryFromString(String data,String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&CmdID.READ.equals(data.substring(4,6))&&EffectiveBit_1.PARAM_EQUIPMENTCATEGORY.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    //单址读取产品类型
    public static String getCmd_ReadProductType(String gateway_mac,String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._1007);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_PRODUCTTYPE.getVal()));
        return cmd.getValue();
    }
    //单址写入产品类型
    public static String getCmd_WriteProductType(String gateway_mac,String mac,String data){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.NODE);
        cmd.setAddr(new Addr(gateway_mac,mac));
        cmd.setOD(OD._1007);
        cmd.setBaseindex(new Index_Constant(EffectiveBit_1.PARAM_PRODUCTTYPE.getVal(),DataTypeUtil.decimalToHex(data.length()/2)));
        cmd.setData(data);
        return cmd.getValue();
    }
    //单址读取产品类型反馈解析，获取产品类型字段   返回null代表解析失败
    public static String getProductTypeFromString(String data,String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&CmdID.READ.equals(data.substring(4,6))&&EffectiveBit_1.PARAM_PRODUCTTYPE.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    /************************************ 场景相关 *****************************************/
    /**
     * 读取网关已经配置好的场景信息
     * @param mac
     * @return
     */
    public static String getCmd_ReadSceneSumData(String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.SCENE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));
        cmd.setOD(OD._null);
        cmd.setBaseindex(new Index_Constant("01"));
        cmd.setData("06");
        return cmd.getValue();
    }

    /**
     * 读取网关已经配置好的场景信息的反馈解析
     * @param data
     * @return
     */
    public static List<Integer> getSceneIdFromString(String data){
        String temp=data.substring(36,48);
        StringBuffer b=new StringBuffer("");
        for(int i=0;i<temp.length();i=i+2){
            StringBuffer tt=new StringBuffer(DataTypeUtil.hexToBinary(temp.substring(i,i+1),true));
            tt.append(DataTypeUtil.hexToBinary(temp.substring(i+1,i+2),true));
            b.append(tt.reverse());
        }
        String bf=b.toString();
        List<Integer> D=new ArrayList<Integer>();
        for(int i=0;i<bf.length();i++){
            if(b.substring(i,i+1).equals("1")){
                D.add(i+1);
            }
        }
        return D;
    }
    /**
     * 场景设置
     * @param cmds      要配置的指令
     * @param mac       网关地址
     * @param bh        编号
     * @param dh        代号
     * @param time      延时时间  2个字节
     * @param startTime 定时时间  2个字节
     * @return
     */
    public static String[] getSetSceneCommand_ForCJ(List<String> cmds, String mac, String bh, String dh, String time, String startTime) {
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.RESET);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));

        StringBuilder sb=new StringBuilder();
        sb.append(SceneParam.PARAM_FLAG_SET).append(SceneParam.PARAM_DEFAULT_NAME).append(SceneParam.PARAM_TYPE_SCENE)
                .append(bh).append(SceneParam.PARAM_FEATURES_SET).append(DataTypeUtil.decimalToHex(cmds.size())).append(dh)
                .append(startTime).append("ffffffffffffffffffffffffffffffffffffffffffffffff").append(time);
        cmd.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(sb.toString().length()/2)));
        cmd.setData(sb.toString());

        String[] s = new String[cmds.size() + 1];
        s[0]=cmd.getValue();
        for(int i=1;i<s.length;i++){
            CmdObj c=new CmdObj();
            c.setCmdID(CmdID.SCENE);
            c.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
            c.setAddr(new Addr(mac));
            StringBuilder b=new StringBuilder();
            b.append(CmdID.WRITE).append(bh).append(DataTypeUtil.decimalToHex(cmds.size())).append(DataTypeUtil.decimalToHex(i)).append(cmds.get(i-1));
            c.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(b.toString().length()/2)));
            c.setData(b.toString());
            s[i]=c.getValue();
        }
        return  s;
    }
    /**
     * 设置联动指令
     * @param cmds
     * @param mac
     * @param bh
     * @param force     01为强制联动；02为非强制联动
     * @param bcf
     * @param mac2
     * @param channel   数据通道
     * @param dataType  数据类型
     * @param compType  比较类型
     * @param dataDown
     * @param dataUp
     * @param time
     * @return
     */
    public static String[] getSetSceneCommand_ForLD(List<String> cmds, String mac, String bh, String force, String bcf, String mac2, String channel, String dataType, String compType, String dataDown, String dataUp, String time) {
        String[] s = new String[cmds.size() + 1];
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.SCENE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));

        StringBuilder sb=new StringBuilder();
        sb.append(SceneParam.PARAM_FLAG_SET).append(SceneParam.PARAM_DEFAULT_NAME).append(SceneParam.PARAM_TYPE_LINK).append(bh)
                .append(SceneParam.PARAM_FEATURES_SET).append(DataTypeUtil.decimalToHex(cmds.size())).append("ffffff").append(force)
                .append(SceneParam.PARAM_LINK_OPEN).append(bcf).append(mac2).append(channel).append(dataType).append(compType)
                .append("0018").append(dataDown).append(dataUp).append(time);
        cmd.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(sb.toString().length()/2)));
        cmd.setData(sb.toString());
        s[0]=cmd.getValue();
        for (int i=1;i<s.length;i++){
            CmdObj c=new CmdObj();
            c.setCmdID(CmdID.SCENE);
            c.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
            c.setAddr(new Addr(mac));
            StringBuilder b=new StringBuilder();
            b.append(CmdID.WRITE).append(bh).append(DataTypeUtil.decimalToHex(cmds.size())).append(DataTypeUtil.decimalToHex(i)).append(cmds.get(i-1));
            c.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(b.toString().length() / 2)));
            c.setData(b.toString());
            s[i]=c.getValue();
        }
        return s;
    }
    /**
     * 删除场景指令
     *
     * @param mac 网关
     * @param num 编号
     * @return
     */
    public static String getDelSceneCommand(String mac, String num) {
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.SCENE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));
        StringBuilder sb=new StringBuilder();
        sb.append(SceneParam.PARAM_FEATURES_DEL).append(num);
        cmd.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(sb.toString().length() / 2)));
        cmd.setData(sb.toString());
        return cmd.getValue();
    }
    /**
     * 触发场景或联动指令
     *
     * @param mac 网关
     * @param num 编号
     * @return
     */
    public static String getStartSceneCommand(String mac, String num) {
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.SCENE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));
        StringBuilder sb=new StringBuilder();
        sb.append(SceneParam.PARAM_FEATURES_START).append(num);
        cmd.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(sb.toString().length()/2)));
        cmd.setData(sb.toString());
        return cmd.getValue();
    }

    /**
     * 读取网关布防撤防跟报警状态
     * @param mac
     * @return
     */
    public static String getCmd_ReadDefense(String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.SCENE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));
        StringBuilder sb=new StringBuilder();
        sb.append("0300ff");
        cmd.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(sb.toString().length()/2)));
        cmd.setData(sb.toString());
        return cmd.getValue();
    }

    /**
     * 解析网关状态  获取撤防状态跟警报状态
     * @param data
     * @return
     */
    public static boolean[] getDefenseDataFromString(String data){
        boolean[] a=new boolean[2];
        int len=data.length();
        a[0]=data.substring(len-8,len-6).equals(DeviceParam.PARAM_STATE_OPEN);
        a[1]=data.substring(len-6,len-4).equals(DeviceParam.PARAM_STATE_OPEN);
        return a;
    }

    /**
     * 设置网关布防撤防状态
     * @param mac
     * @param flag
     * @return
     */
    public static String getCmd_SetDefense(String mac,boolean flag){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.SCENE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));
        StringBuilder sb=new StringBuilder();
        sb.append("05").append(flag?DeviceParam.PARAM_STATE_OPEN:DeviceParam.PARAM_STATE_CLOSE);
        cmd.setBaseindex(new Index_Constant("",DataTypeUtil.decimalToHex(sb.toString().length()/2)));
        cmd.setData(sb.toString());
        return cmd.getValue();
    }

    /**
     * 取消报警状态
     * @param mac 指定网关
     * @return
     */
    public static String getCmd_CancelAlert(String mac){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.SCENE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(mac));
        cmd.setBaseindex(new Index_Constant("01"));
        cmd.setData("08");
        return cmd.getValue();
    }
    /****************************** 配置云端ip及端口，还有本地端口 ***********************************/
    //云端读取远程地址、远程端口、本地端口
    public static String getCmd_ReadDistanceData(String gateway_mac,boolean dip,boolean dp,boolean lp){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.READ);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(gateway_mac));
        cmd.setOD(OD._5060);

        Index_Change index=new Index_Change();
        ChildIndex_Change cc_index=new ChildIndex_Change();
        if(dip){
            cc_index.add(EffectiveBit_4.PARAM_DISTANCEDATA_IP.getVal());
        }
        if(dp){
            cc_index.add(EffectiveBit_4.PARAM_DISTANCEDATA_PORT.getVal());
        }
        if(lp){
            cc_index.add(EffectiveBit_4.PARAM_DISTANCEDATA_LOCAL_PORT.getVal());
        }

        index.setChildIndex_Change(cc_index);
        index.setLen("");
        cmd.setBaseindex(index);
        cmd.setData("");
        return cmd.getValue();
    }
    //云端读取远程地址、远程端口、本地端口反馈解析
    public static Frame_Distance getDistanceDataFromString(String data, String mac){
        Frame_Distance s=null;
        if(CmdID.READ.equals(data.substring(4,6))&&mac.equals(data.substring(6,22))&&OD._5060.equals(data.substring(22,26))){
            String index=data.substring(30,38);
            ChildIndex_Change cc_index=new ChildIndex_Change();
            cc_index.add(EffectiveBit_4.PARAM_DISTANCEDATA_IP.getVal());
            cc_index.add(EffectiveBit_4.PARAM_DISTANCEDATA_PORT.getVal());
            cc_index.add(EffectiveBit_4.PARAM_DISTANCEDATA_LOCAL_PORT.getVal());
            if(EffectiveBit_4.PARAM_DISTANCEDATA_IP.equals(index)){
                //单单远程地址
                s=new Frame_Distance(data.substring(38,data.length()-4),"","");
            }else if(EffectiveBit_4.PARAM_DISTANCEDATA_PORT.equals(index)){
                //单单远程端口
                s=new Frame_Distance("",data.substring(38,42),"");
            }else if(EffectiveBit_4.PARAM_DISTANCEDATA_LOCAL_PORT.equals(index)){
                //单单本地端口
                s=new Frame_Distance("","",data.substring(38,42));
            }else if(cc_index.getValue().equals(index)){
                //三者都有
                s=new Frame_Distance(data.substring(46,data.length()-4),data.substring(42,46),data.substring(38,42));
            }
        }
        return s;
    }
    //云端配置远程地址、远程端口、本地端口
    public static String getCmd_WriteDistanceData(String gateway_mac,String dip,String dp,String lp){
        CmdObj cmd=new CmdObj();
        cmd.setCmdID(CmdID.WRITE);
        cmd.setDstAddrFmtEnum(DstAddrFmtEnum.GATEWAY);
        cmd.setAddr(new Addr(gateway_mac));
        cmd.setOD(OD._5060);

        Index_Change index=new Index_Change();
        ChildIndex_Change cc_index=new ChildIndex_Change();
        StringBuilder sb=new StringBuilder();

        if(dip!=null){
            sb.append(lp);
            cc_index.add(EffectiveBit_4.PARAM_DISTANCEDATA_IP.getVal());
        }
        if(dp!=null){
            sb.append(dp);
            cc_index.add(EffectiveBit_4.PARAM_DISTANCEDATA_PORT.getVal());
        }
        if(lp!=null){
            sb.append(lp);
            cc_index.add(EffectiveBit_4.PARAM_DISTANCEDATA_LOCAL_PORT.getVal());
        }

        index.setChildIndex_Change(cc_index);
        index.setLen(DataTypeUtil.decimalToHex(sb.toString().length()/2+4));
        cmd.setBaseindex(index);
        cmd.setData(sb.toString());
        return cmd.getValue();
    }
    /****************************** 对象获取 ***********************************/
    //获取网关设备信息
    public static Frame_GatewayWiFi getGatewayByString(String data, String ssid) {
        data = data.trim().toLowerCase();
        Frame_GatewayWiFi gateway = null;
        if (data.contains("21005400010089ff")) {
            data = data.substring(16, data.length() - 4);
            String[] temp = new String[3];
            temp[0] = data.substring(0, 32);  //ip
            temp[1] = data.substring(32, 68);  //mac
            temp[2] = data.substring(68, 148);  //device_name
            //获取ip
            String[] temp_ip = temp[0].split("2e");
            StringBuffer ip = new StringBuffer("");
            for (int i = 0; i < temp_ip.length; i++) {
                if (i != temp_ip.length - 1)
                    ip.append(StringUtil.getIpNum(temp_ip[i]));
                else ip.append(StringUtil.getIpNum(StringUtil.getDataEndWithoutZero((temp_ip[i]))));
                if (i != temp_ip.length - 1)
                    ip.append(".");
            }
            //获取mac
            String[] temp_mac = temp[1].split("2d");
            StringBuffer mac = new StringBuffer("");
            for (int i = 0; i < temp_mac.length; i++) {
                temp_mac[i] = temp_mac[i].substring(0, 4);
                mac.append((char) DataTypeUtil.hexStringToBytes(temp_mac[i].substring(0, 2))[0]).append((char) DataTypeUtil.hexStringToBytes(temp_mac[i].substring(2, 4))[0]);
                if (temp_mac.length - 1 != i)
                    mac.append(":");
            }
            //获取device_name
            StringBuffer device_name = new StringBuffer("");
            device_name.append(DataTypeUtil.hexStringToBytes(StringUtil.getDataEndWithoutZero(temp[2])));
            gateway = new Frame_GatewayWiFi(ip.toString(), mac.toString(), "0:0:0:0:0:0",ssid);
        }
        return gateway;
    }
    //获取登入帧信息
    public static Frame_GatewayLogin getLoginFrameByString(String data) {
        Frame_GatewayLogin frame = null;
        try {
            String index = data.substring(30, 38);
            String system_state = data.substring(38, 40);
            String pattern = data.substring(40, 42);
            String user_pwd = data.substring(42, 54);
            String universal_pwd = data.substring(54, 66);
            String wifi_mac = data.substring(66, 82);
            String zb_mac = data.substring(82, 98);
            String product_id = data.substring(98, 110);
            String product_code = data.substring(110, 122);
            String software_version = data.substring(122, 130);
            String hardware_version = data.substring(130, 138);
            frame = new Frame_GatewayLogin(index, system_state, pattern, user_pwd, universal_pwd, wifi_mac, zb_mac, product_id, product_code, software_version, hardware_version);
        } catch (Exception e) {
            Log.e("登入帧解析错误", e.getMessage());
        }
        return frame;
    }
    //获取未知设备
    public static Frame_Modle getNode_Unknow_ByString(String data){
        Frame_Modle zb = null;
        try {
            String ucDeviceType =data.substring(26, 28);
            String ucSensorType =data.substring(28, 30);
            String ucWorkMode ="00";
            String ucWorkState ="00";
            String ucOD_Upload_Interval_0 = "00";
            String ucOD_Upload_Interval_1 = "00";
            String ucOD_Area ="--";
            String ucOD_Func = "00";
            String uiBATVoltage ="0000";
            String ucRSSI ="00";
            String ucLQI ="00";
            String others=data.substring(40,data.length()-4);
            zb = new Frame_Modle(ucDeviceType, ucSensorType, ucWorkMode, ucWorkState, ucOD_Upload_Interval_0, ucOD_Upload_Interval_1, ucOD_Area, ucOD_Func, uiBATVoltage, ucRSSI, ucLQI,others);
        } catch (Exception e) {
            Log.e("节点设备解析错误", e.getMessage());
        }
        return zb;
    }
    //获取已知设备
    public static Frame_Modle getNodeZb_Know_ByString(String data) {
        Frame_Modle zb = null;
        try {
            String ucDeviceType = data.substring(38, 40);
            String ucSensorType = data.substring(40, 42);
            String ucWorkMode = data.substring(42, 44);
            String ucWorkState = data.substring(44, 46);
            String ucOD_Upload_Interval_0 = data.substring(46, 48);
            String ucOD_Upload_Interval_1 = data.substring(48, 50);
            String ucOD_Area = data.substring(50, 52);
            String ucOD_Func = data.substring(52, 54);
            String uiBATVoltage = data.substring(54, 58);
            String ucRSSI = data.substring(58, 60);
            String ucLQI = data.substring(60, 62);
            String others=(data.length()<=66?"":data.substring(62,data.length()-4));
            zb = new Frame_Modle(ucDeviceType, ucSensorType, ucWorkMode, ucWorkState, ucOD_Upload_Interval_0, ucOD_Upload_Interval_1, ucOD_Area, ucOD_Func, uiBATVoltage, ucRSSI, ucLQI,others);
        } catch (Exception e) {
            Log.e("节点设备解析错误", e.getMessage());
        }
        return zb;
    }
}















