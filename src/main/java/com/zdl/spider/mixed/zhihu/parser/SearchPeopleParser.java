package com.zdl.spider.mixed.zhihu.parser;

import com.zdl.spider.mixed.zhihu.entity.AuthorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the search spider of zhihu by people
 * <p>
 * Created by ZDLegend on 2019/4/8 19:15
 */
public class SearchPeopleParser extends AbstractZhihuSearchParser<AuthorEntity> {

    private static final Logger logger = LoggerFactory.getLogger(SearchPeopleParser.class);

    public static SearchPeopleParser getInstance() {
        return new SearchPeopleParser();
    }

    private SearchPeopleParser() {
    }

    @Override
    String getType() {
        return "people";
    }

    @Override
    String getObjectType() {
        return "people";
    }

    public static void main(String[] args) {
        var search = getInstance().execute("丢", 0, 20).join();
        logger.debug("{}", search);
    }
}
