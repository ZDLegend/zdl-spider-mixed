package com.zdl.spider.mixed.zhihu.strategy;

import com.zdl.spider.mixed.utils.JsoupUtil;
import com.zdl.spider.mixed.zhihu.bean.Image;
import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import com.zdl.spider.mixed.zhihu.parser.ZhihuParser;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * 操作答案中的所有图片
 * <p>
 * Created by ZDLegend on 2019/4/12 15:57
 */
public class ImageStrategy implements AnswerStrategy<Image> {

    public static void main(String[] args) {
        ImageStrategy.getInstance().getBySearch("一个人健身前和健身后有什么区别",
                1,
                5,
                image -> image.directSave("C:\\Users\\zdlegend\\Pictures")
        ).join();

        ImageStrategy.getInstance()
                .getByAuthor("he-xiao-lou-42", 20, image -> image.saveByAuthorThenQuestion("C:\\Users\\zdlegend\\Pictures"))
                .join();
    }

    private ImageStrategy() {
    }

    private static ImageStrategy instance = new ImageStrategy();

    public static ImageStrategy getInstance() {
        return instance;
    }

    @Override
    public void resourceHandle(Function<Image, CompletableFuture<Void>> action, ZhihuParser<AnswerEntity> parser) {
        CompletableFuture[] futures = parser.contents()
                .stream()
                .flatMap(answer -> JsoupUtil.getImageAddrByHtml(answer.getContent()).stream().map(s -> Image.of(s, answer)))
                .map(action)
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
    }
}
