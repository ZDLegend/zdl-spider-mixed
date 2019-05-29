package com.zdl.spider.mixed.zhihu.parser;

import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.ClassUtil;
import com.zdl.spider.mixed.zhihu.dto.PageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.ZHIHU_ADDRESS;

/**
 * Created by ZDLegend on 2019/4/8 19:36
 */
public abstract class AbstractZhihuSearchParser<T> extends AbstractZhihuParser<T> {

    private static final String SEARCH_API = ZHIHU_ADDRESS + "api/v4/search_v3?t=%s&q=%s&correction=1&offset=%d&limit=%d";

    private static final Logger logger = LoggerFactory.getLogger(AbstractZhihuParser.class);

    @SuppressWarnings("unchecked")
    @Override
    public ZhihuParser<T> parser(ZhihuParser<T> parser, JSONObject json) {
        pageDto = JSONObject.parseObject(json.getString("paging"), PageDto.class);
        contents = json.getJSONArray("data")
                .stream()
                .map(o -> (JSONObject) o)
                .filter(j -> "search_result".equals(j.getString("type")) && j.containsKey("object"))
                .map(j -> j.getJSONObject("object"))
                .filter(j -> j.containsKey("type") && j.getString("type").equals(getObjectType()))
                .map(j -> j.toJSONString().replace("<em>", "").replace("</em>", ""))
                .map(s -> JSONObject.parseObject(s, (Class<T>) ClassUtil.getGenericType(this.getClass())))
                .collect(Collectors.toList());
        return parser;
    }

    @Override
    public String url(String q, int offset, int limit) {
        return String.format(SEARCH_API, getType(), q, offset, limit);
    }

    abstract String getType();

    abstract String getObjectType();
}
