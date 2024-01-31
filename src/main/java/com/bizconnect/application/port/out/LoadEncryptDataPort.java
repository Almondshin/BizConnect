package com.bizconnect.application.port.out;

import com.bizconnect.application.domain.model.AgencyInfoKey;

import java.util.Optional;

public interface LoadEncryptDataPort {
    Optional<AgencyInfoKey> getAgencyInfoKey(String agencyId);

}
