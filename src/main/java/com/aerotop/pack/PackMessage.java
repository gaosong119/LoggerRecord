package com.aerotop.pack;

import java.nio.charset.StandardCharsets;


public class PackMessage {
    /**
     * @Description:字节组包方法
     * @Author: gaosong
     * @Date: 2020/7/29 14:46
     * @param: frameType 帧类型(0:数据帧 1:指令帧)
     * @param: sourceName 发送方名称
     * @param: loglevel 日志级别(0:Info 1:Warn 2:Error)
     * @param: currentTimeMillis 发送时间(long 类型,当前系统时间毫秒数)
     * @param: process 发送流程(0-10整数,具体代表含义见日志服务通信协议)
     * @param: eventString 事件
     * @param: eventContentString 事件内容
     * @param: reservedString 备注
     * @return: byte[] 待发送数据流
     **/
    public static byte[] packMessageToBytes(byte frameType,String sourceName,byte loglevel,long currentTimeMillis,byte process,
    String eventString,String eventContentString,String reservedString){
        //帧同步
        byte frameMark = (byte) 0xBE;
        //校验和
        byte calcFrameSum = 0;
        //帧长
        int frameLength = 0;
        //发送方长度描述
        int sourceNameLength = sourceName.getBytes(StandardCharsets.UTF_8).length;
        //发送方内容
        byte[] sourceNameBytes = sourceName.getBytes(StandardCharsets.UTF_8);
        //事件字符串
        //String eventString = "事件";
        byte[] eventStringBytes = eventString.getBytes(StandardCharsets.UTF_8);
        int eventLength = eventString.getBytes(StandardCharsets.UTF_8).length;
        //事件内容字符串
        //String eventContentString = "事件内容";
        byte[] eventContentStringBytes = eventContentString.getBytes(StandardCharsets.UTF_8);
        int eventContentLength = eventContentString.getBytes(StandardCharsets.UTF_8).length;
        //备用字符串
        byte[] reservedStringBytes = reservedString.getBytes(StandardCharsets.UTF_8);
        int reservedLength = reservedString.getBytes(StandardCharsets.UTF_8).length;
        //先声明数组
        byte[] messageBytes = new byte[33+sourceNameLength+eventLength+eventContentLength+reservedLength];
        //开始组织messageBytes
        messageBytes[0] = frameMark;//帧同步
        messageBytes[1] = calcFrameSum;//校验和
        byte[] frameLengthBytes = ByteConvertUtils.intToByteArray(frameLength);//帧长
        //将帧长byte[]类型放入messageBytes
        System.arraycopy(frameLengthBytes,0,messageBytes,2,4);
        //帧类型
        messageBytes[6] = frameType;
        //软件名称字符串长度
        byte[] sourceBytesLength = ByteConvertUtils.intToByteArray(sourceNameLength);
        //将软件名称byte[]类型放入messageBytes
        System.arraycopy(sourceBytesLength,0,messageBytes,7,4);
        System.arraycopy(sourceNameBytes,0,messageBytes,11,sourceNameLength);
        //日志级别
        messageBytes[11+sourceNameLength] = loglevel;
        //发送时间
        byte[] currentTimeMillisBytes = ByteConvertUtils.longToByteArray(currentTimeMillis);
        System.arraycopy(currentTimeMillisBytes,0,messageBytes,12+sourceNameLength,8);
        //流程
        messageBytes[20+sourceNameLength] = process;
        //事件字符串长度
        byte[] eventLengthBytes = ByteConvertUtils.intToByteArray(eventLength);
        System.arraycopy(eventLengthBytes,0,messageBytes,21+sourceNameLength,4);
        System.arraycopy(eventStringBytes,0,messageBytes,25+sourceNameLength,eventLength);
        //事件内容字符串长度
        byte[] eventContentLengthBytes = ByteConvertUtils.intToByteArray(eventContentLength);
        System.arraycopy(eventContentLengthBytes,0,messageBytes,25+sourceNameLength+eventLength,4);
        System.arraycopy(eventContentStringBytes,0,messageBytes,29+sourceNameLength+eventLength,eventContentLength);
        //备用字符串长度
        byte[] reservedLengthBytes = ByteConvertUtils.intToByteArray(reservedLength);
        System.arraycopy(reservedLengthBytes,0,messageBytes,29+sourceNameLength+eventLength+eventContentLength,4);
        System.arraycopy(reservedStringBytes,0,messageBytes,33+sourceNameLength+eventLength+eventContentLength,reservedLength);
        //计算帧长
        byte[] intToByteArray = ByteConvertUtils.intToByteArray(messageBytes.length);
        System.arraycopy(intToByteArray,0,messageBytes,2,4);
        //组包完毕后计算校验和
        byte calcPackChecksum = calcPackChecksum(messageBytes);
        messageBytes[1]=calcPackChecksum;
        return messageBytes;
    }
    /**
     * @Description 计算组包校验和
     * @parm bytes:二进制数据
     * @Return byte
     * @Author gaosong
     * @Date 2020/7/16 16:55
     */
    public static byte calcPackChecksum(byte[] bytes){
        if(bytes!=null && bytes.length>2){
            int total = 0 ;
            for(int i=0;i<bytes.length;i++){
                if(i!=1){//校验和在第二个字节，不计算在内
                    total+=bytes[i];
                }
            }
            return (byte) (total & 255);
        }
        return 0;
    }
}
