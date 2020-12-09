package com.aerotop.message;

import com.aerotop.detector.CompratorByLastModified;
import com.aerotop.detector.DirectoryCheckTask;
import com.aerotop.detector.DiskArrayCheckTask;
import com.aerotop.detector.ThreadPoolUtil;
import com.aerotop.enums.FrameTypeEnum;
import com.aerotop.enums.LogLevelEnum;
import com.aerotop.initialization.ConfigLoadToSingle;
import com.aerotop.service.impl.WriterServiceImpl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: LoggerUtil
 * @Description: 封装了与日志写入相关的工具类
 * @Author: gaosong
 * @Date 2020/11/30 14:21
 */
public class LoggerUtil {
    //日期格式化对象,获取目录时使用
    private static final SimpleDateFormat todayDateFormat =new SimpleDateFormat("yyyy-MM-dd");
    //文件名称格式化对象
    private static final SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    //文件内容日期格式化对象
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    /**
     * @Description: 校验message对象添加默认值
     * @Author: gaosong
     * @Date: 2020/11/30 15:05
     * @param message: 写入消息
     * @return: com.aerotop.message.Message
     **/
    public static void perfectMessage(Message message){
        //发送方名称为空直接返回
        if (message.getSourceName() == null || "".equals(message.getSourceName())) {
            return ;
        }
        //若为空使用默认值
        if(message.getFrameType()==null){
            message.setFrameType(FrameTypeEnum.DATAFRAME);
        }
        if(message.getSendTime()==0){
            message.setSendTime(System.currentTimeMillis());
        }
        if(message.getLoglevel()==null){
            message.setLoglevel(LogLevelEnum.debug);
        }
        if(message.getEvent()==null){
            message.setEvent("");
        }
        if(message.getEventCount()==null){
            message.setEventCount("");
        }
        if(message.getReserved()==null){
            message.setReserved("");
        }
        if(message.getTopic()==null){
            message.setTopic("");
        }
    }
    /**
     * @Description: 初始化writerModel对象并更新到映射关系中
     * @Author: gaosong
     * @Date: 2020/11/30 15:13
     * @param sourceName: 发送方名称
     * @return: com.aerotop.message.WriterModel
     **/
    public static WriterModel getWriterModel(String sourceName) {
        //返回写入模板对象
        WriterModel writerModel = null;

        //根据sourceName获取存储目录路径
        String directoryPath =  getDirectory(sourceName);

        //创建目录对象
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            //目录不存在创建目录
            boolean createDirectoryResult = directory.mkdirs();
            if(createDirectoryResult){
                //创建成功,生成文件绝对路径
                File filePath = new File(directoryPath + "/" + sourceName + "_" + fileNameFormat.
                        format(System.currentTimeMillis()) + "_" +
                        Objects.requireNonNull(directory.listFiles()).length + ".log");
                writerModel = createWriterModel(directory,filePath);
            }
            //更新映射关系
            WriterServiceImpl.setSendMapping(sourceName,writerModel);
            //启动目录检测线程
            ThreadPoolUtil.threadPoolExecutor.execute(new DirectoryCheckTask(directory.getParentFile()));
        }else{
            //目录存在则获取writerModel
            writerModel = WriterServiceImpl.getSendMapping().get(sourceName);
            if(writerModel==null){//此情况当用户手动创建目录或程序重新启动时将会出现
                File filePath;
                //获取此目录下文件列表
                File[] files = directory.listFiles();
                if (files != null && files.length > 0) {
                    //将文件排序，获取最后更改文件
                    Arrays.sort(files, new CompratorByLastModified());
                    filePath = files[0];
                }else{
                    //初始化writerModel
                    filePath = new File(directoryPath + "/" + sourceName + "_" + fileNameFormat.
                            format(System.currentTimeMillis()) + "_" +
                            Objects.requireNonNull(directory.listFiles()).length + ".log");
                }
                writerModel = createWriterModel(directory, filePath);
                //更新映射关系
                WriterServiceImpl.setSendMapping(sourceName,writerModel);
            }
        }
        return writerModel;
    }
    /**
     * @Description: 根据发送方获取存储目录
     * @Author: gaosong
     * @Date: 2020/12/1 17:02
     * @param sourceName: 发送方名称
     * @return: java.lang.String
     **/
    private static String getDirectory(String sourceName) {
        String directoryPath = null;
        //判断存储磁盘阵列还是本机磁盘
        String locationDeep = ConfigLoadToSingle.getInstance().getLocationDeep();

        if ("locationForDiskArray".equalsIgnoreCase(locationDeep) && ThreadPoolUtil.getDiskArrayStatus()) {
            //存储磁盘阵列并且磁盘阵列状态为true
            directoryPath = ConfigLoadToSingle.getInstance().getLocationDiskArrayDir()+"/"+sourceName+"/"+
                    todayDateFormat.format(System.currentTimeMillis());

        }else if ("locationForNative".equalsIgnoreCase(locationDeep)) {
            //写入本机磁盘
            directoryPath = ConfigLoadToSingle.getInstance().getLocationDiskLocationDir()+"/"+sourceName+"/"+
                    todayDateFormat.format(System.currentTimeMillis());
        }
        return directoryPath;
    }

    /**
     * @Description: 根据发送方名称实例化WriterModel对象
     * @Author: gaosong
     * @Date: 2020/11/30 15:28
     * @param directory : 目录对象
     * @param filePath : 文件对象
     * @return: com.aerotop.message.WriterModel
     **/
    private static WriterModel createWriterModel(File directory, File filePath) {
        //当前发送方对应的写入模板对象
        WriterModel writerModel = new WriterModel();
        //初始化writerModel
        initWriterModel(writerModel,directory,filePath);

        return writerModel;
    }
    /**
     * @Description: 根据参数初始化writerModel
     * @Author: gaosong
     * @Date: 2020/12/1 18:23
     * @param writerModel: 写入对象
     * @param directory: 目录对象
     * @param filePath: 文件对象
     * @return: void
     **/
    private static void initWriterModel(WriterModel writerModel,File directory, File filePath){
        //设置writerModel
        writerModel.setDirectory(directory);
        writerModel.setFile(filePath);
        FileOutputStream fileOutputStream = null;
        try {
            if(!filePath.exists()){
                filePath.createNewFile();
            }
            fileOutputStream = new FileOutputStream(filePath,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writerModel.setFileOutputStream(fileOutputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream,65536);
        writerModel.setBufferedOutputStream(bufferedOutputStream);
    }
    /**
     * @Description: 执行本地存储逻辑
     * @Author: gaosong
     * @Date: 2020/12/1 10:43
     * @param writerModel : 写入模板
     * @param message : 写入内容
     * @return: void
     **/
    public static void writeProcess(WriterModel writerModel, Message message) {
        try {
            //获取写入内容
            byte[] bytes = assemblyMessage(message);

            //判断文件对象是否超出写入大小
            if (ConfigLoadToSingle.getInstance().getLocationMaxFileSize() < bytes.length + writerModel.getFile().length()) {
                //超出文件大小先关闭通道缓存,默认执行刷新
                writerModel.getBufferedOutputStream().close();
                writerModel.getFileOutputStream().close();

                //重新设置writerModel对象中的写入对象
                resetWriterModel(writerModel,message.getSourceName());
            }

            //执行写入
            writerModel.getBufferedOutputStream().write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            //文件写入失败
            String locationDeep = ConfigLoadToSingle.getInstance().getLocationDeep();
            if("locationForDiskArray".equalsIgnoreCase(locationDeep)){
                //如果配置文件中为磁盘阵列则改为本机磁盘
                ConfigLoadToSingle.getInstance().setLocationDeep("locationForNative");
                //将磁盘阵列状态置为false
                ThreadPoolUtil.setDiskArrayStatus(false);
                //文件创建失败,则启动磁盘阵列检查线程
                ThreadPoolUtil.singleThreadScheduledExecutor.scheduleWithFixedDelay(
                        new DiskArrayCheckTask(writerModel.getDirectory()),0,10, TimeUnit.SECONDS);
            }else if ("locationForNative".equalsIgnoreCase(locationDeep)) {
                //如果配置文件中为本机磁盘则改为磁盘阵列
                ConfigLoadToSingle.getInstance().setLocationDeep("locationForDiskArray");
            }
        }
    }
    /**
     * @Description: 当超出文件规定大小时重新设置writerModel
     * @Author: gaosong
     * @Date: 2020/12/1 16:45
     * @param writerModel : 写入模板
     * @param sourceName : 发送方名称
     * @return: void
     **/
    private static void resetWriterModel(WriterModel writerModel, String sourceName) {
        //生成文件路径
        String filePath = writerModel.getDirectory().getPath() + "/" + sourceName + "_" + fileNameFormat.
                format(System.currentTimeMillis()) + "_" +
                Objects.requireNonNull(writerModel.getDirectory().listFiles()).length + ".log";
        File newFile = new File(filePath);
        //重新设置writerModel
        initWriterModel(writerModel,writerModel.getDirectory(), newFile);
    }

    /**
     * @Description: 根据message对象组织写入流
     * @Author: gaosong
     * @Date: 2020/12/1 16:22
     * @param message: 写入对象
     * @return: byte[]
     **/
    private static byte[] assemblyMessage(Message message) {
        //获取进程中文名称
        String process = getProcessByCoder(message.getProcess());
        //返回的字符串,自动添加换行符
        String messageContent = message.getLoglevel() + "||" + simpleDateFormat.format(message.getSendTime()) + "||" +
                simpleDateFormat.format(System.currentTimeMillis()) + "||" + process + "||" + message.getEvent() + "||"
                + message.getEventCount() + "||" + message.getReserved() + System.lineSeparator();

        //执行判断并写入
        return messageContent.getBytes(StandardCharsets.UTF_8);
    }
    /**
     * @Description: 通过流程编码获取流程字符串
     * @Author: gaosong
     * @Date: 2020/7/17 15:40
     * @param: coder:流程编码
     * @return: String 流程名称
     **/
    private static String getProcessByCoder(int coder) {
        switch (coder) {
            case 0:
                return "转入ljb测试";
            case 1:
                return "ljb";
            case 2:
                return "转入ejrjb测试";
            case 3:
                return "ejrjb";
            case 4:
                return "转入yjrjb测试";
            case 5:
                return "yjrjb";
            case 6:
                return "fs";
            case 7:
                return "tczb";
            case 8:
                return "紧急断电";
            case 9:
                return "空J";
            case 10:
                return "其他";
            default:
                return "";
        }
    }
}
