package com.zdl.spider.mixed.zhihu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.zhihu.entity.QuestionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zdl.spider.mixed.utils.HttpConst.*;
import static com.zdl.spider.mixed.zhihu.zhihuConst.ZHIHU_ADDRESS;

/**
 * the search spider of zhihu
 *
 * Created by ZDLegend on 2019/3/30 14:25
 */
public class Search {

    private static Logger logger = LoggerFactory.getLogger(Search.class);

    private static final String SEARCH_API = ZHIHU_ADDRESS + "api/v4/search_v3?t=general&q=%s&correction=1&offset=%d&limit=%d";

    /**
     * async execute search task
     *
     * @param q need to search for content
     * @param offset offset
     * @param limit limit (At least 20)
     * @param call result call back
     * @param <U> trans type
     * @return the result of searching by future
     */
    private <U> CompletableFuture<U> execute(String q, int offset, int limit, Function<String, U> call){
        String url = String.format(SEARCH_API, q, offset, limit);
        return HttpUtil.get(url, getHeaders(), call);
    }

    /**
     * get search result for json
     */
    public  CompletableFuture<JSONObject> getContentForJson(String q, int offset, int limit) {
        return execute(q, offset, limit, this::paresContent);
    }

    /**
     * get question list by search result
     */
    public  CompletableFuture<List<QuestionEntity>> getQuestionList(String q, int offset, int limit) {
        return execute(q, offset, limit, this::paresContent).thenApply(this::paresQuestionJson);
    }

    private JSONObject paresContent(String content){
        return JSON.parseObject(content);
    }

    private List<QuestionEntity> paresQuestionJson(JSONObject json){

        if(json.containsKey("data")) {
            return json.getJSONArray("data")
                    .stream()
                    .map(o -> (JSONObject)o)
                    .filter(j -> j.containsKey("object") && j.containsKey("type") && j.getString("type").equals("search_result"))
                    .map(j -> j.getJSONObject("object"))
                    .filter(j -> j.containsKey("question"))
                    .map(j -> JSONObject.parseObject(j.getString("question"), QuestionEntity.class))
                    .peek(q -> q.setName(q.getName().replace("<em>", "").replace("</em>", "")))
                    .distinct()
                    .collect(Collectors.toList());
        }

        logger.error("search result do not have 'data' int json:{}", json);
        return new ArrayList<>();
    }

    private String[] getHeaders(){
        return new String[]{AGENT, AGENT_CONTENT, ACCEPT, ACCEPT_JSON};
    }

    public static void main(String[] args) {
        List<QuestionEntity> list = new Search().getQuestionList("体验", 0, 20).join();
        logger.debug("{}", list);
    }
}
