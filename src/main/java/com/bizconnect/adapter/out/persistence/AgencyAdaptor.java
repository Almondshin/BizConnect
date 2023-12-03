package com.bizconnect.adapter.out.persistence;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.port.out.AgencyDataPort;
import org.springframework.stereotype.Service;

@Service
public class AgencyAdaptor implements AgencyDataPort {
    private AgencyRepository agencyRepository;

    @Override
    public Agency checkAgency(Agency agency) {
        // DB 조회를 위해 Agency 객체 전달
        return agencyRepository.findByMallIdAndAgencyId(agency.getAgencyId(), agency.getMallId());
    }
}
