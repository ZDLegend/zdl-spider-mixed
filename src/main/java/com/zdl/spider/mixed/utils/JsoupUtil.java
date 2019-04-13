package com.zdl.spider.mixed.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ZDLegend on 2019/4/2 19:40
 */
public class JsoupUtil {

    /**
     * 获取html字符串中的所有图片地址
     */
    public static List<String> getImageAddrByHtml(String html) {
        Document doc = Jsoup.parseBodyFragment(html);
        Element body = doc.body();
        Elements getDiv = body.getElementsByTag("img");
        return getDiv.stream()
                .map(element -> element.attr("src"))
                .filter(s -> s.startsWith("http"))
                .collect(Collectors.toList());
    }
}
