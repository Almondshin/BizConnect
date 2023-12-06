package com.bizconnect.adapter.out.persistence;

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
    public Agency checkAgency(Agency agency) {

        AgencyJpaEntity entity = convertToEntity(agency);

        Optional<AgencyJpaEntity> foundEntity = agencyRepository.findByAgencyIdAndMallId(entity.getAgencyId(), entity.getMallId());
        Optional<AgencyJpaEntity> foundMallId = agencyRepository.findByMallId(entity.getMallId());
        if(foundEntity.isEmpty() && foundMallId.isPresent()){
            try {
                throw new CheckedMallIdException("ID already registered!");
            } catch (CheckedMallIdException e) {
                throw new RuntimeException(e);
            }
        }

        // Entity를 도메인 객체로 변환
        return foundEntity.map(this::convertToDomain).orElse(null);
    }

    @Override
    public RegistrationDTO registerAgency(RegistrationDTO registrationDTO) {
        AgencyJpaEntity entity = convertToEntity(registrationDTO);
        agencyRepository.save(entity);
        return registrationDTO;
    }

    private AgencyJpaEntity convertToEntity(Agency agency) {
        // Agency 객체를 AgencyJpaEntity 객체로 변환하는 로직
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
        // 필요에 따라 추가 필드 매핑
    }
}
