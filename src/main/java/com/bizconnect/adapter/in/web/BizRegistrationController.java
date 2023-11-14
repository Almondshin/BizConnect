package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.Repository.BizInfoRepository;
import com.bizconnect.application.port.BizInfoDataPort;
import com.bizconnect.domain.entity.BizInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BizRegistrationController implements BizInfoDataPort {

    private BizInfoRepository bizInfoRepository;

    @Override
    public void saveInfo(BizInfo bizInfo) {
        bizInfoRepository.save(bizInfo);
    }
}


