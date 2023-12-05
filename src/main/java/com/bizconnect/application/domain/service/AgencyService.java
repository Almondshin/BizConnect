package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.out.persistence.AgencyJpaEntity;
import com.bizconnect.application.domain.model.Agency;
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
    public void registerAgency(Agency agency) {
        // 제휴사 등록 로직 구현
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
    public Agency getAgencyDetails(String agencyId) {
        // 제휴사 상세 정보 조회 로직 구현
        return null;
    }

    @Override
    public Agency checkAgencyId(Agency agency) {
        return agencyDataPort.checkAgency(agency);
    }

}




