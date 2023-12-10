package com.bizconnect.adapter.out.persistence;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.out.persistence.repository.AgencyRepository;
import com.bizconnect.application.domain.exceptions.CheckedMallIdException;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
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
    public void registerAgency(Agency agency, Client client, SettleManager settleManager) {
        AgencyJpaEntity entity = convertToEntity(agency, client, settleManager);
        agencyRepository.save(entity);
    }

    private AgencyJpaEntity agencyConvertToEntity(Agency agency) {
        AgencyJpaEntity entity = new AgencyJpaEntity();
        entity.setAgencyId(agency.getAgencyId());
        entity.setMallId(agency.getMallId());
        return entity;
    }

    private AgencyJpaEntity convertToEntity(Agency agency, Client client, SettleManager settleManager) {
        AgencyJpaEntity agencyJpaEntity = new AgencyJpaEntity();

        agencyJpaEntity.setAgencyId(agency.getAgencyId());
        agencyJpaEntity.setMallId(agency.getMallId());

        agencyJpaEntity.setClientId(client.getClientId());
        agencyJpaEntity.setCompanyName(client.getCompanyName());
        agencyJpaEntity.setBusinessType(client.getBusinessType());
        agencyJpaEntity.setBizNumber(client.getBizNumber());
        agencyJpaEntity.setCeoName(client.getCeoName());
        agencyJpaEntity.setPhoneNumber(client.getPhoneNumber());
        agencyJpaEntity.setAddress(client.getAddress());
        agencyJpaEntity.setCompanySite(client.getCompanySite());
        agencyJpaEntity.setEmail(client.getEmail());

        agencyJpaEntity.setSettleManagerName(settleManager.getSettleManagerName());
        agencyJpaEntity.setSettleManagerPhoneNumber(settleManager.getSettleManagerPhoneNumber());
        agencyJpaEntity.setSettleManagerEmail(settleManager.getSettleManagerEmail());

        return agencyJpaEntity;
    }

    private Agency convertToDomain(AgencyJpaEntity entity) {
        // AgencyJpaEntity를 Agency로 변환하는 로직
        return new Agency(entity.getAgencyId(), entity.getMallId());
    }
}
