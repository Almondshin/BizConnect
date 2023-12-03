package com.bizconnect.adapter.in.web;

import com.bizconnect.application.port.in.AgencyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgencyController {

    private final AgencyUseCase agencyUseCase;

    public AgencyController(AgencyUseCase agencyUseCase) {
        this.agencyUseCase = agencyUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerMerchant(@RequestBody String agencyId, String mallId) {
        agencyUseCase.checkAgencyId(agencyId, mallId);
        return ResponseEntity.ok().build();
    }

}
