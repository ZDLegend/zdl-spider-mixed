package com.zdl.spider.mixed.zhihu.analysis.spouse;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 回答内容解析
 *
 * Created by ZDLegend on 2019/6/5 11:23
 */
public class ContentAnalysis {

    public static SpouseEntity analysis(String content, SpouseEntity spouseEntity) {

        //将回答内容按段落分开
        List<String> list = Stream.of(content.split("<p>"))
                .map(s -> s.replace("</p>", ""))
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        //通过关键字，一段一段解析
        list.forEach(s -> {

        });

        spouseEntity.setContent(String.join("\n", list));

        return spouseEntity;
    }
}
