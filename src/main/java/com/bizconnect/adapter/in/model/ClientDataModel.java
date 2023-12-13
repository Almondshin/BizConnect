package com.bizconnect.adapter.in.model;

import com.bizconnect.adapter.in.enums.EnumResultCode;
import com.bizconnect.adapter.in.exceptions.IllegalAgencyIdMallIdException;
import lombok.Getter;

@Getter
public class ClientDataModel {
    private String mallId;
    private String agencyId;

    private String clientId;
    private String companyName;
    private String businessType;
    private String bizNumber;
    private String ceoName;
    private String phoneNumber;
    private String address;
    private String companySite;
    private String email;

    private String settleManagerName;
    private String settleManagerPhoneNumber;
    private String settleManagerEmail;

    private static final String AGENCY_MALL_ID_PATTERN = "^[a-zA-Z0-9]+$";

    public ClientDataModel(String agencyId, String mallId) {
        if (!isValidAgencyId(agencyId) || !isValidMallId(mallId)) {
            throw new IllegalAgencyIdMallIdException(EnumResultCode.IllegalArgument, mallId);
        }
        this.mallId = mallId;
        this.agencyId = agencyId;
    }

    private boolean isValidAgencyId(String agencyId) {
        return agencyId != null && !agencyId.isEmpty() && agencyId.matches(AGENCY_MALL_ID_PATTERN);
    }

    private boolean isValidMallId(String mallId) {
        return mallId != null && !mallId.isEmpty() && mallId.matches(AGENCY_MALL_ID_PATTERN);
    }
}
