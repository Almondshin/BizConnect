package com.bizconnect.application.usecase;

import com.bizconnect.domain.entity.AdditionalBizInfo;

public interface BizRegistrationUseCase {
    void registerMerchant(AdditionalBizInfo additionalBizInfo);
    AdditionalBizInfo getMerchantInfo(String mallId, String bizId);

}

