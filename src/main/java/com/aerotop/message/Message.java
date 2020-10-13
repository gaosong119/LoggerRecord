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

    public Message() {
    }

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

    public FrameTypeEnum getFrameType() {
        return frameType;
    }

    public void setFrameType(FrameTypeEnum frameType) {
        this.frameType = frameType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public LogLevelEnum getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(LogLevelEnum loglevel) {
        this.loglevel = loglevel;
    }

    public byte getProcess() {
        return process;
    }

    public void setProcess(byte process) {
        this.process = process;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventCount() {
        return eventCount;
    }

    public void setEventCount(String eventCount) {
        this.eventCount = eventCount;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
