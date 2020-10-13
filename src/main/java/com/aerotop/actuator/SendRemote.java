package com.aerotop.actuator;

import com.aerotop.initialization.ConfigLoadToSingle;
import com.aerotop.initialization.KafkaProducerSingle;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @ClassName: SendRemote
 * @Description: 远程发送工具类
 * @Author: gaosong
 * @Date 2020/10/9 13:58
 */
public class SendRemote {

    /**
     * @Description:发送函数
     * @Author: gaosong
     * @Date: 2020/7/29 15:13
     * @param: topic 发送主题
     * @param: key 当前发送方key
     * @param: topic 发送主题
     * @return: * @return: null
     **/
    public static void send(String topic, byte[] value){
        //获取配置文件中producer-key
        String key = ConfigLoadToSingle.getInstance().getRemoteProducerKey();
        if(StringUtils.isNotBlank(topic) && StringUtils.isNotBlank(key) && value!=null && value.length>0){
            //创建数据发送对象
            ProducerRecord producerRecord = new ProducerRecord(topic, key, value);
            //获取Producer并执行发送
            KafkaProducerSingle.getInstance().getKafkaProducer().send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(e!=null){
                        e.printStackTrace();
                    }
                }
            });

        }
    }
    /**
     * @Description:关闭Producer，防止每发送一次就关闭一次
     * @Author: gaosong
     * @Date: 2020/7/30 15:30
     * @param: null
     * @return: null
     **/
    public static void closeProducer() {
        KafkaProducerSingle.getInstance().getKafkaProducer().close();
    }
}
