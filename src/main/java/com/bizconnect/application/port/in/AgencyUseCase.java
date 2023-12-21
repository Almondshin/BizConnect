package com.bizconnect.application.port.in;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AgencyUseCase {
    void registerAgency(ClientDataModel clientDataModel); // 제휴사 등록
    Optional<ClientDataModel> getAgencyInfo(ClientDataModel clientDataModel);  // Agency 객체를 전달받는 방식으로 변경
    List<Map<String, String>> getEnumValues();
    Optional<PaymentHistoryDataModel> getPaymentInfo(ClientDataModel clientDataModel);
}

