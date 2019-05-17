package com.zdl.spider.mixed.zhihu.strategy;

import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.utils.JsoupUtil;
import com.zdl.spider.mixed.zhihu.ZhihuConst;
import com.zdl.spider.mixed.zhihu.resources.Video;
import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import com.zdl.spider.mixed.zhihu.parser.ZhihuParser;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.getJsonHeaders;

/**
 * 知乎视频下载
 * <p>
 * Created by ZDLegend on 2019/4/23 13:53
 */
public class VideoStrategy implements AnswerStrategy<Video> {

    public static final String VIDEO_DRESS = "https://lens.zhihu.com/api/v4/videos/%s";

    private static final String PLAY_URL = "play_url";

    public static void main(String[] args) {
        VideoStrategy.getInstance().getBySearch("你手机里最舍不得删的视频是哪个",
                1,
                5,
                video -> video.directSave("C:\\Users\\zdlegend\\Videos")
        ).join();
    }

    private VideoStrategy() {
    }

    private static VideoStrategy instance = new VideoStrategy();

    public static VideoStrategy getInstance() {
        return instance;
    }

    @Override
    public void resourceHandle(Function<Video, CompletableFuture<Void>> action, ZhihuParser<AnswerEntity> parser) {
        CompletableFuture[] futures = parser.contents()
                .stream()
                .flatMap(answer -> JsoupUtil.getAddrByHtml(answer.getContent()).values().stream()
                        .filter(s -> s.contains("www.zhihu.com/video"))
                        .map(s -> Video.ofId(s.substring(s.lastIndexOf('/') + 1), answer))
                )
                .map(action)
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
    }

    public static CompletableFuture<String> getPlayUrl(String url) {
        return HttpUtil.get(url, getJsonHeaders(), ZhihuConst::paresContent)
                .thenApply(json -> {
                    if (json.containsKey("playlist")) {
                        JSONObject j = json.getJSONObject("playlist");
                        if (j.containsKey("HD")) {
                            return j.getJSONObject("HD").getString(PLAY_URL);
                        } else if (j.containsKey("SD")) {
                            return j.getJSONObject("SD").getString(PLAY_URL);
                        } else if (j.containsKey("LD")) {
                            return j.getJSONObject("LD").getString(PLAY_URL);
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                });
    }

    /**
     * 视频下载
     *
     * @param url      视频URL
     * @param filePath 文件路径
     * @param name     文件名
     */
    public static CompletableFuture<Void> videoDownload(String url, String filePath, String name) {
        return getPlayUrl(url).thenCompose(videoUrl -> HttpUtil.downLoadFile(videoUrl, filePath, name));
    }
}
