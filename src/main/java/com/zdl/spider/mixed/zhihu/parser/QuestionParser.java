package com.zdl.spider.mixed.zhihu.parser;

import com.zdl.spider.mixed.zhihu.dto.AnswerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.INCLUDE;
import static com.zdl.spider.mixed.zhihu.ZhihuConst.ZHIHU_ADDRESS;

/**
 * Created by ZDLegend on 2019/4/1 15:28
 */
public class QuestionParser extends AbstractZhihuParser<AnswerDto> {

    private static final Logger logger = LoggerFactory.getLogger(QuestionParser.class);

    private static final String QUESTION_API = ZHIHU_ADDRESS + "api/v4/questions/%s/answers?include=%s&limit=%d&offset=%d&sort_by=created";

    public static QuestionParser getInstance() {
        return new QuestionParser();
    }

    private QuestionParser() {
    }

    @Override
    public String url(String q, int offset, int limit) {
        return String.format(QUESTION_API, q, INCLUDE, limit, offset);
    }

    public static void main(String[] args) {
        var question = getInstance().execute("36351193", 0, 20).join();
        logger.debug("{}", question);
    }
}
