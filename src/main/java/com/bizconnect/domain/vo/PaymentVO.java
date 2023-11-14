package com.bizconnect.domain.vo;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class PaymentVO {
    private String tradeNumber; //결제번호
    private String paymentMethod; //결제수단
    private String amount; // 결제금액
    private LocalDateTime tradeDate; //결제시간

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentVO payment = (PaymentVO) o;
        return Objects.equals(tradeNumber, payment.tradeNumber)
                && Objects.equals(paymentMethod, payment.paymentMethod)
                && Objects.equals(amount, payment.amount)
                && Objects.equals(tradeDate, payment.tradeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeNumber, paymentMethod, amount, tradeDate);
    }
}
