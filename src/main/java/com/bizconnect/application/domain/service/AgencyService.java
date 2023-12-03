package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.out.persistence.AgencyRepository;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.port.in.AgencyUseCase;
import org.springframework.stereotype.Service;

@Service
public class AgencyService implements AgencyUseCase {

    private final AgencyRepository agencyRepository;

    public AgencyService(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
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
    public void checkAgencyId(Agency agency) {
        // Agency ID 유효성 검증 로직 구현
    }
}

