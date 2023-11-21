package com.bizconnect.adapter.in.web;

import com.bizconnect.application.usecase.BizRegistrationUseCase;
import com.bizconnect.domain.entity.AdditionalBizInfo;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
public class BizRegistrationController {

    private final BizRegistrationUseCase bizRegistrationUseCase;

    @Autowired
    public BizRegistrationController(BizRegistrationUseCase bizRegistrationUseCase) {
        this.bizRegistrationUseCase = bizRegistrationUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerMerchant(@RequestBody AdditionalBizInfo additionalBizInfo) {
        bizRegistrationUseCase.registerMerchant(additionalBizInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{mallId}/{bizId}")
    public ResponseEntity<AdditionalBizInfo> getMerchantInfo(@PathVariable String mallId, @PathVariable String bizId) {
        AdditionalBizInfo additionalBizInfo = bizRegistrationUseCase.getMerchantInfo(mallId, bizId);
        return ResponseEntity.ok(additionalBizInfo);
    }
}


