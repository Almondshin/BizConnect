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
    public ResponseEntity<?> registerMerchant(Agency agency) {

        System.out.println("agencyId : " + agency.getAgencyId());
        System.out.println("mallId : " + agency.getMallId());
        agencyUseCase.checkAgencyId(new Agency(agency.getAgencyId(), agency.getMallId())); // Agency 객체를 생성하여 유효성 검증
        return ResponseEntity.ok().build();
    }
}
