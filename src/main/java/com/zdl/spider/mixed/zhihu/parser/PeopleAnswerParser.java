package com.zdl.spider.mixed.zhihu.parser;

import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.INCLUDE;
import static com.zdl.spider.mixed.zhihu.ZhihuConst.ZHIHU_ADDRESS;

/**
 * Created by ZDLegend on 2019/4/9 15:19
 */
public class PeopleAnswerParser extends AbstractZhihuParser<AnswerEntity> {

    private static final Logger logger = LoggerFactory.getLogger(QuestionParser.class);

    private static final String PEOPLE_ANSWER_API = ZHIHU_ADDRESS + "api/v4/members/%s/answers?include=%s&limit=%d&offset=%d&sort_by=created";

    public static PeopleAnswerParser getInstance() {
        return new PeopleAnswerParser();
    }

    private PeopleAnswerParser() {
    }

    @Override
    public String url(String q, int offset, int limit) {
        var url = String.format(PEOPLE_ANSWER_API, q, INCLUDE, limit, offset);
        logger.debug("Zhihu Parser url:{}", url);
        return url;
    }

    public static void main(String[] args) {
        var question = new PeopleAnswerParser().execute("tian-wang-mao", 0, 20).join();
        logger.debug("{}", question);
    }
}
