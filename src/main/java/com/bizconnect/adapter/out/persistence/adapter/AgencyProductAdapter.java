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
    public Optional<AgencyProducts> getAgencyProductList(String rateSel) {
        Optional<AgencyProductsJpaEntity> entity = agencyProductRepository.findByRateSel(rateSel);
        return entity.map(this::convertDomain);
    }

    private AgencyProducts convertDomain(AgencyProductsJpaEntity entity){
        AgencyProducts agencyProducts = new AgencyProducts();
        agencyProducts.setRateSel(entity.getRateSel());
        agencyProducts.setName(entity.getName());
        agencyProducts.setPrice(entity.getPrice());
        agencyProducts.setOffer(entity.getOffer());
        agencyProducts.setMonth(entity.getMonth());
        agencyProducts.setFeePerCase(entity.getFeePerCase());
        agencyProducts.setExcessPerCase(entity.getExcessPerCase());
        return agencyProducts;
    }
}
