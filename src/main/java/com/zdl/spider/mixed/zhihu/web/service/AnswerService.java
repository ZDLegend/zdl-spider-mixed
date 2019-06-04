package com.zdl.spider.mixed.zhihu.web.service;

import com.zdl.spider.mixed.zhihu.web.dao.AnswerDao;
import com.zdl.spider.mixed.zhihu.web.entity.AnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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

}
