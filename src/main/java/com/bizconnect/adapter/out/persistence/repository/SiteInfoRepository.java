package com.bizconnect.adapter.out.persistence.repository;

import com.bizconnect.adapter.out.persistence.entity.SiteInfoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteInfoRepository extends JpaRepository<SiteInfoJpaEntity, String> {
    Optional<SiteInfoJpaEntity> findBySiteId(String siteId);
}
