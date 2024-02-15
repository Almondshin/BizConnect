package com.bizconnect.application.port.out.save;

import com.bizconnect.application.domain.model.PaymentHistory;

public interface SavePaymentDataPort {
    void insertPayment(PaymentHistory paymentHistory);
    void updatePayment(PaymentHistory paymentHistory);


}
