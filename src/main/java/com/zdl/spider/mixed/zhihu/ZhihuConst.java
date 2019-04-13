package com.zdl.spider.mixed.zhihu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import static com.zdl.spider.mixed.utils.HttpConst.*;

/**
 * Created by ZDLegend on 2019/3/29 16:34
 */
public final class ZhihuConst {

    public static final String ZHIHU_ADDRESS = "https://www.zhihu.com/";

    public static final String INCLUDE = "data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_labeled%2Cis_recognized%2Cpaid_info%3Bdata%5B*%5D.mark_infos%5B*%5D.url%3Bdata%5B*%5D.author.follower_count%2Cbadge%5B*%5D.topics";

    private ZhihuConst() {
        //do nothing
    }

    public static String[] getJsonHeaders() {
        return new String[]{AGENT, AGENT_CONTENT, ACCEPT, ACCEPT_JSON};
    }

    public static JSONObject paresContent(String content) {
        return JSON.parseObject(content);
    }

}
