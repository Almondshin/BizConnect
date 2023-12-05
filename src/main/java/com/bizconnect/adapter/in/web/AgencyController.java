package com.bizconnect.adapter.in.web;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.port.in.AgencyUseCase;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgencyController {

    private final AgencyUseCase agencyUseCase;

    public AgencyController(AgencyUseCase agencyUseCase) {
        this.agencyUseCase = agencyUseCase;
    }

    @PostMapping("/getAgencySiteStatus")
    public ResponseEntity<?> registerMerchant(@RequestBody Agency agency) {
        System.out.println("agencyId : " + agency.getAgencyId());
        System.out.println("mallId : " + agency.getMallId());

        try {
            Agency result = agencyUseCase.checkAgencyId(new Agency(agency.getAgencyId(), agency.getMallId()));

            if (result == null) {
                // 조건에 따른 처리
                return ResponseEntity.ok().body("조건에 맞지 않는 요청");
            }

            // 성공적인 처리
            return ResponseEntity.ok().body("성공적인 처리 결과");

        } catch (IllegalArgumentException e) {
            System.err.println("Error during checking AgencyId or MallId: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
