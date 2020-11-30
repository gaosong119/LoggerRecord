package com.aerotop.enums;
/**
 * @ClassName: LogLevelEnum
 * @Description: 日志级别枚举类
 * @Author: gaosong
 * @Date 2020/10/9 13:58
 */
public enum LogLevelEnum {
    debug,
    info,
    warn,
    error,
    off;
    /**
     * @Description: 将日志级别枚举类型转为数字类型
     * @Author: gaosong
     * @Date: 2020/11/17 18:37
     * @param logLevelEnum:日志级别枚举数据
     * @return: byte
     **/
    public static byte logLevelToNum(LogLevelEnum logLevelEnum){
        byte i = 0;
        switch (logLevelEnum){
            case debug:
                break;
            case info:i=1;
                break;
            case warn:i=2;
                break;
            case error:i=3;
                break;
            case off:i=4;
                break;
        }
        return i;
    }
    /**
     * @Description:将字符串类型日志级别转为byte类型
     * @Author: gaosong
     * @Date: 2020/11/17 18:42
     * @param logLevelEnum: 日志级别字符串
     * @return: byte
     **/
    public static byte logLevelStringToNum(String logLevelEnum){
        byte i = 0;
        switch (logLevelEnum){
            case "debug":
                break;
            case "info":i=1;
                break;
            case "warn":i=2;
                break;
            case "error":i=3;
                break;
            case "off":i=4;
                break;
        }
        return i;
    }
}
