package com.zdl.spider.mixed.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * 文件操作工具类
 * <p>
 * Created by ZDLegend on 2019/3/29 16:05
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String removeIllegalWord(String name) {
        return name.replace("\\", "、")
                .replace("/", "、")
                .replace(":", "：")
                .replace("*", "")
                .replace("\"", "")
                .replace("<", "《")
                .replace(">", "》")
                .replace("?", "？")
                .replace("|", "");
    }

    /**
     * create file by file`s path
     *
     * @param path file`s path
     * @return File object
     */
    public static void createFile(String path) {

        var file = new File(path);
        var fileParent = file.getParentFile();
        if (fileParent != null && !fileParent.exists()) {
            boolean is = fileParent.mkdirs();
            logger.debug("create dirs {} : {}", fileParent.getAbsolutePath(), is);
        }

        try {
            var is = file.createNewFile();
            logger.debug("create file {} : {}", file.getAbsolutePath(), is);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static InputStream readClassPathResource(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream();
    }

    /**
     * 读取文件字节数组
     * */
    public static byte[] readClassPathFileToBytes(String path) {
        try (InputStream inputStream =  readClassPathResource(path);
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            inputStream.transferTo(buffer);
            return buffer.toByteArray();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new byte[0];
    }

    /**
     * 读取文件内容
     * */
    public static String readClassPathFileToString(String path) {
        byte[] dataBytes = readClassPathFileToBytes(path);
        if (dataBytes != null) {
            return new String(dataBytes);
        }
        return null;
    }

}
