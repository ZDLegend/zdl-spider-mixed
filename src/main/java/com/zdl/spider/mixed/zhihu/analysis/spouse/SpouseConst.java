package com.zdl.spider.mixed.zhihu.analysis.spouse;

import com.zdl.spider.mixed.zhihu.parser.SearchParser;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SpouseConst {

    public static Map<String, String> questionMap = new ConcurrentHashMap<>();

    static {
        getAllQuestionBySpouse().join();

        //无用问题过滤
        questionMap.forEach((k, v) -> {
            if (v.contains("如何看待")) {
                questionMap.remove(k);
            }

            if (v.contains("基友们")) {
                questionMap.remove(k);
            }

            if (v.contains("为什么知乎上")) {
                questionMap.remove(k);
            }

            if (v.contains("如何根据")) {
                questionMap.remove(k);
            }

            if (v.contains("这样的择偶标准")) {
                questionMap.remove(k);
            }
        });

        questionMap.put("330598228", "有什么高端靠谱的相亲途径?");
    }


    /**
     * 通过搜索方式获取择偶标准相关的各个问题(从100个答案中过滤)
     */
    private static CompletableFuture<Void> getAllQuestionBySpouse() {
        return SearchParser.getInstance()
                .pagingParser("择偶标准是怎样的", 100, parser -> parser.contents().forEach(answerDto -> {
                    if (!questionMap.containsKey(answerDto.getQuestion().getId())) {
                        questionMap.put(answerDto.getQuestion().getId(), answerDto.getQuestion().getName());
                    }
                }));
    }

    public static void main(String[] args) {
        getAllQuestionBySpouse().join();
        System.out.println(questionMap);
    }
}
