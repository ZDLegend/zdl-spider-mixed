package com.zdl.spider.mixed.zhihu.web.controller;

import com.zdl.spider.mixed.zhihu.web.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ZDLegend on 2019/5/29 14:53
 */
@RestController
@RequestMapping("api/v1/zhihu")
public class ZhuHuController {

    @Autowired
    private AnswerService answerService;

    @PostMapping("question/{questionId}")
    public void saveAllAnswerByQuestion(@PathVariable String questionId) {
        answerService.saveAllAnswerByQuestion(questionId);
    }
}
