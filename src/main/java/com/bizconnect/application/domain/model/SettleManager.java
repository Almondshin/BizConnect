package com.bizconnect.application.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SettleManager {
    /* 정산 담당자*/
    private final String settleManagerName;
    private final String settleManagerPhoneNumber;
    private final String settleManagerEmail;

    public SettleManager(String settleManagerName, String settleManagerPhoneNumber, String settleManagerEmail) {
        this.settleManagerName = settleManagerName;
        this.settleManagerPhoneNumber = settleManagerPhoneNumber;
        this.settleManagerEmail = settleManagerEmail;
    }
}
