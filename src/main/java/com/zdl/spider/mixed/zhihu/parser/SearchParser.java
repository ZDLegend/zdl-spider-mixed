package com.zdl.spider.mixed.zhihu.parser;

import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.ZHIHU_ADDRESS;

/**
 * the search spider of zhihu
 * <p>
 * Created by ZDLegend on 2019/3/30 14:25
 */
public class SearchParser extends AbstractZhihuParser<AnswerEntity> {

    private static final Logger logger = LoggerFactory.getLogger(SearchParser.class);

    private static final String SEARCH_API = ZHIHU_ADDRESS + "api/v4/search_v3?t=general&q=%s&correction=1&offset=%d&limit=%d";

    @Override
    public String url(String q, int offset, int limit) {
        String url = String.format(SEARCH_API, q, offset, limit);
        logger.debug("Search Parser url:{}", url);
        return url;
    }

    public static void main(String[] args) {
        ZhihuParser search = new SearchParser().execute("体验", 0, 20).join();
        logger.debug("{}", search);
    }
}
