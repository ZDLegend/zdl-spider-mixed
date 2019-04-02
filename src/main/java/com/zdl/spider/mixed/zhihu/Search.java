package com.zdl.spider.mixed.zhihu;

import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import com.zdl.spider.mixed.zhihu.entity.AuthorEntity;
import com.zdl.spider.mixed.zhihu.entity.QuestionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.ZHIHU_ADDRESS;
import static com.zdl.spider.mixed.zhihu.ZhihuConst.getJsonHeaders;

/**
 * the search spider of zhihu
 * <p>
 * Created by ZDLegend on 2019/3/30 14:25
 */
public class Search {

    private static final Logger logger = LoggerFactory.getLogger(Search.class);

    private static final String SEARCH_API = ZHIHU_ADDRESS + "api/v4/search_v3?t=general&q=%s&correction=1&offset=%d&limit=%d";

    private Page page;
    private List<AnswerEntity> answers;

    /**
     * async execute search task
     *
     * @param q      need to search for content
     * @param offset offset
     * @param limit  limit (At least 20)
     * @param call   result call back
     * @param <U>    trans type
     * @return the result of searching by future
     */
    private static <U> CompletableFuture<U> execute(String q, int offset, int limit, Function<String, U> call) {
        String url = String.format(SEARCH_API, q, offset, limit);
        return HttpUtil.get(url, getJsonHeaders(), call);
    }

    /**
     * get search result for json
     */
    public static CompletableFuture<JSONObject> getContentForJson(String q, int offset, int limit) {
        return execute(q, offset, limit, ZhihuConst::paresContent);
    }

    public static Search execute(String q, int offset, int limit) {
        var search = new Search();
        var json = getContentForJson(q, offset, limit).join();
        search.page = JSONObject.parseObject(json.getString("paging"), Page.class);
        search.answers = json.getJSONArray("data")
                .stream()
                .map(o -> (JSONObject) o)
                .map(j -> JSONObject.parseObject(j.toJSONString(), AnswerEntity.class))
                .collect(Collectors.toList());

        return search;
    }

    public List<AnswerEntity> answers() {
        return this.answers;
    }

    public Page page() {
        return page;
    }

    public static void main(String[] args) {
        Search search = Search.execute("体验", 0, 20);
        logger.debug("{}", search);
    }
}
