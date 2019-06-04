package com.zdl.spider.mixed.zhihu.web.service;

import com.zdl.spider.mixed.zhihu.dto.AnswerDto;
import com.zdl.spider.mixed.zhihu.parser.QuestionParser;
import com.zdl.spider.mixed.zhihu.web.dao.AnswerDao;
import com.zdl.spider.mixed.zhihu.web.entity.AnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import static com.zdl.spider.mixed.zhihu.ZhihuConst.MAX_DEEP;

/**
 * Created by ZDLegend on 2019/6/4 10:06
 */
@Service
public class AnswerService extends AbstractService<AnswerEntity> {

    @Autowired
    private AnswerDao answerDao;

    @Override
    public JpaRepository<AnswerEntity, String> getDao() {
        return answerDao;
    }

    /**
     * 保存某个问题下所有回答进数据库
     *
     * @param questionId 问题id
     */
    public void saveAllAnswerByQuestion(String questionId) {
        QuestionParser.getInstance()
                .pagingParser(questionId, MAX_DEEP,
                        parser -> parser.contents()
                                .stream()
                                .map(AnswerDto::toEntity)
                                .forEach(this::insert)
                );
    }
}
