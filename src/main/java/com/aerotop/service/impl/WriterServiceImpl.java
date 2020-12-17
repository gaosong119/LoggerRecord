package com.aerotop.service.impl;

import com.aerotop.enums.LogLevelEnum;
import com.aerotop.initialization.ConfigLoadToSingle;
import com.aerotop.initialization.KafkaProducerSingle;
import com.aerotop.message.LoggerUtil;
import com.aerotop.message.Message;
import com.aerotop.message.SendRemote;
import com.aerotop.message.WriterModel;
import com.aerotop.service.WriterService;

import java.io.*;
import java.util.HashMap;

/**
 * @ClassName: WriterServiceImpl
 * @Description: 写入接口实现类
 * @Author: gaosong
 * @Date 2020/11/30 13:43
 */
public class WriterServiceImpl implements WriterService {
    //版本文件写入内容
    public static final String versionCount = "v2.12-20201212-1600";

    //软件发送方与WriterModel对应的集合,防止重复创建对象
    private static HashMap<String, WriterModel> sendMapping = new HashMap<>();

    //文件写入模板
    private WriterModel writerModel;

    public WriterServiceImpl() {
        //执行版本文件创建函数
        try {
            String versionPath = ConfigLoadToSingle.getInstance().getVersionPath();
            File directory = new File(versionPath);
            File file;
            if(!directory.exists()){
                directory.mkdirs();
            }
            file = new File(versionPath + "/version-RZK.ini");
            //if(!file.exists()){
            //每次都创建并覆盖
            file.createNewFile();
            //}
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(versionCount);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logger(Message message) {
        //完善Message对象,防止异常
        LoggerUtil.perfectMessage(message);

        //获取message日志级别属性
        byte messageLevel = LogLevelEnum.logLevelToNum(message.getLoglevel());

        //获取配置文件日志级别
        byte configLevel = LogLevelEnum.logLevelStringToNum(ConfigLoadToSingle.getInstance().getLocationStorageLevel());

        //消息级别>=配置级别则执行存储,否则丢弃
        if (messageLevel >= configLevel && configLevel != 4) {
            //判断存储方式(远程或者本地)
            String locationInterfaceSelector = ConfigLoadToSingle.getInstance().getLocationInterfaceSelector();
            if ("location".equalsIgnoreCase(locationInterfaceSelector)) {//本地存储
                //获取写入模板
                this.writerModel = LoggerUtil.getWriterModel(message.getSourceName());
                //执行存储过程
                LoggerUtil.writeProcess(this.writerModel,message);
            } else if ("remote".equalsIgnoreCase(locationInterfaceSelector) && !"".equals(message.getTopic())
                    && message.getTopic()!=null) {//远程存储
                //获取kafkaProducer对象执行发送
                SendRemote.send(message.getTopic(), message);
            }
        }
    }
    /**
     * @Description: 关闭kafka producer对象
     * @Author: gaosong
     * @Date: 2020/12/2 11:32
     * @return: void
     **/
    @Override
    public void closeProducer() {
        if(KafkaProducerSingle.getInstance().getKafkaProducer()!=null){
            KafkaProducerSingle.getInstance().getKafkaProducer().close();
        }
    }
    /**
     * @Description: 关闭通道对象
     * @Author: gaosong
     * @Date: 2020/12/2 13:27
     * @return: void
     **/
    @Override
    public void closeChannel(){
        try {
            if(writerModel!=null && writerModel.getFileOutputStream()!=null &&
                    writerModel.getBufferedOutputStream()!=null){
                writerModel.getBufferedOutputStream().close();
                writerModel.getFileOutputStream().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @Description: 刷新通道缓存
     * @Author: gaosong
     * @Date: 2020/12/2 13:32
     * @return: void
     **/
    @Override
    public void flushChannel(){
        try {
            if(writerModel!=null && writerModel.getBufferedOutputStream()!=null){
                writerModel.getBufferedOutputStream().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 设置sendMapping属性值，由于多个对象共享所以加锁控制
     * @Author: gaosong
     * @Date: 2020/12/2 13:36
     * @param sourceName: 发送方名称
     * @param writerModel: 写入模板
     * @return: void
     **/
    public static synchronized void setSendMapping(String sourceName, WriterModel writerModel) {
        sendMapping.put(sourceName,writerModel);
    }
    /**
     * @Description: 获取sendMapping属性
     * @Author: gaosong
     * @Date: 2020/12/9 10:07
     * @return: java.util.HashMap<java.lang.String,com.aerotop.message.WriterModel>
     **/
    public static HashMap<String, WriterModel> getSendMapping() {
        return sendMapping;
    }

}
