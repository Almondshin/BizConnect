package com.bizconnect.adapter.out.persistence;

import com.bizconnect.adapter.out.persistence.repository.AgencyRepository;
import com.bizconnect.application.domain.exceptions.CheckedMallIdException;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.RegistrationDTO;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.port.out.AgencyDataPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AgencyAdapter implements AgencyDataPort {
    private final AgencyRepository agencyRepository;

    public AgencyAdapter(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Override
    public void checkAgency(Agency agency) {
        AgencyJpaEntity entity = agencyConvertToEntity(agency);

        Optional<AgencyJpaEntity> foundAgency = agencyRepository.findByAgencyIdAndMallId(entity.getAgencyId(), entity.getMallId());
        Optional<AgencyJpaEntity> foundMallId = agencyRepository.findByMallId(entity.getMallId());
        if(foundMallId.isPresent()){
            throw new CheckedMallIdException("이미 등록된 ID입니다.");
        }
        // Entity를 도메인 객체로 변환
        foundAgency.map(this::convertToDomain);
    }

    @Override
    public void registerAgency(RegistrationDTO registrationDTO) {
        AgencyJpaEntity entity = convertToEntity(registrationDTO);
        agencyRepository.save(entity);
    }

    private AgencyJpaEntity agencyConvertToEntity(Agency agency) {
        AgencyJpaEntity entity = new AgencyJpaEntity();
        entity.setAgencyId(agency.getAgencyId());
        entity.setMallId(agency.getMallId());
        return entity;
    }

    private AgencyJpaEntity convertToEntity(RegistrationDTO registrationRequest) {
        // AgencyJpaEntity 변환 로직
        Agency agency = registrationRequest.getAgency();
        Client client = registrationRequest.getClient();
        SettleManager settleManager = registrationRequest.getSettleManager();

        AgencyJpaEntity entity = new AgencyJpaEntity();
        entity.setAgencyId(agency.getAgencyId());
        entity.setMallId(agency.getMallId());

        entity.setClientId(client.getClientId());
        entity.setCompanyName(client.getCompanyName());
        entity.setBusinessType(client.getBusinessType());
        entity.setBizNumber(client.getBizNumber());
        entity.setCeoName(client.getCeoName());
        entity.setPhoneNumber(client.getPhoneNumber());
        entity.setAddress(client.getAddress());
        entity.setCompanySite(client.getCompanySite());
        entity.setEmail(client.getEmail());

        entity.setSettleManagerName(settleManager.getSettleManagerName());
        entity.setSettleManagerPhoneNumber(settleManager.getSettleManagerPhoneNumber());
        entity.setSettleManagerEmail(settleManager.getSettleManagerEmail());

        // 추가 필드 매핑
        return entity;
    }

    private Agency convertToDomain(AgencyJpaEntity entity) {
        // AgencyJpaEntity를 Agency로 변환하는 로직
        return new Agency(entity.getAgencyId(), entity.getMallId());
    }
}
