package com.aerotop.service;

import com.aerotop.enums.FrameTypeEnum;
import com.aerotop.enums.LogLevelEnum;
import com.aerotop.message.Message;

import java.io.IOException;

/**
 * @Description: 日志写入接口
 * @Author: gaosong
 * @Date: 2020/11/30 10:21
 **/
public interface WriterService {
    /**
     * @Description: 消息写入接口
     * @Author: gaosong
     * @Date: 2020/12/3 14:15
     * @param message: 消息内容
     * @return: void
     **/
    void logger(Message message);
    /**
     * @Description: 关闭kafka 生产者
     * @Author: gaosong
     * @Date: 2020/11/30 13:35
     * @return: void
     **/
    void closeProducer();
    /**
     * @Description: 关闭BufferedOutputStream通道
     * @Author: gaosong
     * @Date: 2020/11/30 13:40
     * @return: void
     **/
    void closeChannel();
    /**
     * @Description: 刷新BufferedOutputStream通道
     * @Author: gaosong
     * @Date: 2020/11/30 13:40
     * @return: void
     **/
    void flushChannel();
}
