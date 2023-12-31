package com.bizconnect.adapter.out.persistence;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.out.persistence.entity.AgencyJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.AgencyRepository;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.enums.EnumSiteStatus;
import com.bizconnect.application.exceptions.exceptions.DuplicateMemberException;
import com.bizconnect.application.exceptions.exceptions.NullAgencyIdSiteIdException;
import com.bizconnect.application.port.out.LoadAgencyDataPort;
import com.bizconnect.application.port.out.SaveAgencyDataPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class AgencyAdapter implements LoadAgencyDataPort, SaveAgencyDataPort {
    private final AgencyRepository agencyRepository;

    public AgencyAdapter(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Override
    @Transactional
    public Optional<ClientDataModel> getAgencyInfo(Agency agency, Client client) {
        AgencyJpaEntity entity = agencyAndClientConvertToEntity(agency, client);
        Optional<AgencyJpaEntity> foundAgencyInfo = agencyRepository.findByAgencyIdAndSiteId(entity.getAgencyId(), entity.getSiteId());
        if (foundAgencyInfo.isEmpty()){
            throw new NullAgencyIdSiteIdException(EnumResultCode.UnregisteredAgency, agency.getSiteId());
        }
        return foundAgencyInfo.map(this::convertToClientDomain);
    }

    @Override
    @Transactional
    public void registerAgency(Agency agency, Client client, SettleManager settleManager) {
        AgencyJpaEntity entity = agencyAndClientConvertToEntity(agency, client);
        Optional<AgencyJpaEntity> foundSiteId = agencyRepository.findBySiteId(entity.getSiteId());
        // SiteId가 이미 존재하는 경우 DuplicateMemberException을 발생시킵니다.
        if (foundSiteId.isPresent()) {
            throw new DuplicateMemberException(EnumResultCode.DuplicateMember, entity.getSiteId());
        }
        client.setSiteStatus(EnumSiteStatus.PENDING.getCode());
        agencyRepository.save(convertToEntity(agency, client, settleManager));
    }


    @Override
    @Transactional
    public void updateAgency(Agency agency, Client client) {
        Optional<AgencyJpaEntity> optionalEntity = agencyRepository.findByAgencyIdAndSiteId(agency.getAgencyId(), agency.getSiteId());
        if (optionalEntity.isPresent()){
            AgencyJpaEntity entity = optionalEntity.get();
            System.out.println("entity : " + entity);
            agencyRepository.save(entity);
        } else {
            throw  new EntityNotFoundException("optionalEntity : " + agency.getAgencyId() +", "+  agency.getSiteId() + "인 엔터티를 찾을 수 없습니다.");
        }
    }


    private AgencyJpaEntity agencyAndClientConvertToEntity(Agency agency, Client client) {
        AgencyJpaEntity entity = new AgencyJpaEntity();
        entity.setAgencyId(agency.getAgencyId());
        entity.setSiteId(agency.getSiteId());
        entity.setRateSel(client.getRateSel());
        entity.setStartDate(client.getStartDate());
        entity.setEndDate(client.getEndDate());
        return entity;
    }

    private AgencyJpaEntity convertToEntity(Agency agency, Client client, SettleManager settleManager) {
        AgencyJpaEntity agencyJpaEntity = new AgencyJpaEntity();


        agencyJpaEntity.setAgencyId(agency.getAgencyId());
        agencyJpaEntity.setSiteId(agency.getSiteId());

        agencyJpaEntity.setSiteName(client.getSiteName());
        agencyJpaEntity.setCompanyName(client.getCompanyName());
        agencyJpaEntity.setBusinessType(client.getBusinessType());
        agencyJpaEntity.setBizNumber(client.getBizNumber());
        agencyJpaEntity.setCeoName(client.getCeoName());
        agencyJpaEntity.setPhoneNumber(client.getPhoneNumber());
        agencyJpaEntity.setAddress(client.getAddress());
        agencyJpaEntity.setCompanySite(client.getCompanySite());
        agencyJpaEntity.setEmail(client.getEmail());
        agencyJpaEntity.setRateSel(client.getRateSel());
        agencyJpaEntity.setSiteStatus(client.getSiteStatus());
        agencyJpaEntity.setStartDate(client.getStartDate());
        agencyJpaEntity.setEndDate(client.getEndDate());

        agencyJpaEntity.setSettleManagerName(settleManager.getSettleManagerName());
        agencyJpaEntity.setSettleManagerPhoneNumber(settleManager.getSettleManagerPhoneNumber());
        agencyJpaEntity.setSettleManagerEmail(settleManager.getSettleManagerEmail());

        return agencyJpaEntity;
    }

    private ClientDataModel convertToClientDomain(AgencyJpaEntity entity) {
        ClientDataModel clientDataModel = new ClientDataModel();
        SimpleDateFormat
                sdf = new SimpleDateFormat("yyyy-MM-dd");

        clientDataModel.setAgencyId(entity.getAgencyId());
        clientDataModel.setSiteId(entity.getSiteId());

        clientDataModel.setSiteName(entity.getSiteName());
        clientDataModel.setCompanyName(entity.getCompanyName());
        clientDataModel.setBusinessType(entity.getBusinessType());
        clientDataModel.setBizNumber(entity.getBizNumber());
        clientDataModel.setCeoName(entity.getCeoName());
        clientDataModel.setPhoneNumber(entity.getPhoneNumber());
        clientDataModel.setAddress(entity.getAddress());
        clientDataModel.setCompanySite(entity.getCompanySite());
        clientDataModel.setEmail(entity.getEmail());
        clientDataModel.setRateSel(entity.getRateSel());
        clientDataModel.setSiteStatus(entity.getSiteStatus());
        clientDataModel.setStartDate(entity.getStartDate());
        clientDataModel.setEndDate(entity.getEndDate());

        clientDataModel.setSettleManagerName(entity.getSettleManagerName());
        clientDataModel.setSettleManagerPhoneNumber(entity.getSettleManagerPhoneNumber());
        clientDataModel.setSettleManagerEmail(entity.getSettleManagerEmail());

        return clientDataModel;
    }
}
