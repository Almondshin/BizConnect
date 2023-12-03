package com.bizconnect.adapter.out.persistence;

import com.bizconnect.application.port.out.AgencyDataPort;
import org.springframework.stereotype.Service;

@Service
public class AgencyAdaptor implements AgencyDataPort {
    private AgencyRepository agencyRepository;

    @Override
    public void checkAgency(String agencyId, String mallId) {
        agencyRepository.findByMallIdAndAgencyId(agencyId,mallId);
    }
}
