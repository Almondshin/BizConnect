package com.bizconnect.application.usecase;

import com.bizconnect.application.dto.BizConnectParams;
import com.bizconnect.domain.entity.BizInfo;

public interface PaymentUseCase {
    void checkProductPrice(BizConnectParams bizConnectParams);

}

