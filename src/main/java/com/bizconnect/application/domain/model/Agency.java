package com.bizconnect.application.domain.model;

import lombok.Getter;

@Getter
public class Agency {
    private String agencyId;
    private String mallId;

    public Agency(String agencyId, String mallId) {
        if (!isValidAgencyId(agencyId) || !isValidMallId(mallId)) {
            throw new IllegalArgumentException("Invalid agencyId or mallId: Data format is not correct.");
        }
        this.agencyId = agencyId;
        this.mallId = mallId;
    }

    private boolean isValidAgencyId(String agencyId) {
        String regex = "^[a-zA-Z0-9]+$";
        return agencyId != null && !agencyId.isEmpty() && agencyId.matches(regex);
    }

    private boolean isValidMallId(String mallId) {
        // 유효성 검증 로직 (mallId에 대한)
        return mallId != null && !mallId.isEmpty();
    }

}
