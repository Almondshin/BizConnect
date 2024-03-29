package com.bizconnect.adapter.out.persistence.repository.querydsl;

import com.bizconnect.adapter.out.persistence.entity.PaymentJpaEntity;

import javax.validation.constraints.NotBlank;
import java.util.List;

public interface PaymentHistoryRepositoryQueryDSL {
    List<PaymentJpaEntity> findByAgencyIdAndSiteIdAndTrTrace(
            @NotBlank String agencyId,
            @NotBlank String siteId,
            @NotBlank String trTrace
    );
}
