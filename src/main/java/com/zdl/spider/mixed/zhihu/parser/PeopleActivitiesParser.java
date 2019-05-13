package com.zdl.spider.mixed.zhihu.parser;


import com.zdl.spider.mixed.zhihu.entity.ActivityEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.ZHIHU_ADDRESS;

/**
 * 用户动态解析
 * <p>
 * Created by ZDLegend on 2019/4/28 15:04
 */
public class PeopleActivitiesParser extends AbstractZhihuParser<ActivityEntity> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractZhihuParser.class);

    private static final int MAX_PAGE_SIZE = 7;

    private static final String PEOPLE_ACTIVITIES_API = ZHIHU_ADDRESS + "api/v4/members/%s/activities?limit=7";

    public static PeopleActivitiesParser getInstance() {
        return new PeopleActivitiesParser();
    }

    public String url(String q) {
        return url(q, 0, 0);
    }

    public CompletableFuture<ZhihuParser<ActivityEntity>> executeByQ(String q) {
        return execute(q, 0, 0);
    }

    @Override
    public String url(String q, int offset, int limit) {
        return String.format(PEOPLE_ACTIVITIES_API, q);
    }

    @Override
    public CompletableFuture<Void> pagingParser(String q, int y, Consumer<ZhihuParser<ActivityEntity>> consumer) {

        if (y <= MAX_PAGE_SIZE && y > 0) {
            return executeByQ(q)
                    .whenComplete((parser, throwable) -> executeAfter(parser, consumer, throwable))
                    .thenAccept(parser -> logger.info("Execute finish!"));
        } else if (y < 0) {
            //执行全量
            var url = url(q);
            return pagingParser(url, consumer).thenAccept(parser -> logger.info("Execute finish!"));
        } else {
            int cycle = y / MAX_PAGE_SIZE;
            int s = y % MAX_PAGE_SIZE;
            if (s > 0) {
                cycle = cycle + 1;
            }
            return pagingCycle(url(q), cycle, consumer).thenAccept(parser -> logger.info("Execute finish!"));
        }
    }

    public static void main(String[] args) {
        var activities = getInstance().executeByQ("tian-wang-mao").join();
        logger.debug("{}", activities);
    }
}
