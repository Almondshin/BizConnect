package com.bizconnect.application.port.out;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.RegistrationDTO;

public interface AgencyDataPort {
    void checkAgency(Agency agency);
    void registerAgency(RegistrationDTO registrationDTO);
}

