package com.bizconnect.application.port.out;

import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.PaymentHistory;

import java.util.List;
import java.util.Optional;

public interface LoadPaymentDataPort {
    List<PaymentHistoryDataModel> getPaymentHistoryByAgency(Agency agency);

}
