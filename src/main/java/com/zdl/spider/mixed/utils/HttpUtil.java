package com.zdl.spider.mixed.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.zdl.spider.mixed.utils.HttpConst.AGENT;
import static com.zdl.spider.mixed.utils.HttpConst.AGENT_CONTENT;

/**
 * http相关工具类
 * <p>
 * Created by ZDLegend on 2016/12/21.
 */
public final class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final int CONNECTION_TIME_OUT = 30000;

    private HttpUtil() {
    }

    /**
     * 文件下载
     * 使用了java11新特性：InputStream加强 / HTTP Client API
     *
     * @param url      网络文件url
     * @param filePath 文件路径
     */
    public static void downLoadFile(String url, String filePath) {
        var r = HttpRequest.newBuilder(URI.create(url)).build();
        HttpClient.newHttpClient()
                .sendAsync(r, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body)
                .thenAccept(is -> {
                    try (FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath))) {
                        is.transferTo(fileOutputStream);
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                })
                .exceptionally(e -> {
                    logger.error(e.getMessage(), e);
                    return null;
                });
    }

    public static JSONObject post(String url, String data) {

        var r = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofMillis(CONNECTION_TIME_OUT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();

        return HttpClient.newHttpClient()
                .sendAsync(r, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(JSONObject::parseObject)
                .exceptionally(e -> {
                    logger.error(e.getMessage(), e);
                    return null;
                })
                .join();
    }

    public static <U> CompletableFuture<U> get(String url, String[] headers, Function<String, U> apply) {

        var r = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofMillis(CONNECTION_TIME_OUT))
                .headers(headers)
                .build();

        return HttpClient.newHttpClient()
                .sendAsync(r, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(apply)
                .exceptionally(e -> {
                    logger.error(e.getMessage(), e);
                    return null;
                });
    }
}
