package com.zdl.spider.mixed.zhihu.strategy;

import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.utils.JsoupUtil;
import com.zdl.spider.mixed.zhihu.ZhihuConst;
import com.zdl.spider.mixed.zhihu.bean.Video;
import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;
import com.zdl.spider.mixed.zhihu.entity.AuthorEntity;
import com.zdl.spider.mixed.zhihu.parser.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.getJsonHeaders;

/**
 * 知乎视频下载
 * <p>
 * Created by ZDLegend on 2019/4/23 13:53
 */
public class VideoStrategy {

    public static final String VIDEO_DRESS = "https://lens.zhihu.com/api/v4/videos/%s";

    public static void main(String[] args) {
        getBySearch("你手机里最舍不得删的视频是哪个",
                1,
                5,
                video -> video.directSave("C:\\Users\\zdlegend\\Pictures")
        ).join();
    }

    /**
     * 通过搜索获取视频
     *
     * @param content 搜索内容
     * @param x       搜索深度
     * @param y       搜索结果深度
     */
    public static CompletableFuture<Void> getBySearch(String content, int x, int y,
                                                      Function<Video, CompletableFuture<Void>> action) {
        return new SearchParser().pagingParser(content, x, answerParser -> {
            CompletableFuture[] futures = answerParser.contents()
                    .stream()
                    .map(answerEntity -> answerEntity.getQuestion().getId())
                    .map(questionId -> getByQuestion(questionId, y, action))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
        });
    }

    /**
     * 直接获取搜索结果(不需要在搜索结果的问题中搜索时调用)
     *
     * @param content 搜索内容
     * @param x       搜索深度
     */
    public static CompletableFuture<Void> getBySearch(String content, int x, Function<Video, CompletableFuture<Void>> action) {
        return new SearchParser().pagingParser(content, x, parser -> resourceHandle(action, parser));
    }

    /**
     * 通过搜索人名获取视频
     *
     * @param content 搜索内容
     * @param x       搜索深度
     * @param y       搜索结果深度
     */
    public static CompletableFuture<Void> getByPeopleSearch(String content, int x, int y, Function<Video, CompletableFuture<Void>> action) {
        return new SearchPeopleParser().pagingParser(content, x, authorParser -> {
            CompletableFuture[] futures = authorParser.contents()
                    .stream()
                    .map(AuthorEntity::getUrlToken)
                    .map(token -> getByAuthor(token, y, action))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
        });
    }

    /**
     * 获取某个问题下回答内容中的视频
     *
     * @param questionId 问题id
     */
    public static CompletableFuture<Void> getByQuestion(String questionId, int y,
                                                        Function<Video, CompletableFuture<Void>> action) {
        return new QuestionParser().pagingParser(questionId, y, parser -> resourceHandle(action, parser));
    }

    /**
     * 获取某个作者回答内容中的视频
     *
     * @param token 用户id
     */
    public static CompletableFuture<Void> getByAuthor(String token, int y, Function<Video, CompletableFuture<Void>> action) {
        return new PeopleAnswerParser().pagingParser(token, y, parser -> resourceHandle(action, parser));
    }

    private static void resourceHandle(Function<Video, CompletableFuture<Void>> action, ZhihuParser<AnswerEntity> parser) {
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

    /**
     * 获取视频分割后的相对url
     *
     * @param url url
     * @return play_url
     */
    public static CompletableFuture<List<String>> getPlayPartUrl(String url) {
        return HttpUtil.get(url, getJsonHeaders(), ZhihuConst::paresContent)
                .thenApply(json -> {
                    if (json.containsKey("HD")) {
                        return json.getJSONObject("HD").getString("play_url");
                    } else if (json.containsKey("SD")) {
                        return json.getJSONObject("SD").getString("play_url");
                    } else if (json.containsKey("LD")) {
                        return json.getJSONObject("LD").getString("play_url");
                    } else {
                        return null;
                    }
                }).thenCompose(s -> {
                    List<String> list = new ArrayList<>();
                    return HttpUtil.get(url, getJsonHeaders(), content -> {
                        // 提取出相对路径
                        String relUrl = url.replaceAll("/\\w+-\\w+-\\w+-\\w+-\\w+\\.m3u8.*", "");

                        // 正则提取出的为相对路径, 需与前面的relUrl完成拼接
                        Matcher matcher = Pattern.compile("EXTINF:\\d+\\.\\d+,(.+?)#").matcher(content);
                        while (matcher.find()) {
                            list.add(relUrl + "/" + matcher.group(1));
                        }
                        return list;
                    });
                });
    }

    /**
     * 视频下载
     *
     * @param videoId  视频id
     * @param filePath 文件路径
     * @param name     文件名
     */
    public static CompletableFuture<Void> videoDownload(String videoId, String filePath, String name) {
        String url = String.format(VIDEO_DRESS, videoId);
        return getPlayPartUrl(url).thenAccept(list -> HttpUtil.downLoadFiles(list, filePath, name));
    }
}
