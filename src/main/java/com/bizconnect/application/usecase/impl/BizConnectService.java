package com.bizconnect.application.usecase.impl;

import com.bizconnect.adapter.Repository.BizInfoRepository;
import com.bizconnect.application.dto.BizConnectParams;
import com.bizconnect.application.usecase.BizConnectUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BizConnectService implements BizConnectUseCase {

    private final BizInfoRepository bizInformationRepository;
    @Override
    public void registrationBizInfo(BizConnectParams bizInfo) {
        bizInformationRepository.save(bizInfo);
    }

}
