package com.zdl.spider.mixed.zhihu.web.dao;

import com.zdl.spider.mixed.zhihu.web.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ZDLegend on 2019/5/30 17:23
 */
public interface AnswerDao extends JpaRepository<AnswerEntity, String> {
}
