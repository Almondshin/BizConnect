package com.bizconnect.adapter.in.web;

import com.bizconnect.application.usecase.BizRegistrationUseCase;
import com.bizconnect.domain.entity.BizInfo;
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
    public ResponseEntity<?> registerMerchant(@RequestBody BizInfo bizInfo) {
        bizRegistrationUseCase.registerMerchant(bizInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{mallId}/{bizId}")
    public ResponseEntity<BizInfo> getMerchantInfo(@PathVariable String mallId, @PathVariable String bizId) {
        BizInfo bizInfo = bizRegistrationUseCase.getMerchantInfo(mallId, bizId);
        return ResponseEntity.ok(bizInfo);
    }
}


