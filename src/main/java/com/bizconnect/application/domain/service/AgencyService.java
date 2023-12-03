package com.bizconnect.application.domain.service;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.port.in.AgencyUseCase;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

@Service
public class AgencyService implements AgencyUseCase {
    @Override
    public boolean checkAgencyId(String agencyId, String mallId) {
        try {
            // Using the static method from Agency class for validation
            Agency agency = Agency.checkAgency(agencyId, mallId);
            // If no exception is thrown, agencyId is valid
            return true;
        } catch (IllegalArgumentException e) {
            // Handle the exception, maybe log it or take other actions
            // Return false indicating invalid agencyId
            return false;
        }
    }
}

