package com.zdl.spider.mixed.zhihu.resources;

import com.zdl.spider.mixed.utils.FileUtil;
import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * 回答内容对象
 *
 * Created by ZDLegend on 2019/5/17 17:43
 */
public class Content implements Resource  {

    private static Logger logger = LoggerFactory.getLogger(Content.class);

    private String name;

    /**
     * 内容所在回答
     */
    private AnswerEntity answerEntity;

    public static Content of( AnswerEntity answerEntity) {
        Content content = new Content();
        content.answerEntity = answerEntity;
        content.name = answerEntity.getName();
        return content;
    }

    public CompletableFuture<Void> directSave(String localPath) {
        return saveContent(localPath, name);
    }

    public CompletableFuture<Void> saveByAuthorThenQuestion(String basePath) {
        String absolutePath = basePath
                + File.separator + FileUtil.removeIllegalWord(answerEntity.getAuthor().getName())
                + File.separator + FileUtil.removeIllegalWord(answerEntity.getQuestion().getTitle());
        return saveContent(absolutePath, name);
    }

    public CompletableFuture<Void> saveByQuestionThenAuthor(String basePath) {
        String absolutePath = basePath
                + File.separator + FileUtil.removeIllegalWord(answerEntity.getQuestion().getTitle())
                + File.separator + FileUtil.removeIllegalWord(answerEntity.getAuthor().getName());
        return saveContent(absolutePath, name);
    }

    private CompletableFuture<Void> saveContent(String filePath, String name){
        String path = filePath + File.separator + FileUtil.removeIllegalWord(name);
        logger.debug("save to {}", path);
        FileUtil.createFile(path);
        return CompletableFuture.runAsync(() -> {
            try (FileOutputStream fileOutputStream = new FileOutputStream(new File(path))) {
                fileOutputStream.write(answerEntity.getContent().getBytes());
            } catch (IOException e) {
                logger.error("file:{}\nexception:{}", path, e.getMessage(), e);
            }
        });
    }
}
