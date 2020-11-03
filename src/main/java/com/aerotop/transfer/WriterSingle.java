package com.aerotop.transfer;

import com.aerotop.actuator.SendRemote;
import com.aerotop.enums.FrameTypeEnum;
import com.aerotop.enums.LogLevelEnum;
import com.aerotop.initialization.ConfigLoadToSingle;
import com.aerotop.initialization.KafkaProducerSingle;
import com.aerotop.message.Message;
import com.aerotop.pack.PackMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: WriterSingle
 * @Description: 日志写入对象
 * @Author: gaosong
 * @Date 2020/10/9 10:49
 */
public class WriterSingle {
    //instance对象
    private static WriterSingle instance = new WriterSingle();
    //声明全局变量，标识磁盘阵列是否可以写入(true:代表可以写入,false:代表不可以写入)
    AtomicBoolean diskArrayStatus=new AtomicBoolean(true);
    //写入文件句柄
    private File file;
    //缓冲输出字节流对象
    //private BufferedOutputStream bufferedOutputStream;
    private FileOutputStream fileOutputStream;
    //文件内容日期格式化对象
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    //目录名称格式化对象
    private SimpleDateFormat simpleDateFormatDir=new SimpleDateFormat("yyyy-MM-dd");
    //创建线程池
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 5, TimeUnit.SECONDS, new PriorityBlockingQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
    //文件名称格式化对象
    private SimpleDateFormat fileNameFormat =new SimpleDateFormat("yyyyMMdd_HHmmss");//时间格式化对象
    //定时检测磁盘阵列线程池
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor=new ScheduledThreadPoolExecutor(1);
    //帧类型(0:数据帧 1:指令帧)
    private FrameTypeEnum frameType;
    //发送方名称
    private String sourceName;
    private WriterSingle() {
    }

