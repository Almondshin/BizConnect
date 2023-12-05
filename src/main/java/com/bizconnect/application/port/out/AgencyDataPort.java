package com.bizconnect.application.port.out;

import com.bizconnect.adapter.out.persistence.AgencyJpaEntity;
import com.bizconnect.application.domain.model.Agency;

public interface AgencyDataPort {
    AgencyJpaEntity checkAgency(Agency agency); // Agency 객체 반환 타입으로 변경
}

