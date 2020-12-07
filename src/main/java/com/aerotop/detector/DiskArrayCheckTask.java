package com.aerotop.detector;

import com.aerotop.initialization.ConfigLoadToSingle;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName: DiskArrayCheckTask
 * @Description: 周期执行磁盘阵列检查任务
 * @Author: gaosong
 * @Date 2020/12/1 10:09
 */
public class DiskArrayCheckTask implements Runnable {
    //文件创建测试目录对象
    private File directory;

    public DiskArrayCheckTask(File directory) {
        this.directory = directory;
    }

    /**
     * @Description: 执行周期检查磁盘阵列任务
     * @Author: gaosong
     * @Date: 2020/12/1 19:36
     * @return: void
     **/
    @Override
    public void run() {
        File fileTest = new File(directory.getPath() + "/" + System.currentTimeMillis()+".txt");
        if (!fileTest.exists()) {
            try {
                boolean mkdirsResult = fileTest.createNewFile();
                if (mkdirsResult) {
                    //将写入类型改为磁盘阵列
                    ConfigLoadToSingle.getInstance().setLocationDeep("locationForDiskArray");
                    //将磁盘阵列状态改为可用(true)
                    ThreadPoolUtil.setDiskArrayStatus(true);
                    //创建成功说明磁盘阵列恢复正常删除此文件
                    fileTest.delete();
                    //关闭线程池
                    ThreadPoolUtil.singleThreadScheduledExecutor.shutdown();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
