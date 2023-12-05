package com.bizconnect.adapter.out.persistence;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AGENCY_TEST")
@Data
public class AgencyJpaEntity {

	@Id
	@Column(name = "MALL_ID")
	private String mallId;
	@Column(name = "AGENCY_ID")
	private String agencyId;

	@Column(name = "CLIENT_ID")
	private String clientId;
	@Column(name = "COMPANY_NAME")
	private String companyName;
	@Column(name = "BUSINESS_TYPE")
	private String businessType;
	@Column(name = "BIZ_NUMBER")
	private String bizNumber;
	@Column(name = "CEO_NAME")
	private String ceoName;
	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "COMPANY_SITE")
	private String companySite;
	@Column(name = "EMAIL")
	private String email;

	@Column(name = "SETTLE_MANAGER_NAME")
	private String settleManagerName;
	@Column(name = "SETTLE_MANAGER_PHONE_NUMBER")
	private String settleManagerPhoneNumber;
	@Column(name = "SETTLE_MANAGER_EMAIL")
	private String settleManagerEmail;


}
