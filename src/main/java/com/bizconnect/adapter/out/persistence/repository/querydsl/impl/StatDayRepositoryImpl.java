package com.bizconnect.adapter.out.persistence.repository.querydsl.impl;

import com.bizconnect.adapter.out.persistence.entity.QStatDayJpaEntity;
import com.bizconnect.adapter.out.persistence.entity.StatDayJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.querydsl.StatDayRepositoryQueryDSL;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatDayRepositoryImpl implements StatDayRepositoryQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;
    QStatDayJpaEntity qStatDayJpaEntity = QStatDayJpaEntity.statDayJpaEntity;

    @Override
    public List<StatDayJpaEntity> findBySiteIdAndFromDate(String siteId, String toDate, String fromDate) {
        return jpaQueryFactory.selectFrom(qStatDayJpaEntity)
                .where(qStatDayJpaEntity.siteId.eq(siteId)
                        .and(qStatDayJpaEntity.fromDate.goe(toDate))
                        .and(qStatDayJpaEntity.fromDate.loe(fromDate)))
                .fetch();
    }
}
