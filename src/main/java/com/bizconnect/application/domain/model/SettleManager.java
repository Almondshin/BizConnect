package com.bizconnect.application.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SettleManager {
    /* 정산 담당자*/
    private final String settleManagerName;
    private final String settleManagerPhoneNumber;
    private final String settleManagerTelNumber;
    private final String settleManagerEmail;

    public SettleManager(String settleManagerName, String settleManagerPhoneNumber,String settleManagerTelNumber, String settleManagerEmail) {
        this.settleManagerName = settleManagerName;
        this.settleManagerPhoneNumber = settleManagerPhoneNumber;
        this.settleManagerTelNumber = settleManagerTelNumber;
        this.settleManagerEmail = settleManagerEmail;
    }
}
