package com.bizconnect.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "Additional_BIZ_INFO")
public class AdditionalBizInfo {

    @Id
    private String clientId;
    private String clientName;
    private String companyName;
    private String businessType;
    private String bizNumber;
    private String ceoName;
    private String phoneNumber;
    private String address;
    private String email;
    private String siteId;
    private String siteName;
    private String siteCode;
    private String ktCpCd;
    private String telcoSiteUrl;
    private String pcSiteUrl;
    private String mobileSiteUrl;
    private String serviceType;

    // 정산담당자 정보 (현재 임시테이블에서만 사용)
    private String settleManagerName;
    private String settleManagerEmail;
    private String settleManagerPhoneNumber;

}
