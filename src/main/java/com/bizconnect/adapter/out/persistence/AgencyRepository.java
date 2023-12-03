package com.bizconnect.adapter.out.persistence;

import com.bizconnect.application.domain.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AgencyRepository extends JpaRepository<AgencyJpaEntity,String> {
    Agency findByMallIdAndAgencyId(@Param("agencyId") String agencyId, @Param("mallId") String mallId);
}
