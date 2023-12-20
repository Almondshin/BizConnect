package com.bizconnect.adapter.out.persistence;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "AGENCY_TEST")
@Data
public class AgencyJpaEntity {

	@Id
	@Column(name = "MALL_ID")
	private String mallId;

	@NotBlank
	@Column(name = "AGENCY_ID", nullable = false)
	private String agencyId;

	@NotBlank
	@Column(name = "CLIENT_ID", nullable = false)
	private String clientId;
	@NotBlank
	@Column(name = "COMPANY_NAME", nullable = false)
	private String companyName;
	@NotBlank
	@Column(name = "BUSINESS_TYPE", nullable = false)
	private String businessType;
	@NotBlank
	@Column(name = "BIZ_NUMBER", nullable = false)
	private String bizNumber;
	@NotBlank
	@Column(name = "CEO_NAME", nullable = false)
	private String ceoName;
	@NotBlank
	@Column(name = "PHONE_NUMBER", nullable = false)
	private String phoneNumber;
	@NotBlank
	@Column(name = "ADDRESS", nullable = false)
	private String address;
	@NotBlank
	@Column(name = "COMPANY_SITE", nullable = false)
	private String companySite;
	@NotBlank
	@Column(name = "EMAIL", nullable = false)
	private String email;

	@Column(name = "SETTLE_MANAGER_NAME")
	private String settleManagerName;
	@Column(name = "SETTLE_MANAGER_PHONE_NUMBER")
	private String settleManagerPhoneNumber;
	@Column(name = "SETTLE_MANAGER_EMAIL")
	private String settleManagerEmail;
}
