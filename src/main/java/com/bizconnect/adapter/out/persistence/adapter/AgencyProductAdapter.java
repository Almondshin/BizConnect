package com.bizconnect.adapter.out.persistence.adapter;

import com.bizconnect.adapter.out.persistence.entity.AgencyProductsJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.AgencyProductRepository;
import com.bizconnect.application.domain.model.AgencyProducts;
import com.bizconnect.application.port.out.load.LoadAgencyProductDataPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AgencyProductAdapter implements LoadAgencyProductDataPort {

    private final AgencyProductRepository agencyProductRepository;

    public AgencyProductAdapter(AgencyProductRepository agencyProductRepository) {
        this.agencyProductRepository = agencyProductRepository;
    }

    @Override
    public AgencyProducts getAgencyProductByRateSel(String rateSel) {
        Optional<AgencyProductsJpaEntity> entity = agencyProductRepository.findByRateSel(rateSel);
        return entity.map(this::convertDomain).orElse(null);
    }

    private AgencyProducts convertDomain(AgencyProductsJpaEntity entity) {
        return AgencyProducts.builder()
                .rateSel(entity.getRateSel())
                .name(entity.getName())
                .price(entity.getPrice())
                .offer(entity.getOffer())
                .month(entity.getMonth())
                .feePerCase(entity.getFeePerCase())
                .excessPerCase(entity.getExcessPerCase())
                .build();
    }
}
