package com.zdl.spider.mixed.zhihu.parser;

import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.zhihu.Page;
import com.zdl.spider.mixed.zhihu.ZhihuConst;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.getJsonHeaders;

/**
 * zhihu paging api parser
 *
 * @param <T>
 */
public interface ZhihuParser<T> {

    /**
     * async execute zhihu parser task
     *
     * @param q      contents
     * @param offset offset
     * @param limit  limit
     * @return the result of searching by future
     */
    default CompletableFuture<ZhihuParser<T>> execute(String q, int offset, int limit) {
        var url = url(q, offset, limit);
        return HttpUtil.get(url, getJsonHeaders(), ZhihuConst::paresContent)
                .thenApply(json -> parser(this, json));
    }

    default CompletableFuture<ZhihuParser<T>> execute(String url) {
        return HttpUtil.get(url, getJsonHeaders(), ZhihuConst::paresContent)
                .thenApply(json -> parser(this, json));
    }

    /**
     * parser data of json
     */
    ZhihuParser<T> parser(ZhihuParser<T> parser, JSONObject json);

    /**
     * format zhihu restful api
     */
    String url(String q, int offset, int limit);

    /**
     * get contents list
     */
    List<T> contents();

    /**
     * get page info
     */
    Page page();
}
