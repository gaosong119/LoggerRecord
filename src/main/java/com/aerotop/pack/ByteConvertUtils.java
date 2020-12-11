package com.aerotop.pack;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName: ByteConvertUtils
 * @Description: 字节转换工具类
 * @Author: gaosong
 * @Date 2020/7/16 15:42
 */
public class ByteConvertUtils {
    /**
     * @Description: 将short类型转为byte[]
     * @Author: gaosong
     * @Date: 2020/11/25 14:20
     * @param s: short 类型数据
     * @return: byte[]
     **/
    public static byte[] short2byte(short s){
        byte[] b = new byte[2];
        for(int i = 0; i < 2; i++){
            int offset = 16 - (i+1)*8;
            b[i] = (byte)((s >> offset)&0xff);
        }
        return b;
    }
    /**
     * @Description: 将short类型转为byte[],小端模式
     * @Author: gaosong
     * @Date: 2020/11/25 14:20
     * @param s: short 类型数据
     * @return: byte[]
     **/
    public static byte[] short2byteLittle(short s){
        byte[] b = new byte[2];
        for(int i = 0; i < 2; i++){
            int offset = 16 - (i+1)*8;
            b[1-i] = (byte)((s >> offset)&0xff);
        }
        return b;
    }

    /**
     * @Description: float转byte[],小端模式
     * @Author: gaosong
     * @Date: 2020/11/25 19:10
     * @param f:float类型数据
     * @return: byte[]
     **/
    public static byte[] float2byteLittle(float f) {
        // 把float转换为byte[]
        int fBit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[3-i] = (byte) (fBit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }
    /**
     * @Description: float转byte[]
     * @Author: gaosong
     * @Date: 2020/11/25 19:10
     * @param f:
     * @return: byte[]
     **/
    public static byte[] float2byte(float f) {
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }
    /**
    * @Description 将一个字节byte类型转换为int类型
    * @param :bytes 字节数组
    * @param :index 取值下标
    * @Return int
    * @Author gaosong
    * @Date 2020/7/16 15:48
    */
    public static int byte1ToInt(byte[] bytes,int index){
        return bytes[index] & 255;
    }
    /**
     * @Description 将四个字节byte类型转换为int类型
     * @param :bytes 字节数组
     * @param :index 取值下标
     * @Return int
     * @Author gaosong
     * @Date 2020/7/16 15:48
     */
    public static int byte4ToInt(byte[] bytes,int index) {
        if (bytes.length > 3) {
            int b0 = bytes[index] & 0xFF;
            int b1 = bytes[index + 1] & 0xFF;
            int b2 = bytes[index + 2] & 0xFF;
            int b3 = bytes[index + 3] & 0xFF;
            return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
        }
        return 0;
    }
    /**
     * @Description 将8个字节byte类型转换为long类型
     * @param :bytes 字节数组
     * @param :index 取值下标
     * @Return long
     * @Author gaosong
     * @Date 2020/7/16 15:48
     */
    public static long byte8ToLong(byte[] bytes,int index){
        long temp;
        long res = 0;
        if(bytes.length>7){
            int j=0;
            for (int i = index; i < index+8; i++) {
                temp = bytes[i] & 0xff;
                temp <<= 8 * j;
                res |= temp;
                j++;
            }
        }
        return res;
    }
    /**
     * @Description 将N个字节byte类型转换为String类型
     * @param :bytes 字节数组
     * @param :startIndex 取值开始下标
     * @param :endIndex 取值结束下标
     * @Return String
     * @Author gaosong
     * @Date 2020/7/16 15:48
     */
    public static String bytesToString(byte[] bytes,int startIndex,int endIndex){
        if(bytes!=null && endIndex>0 && startIndex!=endIndex){
            byte[] byteModel = new byte[endIndex-startIndex];
            System.arraycopy(bytes, startIndex, byteModel, 0, endIndex-startIndex);
            return new String(byteModel, StandardCharsets.UTF_8);
        }
        return "";
    }

     /**
      * @Description:int 转 byte[]
      * @Author: gaosong
      * @Date: 2020/7/22 16:58
      * @param: int 类型整数
      * @return: byte[]
      **/
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }
    /**
     * @Description:int 转 byte[],小端传输
     * @Author: gaosong
     * @Date: 2020/7/22 16:58
     * @param: int 类型整数
     * @return: byte[]
     **/
    public static byte[] intToBytesLittle(int i) {
        byte[] result = new byte[4];
        //由高位到低位
        result[3] = (byte)((i >> 24) & 0xFF);
        result[2] = (byte)((i >> 16) & 0xFF);
        result[1] = (byte)((i >> 8) & 0xFF);
        result[0] = (byte)(i & 0xFF);
        return result;
    }
    /**
     * @Description:long 转 byte[]
     * @Author: gaosong
     * @Date: 2020/7/22 16:58
     * @param: long 类型整数
     * @return: byte[]
     **/
    public static byte[] longToByteArray(long number){
        long temp = number;
        byte[] b =new byte[8];
        for(int i =0; i < b.length; i++){
            b[i]=new Long(temp & 0xff).byteValue();
            //将最低位保存在最低位
            temp = temp >>8;// 向右移8位
        }
        return b;
    }
    /**
     * @Description: 字符串截取
     * @Author: gaosong
     * @Date: 2020/11/24 9:51
     * @param src: 源字符串
     * @param start_idx: 开始位置
     * @param end_idx: 结束位置
     * @return: java.lang.String
     **/
    public static String substring(String src, int start_idx, int end_idx){
        byte[] b = src.getBytes();
        StringBuilder tgt = new StringBuilder();
        for(int i=start_idx; i<=end_idx; i++){
            tgt.append((char) b[i]);
        }
        return tgt.toString();
    }
    /**
     * @Description:16进制字符串转bytes
     * @Author: gaosong
     * @Date: 2020/9/9 10:52
     * @param: * @param null:
     * @return: * @return: null
     **/
    public static byte[] hexStrToBytes(String src){
        int l =src.length()/2;
        byte[] ret =new byte[l];
        for(int i=0;i<l;i++){
            ret[i]=Integer.valueOf(src.substring(i*2,i*2+2),16).byteValue();
        }
        return ret;
    }
}
