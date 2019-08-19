package com.zdl.spider.mixed.zhihu.web.service;

import com.zdl.spider.mixed.zhihu.analysis.spouse.SpouseEntity;
import com.zdl.spider.mixed.zhihu.web.dao.SpouseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by ZDLegend on 2019/8/13 20:19
 */
@Service
public class SpouseService extends AbstractService<SpouseEntity> {

    @Autowired
    private SpouseDao spouseDao;

    @Override
    public JpaRepository<SpouseEntity, String> getDao() {
        return spouseDao;
    }
}
