package com.aerotop.message;

import com.aerotop.enums.FrameTypeEnum;
import com.aerotop.enums.LogLevelEnum;

/**
 * @ClassName: Message
 * @Description: 消息对象
 * @Author: gaosong
 * @Date 2020/10/9 10:41
 */
public class Message {
    //帧类型(0:数据帧 1:指令帧)
    private FrameTypeEnum frameType;
    //发送方名称
    private String sourceName;
    //日志级别
    private LogLevelEnum loglevel;
    //发送时间
    private long sendTime;
    //流程(0-10)
    private byte process;
    //事件
    private String event;
    //事件内容
    private String eventCount;
    //备用
    private String reserved;
    //主题
    private String topic;
    /**
     * @Description: 空构造器
     * @Author: gaosong
     * @Date: 2020/11/17 19:24
     * @return: null
     **/
    public Message() {
    }
    /**
     * @Description: 含参构造器
     * @Author: gaosong
     * @Date: 2020/11/17 19:25
     * @param frameType: 消息类型
     * @param sourceName: 消息发送方
     * @param loglevel: 日志级别
     * @param sendTime: 发送时间
     * @param process: 进程
     * @param event: 事件
     * @param eventCount: 事件内容
     * @param reserved: 备注
     * @return: null
     **/
    public Message(FrameTypeEnum frameType, String sourceName, LogLevelEnum loglevel, long sendTime, byte process, String event, String eventCount, String reserved) {
        this.frameType = frameType;
        this.sourceName = sourceName;
        this.loglevel = loglevel;
        this.sendTime = sendTime;
        this.process = process;
        this.event = event;
        this.eventCount = eventCount;
        this.reserved = reserved;
    }
    /**
     * @Description: 全参构造器
     * @Author: gaosong
     * @Date: 2020/12/3 14:39
     * @param frameType: 消息类型
     * @param sourceName: 消息发送方
     * @param loglevel: 日志级别
     * @param sendTime: 发送时间
     * @param process: 进程
     * @param event: 事件
     * @param eventCount: 事件内容
     * @param reserved: 备注
     * @param topic: 主题
     * @return: null
     **/
    public Message(FrameTypeEnum frameType, String sourceName, LogLevelEnum loglevel, long sendTime, byte process,
                   String event, String eventCount, String reserved, String topic) {
        this.frameType = frameType;
        this.sourceName = sourceName;
        this.loglevel = loglevel;
        this.sendTime = sendTime;
        this.process = process;
        this.event = event;
        this.eventCount = eventCount;
        this.reserved = reserved;
        this.topic = topic;
    }

    /**
     * @Description: 获取帧类型
     * @Author: gaosong
     * @Date: 2020/11/18 8:35
     * @return: com.aerotop.enums.FrameTypeEnum
     **/
    public FrameTypeEnum getFrameType() {
        return frameType;
    }
    /**
     * @Description: 获取 消息发送方
     * @Author: gaosong
     * @Date: 2020/11/18 8:38
     * @return: java.lang.String
     **/
    public String getSourceName() {
        return sourceName;
    }
    /**
     * @Description:获取日志级别
     * @Author: gaosong
     * @Date: 2020/11/18 8:38
     * @return: com.aerotop.enums.LogLevelEnum
     **/
    public LogLevelEnum getLoglevel() {
        return loglevel;
    }
    /**
     * @Description: 获取进程
     * @Author: gaosong
     * @Date: 2020/11/18 8:39
     * @return: byte
     **/
    public byte getProcess() {
        return process;
    }
    /**
     * @Description: 获取事件
     * @Author: gaosong
     * @Date: 2020/11/18 8:39
     * @return: java.lang.String
     **/
    public String getEvent() {
        return event;
    }
    /**
     * @Description: 获取事件内容
     * @Author: gaosong
     * @Date: 2020/11/18 8:39
     * @return: java.lang.String
     **/
    public String getEventCount() {
        return eventCount;
    }
    /**
     * @Description: 获取备注
     * @Author: gaosong
     * @Date: 2020/11/18 8:39
     * @return: java.lang.String
     **/
    public String getReserved() {
        return reserved;
    }
    /**
     * @Description:获取发送时间
     * @Author: gaosong
     * @Date: 2020/11/18 8:40
     * @return: long
     **/
    public long getSendTime() {
        return sendTime;
    }

    public void setFrameType(FrameTypeEnum frameType) {
        this.frameType = frameType;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setLoglevel(LogLevelEnum loglevel) {
        this.loglevel = loglevel;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public void setProcess(byte process) {
        this.process = process;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setEventCount(String eventCount) {
        this.eventCount = eventCount;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
