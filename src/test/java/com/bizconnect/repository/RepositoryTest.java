package com.bizconnect.repository;

import com.bizconnect.adapter.out.persistence.entity.StatDayJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.StatDayRepository;
import com.bizconnect.application.domain.model.StatDay;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.LongStream;

@SpringBootTest(properties = "spring.profiles.active=local")
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
//@ActiveProfiles("local")
public class RepositoryTest {

    @Autowired
    StatDayRepository statDayRepository;

    @Test
    public void test() {
        String siteId = "testSiteId";
        String toDate = "20240101";
        String fromDate = "20240131";

        System.out.println(statDayRepository.findBySiteIdAndFromDate(siteId,toDate,fromDate));

        List<StatDayJpaEntity> statDayEntities = statDayRepository.findBySiteIdAndFromDate(siteId, toDate, fromDate);
        LongStream incompleteCounts = statDayEntities.stream().mapToLong(StatDayJpaEntity::getIncompleteCnt);
        long amount = incompleteCounts.sum();
        System.out.println(amount);
    }
}
