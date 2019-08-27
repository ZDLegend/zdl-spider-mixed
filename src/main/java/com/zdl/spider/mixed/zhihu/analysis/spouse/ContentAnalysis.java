package com.zdl.spider.mixed.zhihu.analysis.spouse;

import com.zdl.spider.mixed.utils.JsoupUtil;
import com.zdl.spider.mixed.utils.StringUtil;
import com.zdl.spider.mixed.zhihu.dto.AnswerDto;
import com.zdl.spider.mixed.zhihu.dto.AuthorDto;
import com.zdl.spider.mixed.zhihu.parser.QuestionParser;
import com.zdl.spider.mixed.zhihu.parser.ZhihuApi;
import com.zdl.spider.mixed.zhihu.web.entity.AnswerEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

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

    public static void main(String[] args) {
        List<SpouseEntity> list = QuestionParser.getInstance()
                .execute("275359100", 0, 10)
                .thenApply(z -> z.contents().stream().map(AnswerDto::toEntity).map(ContentAnalysis::getSpouse).collect(Collectors.toList())).join();

        System.out.println(list);
    }

    /**
     * 通过回答解析条件类
     */
    public static SpouseEntity getSpouse(AnswerEntity answerEntity) {
        SpouseEntity spouseEntity = new SpouseEntity();
        spouseEntity.setId(answerEntity.getQuestionId() + "_" + answerEntity.getId());
        spouseEntity.setAuthorId(answerEntity.getAuthorId());

        if (StringUtils.isNotBlank(answerEntity.getAuthorId())) {
            AuthorDto authorDto = ZhihuApi.getPeopleInfo(answerEntity.getAuthorId()).join();
            if (authorDto != null && StringUtils.isNotBlank(authorDto.getGender())) {
                spouseEntity.setGender(Integer.valueOf(authorDto.getGender()));
            }
        }

        analysis(answerEntity.getContent(), spouseEntity);

        return spouseEntity;
    }

    /**
     * 判断是否有效
     *
     * @param spouseEntity
     * @return
     */
    public boolean isValid(SpouseEntity spouseEntity) {
        return spouseEntity.getAge() != null
                && spouseEntity.getGender() != null
                && spouseEntity.getHigh() != null
                && spouseEntity.getWeight() != null;
    }

    /**
     * 回答内容解析
     *
     * @param content
     * @param spouseEntity
     * @return
     */
    public static SpouseEntity analysis(String content, SpouseEntity spouseEntity) {

        List<String> image = JsoupUtil.getImageAddrByHtml(content);
        spouseEntity.setHasPic(image.size() > 0);

        //将回答内容按段落分开
        List<String> list = Stream.of(content.split("<p>"))
                .map(s -> s.replace("</p>", ""))
                .flatMap(s -> Stream.of(s.split("\n")))
                .flatMap(s -> Stream.of(s.split("。")))
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        //通过关键字，一段一段解析
        list.forEach(s -> {

            if (s.contains("要求：") || s.contains("希望你：") || s.contains("要求，") || s.contains("希望你，")) {
                return;
            }

            analysisGender(s, spouseEntity);

            List<Integer> i = StringUtil.getDigit(s);

            analysisAge(s, spouseEntity, i);
            analysisHigh(s, spouseEntity, i);
            analysisWeigh(s, spouseEntity, i);
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
                    || sem.contains("，女") || sem.contains("女，")
                    || sem.contains("男朋友")) {
                spouseEntity.setGender(0);
            } else if (sem.contains("本人男") || sem.startsWith("男")
                    || sem.contains("性别男") || sem.contains("：男")
                    || sem.contains("，男") || sem.contains("男，")
                    || sem.contains("女朋友")) {
                spouseEntity.setGender(1);
            }
        }
    }

    private static void analysisAge(String sem, SpouseEntity spouseEntity, List<Integer> list) {
        if (spouseEntity.getAge() == null && sem.contains("年")) {
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
                spouseEntity.setAge(LocalDate.now().getYear() - s2.get(0));
            }
        } else if (spouseEntity.getAge() == null && sem.contains("岁")) {
            List<Integer> s = list.stream().filter(i -> i >= 18 && i <= 40).collect(Collectors.toList());
            if (!s.isEmpty()) {
                spouseEntity.setAge(s.get(0));
            }
        }
    }

    private static void analysisHigh(String sem, SpouseEntity spouseEntity, List<Integer> list) {
        if (spouseEntity.getHigh() == null) {
            List<Integer> s = list.stream()
                    .filter(i -> i >= 140 && i <= 199)
                    .collect(Collectors.toList());
            if (!s.isEmpty() && (sem.contains("身高") || sem.contains("高") || sem.contains("/")
                    || sem.contains("cm") || sem.contains("CM"))) {
                spouseEntity.setHigh(s.get(0));
            }
        }
    }

    private static void analysisWeigh(String sem, SpouseEntity spouseEntity, List<Integer> list) {
        if (spouseEntity.getWeight() == null && (sem.contains("体重") || sem.contains("kg") || sem.contains("KG")
                || sem.contains("g") || sem.contains("G") || sem.contains("/"))) {
            if (sem.contains("kg") || sem.contains("KG")) {
                List<Integer> s = list
                        .stream()
                        .filter(i -> i >= 35 && i <= 100)
                        .collect(Collectors.toList());
                if (!s.isEmpty()) {
                    spouseEntity.setWeight((s.get(s.size() - 1)) * 2);
                }
            } else {
                List<Integer> s = list
                        .stream()
                        .filter(i -> i >= 40 && i <= 200)
                        .collect(Collectors.toList());
                if (!s.isEmpty()) {
                    int w = s.get(s.size() - 1);
                    if (w < 60) {
                        w = w * 2;
                    }
                    spouseEntity.setWeight(w);
                }
            }
        }
    }

    private static void analysisEarth(String sem, SpouseEntity spouseEntity) {
        if (spouseEntity.getCity() == null || spouseEntity.getProvince() == null) {
            Pair<String, String> pair = PcParser.getEarth(sem);
            spouseEntity.setCity(pair.getRight());
            spouseEntity.setProvince(pair.getLeft());

        }
    }

    private static void analysisEducation(String sem, SpouseEntity spouseEntity) {
        if (spouseEntity.getEducation() == null) {
            if (sem.contains("本科") || sem.contains("专升本")) {
                spouseEntity.setEducation("本科");
            } else if (sem.contains("大专")) {
                spouseEntity.setEducation("大专");
                spouseEntity.setIs985(false);
                spouseEntity.setIs211(false);
            } else if (sem.contains("中专")) {
                spouseEntity.setEducation("中专");
                spouseEntity.setIs985(false);
                spouseEntity.setIs211(false);
            } else if (sem.contains("硕") || sem.contains("研究生") || sem.contains("读研")) {
                spouseEntity.setEducation("硕士");
            } else if (sem.contains("博士") || sem.contains("硕博") || sem.contains("读博")) {
                spouseEntity.setEducation("博士");
            }
        }

        if (spouseEntity.getIs985() == null) {
            if (sem.contains("985") || sem.contains("某top")) {
                spouseEntity.setIs985(true);
                spouseEntity.setIs211(true);
                if (spouseEntity.getEducation() == null) {
                    spouseEntity.setEducation("本科");
                }
            } else if (sem.contains("211")) {
                spouseEntity.setIs985(false);
                spouseEntity.setIs211(true);
                if (spouseEntity.getEducation() == null) {
                    spouseEntity.setEducation("本科");
                }
            }
        }
    }
}
