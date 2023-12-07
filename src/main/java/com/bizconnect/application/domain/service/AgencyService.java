package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.out.persistence.AgencyJpaEntity;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.RegistrationDTO;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.out.AgencyDataPort;
import org.springframework.stereotype.Service;

@Service
public class AgencyService implements AgencyUseCase {

    private final AgencyDataPort agencyDataPort;

    public AgencyService(AgencyDataPort agencyDataPort) {
        this.agencyDataPort = agencyDataPort;
    }


    @Override
    public void registerAgency(RegistrationDTO registrationDTO) {
        agencyDataPort.registerAgency(registrationDTO);
    }

    @Override
    public void updateAgency(Agency agency) {
        // 제휴사 정보 업데이트 로직 구현
    }

    @Override
    public void deleteAgency(String agencyId) {
        // 제휴사 삭제 로직 구현
    }

    @Override
    public Client getMallIdDetails(String mallId) {
        return  agencyDataPort.getMallIdDetails(mallId);
    }


    @Override
    public Agency checkAgencyId(Agency agency) {
        return agencyDataPort.checkAgency(agency);
    }

}




