package com.aerotop.detector;

import com.aerotop.initialization.ConfigLoadToSingle;

import java.io.File;
import java.util.Arrays;

/**
 * @ClassName: DirectoryCheckTask
 * @Description: 目录检测任务
 * @Author: gaosong
 * @Date 2020/12/1 20:29
 */
public class DirectoryCheckTask implements Runnable{

    //目录对象(直到发送方名称一级)
    private File directory;

    //保留天数
    private static final Integer retentionDays = ConfigLoadToSingle.getInstance().getLocationStoragePeriod();
    /**
     * @Description: 含参构造器
     * @Author: gaosong
     * @Date: 2020/12/2 9:11
     * @param directory: 目录对象
     * @return: null
     **/
    public DirectoryCheckTask(File directory) {
        this.directory = directory;
    }

    /**
     * @Description: 目录检测任务
     * @Author: gaosong
     * @Date: 2020/12/1 20:29
     * @return: void
     **/
    @Override
    public void run() {
        //根据保留天数删除父目录下的子目录
        deleteDirectory(directory,retentionDays);
    }
    /**
     * @Description: 根据配置删除子目录
     * @Author: gaosong
     * @Date: 2020/12/2 9:27
     * @param path: 父目录
     * @param locationStoragePeriod: 保留天数
     * @return: void
     **/
    private void deleteDirectory(File path, Integer locationStoragePeriod) {
        try {
            File[] files = path.listFiles();
            if (files != null) {
                Arrays.sort(files, new CompratorByLastModified());//将目录排序
                for (int i = locationStoragePeriod; i < files.length; i++) {
                    //递归删除目录下所有文件
                    deleteDir(files[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @Description: 递归删除目录
     * @Author: gaosong
     * @Date: 2020/12/2 9:14
     * @param dir: 将要递归删除的父目录
     * @return: boolean
     **/
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
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
}
