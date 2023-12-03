package com.bizconnect.application.port.in;

import com.bizconnect.application.domain.model.Agency;

public interface AgencyUseCase {
    void registerAgency(Agency agency); // 제휴사 등록
    void updateAgency(Agency agency);   // 제휴사 정보 업데이트
    void deleteAgency(String agencyId); // 제휴사 삭제
    Agency getAgencyDetails(String agencyId); // 제휴사 상세 정보 조회
    void checkAgencyId(Agency agency);  // Agency 객체를 전달받는 방식으로 변경
}

