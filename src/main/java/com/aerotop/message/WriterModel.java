package com.aerotop.message;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @ClassName: WriterModel
 * @Description: 文件写入对象模板
 * @Author: gaosong
 * @Date 2020/11/30 13:55
 */
public class WriterModel {
    //文件所在目录
    private File directory;
    //发送方对应的文件句柄
    private File file;
    //数据输出通道对象
    private FileOutputStream fileOutputStream;
    //缓冲输出字节流对象
    private BufferedOutputStream bufferedOutputStream;
    /**
     * @Description: 空构造函数
     * @Author: gaosong
     * @Date: 2020/11/30 13:57
     * @return: null
     **/
    public WriterModel() {
    }
    /**
     * @Description: 返回当前对象File属性
     * @Author: gaosong
     * @Date: 2020/11/30 13:57
     * @return: java.io.File
     **/
    public File getFile() {
        return file;
    }
    /**
     * @Description: 设置当前对象 file 属性
     * @Author: gaosong
     * @Date: 2020/11/30 13:58
     * @param file: Dile对象
     * @return: void
     **/
    public void setFile(File file) {
        this.file = file;
    }
    /**
     * @Description: 返回当前对象FileOutputStream属性
     * @Author: gaosong
     * @Date: 2020/11/30 13:57
     * @return: java.io.FileOutputStream
     **/
    public FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }
    /**
     * @Description: 设置当前对象 fileOutputStream 属性
     * @Author: gaosong
     * @Date: 2020/11/30 13:58
     * @param fileOutputStream: FileOutputStream对象
     * @return: void
     **/
    public void setFileOutputStream(FileOutputStream fileOutputStream) {
        this.fileOutputStream = fileOutputStream;
    }
    /**
     * @Description: 返回当前对象BufferedOutputStream属性
     * @Author: gaosong
     * @Date: 2020/11/30 13:59
     * @return: java.io.BufferedOutputStream
     **/
    public BufferedOutputStream getBufferedOutputStream() {
        return bufferedOutputStream;
    }
    /**
     * @Description: 设置当前对象bufferedOutputStream属性
     * @Author: gaosong
     * @Date: 2020/11/30 13:59
     * @param bufferedOutputStream: BufferedOutputStream 对象
     * @return: void
     **/
    public void setBufferedOutputStream(BufferedOutputStream bufferedOutputStream) {
        this.bufferedOutputStream = bufferedOutputStream;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }
}
