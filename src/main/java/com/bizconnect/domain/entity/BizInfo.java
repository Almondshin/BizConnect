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
@Table(name = "BIZ_INFO")
public class BizInfo {

    @Id
    @Column(name = "BIZ_ID")
    private String bizId;

    @Column(name = "MALL_ID")
    private String mallId;

    @Column(name = "CEO_NAME")
    private String ceoName;

    @Column(name = "BIZ_NUMBER")
    private String bizNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ADDRESS")
    private String address;

}
