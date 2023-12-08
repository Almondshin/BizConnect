package com.bizconnect.application.port.in;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.RegistrationDTO;

public interface AgencyUseCase {
    void registerAgency(RegistrationDTO registrationDTO); // 제휴사 등록
    void checkAgencyId(Agency agency);  // Agency 객체를 전달받는 방식으로 변경
}

