package com.zdl.spider.mixed.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 文件操作工具类
 * <p>
 * Created by ZDLegend on 2019/3/29 16:05
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * create file by file`s path
     *
     * @param path file`s path
     * @return File object
     */
    public static File createFile(String path) {
        var file = new File(path);
        var fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            boolean is = fileParent.mkdirs();
            logger.debug("create dirs {} : {}", fileParent.getAbsolutePath(), is);
        }

        try {
            var is = file.createNewFile();
            logger.debug("create file {} : {}", file.getAbsolutePath(), is);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return file;
    }

}
