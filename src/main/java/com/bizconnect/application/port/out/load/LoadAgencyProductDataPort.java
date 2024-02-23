package com.bizconnect.application.port.out.load;

import com.bizconnect.application.domain.model.AgencyProducts;
import java.util.Optional;

public interface LoadAgencyProductDataPort {
    AgencyProducts getAgencyProductByRateSel(String rateSel);
}
