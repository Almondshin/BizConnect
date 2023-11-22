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
    /*
        이용기관 아이디 (Client ID) : clientId
        이용기관명 (Client Name) : clientName
        회사명 (Company Name) : companyName
        사업자구분 (Business Type) : businessType
        대표이사명 (CEO Name) : ceoName
        전화번호 (Phone Number) : phoneNumber
        주소 (Address) : address
        이메일 (Email) : email
        사이트 아이디 (Site ID) : siteId
        사이트명 (Site Name) : siteName
        이용기관 SKT, LGU 통신사 ID (Site Code) : siteCode
        이용기관 KT 통신사 ID (KT Site Code) : ktCpCd
        통신사 등록 URL (Telecom Site Url) : telcoSiteUrl
        사이트 도메인 PC (PC Site URL) : pcSiteUrl
        사이트 도메인 MOBILE (MOBILE Site URL) : mobileSiteUrl
        상품 타입 (Service Type) : serviceType
        정산 담당자명 (Settlement Manager Name) : settleManagerName
        정산 담당자 전화번호 (Settlement Manager Phone Number) : settleManagerPhoneNumber
        정산 담당자 이메일 (Settlement Manager Email) : settleManagerEmail
    */

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
    private String settleManagerPhoneNumber;
    private String settleManagerEmail;

}
