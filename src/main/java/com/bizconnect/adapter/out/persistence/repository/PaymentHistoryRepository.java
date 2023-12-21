package com.bizconnect.adapter.out.persistence.repository;

import com.bizconnect.adapter.out.persistence.entity.AgencyJpaEntity;
import com.bizconnect.adapter.out.persistence.entity.PaymentJpaEntity;
import com.bizconnect.application.domain.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentJpaEntity, String> {
    Optional<PaymentJpaEntity> findByAgencyIdAndSiteIdAndStartDateAndEndDateAndRateSelAndPaymentType(
            @NotBlank String agencyId,
            @NotBlank String siteId,
            @NotBlank Date startDate,
            @NotBlank Date endDate,
            String rateSel,
            String paymentType
    );
}
