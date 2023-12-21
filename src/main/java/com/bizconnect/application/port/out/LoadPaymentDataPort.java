package com.bizconnect.application.port.out;

import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.application.domain.model.PaymentHistory;

import java.util.Optional;

public interface LoadPaymentDataPort {
    Optional<PaymentHistoryDataModel> getPaymentHistory(PaymentHistory paymentHistory);

}
