package com.zdl.spider.mixed.zhihu.web.dao;

import com.zdl.spider.mixed.zhihu.analysis.spouse.SpouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ZDLegend on 2019/8/13 20:17
 */
public interface SpouseDao extends JpaRepository<SpouseEntity, String> {
}
