package com.bizconnect.application.port.out;

import com.bizconnect.application.domain.model.Agency;

public interface AgencyDataPort {
    void checkAgency(String agencyId, String mallId);
}
