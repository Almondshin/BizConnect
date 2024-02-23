package com.bizconnect.adapter.out.persistence.repository;

import com.bizconnect.adapter.out.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;
import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentJpaEntity, String> {
    List<PaymentJpaEntity> findByAgencyIdAndSiteIdAndTrTrace(
            @NotBlank String agencyId,
            @NotBlank String siteId,
            @NotBlank String trTrace
    );

}
