package com.bizconnect.application.port.out;

import com.bizconnect.adapter.out.persistence.AgencyJpaEntity;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.RegistrationDTO;
import com.bizconnect.application.domain.model.SettleManager;

public interface AgencyDataPort {
    Agency checkAgency(Agency agency); // Agency 객체 반환 타입으로 변경

    RegistrationDTO registerAgency(RegistrationDTO registrationDTO);

}

