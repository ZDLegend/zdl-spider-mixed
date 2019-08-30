package com.zdl.spider.mixed.zhihu.analysis.spouse;

import com.zdl.spider.mixed.zhihu.dto.AnswerDto;
import com.zdl.spider.mixed.zhihu.parser.SearchParser;
import com.zdl.spider.mixed.zhihu.parser.ZhihuParser;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SpouseConst {

    public static Map<String, String> questionMap = new ConcurrentHashMap<>();


    /**
     * 通过搜索方式获取择偶标准相关的各个问题(从100个答案中过滤)
     */
    public static CompletableFuture<Void> getAllQuestionBySpouse() {
        return SearchParser.getInstance()
                .pagingParser("择偶标准是怎样的", 50, SpouseConst::cc)
                .whenComplete((aVoid, throwable) -> {
                    //无用问题过滤
                    questionMap.forEach((k, v) -> {
                        if(!v.contains("的你")) {
                            questionMap.remove(k);
                        }
                    });

                    questionMap.put("330598228", "有什么高端靠谱的相亲途径?");
                    questionMap.put("275359100", "你的择偶标准是怎样的");
                });
    }

    private static void cc(ZhihuParser<AnswerDto> parser) {
        parser.contents().forEach(answerDto -> {
            if (!questionMap.containsKey(answerDto.getQuestion().getId())) {
                questionMap.put(answerDto.getQuestion().getId(), answerDto.getQuestion().getName());
            }
        });
    }

    public static void main(String[] args) {
        getAllQuestionBySpouse().join();
        System.out.println(SpouseConst.questionMap);
    }
}
