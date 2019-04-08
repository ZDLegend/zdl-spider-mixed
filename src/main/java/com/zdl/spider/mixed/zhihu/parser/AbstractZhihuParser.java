package com.zdl.spider.mixed.zhihu.parser;

import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.ClassUtil;
import com.zdl.spider.mixed.zhihu.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ZDLegend on 2019/4/2 20:32
 */
public abstract class AbstractZhihuParser<T> implements ZhihuParser<T> {

    Page page;
    List<T> contents;

    @SuppressWarnings("unchecked")
    @Override
    public ZhihuParser<T> parser(ZhihuParser<T> parser, JSONObject json) {
        page = JSONObject.parseObject(json.getString("paging"), Page.class);
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
    public Page page() {
        return page;
    }
}
