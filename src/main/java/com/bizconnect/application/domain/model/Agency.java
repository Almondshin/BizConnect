package com.bizconnect.application.domain.model;

import lombok.Getter;

@Getter
public class Agency {
    /* 제휴사 정보 */
    private String agencyId;
    private String mallId;

    public Agency(String agencyId, String mallId) {
        if (!isValidAgencyId(agencyId)) {
            throw new IllegalArgumentException("Invalid agencyId: Data format is not correct.");
        }
        this.agencyId = agencyId;
        this.mallId = mallId;
    }

    private static boolean isValidAgencyId(String agencyId) {
        String regex = "^[a-zA-Z0-9]+$";
        return agencyId != null && !agencyId.isEmpty() && agencyId.matches(regex);
    }
}

