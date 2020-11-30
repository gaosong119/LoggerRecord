package com.aerotop.enums;
/**
 * @ClassName: FrameTypeEnum
 * @Description: 帧类型枚举类
 * @Author: gaosong
 * @Date 2020/10/9 13:58
 */
public enum FrameTypeEnum {
    DATAFRAME,
    COMMANDFRAME;
     /**
      * @Description:将帧类型字段枚举类型转为数字类型
      * @Author: gaosong
      * @Date: 2020/11/17 18:35
      * @param frameTypeEnum: 帧类型枚举数据
      * @return: byte
      **/
    public static byte frameTypeToNum(FrameTypeEnum frameTypeEnum){
        byte b = 0;
        switch (frameTypeEnum){
            case DATAFRAME:
                break;
            case COMMANDFRAME:b=1;
            break;
        }
        return  b;
    }
}
