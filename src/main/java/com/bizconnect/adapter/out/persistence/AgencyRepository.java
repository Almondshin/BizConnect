package com.bizconnect.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AgencyRepository extends JpaRepository<AgencyJpaEntity,String> {
    Optional<AgencyJpaEntity> findByMallIdAndAgencyId(@Param("agencyId") String agencyId, @Param("mallId") String mallId);
}
