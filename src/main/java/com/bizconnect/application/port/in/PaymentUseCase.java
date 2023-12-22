package com.bizconnect.application.port.in;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentDataModel;

import java.util.HashMap;

public interface PaymentUseCase {
    String aes256EncryptEcb(PaymentDataModel paymentDataModel);
    HashMap<String, String> encodeBase64(PaymentDataModel paymentDataModel);

    void insertPaymentData();

}

