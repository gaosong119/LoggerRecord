package com.aerotop.initialization;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * @ClassName: KafkaProducerSingle
 * @Description: Kafka producer 单例类-饿汉式
 * @Author: gaosong
 * @Date 2020/7/29 10:17
 */
public class KafkaProducerSingle {
    //获取KafkaProducerSingle对象
    private static KafkaProducerSingle instance = new KafkaProducerSingle();

    //kafkaProducer类作为此对象的一个属性
    private KafkaProducer kafkaProducer;

    /**
     * @Description: 初始化producer对象
     * @Author: gaosong
     * @Date: 2020/11/17 19:15
     * @return: null
     **/
    private KafkaProducerSingle() {
        //初始化KafkaProducer对象
        kafkaProducer = new KafkaProducer(initConfig());
    }
    /**
     * @Description: 获取 kafka producer 实例
     * @Author: gaosong
     * @Date: 2020/11/17 19:16
     * @return: com.aerotop.initialization.KafkaProducerSingle
     **/
    public static KafkaProducerSingle getInstance() {
        return instance;
    }
    /**
     * @Description:初始化KafkaProducer所需的Properties对象方法
     * @Author: gaosong
     * @Date: 2020/7/27 9:01
     * @return: Properties
     **/
    private Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ConfigLoadToSingle.getInstance().getRemoteServer());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.put(ProducerConfig.RETRIES_CONFIG,5);//设置重发次数
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,81920);//设置缓存区大小
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,167772160);//设置缓存区大小
        return properties;
    }
     /**
      * @Description:通过instance获取KafkaProducer
      * @Author: gaosong
      * @Date: 2020/8/10 10:03
      * @return: KafkaProducer
      **/
    public KafkaProducer getKafkaProducer() {
        return kafkaProducer;
    }
}
