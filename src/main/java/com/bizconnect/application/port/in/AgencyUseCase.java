package com.bizconnect.application.port.in;

import com.bizconnect.adapter.in.model.ClientDataModel;

public interface AgencyUseCase {
    void registerAgency(ClientDataModel clientDataModel); // 제휴사 등록
    void checkAgencyId(ClientDataModel clientDataModel);  // Agency 객체를 전달받는 방식으로 변경
}

