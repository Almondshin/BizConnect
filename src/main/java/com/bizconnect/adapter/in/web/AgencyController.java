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
            Agency result = agencyUseCase.checkAgencyId(agency);
            if (result == null) {
                return ResponseEntity.noContent().build(); // 데이터가 없을 때 204 반환
            }
            return ResponseEntity.ok().body("성공적인 처리 결과"); // 데이터가 있을 때 200 반환

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error during checking AgencyId or MallId : " + agency.getAgencyId() + agency.getMallId());
        }
    }


}
