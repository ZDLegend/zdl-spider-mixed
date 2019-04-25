package com.zdl.spider.mixed.zhihu.parser;

import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * the search spider of zhihu
 * <p>
 * Created by ZDLegend on 2019/3/30 14:25
 */
public class SearchParser extends AbstractZhihuSearchParser<AnswerEntity> {

    private static final Logger logger = LoggerFactory.getLogger(SearchParser.class);

    public static SearchParser getInstance() {
        return new SearchParser();
    }

    private SearchParser() {
    }

    @Override
    String getType() {
        return "general";
    }

    @Override
    String getObjectType() {
        return "answer";
    }

    public static void main(String[] args) {
        var search = new SearchParser().execute("健身", 0, 1).join();
        logger.debug("{}", search);
    }
}
