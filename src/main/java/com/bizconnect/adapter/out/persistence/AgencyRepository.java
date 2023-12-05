package com.bizconnect.adapter.out.persistence;

import com.bizconnect.application.domain.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AgencyRepository extends JpaRepository<AgencyJpaEntity,String> {
    AgencyJpaEntity findByAgencyIdAndMallId(String agencyId, String mallId);
}
