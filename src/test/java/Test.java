import com.aerotop.enums.FrameTypeEnum;
import com.aerotop.enums.LogLevelEnum;
import com.aerotop.message.Message;
import com.aerotop.transfer.WriterSingle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: Test
 * @Description: TODO
 * @Author: gaosong
 * @Date 2020/10/12 13:53
 */
public class Test {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 5, TimeUnit.SECONDS, new PriorityBlockingQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());

    public static void main(String[] args) {
      String aa="测试测试测试测试";
        File file = new File("D:/abc/aa.txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            //BufferedOutputStream bufferedOutputStream =new BufferedOutputStream(fileOutputStream,65536);
            /*while(true){
                if(file.length()>1*1024*1024){
                    file = new File("D:/abc/bb"+new File("D:/abc").listFiles().length+".txt");
                    fileOutputStream.close();
                    //bufferedOutputStream.close();
                    //System.out.println(bufferedOutputStream==null);
                    fileOutputStream = new FileOutputStream(file,true);
                    //bufferedOutputStream =new BufferedOutputStream(fileOutputStream,65536);
                }
                //bufferedOutputStream.write(aa.getBytes(StandardCharsets.UTF_8));
            }*/
            fileOutputStream.write(aa.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //1.初始化写入库
        /*threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Message message=new Message(FrameTypeEnum.DATAFRAME,"发送方1", LogLevelEnum.debug,System.currentTimeMillis(),(byte)1,
                        "事件","事件内容","Thread1");
                while(true){
                    //System.out.println("线程1执行写入");
                    WriterSingle.getInstance().logger(message,"");
                }
            }
        });
       threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //Thread.currentThread().setName("Thread1");
                Message message=new Message(FrameTypeEnum.DATAFRAME,"发送方1", LogLevelEnum.debug,System.currentTimeMillis(),(byte)1,
                        "事件","事件内容","Thread2");
                while(true){
                    //System.out.println("线程2执行写入");
                    WriterSingle.getInstance().logger(message,"");
                }
            }
        });*/
    }
}
