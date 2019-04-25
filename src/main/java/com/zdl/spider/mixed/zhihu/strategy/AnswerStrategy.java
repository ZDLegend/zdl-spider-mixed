package com.zdl.spider.mixed.zhihu.strategy;

import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import com.zdl.spider.mixed.zhihu.entity.AuthorEntity;
import com.zdl.spider.mixed.zhihu.parser.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface AnswerStrategy<T> {

    /**
     * 通过搜索获取回答内容中的资源
     *
     * @param content 搜索内容
     * @param x       搜索深度
     * @param y       搜索结果深度
     */
    default CompletableFuture<Void> getBySearch(String content, int x, int y,
                                                Function<T, CompletableFuture<Void>> action) {
        return SearchParser.getInstance().pagingParser(content, x, answerParser -> {
            CompletableFuture[] futures = answerParser.contents()
                    .stream()
                    .map(answerEntity -> answerEntity.getQuestion().getId())
                    .map(questionId -> getByQuestion(questionId, y, action))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
        });
    }

    /**
     * 直接获取搜索结果(不需要在搜索结果的问题中搜索时调用)
     *
     * @param content 搜索内容
     * @param x       搜索深度
     */
    default CompletableFuture<Void> getBySearch(String content, int x, Function<T, CompletableFuture<Void>> action) {
        return SearchParser.getInstance().pagingParser(content, x, parser -> resourceHandle(action, parser));
    }

    /**
     * 通过搜索人名获取回答内容中的资源
     *
     * @param content 搜索内容
     * @param x       搜索深度
     * @param y       搜索结果深度
     */
    default CompletableFuture<Void> getByPeopleSearch(String content, int x, int y, Function<T, CompletableFuture<Void>> action) {
        return SearchPeopleParser.getInstance().pagingParser(content, x, authorParser -> {
            CompletableFuture[] futures = authorParser.contents()
                    .stream()
                    .map(AuthorEntity::getUrlToken)
                    .map(token -> getByAuthor(token, y, action))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
        });
    }

    /**
     * 获取某个问题下回答内容中的资源
     *
     * @param questionId 问题id
     */
    default CompletableFuture<Void> getByQuestion(String questionId, int y,
                                                  Function<T, CompletableFuture<Void>> action) {
        return QuestionParser.getInstance().pagingParser(questionId, y, parser -> resourceHandle(action, parser));
    }

    /**
     * 获取某个作者回答内容中的资源
     *
     * @param token 用户id
     */
    default CompletableFuture<Void> getByAuthor(String token, int y, Function<T, CompletableFuture<Void>> action) {
        return PeopleAnswerParser.getInstance().pagingParser(token, y, parser -> resourceHandle(action, parser));
    }

    void resourceHandle(Function<T, CompletableFuture<Void>> action, ZhihuParser<AnswerEntity> parser);
}
