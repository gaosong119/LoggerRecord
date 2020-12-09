package com.aerotop.detector;

import java.io.File;
import java.util.Comparator;

/**
 * @ClassName: CompratorByLastModified
 * @Description: 文件最后更改时间排序
 * @Author: gaosong
 * @Date 2020/10/9 15:52
 */
public class CompratorByLastModified implements Comparator<File> {

    @Override
    public int compare(File f1, File f2) {
        long diff = f1.lastModified() - f2.lastModified();
        if (diff > 0)
            return -1;//倒序正序控制
        else if (diff == 0)
            return 0;
        else
            return 1;//倒序正序控制
    }
    public boolean equals(Object obj) {
        return true;
    }
}