    public static WriterSingle getInstance() {
        return instance;
    }
    /**
     * @Description:为用户提供的debug级别的接口
     * @Author: gaosong
     * @Date: 2020/10/12 15:51
     * @param: process:流程,event:事件,eventCount:事件内容,reserved:备注,topic:主题
     * @return: * @return: null
     **/
    public void loggerDebug(byte process,String event,String eventCount,String reserved,String topic){
        Message message = new Message(instance.frameType,instance.sourceName,LogLevelEnum.debug,System.currentTimeMillis()
                ,process,event,eventCount,reserved);
        logger(message, topic);
    }
     /**
      * @Description:为用户提供的info级别的接口
      * @Author: gaosong
      * @Date: 2020/10/12 15:51
      * @param: process:流程,event:事件,eventCount:事件内容,reserved:备注,topic:主题
      * @return: * @return: null
      **/
    public void loggerInfo(byte process,String event,String eventCount,String reserved,String topic){
        Message message = new Message(instance.frameType,instance.sourceName,LogLevelEnum.info,System.currentTimeMillis()
        ,process,event,eventCount,reserved);
        logger(message, topic);
    }
    /**
     * @Description:为用户提供的warn级别的接口
     * @Author: gaosong
     * @Date: 2020/10/12 15:51
     * @param: process:流程,event:事件,eventCount:事件内容,reserved:备注,topic:主题
     * @return: * @return: null
     **/
    public void loggerWarn(byte process,String event,String eventCount,String reserved,String topic){
        Message message = new Message(instance.frameType,instance.sourceName,LogLevelEnum.warn,System.currentTimeMillis()
                ,process,event,eventCount,reserved);
        logger(message, topic);
    }
    /**
     * @Description:为用户提供的error级别的接口
     * @Author: gaosong
     * @Date: 2020/10/12 15:51
     * @param: process:流程,event:事件,eventCount:事件内容,reserved:备注,topic:主题
     * @return: * @return: null
     **/
    public void loggerError(byte process,String event,String eventCount,String reserved,String topic){
        Message message = new Message(instance.frameType,instance.sourceName,LogLevelEnum.error,System.currentTimeMillis()
                ,process,event,eventCount,reserved);
        logger(message, topic);
    }
     /**
      * @Description:日志记录方法，由单例对象调用
      * @Author: gaosong
      * @Date: 2020/10/9 11:03
      * @param: * @param null:
      * @return: * @return: null
      **/
    public void logger(Message message,String topic){
        //获取message日志级别属性
        byte messageLevel = LogLevelEnum.logLevelToNum(message.getLoglevel());
        //获取配置文件日志级别
        byte configLevel = LogLevelEnum.logLevelStringToNum(ConfigLoadToSingle.getInstance().getLocationStorageLevel());
       if(messageLevel>=configLevel && configLevel!=4){//>=配置级别执行存储
           //判断存储方式(远程或者本地)
           String locationInterfaceSelector = ConfigLoadToSingle.getInstance().getLocationInterfaceSelector();
           if("location".equalsIgnoreCase(locationInterfaceSelector)){//本地存储
               String locationDeep = ConfigLoadToSingle.getInstance().getLocationDeep();
               //判断存储磁盘阵列还是本机磁盘
               if("locationForDiskArray".equalsIgnoreCase(locationDeep)){//存储磁盘阵列
                   //判断磁盘阵列是否可以正常写入
                   if(diskArrayStatus.get()){//可以正常写入
                       //获取磁盘阵列存储路径
                       String locationDiskArrayDir = ConfigLoadToSingle.getInstance().getLocationDiskArrayDir();
                       //执行写入方法
                       try {
                           writter(message,locationDiskArrayDir);
                       } catch (Exception e) {
                           e.printStackTrace();
                           //写入失败，将diskArrayStatus改为false,并且执行定时器定时检查磁盘阵列是否恢复正常
                           diskArrayStatus.set(false);
                            //写入本机磁盘
                           //获取本机磁盘路径配置
                           String locationDiskLocationDir = ConfigLoadToSingle.getInstance().getLocationDiskLocationDir();
                           try {
                               writter(message,locationDiskLocationDir);
                           } catch (IOException e2) {
                               e2.printStackTrace();
                           }
                           //启动定时任务检测
                           scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                               File fileTest = new File(locationDiskArrayDir + "/" + System.currentTimeMillis());
                               if (!fileTest.exists()) {
                                   boolean mkdirsResult = fileTest.mkdirs();
                                   if (mkdirsResult) {
                                       diskArrayStatus.set(true);
                                       //创建成功说明磁盘阵列恢复正常
                                       deleteDir(fileTest);
                                       //关闭线程池
                                       scheduledThreadPoolExecutor.shutdown();
                                   }
                               }
                           }, 0, 10, TimeUnit.SECONDS);
                       }
                   }else{//写入本机磁盘
                       //获取本机磁盘路径配置
                       String locationDiskLocationDir = ConfigLoadToSingle.getInstance().getLocationDiskLocationDir();
                       try {
                           writter(message,locationDiskLocationDir);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               }else if("locationForNative".equalsIgnoreCase(locationDeep)){//写入本机磁盘
                    //获取本机磁盘路径配置
                   String locationDiskLocationDir = ConfigLoadToSingle.getInstance().getLocationDiskLocationDir();
                   try {
                       writter(message,locationDiskLocationDir);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }else if("remote".equalsIgnoreCase(locationInterfaceSelector)){//远程存储
               //按照协议组装消息
               byte[] bytes = PackMessage.packMessageToBytes(FrameTypeEnum.frameTypeToNum(message.getFrameType()),message.getSourceName(), LogLevelEnum.logLevelToNum(message.getLoglevel()),
                       System.currentTimeMillis(),message.getProcess(),message.getEvent(),message.getEventCount(),message.getReserved());
               //获取kafkaProducer对象执行发送
               SendRemote.send(topic,bytes);
           }
       }
    }
    /**
     * @Description:通过message判断存储目标并执行写入
     * @Author: gaosong
     * @Date: 2020/10/9 14:25
     * @param: [message, topic]
     * @return: void
     **/
    private synchronized void writter(Message message,String rootPath) throws IOException {
        //组织存储字符串
        String messageString = assemblyMessage(message);
        //执行判断并写入
        byte[] bytes = messageString.getBytes(StandardCharsets.UTF_8);
        if(file!=null && fileOutputStream!=null){
            //判断file大小是否满足追加写入
            if(ConfigLoadToSingle.getInstance().getLocationMaxFileSize()<bytes.length+file.length()) {//超出最大值
                fileOutputStream.flush();
                fileOutputStream.close();//关闭并刷新通道
                file=null;
                fileOutputStream=null;
                //获取存储目录
                String directoryPath = rootPath+"/"+message.getSourceName()+"/"+simpleDateFormatDir.format(System.currentTimeMillis());
                File directory = new File(directoryPath);
                //创建新文件
                String newFile=directoryPath+"/"+message.getSourceName()+"_"+fileNameFormat.format(System.currentTimeMillis())+"_"+directory.listFiles().length+".log";
                file=new File(newFile);
                if(!file.exists()){
                    boolean createFileResult = file.createNewFile();
                    if(createFileResult){
                        fileOutputStream =new FileOutputStream(file,true);
                    }
                }
            }
        }else{
            //获取存储目录
            String directoryPath = rootPath+"/"+message.getSourceName()+"/"+simpleDateFormatDir.format(System.currentTimeMillis());
            File directory = new File(directoryPath);
            if(!directory.exists()){//目录不存在则创建
                boolean createResult = directory.mkdirs();
                if (createResult){
                    //初始化文件对象
                    String filePath = directoryPath+"/"+message.getSourceName()+"_"+fileNameFormat.format(System.currentTimeMillis())+"_0.log";//下标默认从0开始
                    file = new File(filePath);
                    //文件不存在则创建
                    if(!file.exists()){
                        boolean fileResult = file.createNewFile();
                        if(fileResult){
                            fileOutputStream =new FileOutputStream(file,true);
                            //bufferedOutputStream=new BufferedOutputStream(fileOutputStream,65536);
                        }
                    }
                    //根据配置删除多余目录
                    //获取文件保留天数
                    Integer locationStoragePeriod = ConfigLoadToSingle.getInstance().getLocationStoragePeriod();
                    //启用新线程删除其他目录，防止阻塞
                    threadPoolExecutor.allowCoreThreadTimeOut(true);
                    threadPoolExecutor.execute(() -> deleteDirectory(rootPath+"/"+message.getSourceName(),locationStoragePeriod));
                }
            }else if(file==null){//目录存在并且file还未初始化时，初始化file
                File[] files = directory.listFiles();
                if(files!=null && files.length>0){
                    Arrays.sort(files,new CompratorByLastModified());//将文件排序，获取最后更改文件
                    file=files[0];
                    fileOutputStream =new FileOutputStream(file,true);
                    //bufferedOutputStream=new BufferedOutputStream(fileOutputStream,65536);
                }else{//只有空目录
                    //创建新文件
                    String newFile=directoryPath+"/"+message.getSourceName()+"_"+fileNameFormat.format(System.currentTimeMillis())+"_"+directory.listFiles().length+".log";
                    file=new File(newFile);
                    if(!file.exists()){
                        boolean createFileResult = file.createNewFile();
                        if(createFileResult){
                            fileOutputStream =new FileOutputStream(file,true);
                            //bufferedOutputStream=new BufferedOutputStream(fileOutputStream,65536);
                        }
                    }
                }
            }
        }
        //执行写入
        fileOutputStream.write(bytes);
        //bufferedOutputStream.flush();
    }
     /**
      * @Description:获取根目录下所有目录并根据修改日期排序，按照保留天数删除旧目录
      * @Author: gaosong
      * @Date: 2020/10/9 15:23
      * @param: path:目录路径 locationStoragePeriod:保留天数
      * @return: * @return: null
      **/
    private static void deleteDirectory(String path, Integer locationStoragePeriod) {
        try {
            File[] files = new File(path).listFiles();
            if(files!=null){
                Arrays.sort(files,new CompratorByLastModified());//将目录排序
                for(int i=locationStoragePeriod;i < files.length; i++){
                    //递归删除目录下所有文件
                    deleteDir(files[i]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if(children!=null){
                //递归删除目录中的子目录下
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    /**
     * @Description:将message中数据根据配置规则组织成需要存储到文件的字符串
     * @Author: gaosong
     * @Date: 2020/7/22 10:03
     * @param: message
     * @return: String
     **/
    private  String assemblyMessage(Message message) {
        //获取进程中文名称
        String process=getProcessByCoder(message.getProcess());
        //返回的字符串
        String messageContent = message.getLoglevel()+"||"+simpleDateFormat.format(message.getSendTime())+"||"+simpleDateFormat.format(System.currentTimeMillis())+
                "||"+process+"||"+message.getEvent()+"||"+message.getEventCount()+"||"+message.getReserved();
        //自动添加换行符
        return messageContent+System.lineSeparator();
    }
    /**
     * @Description:通过流程编码获取流程字符串
     * @Author: gaosong
     * @Date: 2020/7/17 15:40
     * @param: coder:流程编码
     * @return: String 流程名称
     **/
    public static String getProcessByCoder(int coder){
        switch (coder){
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
 /**
  * @Description:通道刷新并关闭，由用户手动调用，防止反复刷新影响性能
  * @Author: gaosong
  * @Date: 2020/10/10 15:46
  * @param: * @param null:
  * @return: * @return: null
  **/
    public void close() {
        try {
            if(fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setFrameType(FrameTypeEnum frameType) {
        instance.frameType = frameType;
    }

    public void setSourceName(String sourceName) {
        instance.sourceName = sourceName;
    }
    /**
     * @Description:关闭Producer，防止每发送一次就关闭一次
     * @Author: gaosong
     * @Date: 2020/7/30 15:30
     * @param: null
     * @return: null
     **/
    public void closeProducer() {
        KafkaProducerSingle.getInstance().getKafkaProducer().close();
    }
}
