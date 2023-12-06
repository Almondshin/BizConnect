package com.bizconnect.application.domain.model;

import lombok.Getter;

@Getter
public class RegistrationRequest {
    private Agency agency;
    private Client client;
    private SettleManager settleManager;
}
