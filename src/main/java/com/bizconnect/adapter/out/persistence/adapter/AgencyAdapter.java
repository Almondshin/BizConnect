package com.bizconnect.adapter.out.persistence.adapter;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.out.persistence.entity.AgencyJpaEntity;
import com.bizconnect.adapter.out.persistence.entity.SiteInfoJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.AgencyRepository;
import com.bizconnect.adapter.out.persistence.repository.SiteInfoRepository;
import com.bizconnect.application.domain.enums.EnumExtensionStatus;
import com.bizconnect.application.domain.enums.EnumPaymentStatus;
import com.bizconnect.application.domain.enums.EnumSiteStatus;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.domain.service.NotiService;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.exceptions.DuplicateMemberException;
import com.bizconnect.application.exceptions.exceptions.UnregisteredAgencyException;
import com.bizconnect.application.port.out.load.LoadAgencyDataPort;
import com.bizconnect.application.port.out.save.SaveAgencyDataPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class AgencyAdapter implements LoadAgencyDataPort, SaveAgencyDataPort {
    private final AgencyRepository agencyRepository;
    private final SiteInfoRepository siteInfoRepository;

    private final NotiService notiService;
    @Value("${external.admin.url}")
    private String profileSpecificAdminUrl;

    public AgencyAdapter(AgencyRepository agencyRepository, SiteInfoRepository siteInfoRepository, NotiService notiService) {
        this.agencyRepository = agencyRepository;
        this.siteInfoRepository = siteInfoRepository;
        this.notiService = notiService;
    }


    @Override
    @Transactional
    public Optional<ClientDataModel> getAgencyInfo(Agency agency, Client client) {
        AgencyJpaEntity entity = agencyAndClientConvertToEntity(agency, client);
        Optional<AgencyJpaEntity> foundAgencyInfo = agencyRepository.findByAgencyIdAndSiteId(entity.getAgencyId(), entity.getSiteId());
        if (foundAgencyInfo.isEmpty()) {
            throw new UnregisteredAgencyException(EnumResultCode.UnregisteredAgency, agency.getSiteId());
        }
        return foundAgencyInfo.map(this::convertClientModel);
    }


    @Override
    @Transactional
    public void registerAgency(Agency agency, Client client, SettleManager settleManager) {
        AgencyJpaEntity entity = agencyAndClientConvertToEntity(agency, client);
        Optional<SiteInfoJpaEntity> foundSiteIdBySiteInfo = siteInfoRepository.findBySiteId(entity.getSiteId());
        Optional<AgencyJpaEntity> foundSiteIdByAgencyInfo = agencyRepository.findBySiteId(entity.getSiteId());

        // SiteId가 이미 존재하는 경우 DuplicateMemberException을 발생시킵니다.
        if (foundSiteIdBySiteInfo.isPresent() || foundSiteIdByAgencyInfo.isPresent()) {
            throw new DuplicateMemberException(EnumResultCode.DuplicateMember, entity.getSiteId());
        }
        client.setSiteStatus(EnumSiteStatus.PENDING.getCode());
        client.setExtensionStatus(EnumExtensionStatus.DEFAULT.getCode());
        agencyRepository.save(convertToEntity(agency, client, settleManager));
    }

    @Override
    @Transactional
    public void updateAgency(Agency agency, Client client, String paymentStatus) {
        String siteId = agency.getAgencyId() + "-" + agency.getSiteId();
        Optional<AgencyJpaEntity> optionalEntity = agencyRepository.findByAgencyIdAndSiteId(agency.getAgencyId(), siteId);
        if (optionalEntity.isPresent()) {
            AgencyJpaEntity entity = optionalEntity.get();
            if (entity.getExtensionStatus().equals(EnumExtensionStatus.DEFAULT.getCode()) || client.getStartDate().equals(new Date())) {
                entity.setRateSel(client.getRateSel());
                entity.setStartDate(client.getStartDate());
                entity.setEndDate(client.getEndDate());
            }
            entity.setExtensionStatus(EnumExtensionStatus.NOT_EXTENDABLE.getCode());
        } else {
            throw new EntityNotFoundException("optionalEntity : " + agency.getAgencyId() + ", " + agency.getSiteId() + "인 엔터티를 찾을 수 없습니다.");
        }
    }


    private AgencyJpaEntity agencyAndClientConvertToEntity(Agency agency, Client client) {
        AgencyJpaEntity entity = new AgencyJpaEntity();
        String siteId = agency.getAgencyId() + "-" + agency.getSiteId();
        entity.setAgencyId(agency.getAgencyId());
        entity.setSiteId(siteId);
        entity.setRateSel(client.getRateSel());
        entity.setStartDate(client.getStartDate());
        entity.setEndDate(client.getEndDate());
        return entity;
    }

    private AgencyJpaEntity convertToEntity(Agency agency, Client client, SettleManager settleManager) {
        AgencyJpaEntity agencyJpaEntity = new AgencyJpaEntity();

        agencyJpaEntity.setAgencyId(agency.getAgencyId());
        String siteId = agency.getAgencyId() + "-" + agency.getSiteId();
        agencyJpaEntity.setSiteId(siteId);

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
        agencyJpaEntity.setExtensionStatus(client.getExtensionStatus());
        agencyJpaEntity.setStartDate(client.getStartDate());
        agencyJpaEntity.setEndDate(client.getEndDate());

        agencyJpaEntity.setServiceUseAgree(client.getServiceUseAgree());

        agencyJpaEntity.setSettleManagerName(settleManager.getSettleManagerName());
        agencyJpaEntity.setSettleManagerTelNumber(settleManager.getSettleManagerTelNumber());
        agencyJpaEntity.setSettleManagerPhoneNumber(settleManager.getSettleManagerPhoneNumber());
        agencyJpaEntity.setSettleManagerEmail(settleManager.getSettleManagerEmail());

        return agencyJpaEntity;
    }


    private ClientDataModel convertClientModel(AgencyJpaEntity entity) {
        ClientDataModel clientDataModel = new ClientDataModel();

        clientDataModel.setAgencyId(entity.getAgencyId());
        clientDataModel.setSiteId(entity.getSiteId().split("-")[1]);
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
        clientDataModel.setExtensionStatus(entity.getExtensionStatus());
        clientDataModel.setStartDate(entity.getStartDate());
        clientDataModel.setEndDate(entity.getEndDate());

        clientDataModel.setSettleManagerName(entity.getSettleManagerName());
        clientDataModel.setSettleManagerPhoneNumber(entity.getSettleManagerPhoneNumber());
        clientDataModel.setSettleManagerEmail(entity.getSettleManagerEmail());

        return clientDataModel;
    }


}
