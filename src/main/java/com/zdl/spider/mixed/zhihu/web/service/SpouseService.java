package com.zdl.spider.mixed.zhihu.web.service;

import com.zdl.spider.mixed.zhihu.analysis.spouse.ContentAnalysis;
import com.zdl.spider.mixed.zhihu.analysis.spouse.PcParser;
import com.zdl.spider.mixed.zhihu.analysis.spouse.SpouseConst;
import com.zdl.spider.mixed.zhihu.analysis.spouse.SpouseEntity;
import com.zdl.spider.mixed.zhihu.dto.AnswerDto;
import com.zdl.spider.mixed.zhihu.parser.QuestionParser;
import com.zdl.spider.mixed.zhihu.web.dao.SpouseDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Created by ZDLegend on 2019/8/13 20:19
 */
@Service
public class SpouseService extends AbstractService<SpouseEntity> {

    Logger log = LoggerFactory.getLogger(SpouseService.class);

    @Autowired
    private SpouseDao spouseDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public void startAnalysis() {
        transactionTemplate.execute(call -> {
            SpouseConst.getAllQuestionBySpouse().whenComplete((vo, throwable) -> {
                if (throwable != null) {
                    log.error("getAllQuestionBySpouse error", throwable);
                    return;
                }

                SpouseConst.questionMap.forEach((k, v) -> {
                    Pair<String, String> pair = PcParser.getEarth(v);
                    QuestionParser.getInstance()
                            .pagingParser(k, -1,
                                    z -> z.contents().stream()
                                            .map(AnswerDto::toEntity)
                                            .map(ContentAnalysis::getSpouse)
                                            .peek(spouseEntity -> {
                                                if (StringUtils.isNotBlank(pair.getLeft())) {
                                                    spouseEntity.setProvince(pair.getLeft());
                                                }

                                                if (StringUtils.isNotBlank(pair.getRight())) {
                                                    spouseEntity.setCity(pair.getRight());
                                                }
                                            })
                                            .filter(ContentAnalysis::isValid)
                                            .forEach(this::insert)
                            )
                            .join();
                });
            });

            return true;
        });

    }

    @Override
    public JpaRepository<SpouseEntity, String> getDao() {
        return spouseDao;
    }
}
