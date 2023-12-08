package com.bizconnect.application.domain.service;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.RegistrationDTO;
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
        agencyDataPort.checkAgency(registrationDTO.getAgency());
        agencyDataPort.registerAgency(registrationDTO);
    }
    @Override
    public void checkAgencyId(Agency agency) {
        agencyDataPort.checkAgency(agency);
    }
}




