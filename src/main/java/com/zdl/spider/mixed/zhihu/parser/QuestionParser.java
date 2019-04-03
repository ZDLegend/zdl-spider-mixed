package com.zdl.spider.mixed.zhihu.parser;

import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.ZHIHU_ADDRESS;

/**
 * Created by ZDLegend on 2019/4/1 15:28
 */
public class QuestionParser extends AbstractZhihuParser<AnswerEntity> {

    private static final Logger logger = LoggerFactory.getLogger(QuestionParser.class);

    private static final String QUESTION_API = ZHIHU_ADDRESS + "api/v4/questions/%s/contents?include=%s&limit=%d&offset=%d&sort_by=created";

    private static final String INCLUDE = "data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_labeled%2Cis_recognized%2Cpaid_info%3Bdata%5B*%5D.mark_infos%5B*%5D.url%3Bdata%5B*%5D.author.follower_count%2Cbadge%5B*%5D.topics";

    @Override
    public String url(String q, int offset, int limit) {
        String url = String.format(QUESTION_API, q, INCLUDE, limit, offset);
        logger.debug("Zhihu Parser url:{}", url);
        return url;
    }

    public static void main(String[] args) {
        ZhihuParser question = new QuestionParser().execute("265634331", 0, 20).join();
        logger.debug("{}", question);
    }
}
