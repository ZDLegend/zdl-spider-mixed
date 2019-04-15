package com.zdl.spider.mixed.zhihu.strategy;

import com.zdl.spider.mixed.utils.JsoupUtil;
import com.zdl.spider.mixed.zhihu.bean.Image;
import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import com.zdl.spider.mixed.zhihu.entity.AuthorEntity;
import com.zdl.spider.mixed.zhihu.parser.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * 操作答案中的所有图片
 * <p>
 * Created by ZDLegend on 2019/4/12 15:57
 */
public class ImageStrategy {

    public static void main(String[] args) {
        getImagesBySearch("一个人健身前和健身后有什么区别",
                1,
                5,
                image -> image.directSave("C:\\Users\\zdlegend\\Pictures")
        ).join();
    }

    /**
     * 通过搜索获取图片
     *
     * @param content 搜索内容
     * @param x       搜索深度
     * @param y       搜索结果深度
     */
    public static CompletableFuture<Void> getImagesBySearch(String content, int x, int y,
                                                            Function<Image, CompletableFuture<Void>> action) {
        return new SearchParser().pagingParser(content, x, answerParser -> {
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
    public static CompletableFuture<Void> getImagesBySearch(String content, int x, Function<Image, CompletableFuture<Void>> action) {
        return new SearchParser().pagingParser(content, x, parser -> pictureHandle(action, parser));
    }

    /**
     * 通过搜索人名获取图片
     *
     * @param content 搜索内容
     * @param x       搜索深度
     * @param y       搜索结果深度
     */
    public static CompletableFuture<Void> getImagesByPeopleSearch(String content, int x, int y, Function<Image, CompletableFuture<Void>> action) {
        return new SearchPeopleParser().pagingParser(content, x, authorParser -> {
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
    public static CompletableFuture<Void> getImagesByQuestion(String questionId, int y,
                                                              Function<Image, CompletableFuture<Void>> action) {
        return new QuestionParser().pagingParser(questionId, y, parser -> pictureHandle(action, parser));
    }

    /**
     * 获取某个作者回答内容中的图片
     *
     * @param token 用户id
     */
    public static CompletableFuture<Void> getImagesByAuthor(String token, int y, Function<Image, CompletableFuture<Void>> action) {
        return new PeopleAnswerParser().pagingParser(token, y, parser -> pictureHandle(action, parser));
    }

    private static void pictureHandle(Function<Image, CompletableFuture<Void>> action, ZhihuParser<AnswerEntity> parser) {
        CompletableFuture[] futures = parser.contents()
                .stream()
                .flatMap(answer -> JsoupUtil.getImageAddrByHtml(answer.getContent()).stream().map(s -> Image.of(s, answer)))
                .map(action)
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
    }
}
