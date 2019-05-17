package com.zdl.spider.mixed.zhihu.resources;

import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;

import java.util.concurrent.CompletableFuture;

/**
 * 回答内容对象
 *
 * Created by ZDLegend on 2019/5/17 17:43
 */
public class Content implements Resource  {

    /**
     * 内容所在回答
     */
    private AnswerEntity answerEntity;

    public static Content of( AnswerEntity answerEntity) {
        Content content = new Content();
        content.answerEntity = answerEntity;
        return content;
    }

    public CompletableFuture<Void> directSave(String localPath) {
        return null;
    }

    public CompletableFuture<Void> saveByAuthorThenQuestion(String basePath) {
        return null;
    }

    public CompletableFuture<Void> saveByQuestionThenAuthor(String basePath) {
        return null;
    }
}
