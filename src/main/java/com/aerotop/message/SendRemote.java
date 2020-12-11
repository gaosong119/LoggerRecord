package com.aerotop.message;

import com.aerotop.enums.FrameTypeEnum;
import com.aerotop.enums.LogLevelEnum;
import com.aerotop.initialization.ConfigLoadToSingle;
import com.aerotop.initialization.KafkaProducerSingle;
import com.aerotop.message.Message;
import com.aerotop.pack.PackMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @ClassName: SendRemote
 * @Description: 远程发送工具类
 * @Author: gaosong
 * @Date 2020/10/9 13:58
 */
public class SendRemote {
    /**
     * @Description: 发送函数
     * @Author: gaosong
     * @Date: 2020/11/17 18:29
     * @param topic: 发送主题
     * @param message: 发送内容
     * @return: void
     **/
    public static void send(String topic, Message message){
        //按照协议组装消息
        byte[] bytes = PackMessage.packMessageToBytes(FrameTypeEnum.frameTypeToNum(message.getFrameType()),
                message.getSourceName(), LogLevelEnum.logLevelToNum(message.getLoglevel()),System.currentTimeMillis(),
                message.getProcess(), message.getEvent(), message.getEventCount(), message.getReserved());
        //获取配置文件中producer-key
        String key = ConfigLoadToSingle.getInstance().getRemoteProducerKey();
        if(StringUtils.isNotBlank(topic) && StringUtils.isNotBlank(key) && bytes.length > 0){
            //创建数据发送对象
            ProducerRecord producerRecord = new ProducerRecord(topic, key, bytes);
            //获取Producer并执行发送
            KafkaProducerSingle.getInstance().getKafkaProducer().send(producerRecord, (recordMetadata, e) -> {
                if(e!=null){
                    e.printStackTrace();
                }
            });
        }
    }
}
