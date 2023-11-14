package com.bizconnect.domain.entity;

import java.time.LocalDateTime;

public class Payment {

    /*
    결제번호
    결제수단
    결제금액
    결제시간
    ...
    */

    private String tradeNumber; //결제번호
    private String paymentMethod; //결제수단
    private String amount; // 결제금액
    private LocalDateTime tradeDate; //결제시간

}
