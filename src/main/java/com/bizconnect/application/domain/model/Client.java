package com.bizconnect.application.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Client {
    /* 이용기관 정보 */
    private String clientId;
    private String companyName;
    private String businessType;
    private String bizNumber;
    private String ceoName;
    private String phoneNumber;
    private String address;
    private String companySite;
    private String email;
}
