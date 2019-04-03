package com.zdl.spider.mixed.zhihu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import static com.zdl.spider.mixed.utils.HttpConst.*;

/**
 * Created by ZDLegend on 2019/3/29 16:34
 */
public class ZhihuConst {

    public static final String ZHIHU_ADDRESS = "https://www.zhihu.com/";

    public static String[] getJsonHeaders() {
        return new String[]{AGENT, AGENT_CONTENT, ACCEPT, ACCEPT_JSON};
    }

    public static JSONObject paresContent(String content) {
        return JSON.parseObject(content);
    }

}
