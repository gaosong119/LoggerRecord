import com.aerotop.enums.FrameTypeEnum;
import com.aerotop.enums.LogLevelEnum;
import com.aerotop.message.Message;
import com.aerotop.service.impl.WriterServiceImpl;

/**
 * @ClassName: OutputTest
 * @Description: TODO
 * @Author: gaosong
 * @Date 2020/10/13 9:49
 */
public class OutputTest {
    public static void main(String[] args) throws InterruptedException {
        WriterServiceImpl writerService = new WriterServiceImpl();
        //帧类型
        FrameTypeEnum frameTypeEnum = FrameTypeEnum.DATAFRAME;
        //发送方名称
        String sourceName = "发送方测试1";
        //日志级别
        LogLevelEnum logLevelEnum = LogLevelEnum.debug;
        //流程
        byte process =10;
        //事件
        String event = "事件";
        //事件内容
        String eventCount = "事件内容";
        //备注
        String reversed = "备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注" +
                "备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注" +
                "备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注" +
                "备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注" +
                "备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注" +
                "备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注" +
                "备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注" +
                "备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注备注";
        //主题
        String topic = "logService";
        Message message = new Message(frameTypeEnum,sourceName,logLevelEnum,System.currentTimeMillis(),process,event,
                eventCount,reversed,topic);
        int num = 100000;
        long startTime = System.currentTimeMillis();
        for (int i=0;i<num;i++){
            //Thread.sleep(10);
            writerService.logger(message);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("写入"+num+"次数据耗时:"+(endTime-startTime)+" ms");
    }
}
