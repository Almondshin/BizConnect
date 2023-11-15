package com.bizconnect.application.usecase;

import com.bizconnect.application.dto.BizConnectParams;
import com.bizconnect.domain.entity.BizInfo;

public interface BizRegistrationUseCase {
    void registerMerchant(BizInfo bizInfo);
    BizInfo getMerchantInfo(String mallId, String bizId);

}

