package com.huangkangfa.cmdlib;

import android.os.Parcel;
import android.os.Parcelable;

import com.huangkangfa.cmdlib.address.Addr;
import com.huangkangfa.cmdlib.exception.CmdException;
import com.huangkangfa.cmdlib.enums.CmdID;
import com.huangkangfa.cmdlib.enums.DstAddrFmtEnum;
import com.huangkangfa.cmdlib.enums.OD;
import com.huangkangfa.cmdlib.index.BaseIndex;
import com.huangkangfa.cmdlib.utils.DataTypeUtil;

/**
 * 指令帧对象类
 * Created by huangkangfa on 2017/6/15.
 */

public class CmdObj implements Parcelable {
    private static final String head="2a"; //帧头

    private CmdID mCmdID=null;  //命令标识
    private DstAddrFmtEnum mDstAddrFmtEnum=null; //目标地址形式
    private Addr mAddr=null; //目标地址
    private OD mOD=null; //OD类型
    private BaseIndex mBaseindex=null; //子索引
    private String data=null;  //实际数据

    private static final String tail="23"; //帧尾

    public void setCmdID(CmdID mCmdID) {
        this.mCmdID = mCmdID;
    }

    public void setDstAddrFmtEnum(DstAddrFmtEnum mDstAddrFmtEnum) {
        this.mDstAddrFmtEnum = mDstAddrFmtEnum;
    }

    public void setAddr(Addr mAddr) {
        this.mAddr = mAddr;
    }

    public void setOD(OD mOD) {
        this.mOD = mOD;
    }

    public void setBaseindex(BaseIndex mBaseindex) {
        this.mBaseindex = mBaseindex;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValue(){
        checkCmd();
        StringBuilder sb=new StringBuilder();
        sb.append(mCmdID.getVal()).append(mDstAddrFmtEnum.getVal()).append(mAddr.getValue()).append(mOD.getVal()).append(mBaseindex.getValue()).append(data);
        return addShellForCmd(sb.toString());
    }

    /**
     * 给指定字符串加壳
     */
    public static String addShellForCmd(String cmd) {
        return head + DataTypeUtil.decimalToHex(cmd.length() / 2) + cmd + DataTypeUtil.getAddCheck(cmd) + tail;
    }

    /**
     * 校验指令的完整性
     */
    private void checkCmd(){
        if(mCmdID==null){
            throw new CmdException(CmdException.CmdIDisNull);
        }
        if(mDstAddrFmtEnum==null){
            throw new CmdException(CmdException.DstAddrFmtEnumisNull);
        }
        if(mAddr==null){
            throw new CmdException(CmdException.AddrisNull);
        }
        if(mOD==null){
            throw new CmdException(CmdException.ODisNull);
        }
        if(mBaseindex==null){
            throw new CmdException(CmdException.IndexisNull);
        }
        if(data==null){
            throw new CmdException(CmdException.DataisNull);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCmdID == null ? -1 : this.mCmdID.ordinal());
        dest.writeInt(this.mDstAddrFmtEnum == null ? -1 : this.mDstAddrFmtEnum.ordinal());
        dest.writeParcelable(this.mAddr, flags);
        dest.writeInt(this.mOD == null ? -1 : this.mOD.ordinal());
        dest.writeParcelable(this.mBaseindex, flags);
        dest.writeString(this.data);
    }

    public CmdObj() {
    }

    protected CmdObj(Parcel in) {
        int tmpMCmdID = in.readInt();
        this.mCmdID = tmpMCmdID == -1 ? null : CmdID.values()[tmpMCmdID];
        int tmpMDstAddrFmtEnum = in.readInt();
        this.mDstAddrFmtEnum = tmpMDstAddrFmtEnum == -1 ? null : DstAddrFmtEnum.values()[tmpMDstAddrFmtEnum];
        this.mAddr = in.readParcelable(Addr.class.getClassLoader());
        int tmpMOD = in.readInt();
        this.mOD = tmpMOD == -1 ? null : OD.values()[tmpMOD];
        this.mBaseindex = in.readParcelable(BaseIndex.class.getClassLoader());
        this.data = in.readString();
    }

    public static final Parcelable.Creator<CmdObj> CREATOR = new Parcelable.Creator<CmdObj>() {
        @Override
        public CmdObj createFromParcel(Parcel source) {
            return new CmdObj(source);
        }

        @Override
        public CmdObj[] newArray(int size) {
            return new CmdObj[size];
        }
    };
}
