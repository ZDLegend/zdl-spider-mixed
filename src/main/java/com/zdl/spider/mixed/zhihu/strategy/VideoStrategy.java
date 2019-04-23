package com.zdl.spider.mixed.zhihu.strategy;

import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.zhihu.ZhihuConst;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
