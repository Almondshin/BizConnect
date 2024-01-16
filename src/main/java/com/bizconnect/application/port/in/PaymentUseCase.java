package com.bizconnect.application.port.in;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentDataModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PaymentUseCase {
    void checkMchtParams(PaymentDataModel paymentDataModel);
    String aes256EncryptEcb(PaymentDataModel paymentDataModel);
    HashMap<String, String> encodeBase64(PaymentDataModel paymentDataModel);

}

