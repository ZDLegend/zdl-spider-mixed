package com.zdl.spider.mixed.zhihu.analysis.spouse;

import com.zdl.spider.mixed.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 回答内容解析
 * <p>
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
        if (spouseEntity.getGender() == null || spouseEntity.getGender() == 2) {
            if (sem.contains("本人女") || sem.startsWith("女")
                    || sem.contains("性别女") || sem.contains("：女")
                    || sem.contains("，女") || sem.contains("女，")) {
                spouseEntity.setGender(0);
            } else if (sem.contains("本人男") || sem.startsWith("男")
                    || sem.contains("性别男") || sem.contains("：男")
                    || sem.contains("，男") || sem.contains("男，")) {
                spouseEntity.setGender(1);
            }
        }
    }

    private static void analysisAge(String sem, SpouseEntity spouseEntity) {
        if (spouseEntity.getAge() == null) {
            List<Integer> list = StringUtil.getDigit(sem);
            if (sem.contains("年")) {
                List<Integer> s1 = list.stream().filter(i -> i >= 70 && i <= 99).collect(Collectors.toList());
                List<Integer> s2 = list.stream().filter(i -> i >= 1970 && i <= 2019).collect(Collectors.toList());
                if (s1.isEmpty() && s2.isEmpty()) {
                    if (list.contains(0)) {
                        spouseEntity.setAge(LocalDate.now().getYear() - 2000);
                    } else if (list.contains(1)) {
                        spouseEntity.setAge(LocalDate.now().getYear() - 2001);
                    }
                } else if (s2.isEmpty()) {
                    spouseEntity.setAge(LocalDate.now().getYear() - (s1.get(0) + 1900));
                } else {
                    spouseEntity.setAge(LocalDate.now().getYear() - (s1.get(0)));
                }
            }
        }
    }

    private static void analysisHigh(String sem, SpouseEntity spouseEntity) {
        if (spouseEntity.getHigh() == null) {
            List<Integer> list = StringUtil.getDigit(sem)
                    .stream()
                    .filter(i -> i >= 140 && i <= 199)
                    .collect(Collectors.toList());
            if (!list.isEmpty() && (sem.contains("身高") || sem.contains("高") || sem.contains("/"))) {
                spouseEntity.setHigh(list.get(0));
            }
        }
    }

    private static void analysisWeigh(String sem, SpouseEntity spouseEntity) {
        if (spouseEntity.getWeight() == null) {
            List<Integer> list = StringUtil.getDigit(sem)
                    .stream()
                    .filter(i -> i >= 70 && i <= 200)
                    .collect(Collectors.toList());
            if (!list.isEmpty()) {
                spouseEntity.setWeight(list.get(list.size() - 1));
            }
        }
    }

    private static void analysisEarth(String sem, SpouseEntity spouseEntity) {

    }

    private static void analysisEducation(String sem, SpouseEntity spouseEntity) {
        if (spouseEntity.getEducation() == null) {
            if (sem.contains("985") || sem.contains("某top")) {
                spouseEntity.setIs985(true);
                spouseEntity.setIs211(true);
            } else if (sem.contains("211")) {
                spouseEntity.setIs985(false);
                spouseEntity.setIs211(true);
            } else {
                spouseEntity.setIs985(false);
                spouseEntity.setIs211(false);
            }

            if (sem.contains("本科")) {
                spouseEntity.setEducation("本科");
            } else if (sem.contains("大专")) {
                spouseEntity.setEducation("大专");
            } else if (sem.contains("中专")) {
                spouseEntity.setEducation("中专");
            } else if (sem.contains("硕") || sem.contains("研究生") || sem.contains("读研")) {
                spouseEntity.setEducation("硕士");
            } else if (sem.contains("博士") || sem.contains("硕博") || sem.contains("读博")) {
                spouseEntity.setEducation("博士");
            } else {
                spouseEntity.setEducation("高中或高中以下");
            }
        }
    }
}
