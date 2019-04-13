package com.zdl.spider.mixed.zhihu.strategy;

import com.zdl.spider.mixed.utils.JsoupUtil;
import com.zdl.spider.mixed.zhihu.bean.Image;
import com.zdl.spider.mixed.zhihu.entity.AuthorEntity;
import com.zdl.spider.mixed.zhihu.parser.PeopleAnswerParser;
import com.zdl.spider.mixed.zhihu.parser.QuestionParser;
import com.zdl.spider.mixed.zhihu.parser.SearchParser;
import com.zdl.spider.mixed.zhihu.parser.SearchPeopleParser;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 图片策略
 * <p>
 * Created by ZDLegend on 2019/4/12 15:57
 */
public class ImageStrategy {

    /**
     * 通过搜索获取图片
     *
     * @param content 搜索内容
     * @param x       搜索深度
     * @param y       搜索结果深度
     */
    public void getImagesBySearch(String content, int x, int y, Consumer<Image> action) {
        new SearchParser().pagingParser(content, x, answerParser -> {
            CompletableFuture[] futures = answerParser.contents()
                    .stream()
                    .map(answerEntity -> answerEntity.getQuestion().getId())
                    .map(questionId -> getImagesByQuestion(questionId, y, action))
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
    public void getImagesBySearch(String content, int x, Consumer<Image> action) {
        new SearchParser().pagingParser(content, x,
                answerParser -> answerParser.contents()
                        .stream()
                        .flatMap(answer -> JsoupUtil.getImageAddrByHtml(answer.getContent()).stream().map(s -> Image.of(s, answer)))
                        .forEach(action));
    }

    /**
     * 通过搜索人名获取图片
     *
     * @param content 搜索内容
     * @param x       搜索深度
     * @param y       搜索结果深度
     */
    public void getImagesByPeopleSearch(String content, int x, int y, Consumer<Image> action) {
        new SearchPeopleParser().pagingParser(content, x, authorParser -> {
            CompletableFuture[] futures = authorParser.contents()
                    .stream()
                    .map(AuthorEntity::getUrlToken)
                    .map(token -> getImagesByAuthor(token, y, action))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
        });
    }

    /**
     * 获取某个问题下回答内容中的图片
     *
     * @param questionId 问题id
     */
    public CompletableFuture<Void> getImagesByQuestion(String questionId, int y, Consumer<Image> action) {
        return new QuestionParser().pagingParser(questionId, y, parser -> parser.contents()
                .stream()
                .flatMap(answer -> JsoupUtil.getImageAddrByHtml(answer.getContent()).stream().map(s -> Image.of(s, answer)))
                .forEach(action));
    }

    /**
     * 获取某个作者回答内容中的图片
     *
     * @param token 用户id
     */
    public CompletableFuture<Void> getImagesByAuthor(String token, int y, Consumer<Image> action) {
        return new PeopleAnswerParser().pagingParser(token, y, parser -> parser.contents()
                .stream()
                .flatMap(answer -> JsoupUtil.getImageAddrByHtml(answer.getContent()).stream().map(s -> Image.of(s, answer)))
                .forEach(action));
    }
}
