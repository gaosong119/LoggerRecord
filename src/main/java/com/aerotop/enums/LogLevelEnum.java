package com.aerotop.enums;

public enum LogLevelEnum {
    debug,
    info,
    warn,
    error,
    off;
    public static byte logLevelToNum(LogLevelEnum logLevelEnum){
        byte i = 0;
        switch (logLevelEnum){
            case debug:i=0;
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
    public static byte logLevelStringToNum(String logLevelEnum){
        byte i = 0;
        switch (logLevelEnum){
            case "debug":i=0;
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
