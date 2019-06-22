package com.zdl.spider.mixed.zhihu.parser;

import com.alibaba.fastjson.JSON;
import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.zhihu.dto.AuthorDto;

import java.util.concurrent.CompletableFuture;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.ZHIHU_API_ADDRESS;
import static com.zdl.spider.mixed.zhihu.ZhihuConst.getJsonHeaders;

/**
 * Created by ZDLegend on 2019/6/22 14:06
 */
public class ZhihuApi {

    private static final String PEOPLE_INFO_API = ZHIHU_API_ADDRESS + "people/%s";

    public static CompletableFuture<AuthorDto> getPeopleInfo(String peopleId) {
        var url = String.format(PEOPLE_INFO_API, peopleId);
        return HttpUtil.get(url, getJsonHeaders(), s -> JSON.parseObject(s, AuthorDto.class));
    }
}
