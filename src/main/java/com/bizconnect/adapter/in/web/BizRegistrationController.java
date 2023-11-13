package com.bizconnect.adapter.in.web;

import com.bizconnect.application.dto.BizConnectParams;
import com.bizconnect.application.usecase.impl.BizConnectService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BizRegistrationController {

    private final BizConnectService bizConnectService;

    @PostMapping("")
    void registration(BizConnectParams bizConnectParams){
        bizConnectService.registrationBizInfo(bizConnectParams);
    }
}


