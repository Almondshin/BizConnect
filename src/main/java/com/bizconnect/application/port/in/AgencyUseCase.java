package com.bizconnect.application.port.in;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.application.domain.model.AgencyInfoKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AgencyUseCase {
    void registerAgency(ClientDataModel clientDataModel); // 제휴사 등록
    Optional<ClientDataModel> getAgencyInfo(ClientDataModel clientDataModel);  // Agency 객체를 전달받는 방식으로 변경
    List<ClientDataModel> selectAgencyInfo();
    List<Map<String, String>> getProductTypes(String agencyId);

}

