package com.bizconnect.application.port.in;

import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface PaymentUseCase {
    void checkMchtParams(PaymentDataModel paymentDataModel) throws ParseException;
    String aes256EncryptEcb(PaymentDataModel paymentDataModel);
    HashMap<String, String> encodeBase64(PaymentDataModel paymentDataModel);
    List<PaymentHistoryDataModel> getPaymentHistoryByAgency(String agencyId,String siteId);
}

