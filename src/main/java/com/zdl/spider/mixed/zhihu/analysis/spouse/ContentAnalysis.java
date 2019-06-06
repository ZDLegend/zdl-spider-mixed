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
            analysisGender(s, spouseEntity);
            analysisAge(s, spouseEntity);
            analysisHigh(s, spouseEntity);
            analysisWeigh(s, spouseEntity);
            analysisEarth(s, spouseEntity);
            analysisEducation(s, spouseEntity);
        });

        spouseEntity.setContent(String.join("\n", list));

        return spouseEntity;
    }

    private static void analysisGender(String sem, SpouseEntity spouseEntity) {
        if(spouseEntity.getGender() == null || spouseEntity.getGender() == 2) {

            if(sem.contains("本人女")
                    || sem.startsWith("女")
                    || sem.contains("性别女")
                    || sem.contains("：女")
                    || sem.contains("，女")) {
                spouseEntity.setGender(0);
                return;
            }

            if(sem.contains("本人男") || sem.startsWith("男")
                    || sem.contains("性别男") || sem.contains("：男") || sem.contains("，男")) {
                spouseEntity.setGender(1);
            }
        }
    }

    private static void analysisAge(String sem, SpouseEntity spouseEntity) {

    }

    private static void analysisHigh(String sem, SpouseEntity spouseEntity) {

    }

    private static void analysisWeigh(String sem, SpouseEntity spouseEntity) {

    }

    private static void analysisEarth(String sem, SpouseEntity spouseEntity) {

    }

    private static void analysisEducation(String sem, SpouseEntity spouseEntity) {

    }
}
