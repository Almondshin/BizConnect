package com.bizconnect.adapter.out.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Entity
@Table(name = "AGENCY_INFO_KEY")
public class AgencyInfoKeyJpaEntity {

  @Id
  @Column(name = "AGENCY_ID")
  private String agencyId;
  @Column(name = "SITE_NAME")
  private String siteName;
  @Column(name = "AGENCY_URL")
  private String agencyUrl;
  @Column(name = "AGENCY_KEY")
  private String agencyKey;
  @Column(name = "AGENCY_IV")
  private String agencyIv;
  @Column(name = "REG_DATE")
  private Date regDate;
  @Column(name = "MOD_DATE")
  private Date modDate;

}