package com.aerotop.enums;

public enum FrameTypeEnum {
    DATAFRAME,
    COMMANDFRAME;
     /**
      * @Description:将枚举类型转为数字类型
      * @Author: gaosong
      * @Date: 2020/10/9 13:15
      * @param: frameTypeEnum
      * @return: byte
      **/
    public static byte frameTypeToNum(FrameTypeEnum frameTypeEnum){
        byte b = 0;
        switch (frameTypeEnum){
            case DATAFRAME:b=0;
            break;
            case COMMANDFRAME:b=1;
            break;
        }
        return  b;
    }
}
