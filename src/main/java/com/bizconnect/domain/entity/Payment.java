package com.bizconnect.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Payment")
public class Payment {

    /*
    결제번호
    결제수단
    결제금액
    결제시간
    ...
    */
    @Id
    @Column(name = "TRADENUMBER")
    private String tradeNumber; //결제번호
    @Column(name = "PAYMENTMETHOD")
    private String paymentMethod; //결제수단
    @Column(name = "AMOUNT")
    private String amount; // 결제금액
    @Column(name = "TRADEDATE")
    private LocalDateTime tradeDate; //결제시간

}
