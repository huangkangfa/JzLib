package com.huangkangfa.cmdlib.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 进制转化工具
 *
 * @author Administrator
 */
public class DataTypeUtil {
    private static final int DEFAULT_COVERING_POSITION_LENGTH = 4;

    //16-->2
    public static String hexToBinary(String str) {
        return hexToBinary(str, true);
    }

    //16-->2 是否缺位补0
    public static String hexToBinary(String str, boolean isCoveringPosition) {
        int decimal = hexToDecimal(str).intValue();
        String binary = decimalToBinary(decimal);

        if (isCoveringPosition) {
            int len = binary.length();
            if (len < DEFAULT_COVERING_POSITION_LENGTH) {
                for (int i = 0; i < DEFAULT_COVERING_POSITION_LENGTH - len; i++) {
                    binary = "0" + binary;
                }
            }
        }
        return binary;
    }

    //16-->10
    public static Integer hexToDecimal(String str) {
        try {
            int dec = Integer.valueOf(str, 16).intValue();
            return Integer.valueOf(dec);
        } catch (Exception localException) {
        }
        return 0;
    }

    //16-->8
    public static String hexToOctal(String str) {
        int dec = hexToDecimal(str).intValue();
        return decimalToOctal(dec);
    }

    //10-->2
    public static String decimalToBinary(int dec) {
        return Integer.toBinaryString(dec);
    }

    //10->8
    public static String decimalToOctal(int dec) {
        return Integer.toOctalString(dec);
    }

    //10->16
    public static String decimalToHex(int dec) {
        String s=Integer.toHexString(dec);
        return s.length()==1?"0"+s:s;
    }

    //2-->8
    public static String binaryToOctal(String binary) {
        int dec = binaryToDecimal(binary);
        return decimalToOctal(dec);
    }

    //2-->16
    public static String binaryToHex(String binary) {
        int dec = binaryToDecimal(binary);
        return decimalToHex(dec);
    }

    //2-->10
    public static int binaryToDecimal(String binary) {
        return Integer.valueOf(binary, 2).intValue();
    }

    //8-->2
    public static String octalToBinary(String octal) {
        int dec = octalToDecimal(octal);
        return Integer.toBinaryString(dec);
    }

    //8-->10
    public static int octalToDecimal(String octal) {
        return Integer.valueOf(octal, 8).intValue();
    }

    //8==>16
    public static String octalToHex(String octal) {
        int dec = octalToDecimal(octal);
        return decimalToHex(dec);
    }

    //bytes-->16
    public static String bytes2HexString(byte[] b, int vLen) {
        String ret = "";
        for (int i = 0; i < vLen; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex;
        }
        return ret;
    }

    //16-->bytes
    public static byte[] hexStringToBytes(String hexString) {
        if ((hexString == null) || (hexString.equals(""))) {
            return null;
        }

        hexString = hexString.toUpperCase();

        int length = hexString.length() / 2;

        char[] hexChars = hexString.toCharArray();

        byte[] d = new byte[length];

        for (int i = 0; i < length; i++) {
            int pos = i * 2;

            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[(pos + 1)]));
        }

        return d;
    }

    //char->byte
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    //加法和校验值
    public static String getAddCheck(String checkStr) {
        byte[] data = DataTypeUtil.hexStringToBytes(checkStr);
        byte addSum = 0;
        for (int i = 0; i < data.length; i++) {
            addSum += data[i];
        }
        return String.format("%02x", addSum & 0xff);
    }
	
	//异或和校验
    public static String getAddCheckByXOR(String checkStr) {
        byte[] data = DataTypeUtil.hexStringToBytes(checkStr);
        byte[] temp=new byte[1];
        temp[0]=data[0];
        for (int i = 1; i <data.length; i++) {
            temp[0] ^=data[i];
        }
        return DataTypeUtil.bytes2HexString(temp,temp.length);
    }

    //ASCII转换为字符串
    public static String asciiToString(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "ASCII");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    //字符串(16进制)转换为ASCII码
    public static String stringToAscii(String data) {
        StringBuilder result=new StringBuilder("");
        char[] chars=data.toCharArray();
        for(int i=0;i<chars.length;i++){
            int temp=chars[i];
            result.append(setBytesLen(decimalToHex(temp),1,false));
        }
        return result.toString();
    }

    //中文转gb2312
    public static String chineseToGb2312(String s) throws UnsupportedEncodingException{
        return URLEncoder.encode(s,"gb2312").replaceAll("%","");
    }

    //gb2312转中文
    public static String gb2312ToChinese(String s) throws UnsupportedEncodingException{
        return new String(hexStringToBytes(s),"gb2312");
    }

	//获取有符号的单字节数据:信号强度为0，信号强度总为负值，最高位代表符号位，1为负，数值越大信号越好，理论范围0~-127（上电初始上报为0）
    public static String getSymbolDataByOneByte(String data){
        String temp=hexToBinary(data,true);
        String t1=temp.substring(0,1);
        String t2="0"+temp.substring(1,temp.length());
        return "1".equals(t1)?"-"+(128-binaryToDecimal(t2)):""+binaryToDecimal(t2);
    }

    /**
     * 指定字节长度补全
     */
    public static String setBytesLen(String s, int bytesNum,boolean zeroAtAfter) {
        int length = bytesNum * 2;
        if ("".equals(s))
            return "";
        int len = s.length();
        if(len==length)
            return s;
        if (len < length) {
            int temp = length - len;
            for (int i = 0; i < temp; i++){
                s=zeroAtAfter?s+"0":"0"+s;
            }
        } else if (len > length) {
            s = s.substring(len - length, len);
        }
        return s;
    }

    /**
     * 子字符串在父字符串中出现的次数
     */
    public static int stringSub(String str, String substr) {
        int index = 0;
        int count = 0;
        int fromindex = 0;
        while ((index = str.indexOf(substr, fromindex)) != -1) {
            fromindex = index + substr.length();
            count++;
        }
        return count;
    }

    /**
     * 黏包处理
     **/
    private static String tempCmd = "";  //临时半包指令内容
    //获取正式的数据
    public static String[] getTrueData(String data,String head,String end) {
        data = data.toLowerCase();
        boolean endFlag = head.equals(data.substring(data.length() - 2, data.length()));
        String[] s = new String[stringSub(data, end + head) + 1];
        int num = 0;
        for (int i = 0; i < s.length; i++) {
            data = data.substring(num);
            num = data.indexOf(end + head) + 2;
            if (num == 1) {
                num = data.length();
            }
            if (i == 0) {
                String t = data.substring(0, num);
                if (t.substring(0, 2).equals(head)) {
                    tempCmd = "";
                }
                s[0] = tempCmd + t;
            } else {
                s[i] = data.substring(0, num);
                if (i == (s.length - 1) && (!endFlag)) {
                    tempCmd = s[i];
                }
            }
        }
        return s;
    }

    //指令校验->该接受的指令是否符合加法校验和的标准
    public static boolean getCheckBoolean(String buffer) {
        return buffer.substring(buffer.length() - 4, buffer.length() - 2).equals(getAddCheck(buffer.substring(4, buffer.length() - 4)));
    }
}
