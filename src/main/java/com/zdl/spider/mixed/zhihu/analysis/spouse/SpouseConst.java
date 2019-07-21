package com.zdl.spider.mixed.zhihu.analysis.spouse;

import com.zdl.spider.mixed.zhihu.parser.SearchParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SpouseConst {

    public static Map<String, String> questionMap = new HashMap<>();

    static {
        getAllQuestionBySpouse();
    }

    private static CompletableFuture<Void> getAllQuestionBySpouse() {
        return SearchParser.getInstance()
                .pagingParser("择偶标准是怎样的", 100, parser -> parser.contents().forEach(answerDto -> {
                    if(!questionMap.containsKey(answerDto.getQuestion().getId())) {
                        questionMap.put(answerDto.getQuestion().getId(), answerDto.getQuestion().getName());
                    }
                }));
    }

    public static void main(String[] args) {
        getAllQuestionBySpouse().join();
        System.out.println(questionMap);
    }
}
