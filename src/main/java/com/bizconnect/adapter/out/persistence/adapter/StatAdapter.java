package com.bizconnect.adapter.out.persistence.adapter;

import com.bizconnect.adapter.out.persistence.entity.StatDayJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.StatDayRepository;
import com.bizconnect.application.domain.model.StatDay;
import com.bizconnect.application.port.out.load.LoadStatDataPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatAdapter implements LoadStatDataPort {

    private final StatDayRepository statDayRepository;

    public StatAdapter(StatDayRepository statDayRepository) {
        this.statDayRepository = statDayRepository;
    }


    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @Transactional
    public List<StatDay> findBySiteIdAndFromDate(String siteId, String toDate, String fromDate) {
        List<StatDayJpaEntity> entities = statDayRepository.findBySiteIdAndFromDate(siteId, toDate, fromDate);
        return entities.stream()
                .map(this::convertEntity)
                .collect(Collectors.toList());
    }


    private StatDay convertEntity(StatDayJpaEntity entity){
        StatDay statDay = new StatDay();
        statDay.setFromDate(entity.getFromDate());
        statDay.setMokClientId(entity.getMokClientId());
        statDay.setSiteId(entity.getSiteId());
        statDay.setProviderId(entity.getProviderId());
        statDay.setServiceType(entity.getServiceType());
        statDay.setReqCnt(entity.getReqCnt());
        statDay.setSuccessFinalCnt(entity.getSuccessFinalCnt());
        statDay.setRegDate(entity.getRegDate());
        statDay.setIncompleteCnt(entity.getIncompleteCnt());
        return statDay;
    }

//    public StatDayJpaEntity findBySiteIdAndFromDate(String siteId, String toDate, String fromDate) {
//        String stringToDate = sdf.format(toDate);
//        String stringFromDate = sdf.format(fromDate);
//
//        return
//    }
}
