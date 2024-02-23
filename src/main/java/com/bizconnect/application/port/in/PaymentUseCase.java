package com.bizconnect.application.port.in;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.AgencyProducts;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PaymentUseCase {
    void checkMchtParams(ClientDataModel clientDataModel) throws ParseException;
    String aes256EncryptEcb(ClientDataModel clientDataModel, String tradeNum, String trdDt, String trdTm);
    HashMap<String, String> encodeBase64(ClientDataModel clientDataModel , String tradeNum);
    List<PaymentHistoryDataModel> getPaymentHistoryByAgency(String agencyId,String siteId);
    PaymentHistoryDataModel getPaymentHistoryByAgencyLastPayment(String agencyId,String siteId);
    String makeTradeNum();
    AgencyProducts getAgencyProductByRateSel(String rateSel);
    int getExcessAmount(List<PaymentHistoryDataModel> list);
    void insertAutoPayPaymentHistory(String agencyId, String siteId, AgencyProducts product,String reqData);
}

