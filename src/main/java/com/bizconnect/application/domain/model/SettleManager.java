package com.bizconnect.application.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettleManager {
    /* 정산 담당자*/
    private String settleManagerName;
    private String settleManagerPhoneNumber;
    private String settleManagerEmail;
}
