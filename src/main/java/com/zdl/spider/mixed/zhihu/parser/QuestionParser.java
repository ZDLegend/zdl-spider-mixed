package com.zdl.spider.mixed.zhihu.parser;

import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.INCLUDE;
import static com.zdl.spider.mixed.zhihu.ZhihuConst.ZHIHU_ADDRESS;

/**
 * Created by ZDLegend on 2019/4/1 15:28
 */
public class QuestionParser extends AbstractZhihuParser<AnswerEntity> {

    private static final Logger logger = LoggerFactory.getLogger(QuestionParser.class);

    private static final String QUESTION_API = ZHIHU_ADDRESS + "api/v4/questions/%s/answers?include=%s&limit=%d&offset=%d&sort_by=created";

    @Override
    public String url(String q, int offset, int limit) {
        String url = String.format(QUESTION_API, q, INCLUDE, limit, offset);
        logger.debug("Zhihu Parser url:{}", url);
        return url;
    }

    public static void main(String[] args) {
        ZhihuParser question = new QuestionParser().execute("36351193", 0, 20).join();
        logger.debug("{}", question);
    }
}
