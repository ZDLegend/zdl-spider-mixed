package com.zdl.spider.mixed.zhihu.web.service;

import com.zdl.spider.mixed.zhihu.analysis.spouse.ContentAnalysis;
import com.zdl.spider.mixed.zhihu.analysis.spouse.PcParser;
import com.zdl.spider.mixed.zhihu.analysis.spouse.SpouseConst;
import com.zdl.spider.mixed.zhihu.analysis.spouse.SpouseEntity;
import com.zdl.spider.mixed.zhihu.dto.AnswerDto;
import com.zdl.spider.mixed.zhihu.parser.QuestionParser;
import com.zdl.spider.mixed.zhihu.web.dao.SpouseDao;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ZDLegend on 2019/8/13 20:19
 */
@Service
public class SpouseService extends AbstractService<SpouseEntity> {

    @Autowired
    private SpouseDao spouseDao;

    public void startAnalysis() {
        SpouseConst.questionMap.forEach((k, v) -> {
            Pair<String, String> pair = PcParser.getEarth(v);
            QuestionParser.getInstance()
                    .pagingParser(k, -1,
                            z -> z.contents().stream()
                                    .map(AnswerDto::toEntity)
                                    .map(ContentAnalysis::getSpouse)
                                    .peek(spouseEntity -> {
                                        spouseEntity.setCity(pair.getRight());
                                        spouseEntity.setProvince(pair.getLeft());
                                    })
                                    .forEach(this::insert)
                    )
                    .join();

        });
    }

    @Override
    public JpaRepository<SpouseEntity, String> getDao() {
        return spouseDao;
    }
}
