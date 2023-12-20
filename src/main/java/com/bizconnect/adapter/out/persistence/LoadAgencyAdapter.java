package com.bizconnect.adapter.out.persistence;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.out.persistence.enums.EnumResultCode;
import com.bizconnect.adapter.out.persistence.repository.AgencyRepository;
import com.bizconnect.adapter.out.persistence.exceptions.DuplicateMemberException;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.port.out.LoadAgencyDataPort;
import com.bizconnect.application.port.out.SaveAgencyDataPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoadAgencyAdapter implements LoadAgencyDataPort, SaveAgencyDataPort {
    private final AgencyRepository agencyRepository;

    public LoadAgencyAdapter(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Override
    public void checkAgency(Agency agency) {
        AgencyJpaEntity entity = agencyConvertToEntity(agency);

        Optional<AgencyJpaEntity> foundAgency = agencyRepository.findByAgencyIdAndMallId(entity.getAgencyId(), entity.getMallId());
        Optional<AgencyJpaEntity> foundMallId = agencyRepository.findByMallId(entity.getMallId());
        if (foundMallId.isPresent()) {
            throw new DuplicateMemberException(EnumResultCode.DuplicateMember, entity.getMallId());
        }
        // Entity를 도메인 객체로 변환
        foundAgency.map(this::convertToAgency);
    }

    @Override
    public void registerAgency(Agency agency, Client client, SettleManager settleManager) {
        convertToEntity(agency, client, settleManager);
    }

    @Override
    public Optional<ClientDataModel> getAgencyInfo(Agency agency, Client client, SettleManager settleManager) {
        AgencyJpaEntity entity = convertToEntity(agency, client, settleManager);
        Optional<AgencyJpaEntity> foundAgency = agencyRepository.findByAgencyIdAndMallId(entity.getAgencyId(), entity.getMallId());
        return foundAgency.map(this::convertToClientDomain);
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

    private Agency convertToAgency(AgencyJpaEntity entity) {
        // AgencyJpaEntity를 Agency로 변환하는 로직
        return new Agency(entity.getAgencyId(), entity.getMallId());
    }

    private ClientDataModel convertToClientDomain(AgencyJpaEntity entity) {

        ClientDataModel clientDataModel = new ClientDataModel();

        clientDataModel.setAgencyId(entity.getAgencyId());
        clientDataModel.setMallId(entity.getMallId());

        clientDataModel.setClientId(entity.getClientId());
        clientDataModel.setCompanyName(entity.getCompanyName());
        clientDataModel.setBusinessType(entity.getBusinessType());
        clientDataModel.setBizNumber(entity.getBizNumber());
        clientDataModel.setCeoName(entity.getCeoName());
        clientDataModel.setPhoneNumber(entity.getPhoneNumber());
        clientDataModel.setAddress(entity.getAddress());
        clientDataModel.setCompanySite(entity.getCompanySite());
        clientDataModel.setEmail(entity.getEmail());

        clientDataModel.setSettleManagerName(entity.getSettleManagerName());
        clientDataModel.setSettleManagerPhoneNumber(entity.getSettleManagerPhoneNumber());
        clientDataModel.setSettleManagerEmail(entity.getSettleManagerEmail());

        return clientDataModel;
    }
}
