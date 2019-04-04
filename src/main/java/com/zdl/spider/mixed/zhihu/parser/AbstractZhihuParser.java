package com.zdl.spider.mixed.zhihu.parser;

import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.ClassUtil;
import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.zhihu.Page;
import com.zdl.spider.mixed.zhihu.ZhihuConst;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.getJsonHeaders;

/**
 * Created by ZDLegend on 2019/4/2 20:32
 */
public abstract class AbstractZhihuParser<T> implements ZhihuParser<T> {

    private Page page;
    private List<T> contents;

    @SuppressWarnings("unchecked")
    private ZhihuParser<T> parser(ZhihuParser<T> parser, JSONObject json) {
        page = JSONObject.parseObject(json.getString("paging"), Page.class);
        contents = json.getJSONArray("data")
                .stream()
                .map(o -> (JSONObject) o)
                .map(j -> JSONObject.parseObject(j.toJSONString(), (Class<T>) ClassUtil.getGenericType(this.getClass())))
                .collect(Collectors.toList());
        return parser;
    }

    @Override
    public CompletableFuture<ZhihuParser<T>> execute(String q, int offset, int limit) {
        var url = url(q, offset, limit);
        return HttpUtil.get(url, getJsonHeaders(), ZhihuConst::paresContent)
                .thenApply(json -> parser(this, json));
    }

    @Override
    public List<T> contents() {
        return contents;
    }

    @Override
    public Page page() {
        return page;
    }
}
