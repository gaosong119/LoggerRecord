package com.aerotop.initialization;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @ClassName: ConfigLoadToSingle
 * @Description: 加载配置文件所有配置项
 * @Author: gaosong
 * @Date 2020/10/9 9:56
 */
public class ConfigLoadToSingle {
    //instance对象
    private static ConfigLoadToSingle instance = new ConfigLoadToSingle();
    //远程发送-目标地址:端口 配置
    private String remoteServer;
    //远程发送-消息发送key
    private String remoteProducerKey;
    //本地记录-日志保留天数
    private Integer LocationStoragePeriod;
    //本地记录-单个日志文件大小(单位:MB)
    private Integer LocationMaxFileSize;
    //本地记录-日志存储级别
    private String LocationStorageLevel;
    //本地记录-接口调用配置
    private String LocationInterfaceSelector;
    //本地记录-本地记录深层配置
    private String LocationDeep;
    //本地记录-磁盘阵列存储目录配置
    private String LocationDiskArrayDir;
    //本地记录-本地磁盘存储目录配置
    private String LocationDiskLocationDir;
    //私有构造器,加载配置文件配置项到instance
    private ConfigLoadToSingle() {}
    static {
        try {
            Properties props = new Properties();
            InputStreamReader inputStream = new InputStreamReader(ConfigLoadToSingle.class.getClassLoader().getResourceAsStream("logger-config.properties"), StandardCharsets.UTF_8);
            props.load(inputStream);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                switch (key){
                    case "bootstrap-servers":
                        instance.remoteServer=props.getProperty(key);
                        break;
                    case "producer-key":
                        instance.remoteProducerKey=props.getProperty(key);
                        break;
                    case "storagePeriod":
                        instance.LocationStoragePeriod= Integer.valueOf(props.getProperty(key));
                        break;
                    case "maxFileSize":
                        instance.LocationMaxFileSize=Integer.valueOf(props.getProperty(key));
                        break;
                    case "storageLevel":
                        instance.LocationStorageLevel=props.getProperty(key);
                        break;
                    case "interfaceSelector":
                        instance.LocationInterfaceSelector=props.getProperty(key);
                        break;
                    case "locationDeep":
                        instance.LocationDeep=props.getProperty(key);
                        break;
                    case "diskArrayDir":
                        instance.LocationDiskArrayDir=props.getProperty(key);
                        break;
                    case "diskLocationDir":
                        instance.LocationDiskLocationDir=props.getProperty(key);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ConfigLoadToSingle getInstance() {
        return instance;
    }

    public String getRemoteServer() {
        return remoteServer;
    }

    public String getRemoteProducerKey() {
        return remoteProducerKey;
    }

    public Integer getLocationStoragePeriod() {
        return LocationStoragePeriod;
    }

    public Integer getLocationMaxFileSize() {
        return LocationMaxFileSize*1024*1024;
    }

    public String getLocationStorageLevel() {
        return LocationStorageLevel;
    }

    public String getLocationInterfaceSelector() {
        return LocationInterfaceSelector;
    }

    public String getLocationDeep() {
        return LocationDeep;
    }

    public String getLocationDiskArrayDir() {
        return LocationDiskArrayDir;
    }

    public String getLocationDiskLocationDir() {
        return LocationDiskLocationDir;
    }
}
