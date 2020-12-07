package com.aerotop.detector;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: DiskArrayChecker
 * @Description: 磁盘阵列检查器
 * @Author: gaosong
 * @Date 2020/12/1 9:40
 */
public class ThreadPoolUtil {
    //开启文件保留策略检查线程池,这里使用无容量队列并将核心线程数设置为0防止线程一直存活
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 5,
            0L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());

    //开启磁盘阵列检查线程的线程池,只有一个线程周期执行即可
    public static ScheduledExecutorService singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();


    //磁盘阵列状态标识(true:代表可以写入,false:代表不可以写入)
    private static AtomicBoolean diskArrayStatus = new AtomicBoolean(true);

    /**
     * @Description: 获取磁盘阵列状态标识
     * @Author: gaosong
     * @Date: 2020/12/1 10:07
     * @return: boolean
     **/
    public static boolean getDiskArrayStatus() {
        return diskArrayStatus.get();
    }
    /**
     * @Description: 设置磁盘阵列状态标识
     * @Author: gaosong
     * @Date: 2020/12/1 10:07
     * @param status: 状态值
     * @return: void
     **/
    public static void setDiskArrayStatus(boolean status) {
        diskArrayStatus.set(status);
    }
}
