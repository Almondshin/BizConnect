package com.bizconnect.adapter.out.persistence;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.port.out.AgencyDataPort;
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.util.Optional;

@Service
public class AgencyAdapter implements AgencyDataPort {

    private final AgencyRepository agencyRepository;

    public AgencyAdapter(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Override
    public Agency checkAgency(Agency agency) {
        AgencyJpaEntity entity = convertToEntity(agency);
        Optional<AgencyJpaEntity> foundEntity = agencyRepository.findByAgencyIdAndMallId(entity.getAgencyId(), entity.getMallId());
        Optional<AgencyJpaEntity> foundMallId = agencyRepository.findByMallId(entity.getMallId());
        if(foundEntity.isEmpty() && foundMallId.isPresent()){
            try {
                throw new AlreadyBoundException("ID already registered!");
            } catch (AlreadyBoundException e) {
                throw new RuntimeException(e);
            }
        }

        // Entity를 도메인 객체로 변환
        return foundEntity.map(this::convertToDomain).orElse(null);
    }

    private AgencyJpaEntity convertToEntity(Agency agency) {
        // AgencyJpaEntity 변환 로직
        AgencyJpaEntity entity = new AgencyJpaEntity();
        entity.setAgencyId(agency.getAgencyId());
        entity.setMallId(agency.getMallId());
        // 추가 필드 매핑
        return entity;
    }

    private Agency convertToDomain(AgencyJpaEntity entity) {
        // AgencyJpaEntity를 Agency로 변환하는 로직
        return new Agency(entity.getAgencyId(), entity.getMallId());
        // 필요에 따라 추가 필드 매핑
    }
}
