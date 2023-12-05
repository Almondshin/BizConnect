package com.bizconnect.adapter.out.persistence;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.port.out.AgencyDataPort;
import org.springframework.stereotype.Service;

@Service
public class AgencyAdapter implements AgencyDataPort {

    private final AgencyRepository agencyRepository;

    public AgencyAdapter(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Override
    public AgencyJpaEntity checkAgency(Agency agency) {
        AgencyJpaEntity entity = convertToEntity(agency);
        // 여기서 entity를 사용하여 데이터베이스 조회

        System.out.println("entity.getAgencyId() : " + entity.getAgencyId());
        System.out.println("entity.getMallId() : " + entity.getMallId());

        System.out.println("findByAgencyIdAndMallId : " + agencyRepository.findByAgencyIdAndMallId(entity.getAgencyId(), entity.getMallId()));
        return agencyRepository.findByAgencyIdAndMallId(entity.getAgencyId(), entity.getMallId());
    }

    private AgencyJpaEntity convertToEntity(Agency agency) {
        AgencyJpaEntity entity = new AgencyJpaEntity();
        entity.setAgencyId(agency.getAgencyId());
        entity.setMallId(agency.getMallId());
        // 추가 필드 매핑
        return entity;
    }
}
