package com.zdl.spider.mixed.zhihu.parser;

import com.zdl.spider.mixed.zhihu.Page;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * zhihu paging api parser
 *
 * @param <T>
 */
public interface ZhihuParser<T> {

    /**
     * async execute zhihu parser task
     *
     * @param q      need to search for contents
     * @param offset offset
     * @param limit  limit
     * @return the result of searching by future
     */
    CompletableFuture<ZhihuParser<T>> execute(String q, int offset, int limit);

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
