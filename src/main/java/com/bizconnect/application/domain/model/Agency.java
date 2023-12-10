package com.bizconnect.application.domain.model;

import com.bizconnect.application.domain.exceptions.IllegalAgencyIdMallIdException;
import lombok.Getter;

@Getter
public class Agency {
    private String agencyId;
    private String mallId;
    private static final String REGEX = "^[a-zA-Z0-9]+$";

    public Agency(String agencyId, String mallId) {
        if (!isValidAgencyId(agencyId) || !isValidMallId(mallId)) {
            try {
//                throw new IllegalAgencyIdMallIdException("Invalid agencyId or mallId: Data format is not correct.");
                throw new IllegalAgencyIdMallIdException("데이터 형식이 올바르지 않습니다.");
            } catch (IllegalAgencyIdMallIdException e) {
                throw new RuntimeException(e);
            }
        }
        this.agencyId = agencyId;
        this.mallId = mallId;
    }

    private boolean isValidAgencyId(String agencyId) {
        return agencyId != null && !agencyId.isEmpty() && agencyId.matches(REGEX);
    }

    private boolean isValidMallId(String mallId) {
        return mallId != null && !mallId.isEmpty() && mallId.matches(REGEX);
    }

}
