package com.bizconnect.application.port.out.load;

import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.PaymentHistory;

import java.util.List;
import java.util.Optional;

public interface LoadPaymentDataPort {
    List<PaymentHistory> getPaymentHistoryByAgency(Agency agency);
    Optional<PaymentHistory> getPaymentHistoryByTradeNum(String tradeNum);

}
