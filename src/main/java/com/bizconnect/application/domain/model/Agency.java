package com.bizconnect.application.domain.model;

import lombok.Getter;

@Getter
public class Agency {
    private String agencyId;
    private String mallId;
    private static final String regex = "^[a-zA-Z0-9]+$";

    public Agency(String agencyId, String mallId) {
        if (!isValidAgencyId(agencyId) || !isValidMallId(mallId)) {
            throw new IllegalArgumentException("Invalid agencyId or mallId: Data format is not correct.");
        }
        this.agencyId = agencyId;
        this.mallId = mallId;
    }

    private boolean isValidAgencyId(String agencyId) {
        return agencyId != null && !agencyId.isEmpty() && agencyId.matches(regex);
    }

    private boolean isValidMallId(String mallId) {
        return mallId != null && !mallId.isEmpty() && mallId.matches(regex);
    }

}
