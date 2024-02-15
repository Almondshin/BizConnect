package com.bizconnect.adapter.out.persistence.adapter;

import com.bizconnect.adapter.out.persistence.entity.AgencyInfoKeyJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.AgencyInfoKeyRepository;
import com.bizconnect.application.domain.model.AgencyInfoKey;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.exceptions.UnregisteredAgencyException;
import com.bizconnect.application.port.out.load.LoadEncryptDataPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AgencyInfoKeyAdapter implements LoadEncryptDataPort {

    private final AgencyInfoKeyRepository agencyInfoKeyRepository;

    public AgencyInfoKeyAdapter( AgencyInfoKeyRepository agencyInfoKeyRepository) {
        this.agencyInfoKeyRepository = agencyInfoKeyRepository;
    }


    @Override
    @Transactional
    public Optional<AgencyInfoKey> getAgencyInfoKey(String agencyId) {
        Optional<AgencyInfoKeyJpaEntity> entity = agencyInfoKeyRepository.findByAgencyId(agencyId);
        if (entity.isEmpty()){
            throw new UnregisteredAgencyException(EnumResultCode.UnregisteredAgency,agencyId);
        }
        return entity.map(this::convertToAgencyInfoKeyDomain);
    }
    private AgencyInfoKey convertToAgencyInfoKeyDomain(AgencyInfoKeyJpaEntity entity){
        AgencyInfoKey agencyInfoKey = new AgencyInfoKey();
        agencyInfoKey.setAgencyId(entity.getAgencyId());
        agencyInfoKey.setSiteName(entity.getSiteName());
        agencyInfoKey.setAgencyProductType(entity.getAgencyProductType());
        agencyInfoKey.setAgencyUrl(entity.getAgencyUrl());
        agencyInfoKey.setAgencyKey(entity.getAgencyKey());
        agencyInfoKey.setAgencyIv(entity.getAgencyIv());
        return agencyInfoKey;
    }



}
