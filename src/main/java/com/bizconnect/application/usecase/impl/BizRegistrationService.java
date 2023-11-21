package com.bizconnect.application.usecase.impl;

import com.bizconnect.application.usecase.BizRegistrationUseCase;
import com.bizconnect.domain.aggregate.BizRequestAggregate;
import com.bizconnect.domain.entity.AdditionalBizInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BizRegistrationService implements BizRegistrationUseCase {
    private final BizRequestAggregate bizRequestAggregate;

    @Autowired
    public BizRegistrationService(BizRequestAggregate bizRequestAggregate) {
        this.bizRequestAggregate = bizRequestAggregate;
    }

    @Override
    @Transactional
    public void registerMerchant(AdditionalBizInfo additionalBizInfo) {
        bizRequestAggregate.registerBiz(additionalBizInfo);
    }

    @Override
    @Transactional
    public AdditionalBizInfo getMerchantInfo(String mallId, String bizId) {
        return bizRequestAggregate.selectBizInfo(mallId, bizId);
        // 조회 결과에 대한 처리 로직
    }
}
