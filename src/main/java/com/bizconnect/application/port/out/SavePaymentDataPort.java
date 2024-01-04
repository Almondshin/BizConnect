package com.bizconnect.application.port.out;

import com.bizconnect.application.domain.model.PaymentHistory;

public interface SavePaymentDataPort {
    void insertPayment(PaymentHistory paymentHistory);
    void updatePayment(PaymentHistory paymentHistory);


}
