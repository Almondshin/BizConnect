package com.bizconnect.application.port.in;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.out.payment.model.HFDataModel;
import com.bizconnect.application.domain.model.PaymentHistory;

import java.util.HashMap;
import java.util.Map;

public interface PaymentUseCase {
    String aes256EncryptEcb(PaymentDataModel paymentDataModel);
    HashMap<String, String> encodeBase64(PaymentDataModel paymentDataModel);
    void checkMchtParams(PaymentDataModel paymentDataModel);
}

