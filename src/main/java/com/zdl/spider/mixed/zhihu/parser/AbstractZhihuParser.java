package com.zdl.spider.mixed.zhihu.parser;

import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.ClassUtil;
import com.zdl.spider.mixed.zhihu.dto.PageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by ZDLegend on 2019/4/2 20:32
 */
public abstract class AbstractZhihuParser<T> implements ZhihuParser<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractZhihuParser.class);
    private static final int MAX_PAGE_SIZE = 20;

    PageDto pageDto;
    List<T> contents;

    @SuppressWarnings("unchecked")
    @Override
    public ZhihuParser<T> parser(ZhihuParser<T> parser, JSONObject json) {
        pageDto = JSONObject.parseObject(json.getString("paging"), PageDto.class);
        contents = json.getJSONArray("data")
                .stream()
                .map(o -> (JSONObject) o)
                .map(j -> JSONObject.parseObject(j.toJSONString(), (Class<T>) ClassUtil.getGenericType(this.getClass())))
                .collect(Collectors.toList());
        return parser;
    }

    @Override
    public List<T> contents() {
        return contents;
    }

    @Override
    public PageDto page() {
        return pageDto;
    }

    public CompletableFuture<Void> pagingParser(String q, int deep, Consumer<ZhihuParser<T>> consumer) {
        return pagingParser(q, 0, deep, consumer);
    }

    CompletableFuture<ZhihuParser<T>> pagingParser(String url, Consumer<ZhihuParser<T>> consumer) {
        return execute(url)
                .whenComplete((p, throwable) -> executeAfter(p, consumer, throwable))
                .thenCompose(parser -> {
                    if (parser.page().isEnd()) {
                        CompletableFuture<ZhihuParser<T>> completableFuture = new CompletableFuture<>();
                        completableFuture.complete(parser);
                        return completableFuture;
                    } else {
                        return pagingParser(parser.page().getNext(), consumer);
                    }
                });
    }

    CompletableFuture<ZhihuParser<T>> pagingCycle(String url, int cycle, Consumer<ZhihuParser<T>> consumer) {
        return execute(url)
                .whenComplete((p, throwable) -> executeAfter(p, consumer, throwable))
                .thenCompose(parser -> {
                    int newCycle = cycle - 1;
                    if (parser.page().isEnd() || newCycle <= 0) {
                        CompletableFuture<ZhihuParser<T>> completableFuture = new CompletableFuture<>();
                        completableFuture.complete(parser);
                        return completableFuture;
                    } else {
                        return pagingCycle(parser.page().getNext(), newCycle, consumer);
                    }
                });
    }

    private CompletableFuture<Void> pagingParser(String q, int start, int deep, Consumer<ZhihuParser<T>> consumer) {
        if (deep <= MAX_PAGE_SIZE && deep > 0) {
            return execute(q, start, deep)
                    .whenComplete((parser, throwable) -> executeAfter(parser, consumer, throwable))
                    .thenAccept(parser -> logger.info("Execute finish!"));
        } else if (deep < 0) {
            //执行全量
            var url = url(q, start, MAX_PAGE_SIZE);
            return pagingParser(url, consumer).thenAccept(parser -> logger.info("Execute finish!"));
        } else {
            //分页执行
            return execute(q, start, MAX_PAGE_SIZE)
                    .whenComplete((parser, throwable) -> executeAfter(parser, consumer, throwable))
                    .thenCompose(parser -> {
                        int surplus = deep - start;
                        if (parser.page().isEnd() || surplus == 0) {
                            CompletableFuture<Void> completableFuture = new CompletableFuture<>();
                            completableFuture.complete(null);
                            return completableFuture;
                        } else {
                            return pagingParser(q, start + MAX_PAGE_SIZE, surplus, consumer);
                        }
                    }).thenAccept(parser -> logger.info("Execute finish!"));
        }
    }

    void executeAfter(ZhihuParser<T> parser, Consumer<ZhihuParser<T>> consumer, Throwable throwable) {
        if (parser != null) {
            consumer.accept(parser);
        } else {
            logger.error(throwable.getMessage(), throwable);
        }
    }
}
